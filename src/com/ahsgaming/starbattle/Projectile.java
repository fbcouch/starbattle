package com.ahsgaming.starbattle;

import com.ahsgaming.starbattle.json.ShipLoader;

/**
 * Created with IntelliJ IDEA.
 * User: jami
 * Date: 6/16/13
 * Time: 11:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class Projectile extends GameObject {
    public static final String LOG = "Projectile";

    ShipLoader.JsonProjectile proto;

    float damage;
    GameObject owner;

    public Projectile(String image) {
        super(image);
    }

    public Projectile(ShipLoader.JsonProjectile proto) {
        this(proto.image);
        this.proto = proto;


    }
}
