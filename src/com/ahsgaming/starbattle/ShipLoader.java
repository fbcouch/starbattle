package com.ahsgaming.starbattle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * Created with IntelliJ IDEA.
 * User: jami
 * Date: 6/17/13
 * Time: 3:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class ShipLoader {
    public static final String LOG = "ShipLoader";

    String version;

    Array<JsonShip> jsonShips;
    Array<JsonEmplacement> jsonEmplacements;
    Array<JsonProjectile> jsonProjectiles;

    public ShipLoader(String file) {
        JsonReader jsonReader = new JsonReader();
        ObjectMap<String, Object> json = (ObjectMap<String, Object>)jsonReader.parse(Gdx.files.local("assets/" + file));

        version = getStringProperty(json, "version");

        jsonShips = new Array<JsonShip>();
        if (json.containsKey("ships")) {
            Array<Object> ships = (Array<Object>)json.get("ships");
            for (Object shipobj: ships)
                jsonShips.add(new JsonShip((ObjectMap<String, Object>) shipobj));
        }

        jsonEmplacements = new Array<JsonEmplacement>();
        if (json.containsKey("emplacements")) {
            Array<Object> emps = (Array<Object>)json.get("emplacements");
            for (Object empobj: emps)
                jsonEmplacements.add(new JsonEmplacement((ObjectMap<String, Object>) empobj));
        }

        jsonProjectiles = new Array<JsonProjectile>();
        if (json.containsKey("projectiles")) {
            Array<Object> projs = (Array<Object>)json.get("projectiles");
            for (Object projobj: projs)
                jsonProjectiles.add(new JsonProjectile((ObjectMap<String, Object>) projobj));
        }
    }

    public static class JsonShip {
        String id = "";
        String name = "", desc = "";
        String image = "";
        float speed, turnSpeed, accel, hull, hullRegen, shield, shieldRegen, armor;
        Array<Object> emplacements;

        public JsonShip() {
            emplacements = new Array<Object>();
        }

        public JsonShip(ObjectMap<String, Object> json) {
            this();

            id = ShipLoader.getStringProperty(json, "id");
            name = ShipLoader.getStringProperty(json, "name");
            desc = ShipLoader.getStringProperty(json, "desc");
            image = ShipLoader.getStringProperty(json, "image");
            speed = ShipLoader.getFloatProperty(json, "speed");
            turnSpeed = ShipLoader.getFloatProperty(json, "turn-speed");
            accel = ShipLoader.getFloatProperty(json, "accel");
            hull = ShipLoader.getFloatProperty(json, "hull");
            hullRegen = ShipLoader.getFloatProperty(json, "hull-regen");
            shield = ShipLoader.getFloatProperty(json, "shield");
            shieldRegen = ShipLoader.getFloatProperty(json, "shield-regen");
            armor = ShipLoader.getFloatProperty(json, "armor");

            if (json.containsKey("emplacements"))
                emplacements.addAll((Array<Object>)json.get("emplacements"));
        }
    }

    public static class JsonEmplacement {
        String id = "", name = "", desc = "", image = "", type = "";
        float turnSpeed, ammo, maxAmmo, ammoRegen, fireRate;
        String projectile = "";

        public JsonEmplacement(ObjectMap<String, Object> json) {
            id = ShipLoader.getStringProperty(json, "id");
            name = ShipLoader.getStringProperty(json, "name");
            desc = ShipLoader.getStringProperty(json, "desc");
            image = ShipLoader.getStringProperty(json, "image");
            type = ShipLoader.getStringProperty(json, "type");
            turnSpeed = ShipLoader.getFloatProperty(json, "turn-speed");
            ammo = ShipLoader.getFloatProperty(json, "ammo");
            maxAmmo = ShipLoader.getFloatProperty(json, "max-ammo");
            ammoRegen = ShipLoader.getFloatProperty(json, "ammo-regen");
            fireRate = ShipLoader.getFloatProperty(json, "fire-rate");
            projectile = ShipLoader.getStringProperty(json, "projectile");
        }
    }

    public static class JsonProjectile {
        String id = "", image = "";
        float initSpeed, maxSpeed, accel, lifetime, damage;

        public JsonProjectile(ObjectMap<String, Object> json) {
            id = ShipLoader.getStringProperty(json, "id");
            image = ShipLoader.getStringProperty(json, "image");
            initSpeed = ShipLoader.getFloatProperty(json, "init-speed");
            maxSpeed = ShipLoader.getFloatProperty(json, "max-speed");
            accel = ShipLoader.getFloatProperty(json, "accel");
            lifetime = ShipLoader.getFloatProperty(json, "lifetime");
            damage = ShipLoader.getFloatProperty(json, "damage");
        }
    }

    public static String getStringProperty(ObjectMap<String, Object> json, String id) {
        if (json.containsKey(id))
            return json.get(id).toString();
        return "";
    }

    public static float getFloatProperty(ObjectMap<String, Object> json, String id) {
        if (json.containsKey(id))
            return Float.parseFloat(json.get(id).toString());
        return 0;
    }
}
