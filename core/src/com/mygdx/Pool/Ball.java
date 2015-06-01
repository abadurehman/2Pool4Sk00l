package com.mygdx.Pool;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.brashmonkey.spriter.Point;

/**
 * Created by SrinjoyMajumdar on 5/11/15.
 */
public class Ball extends Sprite {
    /**
     * Physical Ball number
     */
    private int numBall;
    /**
     * Sprite body of the ball
     */
    private Sprite ball;
    /**
     * Region of the packed image
     */
    private TextureAtlas atlas;
    /**
     * Skin of the game
     */
    private Skin skin;

    /**
     * Creates the ball
     * @param num number of the ball (both physically and in the arrawy)
     */
    public Ball(int num) {

        numBall = num;
        atlas = new TextureAtlas("PoolBalls.txt");
        skin = new Skin();
        skin.addRegions(atlas);
        ball = skin.getSprite(numBall + "");
        ball.setSize(25, 25);
        super.set(ball);

    }

    /**
     * Gets the distance between your ball, and ball1
     * @param ball1 the ball you want to measure to
     * @return double distance
     */
    public double getDistance(Ball ball1) {
        return Math.sqrt(Math.pow(ball.getOriginX() - ball1.getOriginX(), 2) + Math.pow(ball.getOriginY() - ball1.getOriginY(), 2));
    }

    /**
     * Gets the distance between your ball and another point
     * @param vector is the point that you want to find the distance to.
     * @return double distance
     */
    public double getDistance(Vector2 vector) {
        return vector.dst(ball.getOriginX(), ball.getOriginY());

    }

    /**
     * Gets the type of your ball, whether it's the cue ball, solid, or striped
     * @return a String with your ball-type
     */
    public String getType() {
        if (numBall < 8)
            return "solid";
        else if (numBall > 8)
            return "striped";
        else if (numBall == 0)
            return "cue";
        else
            return "8ball";
    }

    /**
     * Gets the x-coordinate of the center of your ball
     * @return double x-coordinate
     */
    public double getCenterX() {
        return (ball.getX() + ball.getWidth() / 2);
    }
    /**
     * Gets the y-coordinate of the center of your ball
     * @return double y-coordinate
     */
    public double getCenterY() {
        return (ball.getY() + ball.getWidth() / 2);
    }

    /**
     * Gets the number of your ball
     * @return integer number
     */
    public int getNum() {
        return numBall;
    }


}
