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

    Array<GameObject> team1, team2;

    Ship playerShip;

    public enum GameState {
        GS_RUNNING, GS_PAUSED, GS_OVER;
    }

    GameState gameState;

    public GameController(StarBattle game) {
        this.game = game;

        gameObjects = new Array<GameObject>();
        groupObjects = new Group();

        gameState = GameState.GS_RUNNING;
    }

    public void update(float delta) {

        if (gameState == GameState.GS_OVER || gameState == GameState.GS_PAUSED) {
            return; // TODO we may want to actually do something here eventually
        }

        team1 = new Array<GameObject>();
        team2 = new Array<GameObject>();

        for (int k = 0; k < gameObjects.size; k++) {
            GameObject g = gameObjects.get(k);

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

            if (g.isRemove()) {
                removeGameObject(g);
                k--;
            }

            if (!g.isRemove()) {
                switch(g.getTeam()) {
                    case 1:
                        team1.add(g);
                        break;
                    case 2:
                        team2.add(g);
                }
            }
        }

        if (team1.size == 0 || team2.size == 0 || (playerShip != null && playerShip.isRemove())) {
            // gameover!
            if (!StarBattle.DEBUG_TEST) gameState = GameState.GS_OVER;
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

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public Ship getPlayerShip() {
        return playerShip;
    }

    public void setPlayerShip(Ship playerShip) {
        this.playerShip = playerShip;
    }
}
