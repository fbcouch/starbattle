package com.ahsgaming.starbattle.screens;

import com.ahsgaming.starbattle.GameObject;
import com.ahsgaming.starbattle.StarBattle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

/**
 * Created with IntelliJ IDEA.
 * User: jami
 * Date: 6/14/13
 * Time: 2:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class LevelScreen extends AbstractScreen {
    public static final String LOG = "LevelScreen";

    GameObject playerShip;

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
        playerShip = new GameObject("sloop.png");
        playerShip.init();


    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        stage.addActor(playerShip);
        playerShip.setPosition((stage.getWidth() - playerShip.getWidth()) * 0.5f,
                               (stage.getHeight() - playerShip.getHeight()) * 0.5f);


    }

    @Override
    public void render(float delta) {
        super.render(delta);

        playerShip.rotate(1);
    }
}
