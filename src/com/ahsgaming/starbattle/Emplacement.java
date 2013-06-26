package com.ahsgaming.starbattle;

import com.ahsgaming.starbattle.json.ShipLoader;
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

    String type = "";
    float fireRate, secSinceLastFire;
    float curAmmo, maxAmmo, regenAmmo;

    ShipLoader.JsonEmplacement proto;
    ShipLoader.JsonProjectile projectile;

    Ship target;

    public Vector2 targetCoords;

    public Emplacement(String image) {
        super(image);

        fireRate = 1;
        secSinceLastFire = 0;
        curAmmo = 1;
        maxAmmo = 10;
        regenAmmo = 1;

    }

    public Emplacement(ShipLoader.JsonEmplacement proto) {
        this(proto.image);
        this.proto = proto;

        type = proto.type;
        turnSpeed = proto.turnSpeed;
        maxAmmo = proto.maxAmmo;
        curAmmo = proto.ammo;
        regenAmmo = proto.ammoRegen;
        fireRate = proto.fireRate;
        projectile = game.getShipLoader().getJsonProjectile(proto.projectile);
    }

    @Override
    public void init() {
        super.init();

        if (proto != null) {
            if (proto.originX != 0 || proto.originY != 0)
                setOrigin(proto.originX, proto.originY);
        }
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        curAmmo += regenAmmo * delta;
        if (curAmmo > maxAmmo) curAmmo = maxAmmo;

        secSinceLastFire += delta;         // TODO make this anticipate movement, etc
        if (target != null) {
            // translate target coords to local coords
            Vector2 targetCoords = new Vector2(target.getX() + target.getOriginX(), target.getY() + target.getOriginY());
            if (this.targetCoords == null) this.targetCoords = new Vector2();
            this.targetCoords.set(targetCoords);
            Vector2 theseCoords = new Vector2(getOriginX(), getOriginY());
            theseCoords = convertToParentCoords(theseCoords);
            theseCoords = ((Ship)getParent()).convertToParentCoords(theseCoords);
            targetCoords.sub(theseCoords);
            float angle = rotateToward(delta, targetCoords, getParent().getRotation()).angle();
            if (angle - 5 < getRotation() && angle + 5 > getRotation()) {
                if (canFire()) {
                    fire();
                    secSinceLastFire = 0;
                }
            }
        }
    }

    public boolean canFire() {
        return (curAmmo >= 1 && secSinceLastFire > fireRate);
    }

    public void fire() {
        GameObject bullet = new Projectile((Ship)getParent(), projectile);

        bullet.setRotation(getRotation() + getParent().getRotation());

        bullet.init();

        game.getStatService().shotFired((GameObject)this.getParent());

        Vector2 bulletOrigin = ((GameObject)getParent()).convertToParentCoords(
                convertToParentCoords(new Vector2(getWidth(), (getHeight() - bullet.getHeight()) * 0.5f))
        );

        bullet.setPosition(bulletOrigin.x, bulletOrigin.y);
        game.getGameController().addGameObject(bullet);
        curAmmo -= 1;
    }

    public Ship getTarget() {
        return target;
    }

    public void setTarget(Ship target) {
        this.target = target;
    }

    public float getRangeSq() {
        return (float)Math.pow((projectile.maxSpeed * projectile.lifetime), 2);
    }
}
