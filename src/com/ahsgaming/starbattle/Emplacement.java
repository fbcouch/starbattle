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

        setOrigin(10, 10);
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
        // get the vector from the origin to the firing point
        Vector2 bulletOrigin = new Vector2(getWidth() - getOriginX(),
                                          (getHeight() - bullet.getHeight()) * 0.5f - getOriginY());
        // rotate this by the rotation
        bulletOrigin.rotate(getRotation());
        // add the distance from the (0,0) of the image to the rotation origin and the position within the parental ref.
        bulletOrigin.add(getX() + getOriginX(), getY() + getOriginY());
        // transform this vector to the parental rotation origin
        bulletOrigin.sub(getParent().getOriginX(), getParent().getOriginY());
        // rotate by the parent's rotation
        bulletOrigin.rotate(getParent().getRotation());
        // transform back to the parent's (0, 0) and then to the container frame of reference
        bulletOrigin.add(getParent().getOriginX(), getParent().getOriginY()).add(getParent().getX(), getParent().getY());

        bullet.setPosition(bulletOrigin.x, bulletOrigin.y);
        game.addGameObject(bullet);
        curAmmo -= 1;
    }
}
