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

        version = Utils.getStringProperty(json, "version");

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
        public int cost, bounty;

        public JsonShip() {
            emplacements = new Array<JsonShipEmplacement>();
        }

        public JsonShip(ObjectMap<String, Object> json) {
            this();

            id =Utils.getStringProperty(json, "id");
            name = Utils.getStringProperty(json, "name");
            desc = Utils.getStringProperty(json, "desc");
            image = Utils.getStringProperty(json, "image");
            speed = Utils.getFloatProperty(json, "speed");
            turnSpeed = Utils.getFloatProperty(json, "turn-speed");
            accel = Utils.getFloatProperty(json, "accel");
            hull = Utils.getFloatProperty(json, "hull");
            hullRegen = Utils.getFloatProperty(json, "hull-regen");
            shield = Utils.getFloatProperty(json, "shield");
            shieldRegen = Utils.getFloatProperty(json, "shield-regen");
            armor = Utils.getFloatProperty(json, "armor");
            cost = Utils.getIntProperty(json, "cost");
            bounty = Utils.getIntProperty(json, "bounty");

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
        public Array<JsonEmplacementProjectile> projectiles;
        public int originX, originY;

        public JsonEmplacement(ObjectMap<String, Object> json) {
            id = Utils.getStringProperty(json, "id");
            name = Utils.getStringProperty(json, "name");
            desc = Utils.getStringProperty(json, "desc");
            image = Utils.getStringProperty(json, "image");
            type = Utils.getStringProperty(json, "type");
            turnSpeed = Utils.getFloatProperty(json, "turn-speed");
            ammo = Utils.getFloatProperty(json, "ammo");
            maxAmmo = Utils.getFloatProperty(json, "max-ammo");
            ammoRegen = Utils.getFloatProperty(json, "ammo-regen");
            fireRate = Utils.getFloatProperty(json, "fire-rate");

            projectiles = new Array<JsonEmplacementProjectile>();
            if (json.containsKey("projectiles")) {
                Array<Object> proj = (Array<Object>)json.get("projectiles");
                for (Object o: proj)
                    projectiles.add(new JsonEmplacementProjectile((ObjectMap<String, Object>)o));
            }

            originX = Utils.getIntProperty(json, "origin-x");
            originY = Utils.getIntProperty(json, "origin-y");
        }
    }

    public static class JsonShipEmplacement {
        public String emplacement = "";
        public float x, y;
        public float angleMin, angleMax;
        public Array<String> fits;

        public JsonShipEmplacement(ObjectMap<String, Object> json) {
            emplacement = Utils.getStringProperty(json, "emplacement");
            x = Utils.getFloatProperty(json, "x");
            y = Utils.getFloatProperty(json, "y");

            angleMin = Utils.getFloatProperty(json, "angle-min", -180);
            angleMax = Utils.getFloatProperty(json, "angle-max", 180);

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
            angleMin = jse.angleMin;
            angleMax = jse.angleMax;
            fits = new Array<String>();
            fits.addAll(jse.fits);
        }
    }

    public static class JsonProjectile {
        public String id = "", image = "";
        public float initSpeed, maxSpeed, accel, lifetime, damage;

        public JsonProjectile(ObjectMap<String, Object> json) {
            id = Utils.getStringProperty(json, "id");
            image = Utils.getStringProperty(json, "image");
            initSpeed = Utils.getFloatProperty(json, "init-speed");
            maxSpeed = Utils.getFloatProperty(json, "max-speed");
            accel = Utils.getFloatProperty(json, "accel");
            lifetime = Utils.getFloatProperty(json, "lifetime");
            damage = Utils.getFloatProperty(json, "damage");
        }
    }

    public static class JsonEmplacementProjectile {
        public String id = "";
        public int x, y;

        public JsonEmplacementProjectile(ObjectMap<String, Object> json) {
            id = Utils.getStringProperty(json, "id");
            x = Utils.getIntProperty(json, "x");
            y = Utils.getIntProperty(json, "y");
        }
    }
}
