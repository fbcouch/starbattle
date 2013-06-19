package com.ahsgaming.starbattle;

import com.ahsgaming.starbattle.json.ProfileService;
import com.ahsgaming.starbattle.json.ShipLoader;
import com.ahsgaming.starbattle.screens.LevelScreen;
import com.ahsgaming.starbattle.screens.MainMenuScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

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
    ProfileService profileService;
    GameController gameController;

    FPSLogger fpsLogger;

    @Override
    public void create() {
        starBattle = this;

        textureAtlas = new TextureAtlas(Gdx.files.local("assets/assets.atlas"));
        shipLoader = new ShipLoader("shiplist.json");
        profileService = new ProfileService("profiles.json");
        gameController = new GameController(this);

        setScreen(new MainMenuScreen(this));

        fpsLogger = new FPSLogger();

        profileService.saveProfiles();
    }

    @Override
    public void render() {
        super.render();

        gameController.update(Gdx.graphics.getDeltaTime());

        if (DEBUG) fpsLogger.log();
    }

    public TextureAtlas getTextureAtlas() {
        return textureAtlas;
    }

    public ShipLoader getShipLoader() {
        return shipLoader;
    }

    public ProfileService getProfileService() {
        return profileService;
    }

    public GameController getGameController() {
        return gameController;
    }
}
