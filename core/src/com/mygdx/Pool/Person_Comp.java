package com.mygdx.Pool;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;

/**
 * Created by SrinjoyMajumdar on 5/4/15.
 */
public class Person_Comp extends GameScreen {

    private final float PIXELS_TO_METERS = 100f;
    private ArrayList<Ball> poolBalls;
    private ArrayList<Body> ballBodies;
    private PoolTable table;
    private World world;
    private ShapeRenderer shapeRenderer;
    private Box2DDebugRenderer debugRenderer;
    private Matrix4 debugMatrix;
    private OrthographicCamera camera;
    private Rectangle t;

    private int POWER_REDUCTION = 6000;

    private int screenHeight;
    private int screenWidth;
    private Ball cue;
    private float lineX;
    private float lineY;
    private boolean shot;
    private boolean firstContact = false;

    Player currPlayer;
    Player watchPlayer;
    boolean aligned = false;
    int shotNum = 0;
    int numPocketed = 0;

    boolean cuePocket = false;

    boolean brake = true;


    private Player player1 = new Player("p1", 1);
    private Player AI = new Player("p2", 2);
    public Person_Comp(final Pool gam) {


        super(gam);
    }

    public void render(float delta) {
        cue = super.cue;
        poolBalls = super.poolBalls;
        POWER_REDUCTION = super.POWER_REDUCTION;
        ballBodies = super.ballBodies;
        AI ai = new AI();
        Vector2 targetCenter = ai.choice(super.poolBalls, super.currPlayer, super.table);

        if (super.currPlayer.getPlayerNumber() == 2 && !super.moving()) {
            Vector2 line = new Vector2(targetCenter.x - cue.getHeight() / 2, targetCenter.y - cue.getHeight() / 2);
            Vector2 power = new Vector2(-(poolBalls.get(0).getX() - targetCenter.x) / POWER_REDUCTION,
                    -(poolBalls.get(0).getY() - line.y) / POWER_REDUCTION);
            Vector2 position = new Vector2(ballBodies.get(0).getPosition().x + cue.getWidth() / 2,
                    ballBodies.get(0).getPosition().y + cue.getWidth() / 2);
            super.line = line;
            super.power = power;
            super.position = position;
            super.AI = true;
            super.aligned = true;

            float powerShot = (float) ai.minDist / (super.table.getTableInside().getWidth() / 1.5f);

            super.table.getSlider().setValue(0.6f);
        } else
            super.AI = false;

        super.render(delta);
    }


}

