package com.ahsgaming.starbattle;

import com.ahsgaming.starbattle.json.ShipLoader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

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

    float minAngle, maxAngle;

    ShipLoader.JsonEmplacement proto;
    Array<ShipLoader.JsonProjectile> projectiles;

    Ship target;

    public Vector2 targetCoords;

    public Emplacement(String image) {
        super(image);

        fireRate = 1;
        secSinceLastFire = 0;
        curAmmo = 1;
        maxAmmo = 10;
        regenAmmo = 1;
        projectiles = new Array<ShipLoader.JsonProjectile>();
        minAngle = -180;
        maxAngle = 180;
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

        for (ShipLoader.JsonEmplacementProjectile jep: proto.projectiles)
            projectiles.add(game.getShipLoader().getJsonProjectile(jep.id));
    }

    @Override
    public void init() {
        super.init();

        if (proto != null) {
            if (proto.originX != 0 || proto.originY != 0)
                setOrigin(proto.originX, proto.originY);
        }

        setRotation((minAngle + maxAngle) / 2);
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

            // TODO fix this so that the min/max angle are factored into which direction to rotate

            float targetAngle = targetCoords.angle() - getParent().getRotation();

            while (targetAngle > 180) targetAngle -= 360;
            while (targetAngle < -180) targetAngle += 360;

            float angle = targetAngle - getRotation();

            while (angle > 180) angle -= 360;
            while (angle < -180) angle += 360;

            if (Math.abs(angle) < turnSpeed * delta) {
                setRotation(targetAngle);
            } else {
                rotate(turnSpeed * delta * (angle > 0 ? 1 : -1));
            }

            float offset = 0;
            if (minAngle >= 0) offset = 180;

            while (getRotation() < -180 + offset) rotate(360);
            while (getRotation() > 180 + offset) rotate(-360);

            if (getRotation() > maxAngle)
                setRotation(maxAngle);

            if (getRotation() < minAngle)
                setRotation(minAngle);

            while (getRotation() < -180) rotate(360);
            while (getRotation() > 180) rotate(-360);

            if (Math.abs(targetAngle - getRotation()) < 5) {
                if (canFire()) {
                    fire();
                    secSinceLastFire = 0;
                }
            }
        }
    }

    public boolean canFire() {
        return (curAmmo >= projectiles.size && secSinceLastFire > fireRate);
    }

    public void fire() {
        for (int i=0;i<projectiles.size;i++) {
            ShipLoader.JsonProjectile projectile = projectiles.get(i);
            ShipLoader.JsonEmplacementProjectile jep = proto.projectiles.get(i);

            GameObject bullet = new Projectile((Ship)getParent(), projectile);

            bullet.setRotation(getRotation() + getParent().getRotation());

            bullet.init();

            game.getStatService().shotFired((GameObject)this.getParent());

            Vector2 bulletOrigin = ((GameObject)getParent()).convertToParentCoords(
                    convertToParentCoords(new Vector2(jep.x, jep.y))
            );

            bullet.setPosition(bulletOrigin.x, bulletOrigin.y);
            game.getGameController().addGameObject(bullet);
            curAmmo -= 1;
        }

    }

    public Ship getTarget() {
        return target;
    }

    public void setTarget(Ship target) {
        this.target = target;
    }

    public float getRangeSq() {
        return (float)Math.pow((projectiles.get(0).maxSpeed * projectiles.get(0).lifetime), 2);
    }

    public void setAngleConstraint(float min, float max) {
        minAngle = min;
        maxAngle = max;
    }
}
