package com.ahsgaming.starbattle.json;

import com.ahsgaming.starbattle.GameObject;
import com.badlogic.gdx.utils.Array;

/**
 * starbattle
 * (c) 2013 Jami Couch
 * Created on 6/25/13 by jami
 * ahsgaming.com
 */
public class StatService {
    public static final String LOG = "StatService";

    Array<StatEntry> statEntryArray;

    public StatService() {
        statEntryArray = new Array<StatEntry>();
    }

    public void addStat(GameObject damager, GameObject damaged, float amount) {
        statEntryArray.add(new StatEntry(damager, damaged, amount));
    }

    public void reset() {
        statEntryArray.clear();
    }

    public float getDamageDealtBy(GameObject obj) {
        float sum = 0;
        for (StatEntry se: statEntryArray)
            if (se.damager == obj) sum += se.amount;

        return sum;
    }

    public float getDamageTakenBy(GameObject obj) {
        float sum = 0;
        for (StatEntry se: statEntryArray)
            if (se.damaged == obj) sum += se.amount;

        return sum;
    }

    public static class StatEntry {
        GameObject damager;
        GameObject damaged;
        float amount;

        public StatEntry(GameObject damager, GameObject damaged, float amount) {
            this.damager = damager;
            this.damaged = damaged;
            this.amount = amount;
        }
    }
}
