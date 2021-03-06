package com.ahsgaming.starbattle;

import com.ahsgaming.starbattle.json.ShipLoader;
import com.ahsgaming.starbattle.screens.LevelScreen;
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

            float targetAngle = getTargetAngle(targetCoords);

            int rotateDir = getRotateDir(targetAngle);

            //if (((LevelScreen)game.getScreen()).getPlayerShip() == this.getParent()) {
                Gdx.app.log(LOG, String.format("targetAngle: %.1f; rotateDir: %d", targetAngle, rotateDir));
            //} /**/

            clampedRotate(rotateDir, targetAngle, delta);

            if (Math.abs(getAngleDist(getRotation(), targetAngle)) < 5) {
                if (canFire()) {
                    if (!StarBattle.DEBUG_NOFIRE) fire();
                    secSinceLastFire = 0;
                }
            }
        }
    }

    public float getTargetAngle(Vector2 targetCoords) {
        Vector2 theseCoords = new Vector2(getOriginX(), getOriginY());
        theseCoords = convertToParentCoords(theseCoords);
        theseCoords = ((Ship)getParent()).convertToParentCoords(theseCoords);
        targetCoords.sub(theseCoords);

        return clampAngle(targetCoords.angle() - getParent().getRotation(), 0);
    }

    public int getRotateDir(float targetAngle) {
        int rotateDir = 0;
        targetAngle = clampAngle(targetAngle, (minAngle > 0 ? 0 : 0));
        setRotation(clampAngle(getRotation(), (minAngle > 0 ? 0 : 0)));
        float angleDiff = getAngleDist(getRotation(), targetAngle);

        Gdx.app.log(LOG, String.format("r: %.1f, tA: %.1f, AngleDiff: %.1f", getRotation(), targetAngle, angleDiff));

        if (angleDiff > 0) {
            rotateDir = 1;
            if (getAngleDist(maxAngle, targetAngle) > 0 &&
                    Math.abs(getAngleDist(maxAngle, targetAngle)) > Math.abs(getAngleDist(minAngle, targetAngle)))
                rotateDir = -1;
        }
        else if (angleDiff < 0) {
            rotateDir = -1;
            if (getAngleDist(minAngle, targetAngle) < 0 &&
                    Math.abs(getAngleDist(maxAngle, targetAngle)) < Math.abs(getAngleDist(minAngle, targetAngle)))
                rotateDir = 1;
        }

        return rotateDir;
    }

    public void clampedRotate(int rotateDir, float targetAngle, float delta) {
        if (Math.abs(targetAngle - getRotation()) < turnSpeed * delta) {
            setRotation(targetAngle);
        } else {
            rotate(turnSpeed * delta * rotateDir);
        }

        setRotation(clampAngle(getRotation(), (minAngle >= 0 ? 0 : 180)));

        if (getRotation() > maxAngle)
            setRotation(maxAngle);

        if (getRotation() < minAngle)
            setRotation(minAngle);

        setRotation(clampAngle(getRotation(), 180));
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

    public static float getAngleDist(float angle1, float angle2) {
        float dist360 = 0;

        angle1 = clampAngle(angle1, 0);
        angle2 = clampAngle(angle2, 0);

        dist360 = angle2 - angle1;

        float dist180 = 0;

        angle1 = clampAngle(angle1, 180);
        angle2 = clampAngle(angle2, 180);

        dist180 = angle2 - angle1;

        return (Math.abs(dist360) < Math.abs(dist180) ? dist360 : dist180);
    }

    public static float clampAngle(float angle, float offset) {
        while (angle >= 360 - offset) angle -= 360;
        while (angle < 0 - offset) angle += 360;
        return angle;
    }
}
