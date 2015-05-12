package com.mygdx.Pool;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

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
        ball.setScale((float) 0.1);
        super.set(ball);

    }

    public double getDistance(Ball ball1) {
        return Math.sqrt(Math.pow(ball.getOriginX() - ball1.getOriginX(), 2) + Math.pow(ball.getOriginY() - ball1.getOriginY(), 2));
    }

    public int getNum() {
        return numBall;
    }


}
