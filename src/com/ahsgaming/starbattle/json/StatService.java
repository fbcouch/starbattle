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
    Array<KillEntry> killEntryArray;

    ObjectMap<GameObject, Integer> shotsFired;
    ObjectMap<GameObject, Integer> shotsHit;

    public StatService() {
        statEntryArray = new Array<DamageEntry>();
        killEntryArray = new Array<KillEntry>();
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

    public void addKill(GameObject killer, GameObject victim) {
        killEntryArray.add(new KillEntry(killer, victim));
    }

    public void reset() {
        statEntryArray.clear();
        killEntryArray.clear();
        shotsFired.clear();
        shotsHit.clear();
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

    public int getKillsFor(GameObject obj) {
        int sum = 0;
        for (KillEntry ke: killEntryArray)
            if (ke.killer == obj) sum++;
        return sum;
    }

    public Array<KillEntry> getKillArrayFor(GameObject obj) {
        Array<KillEntry> killArray = new Array<KillEntry>();
        for (KillEntry ke: killEntryArray)
            if (ke.killer == obj) killArray.add(ke);
        return killArray;
    }

    public int getDeathsFor(GameObject obj) {
        int sum = 0;
        for (KillEntry ke: killEntryArray)
            if (ke.victim == obj) sum++;
        return sum;
    }

    public StatEntry getStatsForObject(GameObject obj) {
        return new StatEntry(getDamageTakenBy(obj), getDamageDealtBy(obj), getShotsFiredBy(obj), getShotsHitBy(obj),
                             getKillsFor(obj), getDeathsFor(obj));
    }

    public static class KillEntry {
        public GameObject killer;
        public GameObject victim;

        public KillEntry(GameObject killer, GameObject victim) {
            this.killer = killer;
            this.victim = victim;
        }
    }

    public static class DamageEntry {
        public GameObject damager;
        public GameObject damaged;
        public float amount;

        public DamageEntry(GameObject damager, GameObject damaged, float amount) {
            this.damager = damager;
            this.damaged = damaged;
            this.amount = amount;
        }
    }

    public static class StatEntry {
        public float damageTaken, damageDealt;
        public int shotsTaken, shotsHit, kills, deaths;

        public StatEntry() {

        }

        public StatEntry(float damageTaken, float damageDealt, int shotsTaken, int shotsHit, int kills, int deaths) {
            this.damageTaken = damageTaken;
            this.damageDealt = damageDealt;
            this.shotsHit = shotsHit;
            this.shotsTaken = shotsTaken;
            this.kills = kills;
            this.deaths = deaths;
        }

        public StatEntry(Object json) {
            ObjectMap<String, Object> om = (ObjectMap<String, Object>)json;

            damageTaken = Utils.getFloatProperty(om, "damage-taken");
            damageDealt = Utils.getFloatProperty(om, "damage-dealt");
            shotsHit = Utils.getIntProperty(om, "shots-hit");
            shotsTaken = Utils.getIntProperty(om, "shots-taken");
            kills = Utils.getIntProperty(om, "kills");
            deaths = Utils.getIntProperty(om, "deaths");
        }

        public StatEntry add(StatEntry other) {
            damageTaken += other.damageTaken;
            damageDealt += other.damageDealt;
            shotsHit += other.shotsHit;
            shotsTaken += other.shotsTaken;
            kills += other.kills;
            deaths += other.deaths;
            return this;
        }

        @Override
        public String toString() {
            return "{" +
                    Utils.toJsonProperty("damage-taken", damageTaken) +
                    Utils.toJsonProperty("damage-dealt", damageDealt) +
                    Utils.toJsonProperty("shots-hit", shotsHit) +
                    Utils.toJsonProperty("shots-taken", shotsTaken) +
                    Utils.toJsonProperty("kills", kills) +
                    Utils.toJsonProperty("deaths", deaths) +
                    "}";
        }
    }
}
