package com.ahsgaming.starbattle.tests;

import com.ahsgaming.starbattle.Emplacement;
import com.ahsgaming.starbattle.StarBattle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * starbattle
 * (c) 2013 Jami Couch
 * Created on 6/27/13 by jami
 * ahsgaming.com
 */
public class EmplacementTests {
    @Test
    public void testGetTargetAngle() throws Exception {

    }

    @Test
    public void testGetRotateDir() throws Exception {
        // simplest test: no constraints

    }

    @Test
    public void testClampedRotate() throws Exception {

    }

    @Test
    public void testGetAngleDist() throws Exception {

        for (float i=0; i<8; i++)
        {
            float angle1 = i * 45;
            float angle1c = Emplacement.clampAngle(angle1, 180);
            for (float j=0; j<8;j++) {
                float angle2 = j * 45;
                float angle2c = Emplacement.clampAngle(angle2, 180);
                // test 0-360 clamped angles, ccw
                assertEquals(
                        Emplacement.clampAngle(angle2 - angle1, 0),
                        Emplacement.clampAngle(Emplacement.getAngleDist(angle1, angle2), 0)
                );
                // test 0-360 clamped angles, cw
                assertEquals(
                        Emplacement.clampAngle(angle1 - angle2, 0),
                        Emplacement.clampAngle(Emplacement.getAngleDist(angle2, angle1), 0)
                );
                // test -180-180 clamped angles, ccw
                assertEquals(
                        Emplacement.clampAngle(angle2 - angle1, 0),
                        Emplacement.clampAngle(Emplacement.getAngleDist(angle1c, angle2c), 0)
                );
                // test -180-180 clamped angles, cw
                assertEquals(
                        Emplacement.clampAngle(angle1 - angle2, 0),
                        Emplacement.clampAngle(Emplacement.getAngleDist(angle2c, angle1c), 0)
                );
            }
        }
    }

    @Test
    public void testClampAngle() throws Exception {

        for (float i = 0; i < 360; i++)
            assertEquals(i, Emplacement.clampAngle(i, 0));

        for (float i = 360; i < 720; i++)
            assertEquals(i % 360, Emplacement.clampAngle(i, 0));

        for (float i = -360; i < 0; i++)
            assertEquals(Emplacement.clampAngle(i, 0), i + 360);

        for (float i = -180; i < 180; i++)
            assertEquals(i, Emplacement.clampAngle(i, 180));

        for (float i = 181; i < 360; i++)
            assertEquals(-1 * (360 - i), Emplacement.clampAngle(i, 180));
    }
}
