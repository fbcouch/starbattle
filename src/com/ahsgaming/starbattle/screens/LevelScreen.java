package com.ahsgaming.starbattle.screens;

import com.ahsgaming.starbattle.GameObject;
import com.ahsgaming.starbattle.Ship;
import com.ahsgaming.starbattle.StarBattle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
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
    Group levelGroup;
    Vector2 camera;


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
        playerShip = new Ship("sloop");
        playerShip.init();
        playerShip.setVelocity(new Vector2(20, 0));
        game.addGameObject(playerShip);

        levelGroup = game.getGroupObjects();
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
        super.resize(width, height);

        stage.addActor(levelGroup);

        levelGroup.addActor(new Label("X", getSkin()));

    }

    @Override
    public void render(float delta) {
        super.render(delta);

        camera.set(playerShip.getX() - (stage.getWidth() - playerShip.getWidth()) * 0.5f, playerShip.getY() - (stage.getHeight() - playerShip.getHeight()) * 0.5f);
        levelGroup.setPosition(-1 * camera.x, -1 * camera.y);

    }
}
