package com.ahsgaming.starbattle;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

/**
 * Created with IntelliJ IDEA.
 * User: jami
 * Date: 6/14/13
 * Time: 2:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class StarBattleDesktop {
    public static void main(String[] args) {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "Star Battle | ahsgaming.com | (c) 2013 Jami Couch";
        cfg.useGL20 = true; // TODO fix this
        cfg.width = 1280;
        cfg.height = 768;
        cfg.fullscreen = false;
        cfg.resizable = true;

        new LwjglApplication(new StarBattle(), cfg);
    }
}
