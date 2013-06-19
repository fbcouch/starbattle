package com.ahsgaming.starbattle.screens;

import com.ahsgaming.starbattle.StarBattle;
import com.ahsgaming.starbattle.json.ProfileService;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

/**
 * starbattle
 * (c) 2013 Jami Couch
 * Created on 6/19/13 by jami
 * ahsgaming.com
 */
public class OptionsScreen extends AbstractScreen {
    public static final String LOG = "OptionsScreen";

    public OptionsScreen(StarBattle game) {
        super(game);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        Table mainTable = new Table(getSkin());
        mainTable.setFillParent(true);
        stage.addActor(mainTable);

        Array<ProfileService.Profile> profiles = game.getProfileService().getProfiles();
        String[] items = new String[profiles.size];
        for (int i=0; i < items.length; i++) {
            items[i] = profiles.get(i).id;
        }

        List listProfile = new List(items, getSkin());
        listProfile.setHeight(stage.getHeight());
        mainTable.add(listProfile).minWidth(300).minHeight(300).expandY();

        mainTable.add().expandX();
    }
}
