package com.ahsgaming.starbattle.json;

import com.ahsgaming.starbattle.StarBattle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.ObjectMap;

import java.io.IOException;

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

    public Array<Profile> getProfiles() {
        Array<Profile> profiles = new Array<Profile>(this.profiles.size);
        profiles.addAll(this.profiles);
        return profiles;
    }

    public void setSelectedProfile(String id) {
        selected = id;
    }

    public void saveProfiles() {
        PrettyJsonWriter writer = new PrettyJsonWriter(Gdx.files.local("assets/profiles/profiles.json").writer(false));

        String json = "{" + Utils.toJsonProperty("selected", selected);
        json += "profiles: [";
        for (Profile p: profiles) {
            json += "\"" + p.id + "\",";
        }
        json += "],";
        json += "}";

        try {
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Profile p: profiles) {
            writer = new PrettyJsonWriter(Gdx.files.local("assets/profiles/" + p.id + ".json").writer(false));
            try {
                writer.write(p.toString());
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static class Profile {
        public String id;
        public Array<ShipLoader.JsonShip> ships;
        public Array<StatService.StatEntry> shipStats;
        public StatService.StatEntry stats;

        public Profile(ObjectMap<String, Object> json) {
            id = Utils.getStringProperty(json, "id");

            ships = new Array<ShipLoader.JsonShip>();
            shipStats = new Array<StatService.StatEntry>();
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
                    StatService.StatEntry se;
                    if (om.containsKey("stats"))
                        se = new StatService.StatEntry(om.get("stats"));
                    else
                        se = new StatService.StatEntry();
                    shipStats.add(se);
                }
            }

            if (json.containsKey("stats"))
                stats = new StatService.StatEntry(json.get("stats"));
            else
                stats = new StatService.StatEntry();
        }

        @Override
        public String toString() {
            String retString = "{";

            retString += Utils.toJsonProperty("id", id);
            retString += "ships: [";

            for (int i=0; i < ships.size; i++) {
                ShipLoader.JsonShip ship = ships.get(i);
                retString += "{";
                retString += Utils.toJsonProperty("id", ship.id);
                retString += Utils.toJsonProperty("name", ship.name);
                retString += "emplacements: [";
                for (ShipLoader.JsonShipEmplacement emp: ship.emplacements) {
                    retString += "\"" + emp.emplacement + "\",";
                }
                retString += "],";

                retString += Utils.toJsonProperty("stats", shipStats.get(i));

                retString += "},";
            }

            retString += "],";

            retString += Utils.toJsonProperty("stats", stats);

            retString += "}";
            return retString;
        }
    }
}
