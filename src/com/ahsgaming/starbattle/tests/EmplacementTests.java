package com.ahsgaming.starbattle.tests;

import com.ahsgaming.starbattle.Emplacement;
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

    }

    @Test
    public void testClampedRotate() throws Exception {

    }

    @Test
    public void testSetAngleConstraint() throws Exception {

    }

    @Test
    public void testGetAngleDist() throws Exception {

    }

    @Test
    public void testClampAngle() throws Exception {

        for (int i = 0; i < 720; i++)
            assertEquals(Emplacement.clampAngle(i, 0), (float)(i % 360));

        for (int i = -360; i < 0; i++)
            assertEquals(Emplacement.clampAngle(i, 0), i + 360);

        for (int i = -180; i < 180; i++)
            assertEquals(Emplacement.clampAngle(i, 180), i);

        for (int i = 181; i < 360; i++)
            assertEquals(Emplacement.clampAngle(i, 180), -1 * (360 - i));
    }
}
