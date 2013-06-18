package com.ahsgaming.starbattle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;

/**
 * Created with IntelliJ IDEA.
 * User: jami
 * Date: 6/17/13
 * Time: 10:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class GameController {
    public static final String LOG = "GameController";

    final StarBattle game;

    Array<GameObject> gameObjects;
    Group groupObjects;

    public GameController(StarBattle game) {
        this.game = game;

        gameObjects = new Array<GameObject>();
        groupObjects = new Group();
    }

    public void update(float delta) {
        for (GameObject g: gameObjects) {
            // update velocity
            g.getVelocity().add(new Vector2(g.getAcceleration().mul(delta)));
            if (g.getVelocity().len2() > g.getMaxVelocity2())
                g.getVelocity().div(g.getVelocity().len()).mul(g.getMaxSpeed());

            g.setPosition(g.getX() + g.getVelocity().x * delta, g.getY() + g.getVelocity().y * delta);

            // collisions
            for (int i = 0; i < gameObjects.size; i++) {
                GameObject iobj = gameObjects.get(i);
                for (int j = i + 1; j < gameObjects.size; j++) {
                    GameObject jobj = gameObjects.get(j);
                    if (iobj.canCollide(jobj) && jobj.canCollide(iobj) && GameObject.collideRect(iobj, jobj)) {
                        iobj.collide(jobj);
                        jobj.collide(iobj);
                    }
                }
            }

            g.update(delta);

            if (g.isRemove())
                removeGameObject(g);
        }
    }

    public void addGameObject(GameObject g) {
        gameObjects.add(g);
        groupObjects.addActor(g);
    }

    public void removeGameObject(GameObject g) {
        gameObjects.removeValue(g, true);
        g.remove();
    }

    public Array<GameObject> getGameObjects() {
        Array<GameObject> objs = new Array<GameObject>(gameObjects.size);
        objs.addAll(gameObjects);
        return objs;
    }

    public Group getGroupObjects() {
        return groupObjects;
    }
}
