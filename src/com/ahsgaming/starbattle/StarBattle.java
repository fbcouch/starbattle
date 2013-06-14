package com.ahsgaming.starbattle;

import com.ahsgaming.starbattle.screens.LevelScreen;
import com.badlogic.gdx.Game;

/**
 * Created with IntelliJ IDEA.
 * User: jami
 * Date: 6/14/13
 * Time: 2:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class StarBattle extends Game {
    public static final boolean DEBUG = true;

    @Override
    public void create() {
        setScreen(new LevelScreen(this));
    }

}
