package com.ahsgaming.starbattle;

import com.ahsgaming.starbattle.json.ProfileService;
import com.ahsgaming.starbattle.json.ShipLoader;
import com.ahsgaming.starbattle.json.StatService;
import com.ahsgaming.starbattle.screens.LevelScreen;
import com.ahsgaming.starbattle.screens.MainMenuScreen;
import com.ahsgaming.starbattle.screens.OptionsScreen;
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

    TextureService textureService;
    ShipLoader shipLoader;
    ProfileService profileService;
    GameController gameController;
    StatService statService;

    FPSLogger fpsLogger;

    @Override
    public void create() {
        starBattle = this;

        textureService = new TextureService("assets.atlas");
        shipLoader = new ShipLoader("shiplist.json");
        profileService = new ProfileService("profiles.json");
        statService = new StatService();

        setScreen(new MainMenuScreen(this));

        fpsLogger = new FPSLogger();

        profileService.saveProfiles();
    }

    @Override
    public void render() {
        super.render();

        if (gameController != null) gameController.update(Gdx.graphics.getDeltaTime());

        if (DEBUG) fpsLogger.log();
    }

    @Override
    public void dispose() {
        super.dispose();
        if (profileService != null) profileService.saveProfiles();
    }

    public void exit() {
        Gdx.app.exit();
    }

    public void showMainMenu() {
        setScreen(new MainMenuScreen(this));
    }

    public void showOptions() {
        setScreen(new OptionsScreen(this));
    }

    public void startLevel() {
        gameController = new GameController(this);
        setScreen(new LevelScreen(this));
    }

    public void endLevel(Ship playerShip) {
        StatService.StatEntry se = statService.getStatsForObject(playerShip);

        profileService.updateStats(playerShip.proto, se);

        profileService.saveProfiles();
    }

    public TextureService getTextureService() {
        return textureService;
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

    public StatService getStatService() {
        return statService;
    }
}
