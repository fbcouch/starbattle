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
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Created with IntelliJ IDEA.
 * User: jami
 * Date: 6/14/13
 * Time: 3:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class GameObject extends Group {
    public static final String LOG = "GameObject";

    final StarBattle game;

    Vector2 velocity;
    Vector2 acceleration;
    Vector2 moveTarget;

    float maxVelocity = 100;

    String image;

    TextureRegion region;
    Image rootImage;

    public GameObject(String image) {
        this.game = StarBattle.starBattle;

        velocity = new Vector2();
        acceleration = new Vector2();
        this.image = image;

    }

    public void init() {
        region = game.getTextureAtlas().createSprite(image);
        this.setBounds(0, 0, region.getRegionWidth(), region.getRegionHeight());
        rootImage = new Image(region);
        setOrigin(rootImage.getWidth() * 0.5f, rootImage.getHeight() * 0.5f);
        addActor(rootImage);
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


                // TODO improve pathing
                if (getRotation() == moveVector.angle()) {
                    setAcceleration(new Vector2(10, 0).rotate(getRotation()));
                } else {
                    if (velocity.len2() > 0) {
                        setAcceleration(new Vector2(-10, 0).rotate(velocity.angle()));
                    }
                }
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

    public Vector2 convertToParentCoords(Vector2 coords) {
        // convert to origin-normalized vector, rotate, convert to parental coords
        return new Vector2(coords).sub(getOriginX(), getOriginY()).rotate(getRotation()).add(getOriginX(), getOriginY()).add(getX(), getY());
    }
}
