package com.ahsgaming.starbattle.json;

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

    public JsonShip getJsonShip(String id) {
        for (JsonShip js: jsonShips) {
            if (js.id.equals(id)) return js;
        }
        return null;
    }

    public JsonEmplacement getJsonEmplacement(String id) {
        for (JsonEmplacement je: jsonEmplacements) {
            if (je.id.equals(id)) return je;
        }
        return null;
    }

    public JsonProjectile getJsonProjectile(String id) {
        for (JsonProjectile jp: jsonProjectiles) {
            if (jp.id.equals(id)) return jp;
        }
        return null;
    }

    public static class JsonShip {
        public String id = "";
        public String name = "", desc = "";
        public String image = "";
        public float speed, turnSpeed, accel, hull, hullRegen, shield, shieldRegen, armor;
        public Array<JsonShipEmplacement> emplacements;

        public JsonShip() {
            emplacements = new Array<JsonShipEmplacement>();
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

            if (json.containsKey("emplacements")) {
                Array<Object> emps = (Array<Object>)json.get("emplacements");
                emplacements.clear();
                for (Object emp: emps) {
                    emplacements.add(new JsonShipEmplacement((ObjectMap<String, Object>) emp));
                }
            }
        }

        public JsonShip(JsonShip ship) {
            id = ship.id;
            name = ship.name;
            desc = ship.desc;
            image = ship.image;
            speed = ship.speed;
            turnSpeed = ship.turnSpeed;
            accel = ship.accel;
            hull = ship.hull;
            hullRegen = ship.hullRegen;
            shield = ship.shield;
            shieldRegen = ship.shieldRegen;
            armor = ship.armor;

            emplacements = new Array<JsonShipEmplacement>();
            for (JsonShipEmplacement jse: ship.emplacements) {
                emplacements.add(new JsonShipEmplacement(jse));
            }
        }
    }

    public static class JsonEmplacement {
        public String id = "", name = "", desc = "", image = "", type = "";
        public float turnSpeed, ammo, maxAmmo, ammoRegen, fireRate;
        public String projectile = "";

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

    public static class JsonShipEmplacement {
        public String emplacement = "";
        public float x, y;
        public Array<String> fits;

        public JsonShipEmplacement(ObjectMap<String, Object> json) {
            emplacement = ShipLoader.getStringProperty(json, "emplacement");
            x = ShipLoader.getFloatProperty(json, "x");
            y = ShipLoader.getFloatProperty(json, "y");
            fits = new Array<String>();
            if (json.containsKey("fits")) {
                Array<Object> f = (Array<Object>)json.get("fits");
                for (Object fo: f)
                    fits.add(fo.toString());
            }
        }

        public JsonShipEmplacement(JsonShipEmplacement jse) {
            emplacement = jse.emplacement;
            x = jse.x;
            y = jse.y;
            fits = new Array<String>();
            fits.addAll(jse.fits);
        }
    }

    public static class JsonProjectile {
        public String id = "", image = "";
        public float initSpeed, maxSpeed, accel, lifetime, damage;

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
