package com.ahsgaming.starbattle;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
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

    float maxSpeed, maxAccel, turnSpeed;

    String image;

    TextureRegion region;
    Image rootImage;

    boolean remove = false;

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
                Vector2 moveVector = rotateToward(delta, moveTarget);

                // TODO improve pathing
                if (getRotation() == moveVector.angle()) {
                    setAcceleration(new Vector2(maxAccel, 0).rotate(getRotation()));
                } else {
                    if (velocity.len2() > 0) {
                        setAcceleration(new Vector2(-1 * maxAccel, 0).rotate(velocity.angle()));
                    }
                }
            }
        }
    }

    protected Vector2 rotateToward(float delta, Vector2 target) {
        return rotateToward(delta, target, 0);
    }

    protected Vector2 rotateToward(float delta, Vector2 target, float rotationOffset) {
        // get the direction vector for where to move
        Vector2 moveVector = new Vector2(target).sub(getX() + getWidth() * 0.5f, getY() + getHeight() * 0.5f);
        float angleToMove = moveVector.angle() - getRotation() - rotationOffset;

        // clamp this between [-180, 180] so we know which way to move
        while (angleToMove > 180) angleToMove -= 360;
        while (angleToMove < -180) angleToMove += 360;

        // rotate!
        if (Math.abs(angleToMove) <= turnSpeed * delta)
            setRotation(moveVector.angle() - rotationOffset);
        else if (angleToMove > 0)
            setRotation((getRotation() + turnSpeed * delta) % 360);
        else
            setRotation((getRotation() - turnSpeed * delta) % 360);

        while (getRotation() < 0) rotate(360);
        while (getRotation() > 360) rotate(-360);

        return moveVector.rotate(-1 * rotationOffset);
    }

    public boolean canCollide(GameObject other) {
        return false;
    }

    public void collide(GameObject other) {

    }

    public void takeDamage(float amount) {

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

    public float getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public float getMaxVelocity2() {
        return maxSpeed * maxSpeed;
    }

    public Vector2 convertToParentCoords(Vector2 coords) {
        // convert to origin-normalized vector, rotate, convert to parental coords
        return new Vector2(coords).sub(getOriginX(), getOriginY()).rotate(getRotation()).add(getOriginX(), getOriginY()).add(getX(), getY());
    }

    public boolean isRemove() {
        return remove;
    }

    public void setRemove(boolean remove) {
        this.remove = remove;
    }

    public static boolean collideRect(GameObject obj1, GameObject obj2) {
        return !(obj1.getX() > obj2.getRight() || obj2.getX() > obj1.getRight()
        || obj1.getY() > obj2.getTop() || obj2.getY() > obj1.getTop());
    }

    public static float getDistanceSq(GameObject obj1, GameObject obj2) {
        return (float)(Math.pow((obj1.getX() + obj1.getWidth() * 0.5f) - (obj2.getX() + obj2.getWidth() * 0.5f), 2)
                + Math.pow((obj1.getY() + obj1.getHeight() * 0.5f) - (obj2.getY() + obj2.getHeight() * 0.5f), 2));
    }
}
