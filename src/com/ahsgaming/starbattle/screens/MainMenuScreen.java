package com.ahsgaming.starbattle.screens;

import com.ahsgaming.starbattle.StarBattle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * starbattle
 * (c) 2013 Jami Couch
 * Created on 6/19/13 by jami
 * ahsgaming.com
 */
public class MainMenuScreen extends AbstractScreen {


    /**
     * Constructor
     *
     * @param game
     */
    public MainMenuScreen(StarBattle game) {
        super(game);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        Table table = new Table(getSkin());
        stage.addActor(table);
        table.setFillParent(true);

        table.add("Star Battle");

        table.row();

        TextButton btnPlay = new TextButton("Play", getSkin());

        table.add(btnPlay).size(350, 100).pad(10);

        table.row();

        TextButton btnOptions = new TextButton("Options", getSkin());

        table.add(btnOptions).size(350, 100).pad(10);

        table.row();

        TextButton btnExit = new TextButton("Exit", getSkin());

        table.add(btnExit).size(350, 100).pad(10);

        table.row();

        btnPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                game.startLevel(); // TODO implement game setup
            }
        });

        btnOptions.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                game.showOptions();
            }
        });

        btnExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                game.exit();
            }
        });
    }
}
