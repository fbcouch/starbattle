package com.ahsgaming.starbattle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Created with IntelliJ IDEA.
 * User: jami
 * Date: 6/14/13
 * Time: 3:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class GameObject extends Actor {
    public static final String LOG = "GameObject";

    Vector2 velocity;
    Vector2 acceleration;
    Vector2 moveTarget;

    float maxVelocity = 100;

    String image;

    TextureRegion region;

    public GameObject(String image) {
        velocity = new Vector2();
        acceleration = new Vector2();
        this.image = image;
    }

    public void init() {
        region = new TextureRegion(new Texture(Gdx.files.local("assets/" + this.image)));
        this.setBounds(0, 0, region.getRegionWidth(), region.getRegionHeight());
    }

    /* (non-Javadoc)
	 * @see com.badlogic.gdx.scenes.scene2d.Actor#draw(com.badlogic.gdx.graphics.g2d.SpriteBatch, float)
	 */
    @Override
    public void draw(SpriteBatch batch, float parentAlpha) {

        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

        if (region != null)
            batch.draw(region, getX(), getY(), getWidth() * 0.5f, getHeight() * 0.5f, getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());


    }

    public void update(float delta) {
        if (moveTarget != null) {
            if (new Rectangle(getX(), getY(), getWidth(), getHeight()).contains(moveTarget.x, moveTarget.y))
                clearMoveTarget();
            else {
                // get the direction vector for where to move
                Vector2 moveVector = new Vector2(moveTarget).sub(getX() + getWidth() * 0.5f, getY() + getHeight() * 0.5f);
                float angleToMove = moveVector.angle() - getRotation();

                // clamp this between [-180, 180] so we know which way to move
                while (angleToMove > 180) angleToMove -= 360;
                while (angleToMove < -180) angleToMove += 360;

                // rotate! TODO: create a property for rotation speed to use here
                if (Math.abs(angleToMove) <= 5)
                    setRotation(moveVector.angle());
                else if (angleToMove > 0)
                    setRotation((getRotation() + 5) % 360);
                else
                    setRotation((getRotation() - 5) % 360);
            }
        }
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity.set(velocity.x, velocity.y);
    }

    public Vector2 getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(Vector2 acceleration) {
        this.acceleration.set(acceleration.x, acceleration.y);
    }

    public Vector2 getMoveTarget() {
        return moveTarget;
    }

    public void setMoveTarget(Vector2 moveTarget) {
        this.moveTarget.set(moveTarget.x, moveTarget.y);
    }

    public void setMoveTarget(float x, float y) {
        if (moveTarget == null) moveTarget = new Vector2();
        this.moveTarget.set(x, y);
    }

    public void clearMoveTarget() {
        moveTarget = null;
    }

    public float getMaxVelocity() {
        return maxVelocity;
    }

    public void setMaxVelocity(float maxVelocity) {
        this.maxVelocity = maxVelocity;
    }

    public float getMaxVelocity2() {
        return maxVelocity * maxVelocity;
    }


}
