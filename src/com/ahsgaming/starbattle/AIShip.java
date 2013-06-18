package com.ahsgaming.starbattle;

import com.ahsgaming.starbattle.json.ShipLoader;
import com.badlogic.gdx.math.Vector2;

/**
 * starbattle
 * (c) 2013 Jami Couch
 * Created on 6/18/13 by jami
 * ahsgaming.com
 */
public class AIShip extends Ship {
    public static final String LOG = "AIShip";

    float sinceLastPathChange = 0;
    float pathChangeEverySec = 5;

    public AIShip(ShipLoader.JsonShip proto) {
        super(proto);
    }

    @Override
    public void update(float delta) {

        // choose movement target
        sinceLastPathChange += delta;
        if (moveTarget == null || sinceLastPathChange > pathChangeEverySec) {
            if (target != null) {
                moveTarget = new Vector2(target.getX() + target.getOriginX() + (float)(Math.random() * 200 - 100), target.getY() + target.getOriginY()  + (float)(Math.random() * 200 - 100));
            } else {
                moveTarget = null;
            }
            sinceLastPathChange = 0;
        }

        super.update(delta);
    }
}
