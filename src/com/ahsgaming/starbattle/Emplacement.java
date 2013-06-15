package com.ahsgaming.starbattle;

/**
 * Created with IntelliJ IDEA.
 * User: jami
 * Date: 6/15/13
 * Time: 9:47 AM
 * To change this template use File | Settings | File Templates.
 */
public class Emplacement extends GameObject {
    public Emplacement(String image) {
        super(image);
    }

    @Override
    public void init() {
        super.init();

        setOrigin(10, 10);
    }
}
