package com.ahsgaming.starbattle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

/**
 * Created with IntelliJ IDEA.
 * User: jami
 * Date: 6/15/13
 * Time: 9:47 AM
 * To change this template use File | Settings | File Templates.
 */
public class Emplacement extends GameObject {
    public static final String LOG = "Emplacement";

    float fireRate, secSinceLastFire;
    float curAmmo, maxAmmo, regenAmmo;
    float projInitSpeed, projMaxSpeed, projAccel, projLifetime, projDamage;     // probably will want all these defined
                                                                                // in "projectile type"

    public Emplacement(String image) {
        super(image);

        fireRate = 1;
        secSinceLastFire = 0;
        curAmmo = 1;
        maxAmmo = 10;
        regenAmmo = 1;
        projInitSpeed = 300;
        projMaxSpeed = 300;

    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        secSinceLastFire += delta;
        if (canFire()) {
            fire();
            secSinceLastFire = 0;
        }

        curAmmo += regenAmmo * delta;
        if (curAmmo > maxAmmo) curAmmo = maxAmmo;

    }

    public boolean canFire() {
        return (curAmmo >= 1 && secSinceLastFire > fireRate);
    }

    public void fire() {
        GameObject bullet = new GameObject("laser-green");
        bullet.init();
        bullet.setRotation(getRotation() + getParent().getRotation());
        bullet.setVelocity(new Vector2(projInitSpeed, 0).rotate(getRotation() + getParent().getRotation()));
        bullet.setMaxVelocity(projMaxSpeed);

        // TODO fix this - doesn't work exactly right

        Vector2 bulletOrigin = ((GameObject)getParent()).convertToParentCoords(
                convertToParentCoords(new Vector2(getWidth(), (getHeight() - bullet.getHeight()) * 0.5f))
        );

        bullet.setPosition(bulletOrigin.x, bulletOrigin.y);
        game.addGameObject(bullet);
        curAmmo -= 1;
    }
}
