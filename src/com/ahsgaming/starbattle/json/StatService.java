package com.ahsgaming.starbattle.json;

import com.ahsgaming.starbattle.GameObject;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * starbattle
 * (c) 2013 Jami Couch
 * Created on 6/25/13 by jami
 * ahsgaming.com
 */
public class StatService {
    public static final String LOG = "StatService";

    Array<DamageEntry> statEntryArray;

    ObjectMap<GameObject, Integer> shotsFired;
    ObjectMap<GameObject, Integer> shotsHit;

    public StatService() {
        statEntryArray = new Array<DamageEntry>();
        shotsFired = new ObjectMap<GameObject, Integer>();
        shotsHit = new ObjectMap<GameObject, Integer>();
    }

    public void addDamageStat(GameObject damager, GameObject damaged, float amount) {
        statEntryArray.add(new DamageEntry(damager, damaged, amount));
    }

    public void shotFired(GameObject obj) {
        if (!shotsFired.containsKey(obj))
            shotsFired.put(obj, 0);
        shotsFired.put(obj, shotsFired.get(obj) + 1);
    }

    public void shotHit(GameObject obj) {
        if (!shotsHit.containsKey(obj))
            shotsHit.put(obj, 0);
        shotsHit.put(obj, shotsHit.get(obj) + 1);
    }

    public void reset() {
        statEntryArray.clear();
    }

    public float getDamageDealtBy(GameObject obj) {
        float sum = 0;
        for (DamageEntry se: statEntryArray)
            if (se.damager == obj) sum += se.amount;

        return sum;
    }

    public float getDamageTakenBy(GameObject obj) {
        float sum = 0;
        for (DamageEntry se: statEntryArray)
            if (se.damaged == obj) sum += se.amount;

        return sum;
    }

    public int getShotsFiredBy(GameObject obj) {
        return (shotsFired.containsKey(obj) ? shotsFired.get(obj) : 0);
    }

    public int getShotsHitBy(GameObject obj) {
        return (shotsHit.containsKey(obj) ? shotsHit.get(obj) : 0);
    }

    public static class DamageEntry {
        GameObject damager;
        GameObject damaged;
        float amount;

        public DamageEntry(GameObject damager, GameObject damaged, float amount) {
            this.damager = damager;
            this.damaged = damaged;
            this.amount = amount;
        }
    }

    public static class StatEntry {
        public float damageTaken, damageDealt;
        public int shotsTaken, shotsHit;

        public StatEntry() {

        }

        public StatEntry(float damageTaken, float damageDealt, int shotsTaken, int shotsHit) {
            this.damageTaken = damageTaken;
            this.damageDealt = damageDealt;
            this.shotsHit = shotsHit;
            this.shotsTaken = shotsTaken;
        }

        public StatEntry(Object json) {
            ObjectMap<String, Object> om = (ObjectMap<String, Object>)json;

            damageTaken = Utils.getFloatProperty(om, "damage-taken");
            damageDealt = Utils.getFloatProperty(om, "damage-dealt");
            shotsHit = Utils.getIntProperty(om, "shots-hit");
            shotsTaken = Utils.getIntProperty(om, "shots-taken");
        }

        @Override
        public String toString() {
            return "{" +
                    Utils.toJsonProperty("damage-taken", damageTaken) +
                    Utils.toJsonProperty("damage-dealt", damageDealt) +
                    Utils.toJsonProperty("shots-hit", shotsHit) +
                    Utils.toJsonProperty("shots-taken", shotsTaken) +
                    "}";
        }
    }
}
