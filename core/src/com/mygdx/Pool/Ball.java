package com.mygdx.Pool;

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
    private int numBall;
    private Sprite ball;
    private TextureAtlas atlas;
    private Skin skin;


    public Ball(int num) {

        numBall = num;
        atlas = new TextureAtlas("PoolBalls.txt");
        skin = new Skin();
        skin.addRegions(atlas);
        ball = skin.getSprite(numBall + "");
        ball.setSize(25, 25);

        super.set(ball);

    }


    public double getDistance(Ball ball1) {
        return Math.sqrt(Math.pow(ball.getOriginX() - ball1.getOriginX(), 2) + Math.pow(ball.getOriginY() - ball1.getOriginY(), 2));
    }

    public double getDistance(Vector2 vector) {
        return vector.dst(ball.getOriginX(), ball.getOriginY());

    }

    public double getCenterX() {
        return (ball.getX() + ball.getWidth() / 2);
    }

    public double getCenterY() {
        return (ball.getY() + ball.getWidth() / 2);
    }


    public int getNum() {
        return numBall;
    }


}
