package com.ahsgaming.starbattle;

import com.ahsgaming.starbattle.json.ShipLoader;
import com.badlogic.gdx.math.Vector2;

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

    float damage, lifetime;
    GameObject owner;

    public Projectile(GameObject owner, String image) {
        super(image);
        this.owner = owner;
    }

    public Projectile(GameObject owner, ShipLoader.JsonProjectile proto) {
        this(owner, proto.image);
        this.proto = proto;

        this.damage = proto.damage;
        this.maxAccel = proto.accel;
        this.maxSpeed = proto.maxSpeed;
        this.lifetime = proto.lifetime;
    }

    @Override
    public void init() {
        super.init();

        this.velocity = new Vector2(proto.initSpeed, 0).rotate(getRotation());
        this.acceleration = new Vector2(proto.accel, 0).rotate(getRotation());
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        lifetime -= delta;
        if (lifetime <= 0)
            setRemove(true);

    }

    @Override
    public boolean canCollide(GameObject other) {
        if (other instanceof Ship && owner != other)
            return true;
        return super.canCollide(other);
    }

    @Override
    public void collide(GameObject other) {
        super.collide(other);

        other.takeDamage(damage, (this.owner != null ? this.owner : this));

        setRemove(true);
    }
}
