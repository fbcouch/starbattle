package com.ahsgaming.starbattle.screens;

import com.ahsgaming.starbattle.AIShip;
import com.ahsgaming.starbattle.GameObject;
import com.ahsgaming.starbattle.Ship;
import com.ahsgaming.starbattle.StarBattle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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

    float zoom = 1.0f;          // change this to zoom (lower = zoom in; higher = zoom out)

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
        playerShip = new Ship(game.getShipLoader().getJsonShip("sloop"));
        playerShip.init();
        game.getGameController().addGameObject(playerShip);

        AIShip enemyShip = new AIShip(game.getShipLoader().getJsonShip("sloop"));
        enemyShip.init();
        enemyShip.setPosition(200, 200);
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

        for (int x = -5; x < 5; x++) {
            for (int y = -5; y < 5; y++) {
                Image i = new Image(bgImage);
                i.setPosition(x * bgImage.getRegionWidth(), y * bgImage.getRegionHeight());
                bgGroup.addActor(i);
            }
        }


    }

    @Override
    public void render(float delta) {
        super.render(delta);

        camera.set(playerShip.getX() - (stage.getWidth() - playerShip.getWidth()) * 0.5f, playerShip.getY() - (stage.getHeight() - playerShip.getHeight()) * 0.5f);
        levelGroup.setPosition(-1 * camera.x, -1 * camera.y);
        bgGroup.setPosition(levelGroup.getX(), levelGroup.getY());

        if (game.DEBUG) {
            ShapeRenderer shapeRenderer = new ShapeRenderer();

            shapeRenderer.setColor(0, 0, 1, 1);
            for (GameObject obj: game.getGameController().getGameObjects()) {
                if (obj instanceof Ship && obj.getMoveTarget() != null) {
                    shapeRenderer.begin(ShapeRenderer.ShapeType.Circle);
                    shapeRenderer.circle(obj.getMoveTarget().x - camera.x, obj.getMoveTarget().y - camera.y, 5);
                    shapeRenderer.end();

                    shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                    shapeRenderer.line(obj.getX() + obj.getOriginX() - camera.x, obj.getY() + obj.getOriginY() - camera.y,
                            obj.getMoveTarget().x - camera.x, obj.getMoveTarget().y - camera.y);
                    shapeRenderer.end();
                }
            }

        }
    }
}
