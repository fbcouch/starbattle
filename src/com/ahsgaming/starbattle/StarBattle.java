package com.ahsgaming.starbattle;

import com.ahsgaming.starbattle.screens.LevelScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;

/**
 * Created with IntelliJ IDEA.
 * User: jami
 * Date: 6/14/13
 * Time: 2:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class StarBattle extends Game {
    public static final boolean DEBUG = true;

    public static StarBattle starBattle;

    TextureAtlas textureAtlas;
    ShipLoader shipLoader;

    Array<GameObject> gameObjects;
    Group groupObjects;

    @Override
    public void create() {
        starBattle = this;
        gameObjects = new Array<GameObject>();
        textureAtlas = new TextureAtlas(Gdx.files.local("assets/assets.atlas"));
        shipLoader = new ShipLoader("shiplist.json");

        groupObjects = new Group();
        setScreen(new LevelScreen(this));
    }

    @Override
    public void render() {
        super.render();

        float delta = Gdx.graphics.getDeltaTime();

        for (GameObject g: gameObjects) {
            // update velocity
            g.getVelocity().add(new Vector2(g.getAcceleration().mul(delta)));
            if (g.getVelocity().len2() > g.getMaxVelocity2())
                g.getVelocity().div(g.getVelocity().len()).mul(g.getMaxVelocity());

            g.setPosition(g.getX() + g.getVelocity().x * delta, g.getY() + g.getVelocity().y * delta);

            g.update(delta);
        }
    }

    public void addGameObject(GameObject g) {
        gameObjects.add(g);
        groupObjects.addActor(g);
    }

    public TextureAtlas getTextureAtlas() {
        return textureAtlas;
    }

    public Group getGroupObjects() {
        return groupObjects;
    }
}
