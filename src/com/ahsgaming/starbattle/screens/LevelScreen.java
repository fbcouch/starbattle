package com.ahsgaming.starbattle.screens;

import com.ahsgaming.starbattle.*;
import com.badlogic.gdx.Gdx;
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
        playerShip = new Ship(game.getProfileLoader().getSelectedProfile().ships.first());
        playerShip.init();
        playerShip.setPosition(mapBounds.x, mapBounds.y + (mapBounds.getHeight()- playerShip.getHeight()) * 0.5f);
        game.getGameController().addGameObject(playerShip);

        AIShip enemyShip = new AIShip(game.getShipLoader().getJsonShip("sloop"));
        enemyShip.init();
        enemyShip.setPosition(mapBounds.x + mapBounds.width - enemyShip.getWidth(), mapBounds.y + mapBounds.getHeight() * 0.5f - enemyShip.getHeight() * 0.5f);
        enemyShip.setRotation(180);
        game.getGameController().addGameObject(enemyShip);

        bgImage = game.getTextureAtlas().createSprite("default_background");

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

        }
    }
}
