package com.ahsgaming.starbattle;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: jami
 * Date: 6/14/13
 * Time: 3:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class Ship extends GameObject {
    public static final String LOG = "Ship";

    Array<Emplacement> emplacements;

    public Ship(String image) {
        super(image);

        emplacements = new Array<Emplacement>();
    }


    @Override
    public void init() {
        super.init();

        Emplacement e = new Emplacement("laser");
        e.init();
        e.setPosition(getWidth() * 0.75f - e.getWidth() * 0.5f, (getHeight() - e.getHeight()) * 0.5f);
        addEmplacement(e);

        e = new Emplacement("laser");
        e.init();
        e.setPosition(getWidth() * 0.3f - e.getWidth() * 0.5f, (getHeight() - e.getHeight()) * 0.5f);
        addEmplacement(e);
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        for (Emplacement emp: emplacements) {
            emp.rotate(5 * (Math.random() >= 0.5 ? -1 : 1));
        }
    }

    public void addEmplacement(Emplacement emp) {
        emplacements.add(emp);
        addActor(emp);
    }

    public void clearEmplacements() {
        while(emplacements.size > 0) {
            emplacements.pop().remove();
        }
    }

}
