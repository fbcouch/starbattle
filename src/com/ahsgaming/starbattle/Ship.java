package com.ahsgaming.starbattle;

import com.ahsgaming.starbattle.json.ShipLoader;
import com.badlogic.gdx.graphics.Color;
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

    float maxHull, curHull, regenHull;
    float maxShield, curShield, regenShield;
    float armor;

    ProgressBar healthBar, shieldBar;

    Array<Emplacement> emplacements;

    ShipLoader.JsonShip proto;

    Ship target;

    public Ship(String image) {
        super(image);

        emplacements = new Array<Emplacement>();

        maxHull = 100;
        curHull = maxHull;

        maxShield = 100;
        curShield = maxShield;

        armor = 0;
    }

    public Ship(ShipLoader.JsonShip proto) {
        this(proto.image);
        this.proto = proto;

        maxSpeed = proto.speed;
        turnSpeed = proto.turnSpeed;
        maxAccel = proto.accel;

        maxHull = proto.hull;
        curHull = proto.hull;
        regenHull = proto.hullRegen;
        maxShield = proto.shield;
        curShield = proto.shield;
        regenShield = proto.shieldRegen;
        armor = proto.armor;
    }


    @Override
    public void init() {
        super.init();

        healthBar = new ProgressBar();
        healthBar.setSize(100, 2);
        shieldBar = new ProgressBar();
        shieldBar.setSize(100, 2);
        shieldBar.setHighColor(new Color(0, 0.5f, 1, 1));
        shieldBar.setMedColor(shieldBar.getHighColor());
        shieldBar.setLowColor(shieldBar.getHighColor());

        emplacements.clear();

        for (ShipLoader.JsonShipEmplacement jse: proto.emplacements) {
            Emplacement e = new Emplacement(game.getShipLoader().getJsonEmplacement(jse.emplacement));
            e.init();
            e.setPosition(jse.x - e.getWidth() * 0.5f, jse.y - e.getHeight() * 0.5f);
            addEmplacement(e);
        }
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        float rangesq = 0;
        for (Emplacement emp: emplacements) {
            float er = emp.getRangeSq();
            if (er > rangesq) rangesq = er;
        }

        Array<Ship> possibleTargets = new Array<Ship>();
        for (GameObject obj: game.getGameController().getGameObjects()) {
            if (obj instanceof Ship && obj != this && GameObject.getDistanceSq(this, obj) <= rangesq) {
                possibleTargets.add((Ship)obj);
            }
        }

        target = null;
        if (possibleTargets.size > 0) target = possibleTargets.get(0);

        for (Emplacement emp: emplacements) {
            emp.update(delta);
            emp.setTarget(target);
        }

        if (curHull <= 0) {
            setRemove(true);
            for (Emplacement emp: emplacements) emp.setRemove(true);
        }
    }

    @Override
    public void draw(SpriteBatch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        float hullPct = curHull / maxHull;
        healthBar.setCurrent(hullPct);
        float shieldPct = curShield / maxShield;
        shieldBar.setCurrent(shieldPct);

        healthBar.draw(batch, getX() + (getWidth() - healthBar.getWidth()) * 0.5f, getY(), parentAlpha);
        shieldBar.draw(batch, getX() + (getWidth() - shieldBar.getWidth()) * 0.5f, getY() + 3, parentAlpha);
    }

    @Override
    public boolean canCollide(GameObject other) {
        return true;
    }

    @Override
    public void collide(GameObject other) {
        super.collide(other);
    }

    @Override
    public void takeDamage(float amount) {
        super.takeDamage(amount);

        amount -= armor;
        if (amount > 0) {
            curShield -= amount;
            if (curShield < 0) {
                curHull += curShield;
                curShield = 0;
            }
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

    public float getMaxHull() {
        return maxHull;
    }

    public void setMaxHull(float maxHull) {
        this.maxHull = maxHull;
    }

    public float getCurHull() {
        return curHull;
    }

    public void setCurHull(float curHull) {
        this.curHull = curHull;
    }

    public float getRegenHull() {
        return regenHull;
    }

    public void setRegenHull(float regenHull) {
        this.regenHull = regenHull;
    }

    public float getMaxShield() {
        return maxShield;
    }

    public void setMaxShield(float maxShield) {
        this.maxShield = maxShield;
    }

    public float getCurShield() {
        return curShield;
    }

    public void setCurShield(float curShield) {
        this.curShield = curShield;
    }

    public float getRegenShield() {
        return regenShield;
    }

    public void setRegenShield(float regenShield) {
        this.regenShield = regenShield;
    }

    public float getArmor() {
        return armor;
    }

    public void setArmor(float armor) {
        this.armor = armor;
    }
}
