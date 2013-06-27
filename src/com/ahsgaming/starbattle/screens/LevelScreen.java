package com.ahsgaming.starbattle.screens;

import com.ahsgaming.starbattle.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Created with IntelliJ IDEA.
 * User: jami
 * Date: 6/14/13
 * Time: 2:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class LevelScreen extends AbstractScreen {
    public static final String LOG = "LevelScreen";

    Ship playerShip;
    Group levelGroup, bgGroup;
    Vector2 camera;

    TextureRegion bgImage;

    float zoom = 1f;          // change this to zoom (lower = zoom in; higher = zoom out)

    Rectangle mapBounds = new Rectangle(0, 0, 1024, 1024);

    // TODO turn this into a fully fledged hud
    Label damageDealt, damageTaken;
    Label shots;

    float gameOverCountdown = 5.0f;

    /**
     * Constructor
     *
     * @param game
     */
    public LevelScreen(StarBattle game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();
        bgGroup = new Group();
        playerShip = new Ship(game.getProfileService().getSelectedProfile().ships.first());
        playerShip.init();
        playerShip.setTeam(1);
        playerShip.setPosition(mapBounds.x, mapBounds.y + (mapBounds.getHeight()- playerShip.getHeight()) * 0.5f);
        game.getGameController().addGameObject(playerShip);
        game.getGameController().setPlayerShip(playerShip);

        AIShip otherShip = new AIShip(game.getShipLoader().getJsonShip("sloop"));
        otherShip.init();
        otherShip.setTeam(2);
        otherShip.setPosition(mapBounds.x + mapBounds.width - otherShip.getWidth(), mapBounds.y + mapBounds.getHeight() * 0.5f - otherShip.getHeight() * 0.5f);
        otherShip.setRotation(180);
        game.getGameController().addGameObject(otherShip);

        otherShip = new AIShip(game.getShipLoader().getJsonShip("sloop"));
        otherShip.init();
        otherShip.setTeam(2);
        otherShip.setPosition(mapBounds.x + mapBounds.width - otherShip.getWidth(), mapBounds.y + mapBounds.getHeight() * 0.5f - otherShip.getHeight() * 0.5f + 200);
        otherShip.setRotation(180);
        game.getGameController().addGameObject(otherShip);

        otherShip = new AIShip(game.getShipLoader().getJsonShip("sloop"));
        otherShip.init();
        otherShip.setTeam(1);
        otherShip.setPosition(mapBounds.x, mapBounds.y + mapBounds.getHeight() * 0.5f - otherShip.getHeight() * 0.5f + 200);
        game.getGameController().addGameObject(otherShip);

        bgImage = game.getTextureService().createSprite("default_background");

        levelGroup = game.getGameController().getGroupObjects();
        camera = new Vector2();

        stage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                // handle input
                playerShip.setMoveTarget(x + camera.x, y + camera.y);

            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                // handle input
                playerShip.setMoveTarget(x + camera.x, y + camera.y);
            }
        });
    }

    @Override
    public void resize(int width, int height) {
        super.resize((int)(width * zoom), (int)(height * zoom));
        stage.addActor(bgGroup);
        stage.addActor(levelGroup);

        for (int x = 0; x < Math.ceil(mapBounds.getWidth() / bgImage.getRegionWidth()); x++) {
            for (int y = 0; y < Math.ceil(mapBounds.getHeight() / bgImage.getRegionHeight()); y++) {
                Image i = new Image(bgImage);
                i.setPosition(x * bgImage.getRegionWidth() + mapBounds.x, y * bgImage.getRegionHeight() + mapBounds.y);
                bgGroup.addActor(i);
            }
        }


        damageDealt = new Label("Damage Dealt: 0", getSkin());
        damageTaken = new Label("Damage Taken: 0", getSkin());
        shots = new Label("Accuracy: 0/0 (0%)", getSkin());

        damageDealt.setPosition(0, 0);
        damageTaken.setPosition(0, damageDealt.getTop());
        shots.setPosition(0, damageTaken.getTop());

        stage.addActor(damageDealt);
        stage.addActor(damageTaken);
        stage.addActor(shots);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        camera.set(playerShip.getX() - (stage.getWidth() - playerShip.getWidth()) * 0.5f, playerShip.getY() - (stage.getHeight() - playerShip.getHeight()) * 0.5f);

        // clamp camera to map bounds
        if (camera.x < mapBounds.x) camera.x = mapBounds.x;
        if (camera.x > mapBounds.x + mapBounds.width) camera.x = mapBounds.x + mapBounds.width;

        if (camera.y < mapBounds.y) camera.y = mapBounds.y;
        if (camera.y > mapBounds.y + mapBounds.height) camera.y = mapBounds.y + mapBounds.height;

        if (mapBounds.width < stage.getWidth()) camera.x = mapBounds.x + mapBounds.width * 0.5f - stage.getWidth() * 0.5f;
        if (mapBounds.height < stage.getHeight()) camera.y = mapBounds.y + mapBounds.height * 0.5f - stage.getHeight() * 0.5f;


        levelGroup.setPosition(-1 * camera.x, -1 * camera.y);
        bgGroup.setPosition(levelGroup.getX(), levelGroup.getY());

        // update HUD
        damageDealt.setText(String.format("Damage Dealt: %.0f", game.getStatService().getDamageDealtBy(playerShip)));
        damageTaken.setText(String.format("Damage Taken: %.0f", game.getStatService().getDamageTakenBy(playerShip)));
        int shotsFired = game.getStatService().getShotsFiredBy(playerShip);
        int shotsHit = game.getStatService().getShotsHitBy(playerShip);
        float accuracy = 0;
        if (shotsFired > 0)
            accuracy = (float)shotsHit / shotsFired * 100f;

        shots.setText(String.format("Shots: %d / %d (%.1f%%)", shotsHit, shotsFired, accuracy));

        if (game.DEBUG) {
            ShapeRenderer shapeRenderer = new ShapeRenderer();


            for (GameObject obj: game.getGameController().getGameObjects()) {
                if (obj instanceof Ship) {

                    shapeRenderer.begin(ShapeRenderer.ShapeType.Circle);
                    shapeRenderer.setColor(0, 0, 1, 1);
                    if (obj.getMoveTarget() != null) shapeRenderer.circle(obj.getMoveTarget().x - camera.x, obj.getMoveTarget().y - camera.y, 5);
                    shapeRenderer.setColor(1, 0, 0, 1);

                    for (Emplacement emp: ((Ship)obj).getEmplacements()) {
                        if (emp.targetCoords != null) shapeRenderer.circle(emp.targetCoords.x - camera.x, emp.targetCoords.y - camera.y, 5);
                    }
                    shapeRenderer.end();

                    shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                    shapeRenderer.setColor(0, 0, 1, 1);
                    if (obj.getMoveTarget() != null)
                        shapeRenderer.line(obj.getX() + obj.getOriginX() - camera.x, obj.getY() + obj.getOriginY() - camera.y,
                            obj.getMoveTarget().x - camera.x, obj.getMoveTarget().y - camera.y);
                    shapeRenderer.end();

                }
            }

            shapeRenderer.begin(ShapeRenderer.ShapeType.Rectangle);
            shapeRenderer.setColor(1, 1, 1, 1);
            shapeRenderer.rect(mapBounds.x - camera.x, mapBounds.y - camera.y, mapBounds.width, mapBounds.height);
            shapeRenderer.end();

            if (game.getGameController().getGameState() == GameController.GameState.GS_OVER) {
                gameOverCountdown -= delta;
                if (gameOverCountdown <= 0) {

                    game.endLevel(playerShip);

                    game.showMainMenu();
                }
            }

        }
    }
}
