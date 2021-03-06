package com.ahsgaming.starbattle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Created with IntelliJ IDEA.
 * User: jami
 * Date: 5/4/13
 * Time: 10:38 AM
 * To change this template use File | Settings | File Templates.
 */
public class ProgressBar {
    public static final String LOG = "ProgressBar";

    float current = 1;
    Color highColor = new Color(0, 1, 0, 1);
    Color medColor = new Color(1, 1, 0, 1);
    Color lowColor = new Color(1, 0, 0, 1);

    Texture img;

    Vector2 size;

    public ProgressBar() {
        super();
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(1, 1, 1, 1);
        pixmap.fill();
        img = new Texture(pixmap);
        size = new Vector2(1, 1);
    }

    public void draw(SpriteBatch batch, float x, float y, float parentAlpha) {
        Color color = lowColor;
        if (current >= 0.5f) color = highColor;
        else if (current >= 0.25f) color = medColor;

        batch.setColor(color);
        batch.getColor().a *= parentAlpha;
        batch.draw(img, x, y, getWidth() * current, getHeight());
    }

    public float getWidth() { return size.x; }

    public void setWidth(float width) { size.x = width; }

    public float getHeight() { return size.y; }

    public void setHeight(float height) { size.y = height; }

    public Vector2 getSize() { return size; }

    public void setSize(float width, float height) { size.set(width, height); }

    public float getCurrent() {
        return current;
    }

    public void setCurrent(float current) {
        this.current = current;
    }

    public Color getHighColor() {
        return highColor;
    }

    public void setHighColor(Color highColor) {
        this.highColor = highColor;
    }

    public Color getMedColor() {
        return medColor;
    }

    public void setMedColor(Color medColor) {
        this.medColor = medColor;
    }

    public Color getLowColor() {
        return lowColor;
    }

    public void setLowColor(Color lowColor) {
        this.lowColor = lowColor;
    }
}
