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
    /**
     * Pixel to meters converstion
     */
    private final float PIXELS_TO_METERS = 100f;
    /**
     * Arraylist of pool balls objects
     */
    private ArrayList<Ball> poolBalls;
    /**
     * Arraylist of ball bodies
     */
    private ArrayList<Body> ballBodies;
    /**
     * Table
     */
    private PoolTable table;
    /**
     * World of existence
     */
    private World world;
    /**
     * Draws a line with a specific width
     */
    private ShapeRenderer shapeRenderer;
    /**
     * Tests the game, and ignores sprites
     */
    private Box2DDebugRenderer debugRenderer;
    /**
     * 4x4 Matrix used to debug
     */
    private Matrix4 debugMatrix;
    /**
     * Your View of the game
     */
    private OrthographicCamera camera;
    /**
     * New rectangle
     */
    private Rectangle t;
    /**
     * Power reduction from input
     */
    private int POWER_REDUCTION = 6000;

    private int screenHeight;
    /**
     * Width of the screen
     */
    private int screenWidth;
    /**
     * cue ball
     */
    Ball cue;
    /**
     * X coordinate of the cue ball
     */
    private float lineX;
    /**
     * Y coordinate of the cue ball
     */
    private float lineY;
    /**
     * Has the shot been taken
     */
    private boolean shot;
    /**
     * If the cue ball contacts the right ball (solid vs stripe)
     */
    private boolean firstContact = false;

    /**
     * Whichever player's turn it is.
     */
    Player currPlayer;
    /**
     * The watching player, who is waiting their turn.
     */
    Player watchPlayer;
    /**
     * Are the balls aligned to a pocket
     */
    boolean aligned = false;
    /**
     * Counter for how many shots have been taken
     */
    int shotNum = 0;
    /**
     * Number of balls scored.
     */
    int numPocketed = 0;
    /**
     * Is the cue ball in a pocket
     */
    boolean cuePocket = false;
    /**
     * Is true until the first ball is scored by either player
     * returns false when the "break" period is over
     */
    boolean brake = true;

    /**
     * New player (user)
     */
    private Player player1 = new Player("p1", 1);
    /**
     * New computer player
     */
    private Player AI = new Player("p2", 2);
    public Person_Comp(final Pool gam) {


        super(gam);
    }

    /**
     * Renders the game
     * @param delta
     */
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

