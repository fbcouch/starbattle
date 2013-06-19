package com.ahsgaming.starbattle.json;

import com.ahsgaming.starbattle.StarBattle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * starbattle
 * (c) 2013 Jami Couch
 * Created on 6/18/13 by jami
 * ahsgaming.com
 */
public class ProfileService {
    public static final String LOG = "ProfileService";

    Array<Profile> profiles;
    String selected = "";

    public ProfileService(String file) {
        JsonReader reader = new JsonReader();
        ObjectMap<String, Object> json = (ObjectMap<String, Object>)reader.parse(Gdx.files.local("assets/profiles/" + file));

        selected = Utils.getStringProperty(json, "selected");

        profiles = new Array<Profile>();
        if (json.containsKey("profiles")) {
            Array<Object> pobj = (Array<Object>)json.get("profiles");
            for (Object o: pobj) {
                String profileFile = o.toString();
                profiles.add(new Profile((ObjectMap<String, Object>)reader.parse(Gdx.files.local("assets/profiles/" + profileFile + ".json"))));
            }
        }
    }

    public Profile getProfile(String id) {
        for (Profile p: profiles)
            if (p.id.equals(id)) return p;

        return null;
    }

    public Profile getSelectedProfile() {
        return getProfile(selected);
    }

    public void setSelectedProfile(String id) {
        selected = id;
    }

    public static class Profile {
        public String id;
        public Array<ShipLoader.JsonShip> ships;

        public Profile(ObjectMap<String, Object> json) {
            id = Utils.getStringProperty(json, "id");

            ships = new Array<ShipLoader.JsonShip>();
            if (json.containsKey("ships")) {
                Array<Object> objs = (Array<Object>) json.get("ships");
                for (Object o: objs) {
                    ObjectMap<String, Object> om = (ObjectMap<String, Object>)o;
                    ShipLoader.JsonShip ship = new ShipLoader.JsonShip(StarBattle.starBattle.getShipLoader().getJsonShip(
                            Utils.getStringProperty(om, "id")));
                    if (om.containsKey("name"))
                        ship.name = Utils.getStringProperty(om, "name");

                    if (om.containsKey("emplacements")) {
                        Array<Object> emplArray = (Array<Object>)om.get("emplacements");

                        for (int i = 0; i < emplArray.size; i++) {
                            if (i < ship.emplacements.size)
                                ship.emplacements.get(i).emplacement = emplArray.get(i).toString();
                        }
                    }
                    ships.add(ship);
                }
            }
        }
    }
}
