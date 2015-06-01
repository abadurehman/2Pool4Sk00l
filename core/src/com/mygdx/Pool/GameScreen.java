package com.mygdx.Pool;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.utils.Array;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

/**
 * Created by SrinjoyMajumdar on 5/5/15.
 */
public class GameScreen implements Screen, InputProcessor {

    /**
     * The current game
     */
    final Pool game;
    /**
     * Pixel to meters conversion
     */
    private final float PIXELS_TO_METERS = 100f;
    /**
     * Arraylist of balls
     */
    private ArrayList<Ball> poolBalls;
    /**
     * Arraylist of the bodies
     */
    private ArrayList<Body> ballBodies;
    /**
     * The table
     */
    private PoolTable table;
    /**
     * The world that you're playing in.
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
     * Your view of the game
     */
    private OrthographicCamera camera;
    /**
     *  The new rectangle
     */
    private Rectangle t;
    /**
     * How much to reduce the force from the input
     */
    private int POWER_REDUCTION = 6000;
    /**
     * Height of the screen
     */
    private int screenHeight;
    /**
     * Width of the screen
     */
    private int screenWidth;
    /**
     * Cue ball
     */
    private Ball cue;
    /**
     *  X coordinate of the cue ball
     */
    private float lineX;
    /**
     * Y coordinate of the cue ball
     */
    private float lineY;
    /**
     * Boolean if the ball has been shot
     */
    private boolean shot;
    /**
     * If the first ball the cue contacts is the right ball.
     */
    private boolean firstContact = false;
    /**
     * First player
     */
    Player player1;
    /**
     * Second Plyaer (usually the younger sibling)
     */
    Player player2;
    /**
     * Whichever player's turn it is.
     */
    Player currPlayer;
    /**
     * The watching player, who is waiting their turn.
     */
    Player watchPlayer;
    /**
     * Is the cue ball and target ball aligned to a pocket
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
     * Constructs a game screen
     * @param gam Current game
     */
    public GameScreen(final Pool gam) {

        game = gam;
        screenHeight = 540;
        screenWidth = 960;
        world = new World(new Vector2(0, 0), true); //create world for physics
        table = new PoolTable();
        Gdx.input.setInputProcessor(this);
        camera = new OrthographicCamera(960, 540);

        poolBalls = new ArrayList<Ball>(16);
        ballBodies = new ArrayList<Body>(16);
        float cueX = 164;
        float cueY = -45;
        createBall(0, cueX, cueY);
        lineX = poolBalls.get(0).getX();
        lineY = poolBalls.get(0).getY();
        cue = poolBalls.get(0);
        float x = -156 * (screenWidth / 800);
        float y = -43 * (screenHeight / 480);
        int count2 = 1;
        for (int i = 1; i <= 5; i++) {

            for (int z = i; z > 0; z--) {
                createBall(count2, x, y);
                if (count2 != 1)
                    y -= cue.getWidth();
                count2++;
            }
            y = (poolBalls.get(count2 - i).getY() + cue.getWidth());
            x -= cue.getWidth();
        }//create balls

        player1 = new Player("p1", 1);
        player2 = new Player("p2", 2);
        currPlayer = player1;
        watchPlayer = player2;
        createWallModels(); //create world walls
        debugRenderer = new Box2DDebugRenderer();
        shapeRenderer = new ShapeRenderer();
    }

    /**
     * Renders the game screen as the game progresses
     * @param delta
     */
    public void render(float delta) {
        camera.update();
        debugRenderer.render(world, camera.combined);
        world.step(1 / 60f, 6, 2);
        shapeRenderer.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 0.1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        table.draw();
        game.batch.end();

        if (!moving())
            shapeRenderer.setColor(0, 1, 0, 0);
        else
            shapeRenderer.setColor(1, 0, 0, 0);

        game.batch.setProjectionMatrix(camera.combined);
        debugMatrix = game.batch.getProjectionMatrix().cpy().scale(PIXELS_TO_METERS,
                PIXELS_TO_METERS, 0);

        game.batch.begin();

        for (int i = poolBalls.size() - 1; i > -1; i--) {
            Ball b = poolBalls.get(i);
            if (!(cuePocket && b.getNum() == 0)) {
                b.setPosition((ballBodies.get(i).getPosition().x) * PIXELS_TO_METERS -
                        poolBalls.get(i).getHeight() / 2, (ballBodies.get(i).getPosition().y) * PIXELS_TO_METERS
                        - b.getHeight() / 2);
            }

            b.draw(game.batch);
            if (pocketed(b)) {
                if (b.getNum() == 0) {
                    cuePocket = true;
                } else {
                    currPlayer.scoredBall(b);
                    poolBalls.remove(i);
                    world.destroyBody(ballBodies.get(i));
                    ballBodies.remove(i);
                }
            }
        }
        int xp1 = -200;
        int xp2 = 200;

        for (Ball b : player1.getScoredBalls()) {
            b.setCenterX(xp1);
            b.setCenterY(185);
            b.draw(game.batch);
            xp1 += b.getWidth();
        }
        for (Ball b : player2.getScoredBalls()) {
            b.setCenterX(xp2);
            b.setCenterY(185);
            b.draw(game.batch);
            xp2 -= b.getWidth();
        }

        game.batch.end();

        if (Gdx.input.isTouched() && !table.getSlider().isDragging()) {

            Vector2 touchPos = new Vector2();
            touchPos.set(((Gdx.input.getX() - screenWidth / 2) * (camera.viewportWidth / screenWidth)), -(Gdx.input.getY() - screenHeight / 2) * (camera.viewportHeight / screenHeight));
            System.out.println(touchPos);
            lineX = touchPos.x;
            lineY = touchPos.y;
            if (!cuePocket) {
                shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                shapeRenderer.line((float) poolBalls.get(0).getX() + poolBalls.get(0).getWidth() / 2, (float) poolBalls.get(0).getY() + poolBalls.get(0).getHeight() / 2, (float) lineX, (float) lineY);
                shapeRenderer.end();
            } else {
                poolBalls.get(0).setCenterX(lineX);
                poolBalls.get(0).setCenterY(lineY);
            }



        }
        if (currPlayer != null) {
            int x;
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            if (currPlayer.getPlayerNumber() == 1)
                x = -355;
            else
                x = 269;
            shapeRenderer.box(x, 175, 0, 70, 70, 0);
            shapeRenderer.end();
        }
        if (aligned && table.getSlider().getValue() != 1 && !table.getSlider().isDragging()) {
            System.out.println("shot");
            double val = table.getSlider().getValue();
            table.getSlider().setValue(1);
            int temp;
            temp = POWER_REDUCTION;
            POWER_REDUCTION *= val;

            AI ai = new AI();
            Vector2 targetCenter = ai.choice(poolBalls, currPlayer, table);

            Vector2 line = new Vector2(lineX - cue.getHeight() / 2, lineY - cue.getHeight() / 2);
            Vector2 power = new Vector2(-(poolBalls.get(0).getX() - line.x) / POWER_REDUCTION,
                    -(poolBalls.get(0).getY() - line.y) / POWER_REDUCTION);
            Vector2 position = new Vector2(ballBodies.get(0).getPosition().x + cue.getWidth() / 2,
                    ballBodies.get(0).getPosition().y + cue.getWidth() / 2);


            ballBodies.get(0).applyLinearImpulse(power, position, true);
            ballBodies.get(0).setAngularVelocity(0);
            Gdx.input.setInputProcessor(this);
            aligned = false;
            shot = true;
            firstContact = false;
            POWER_REDUCTION = temp;
        }
        if (shot && !firstContact) {
            for (Contact c : world.getContactList()) {
                if (c.isTouching()) {
                    Ball a = (Ball) c.getFixtureA().getBody().getUserData();
                    Ball b = (Ball) c.getFixtureB().getBody().getUserData();
                    if (a != null && b != null && currPlayer.getType() != null) {
                        if (a.getNum() == 0) {
                            if ((b.getNum() < 8 && currPlayer.getType().equals("striped") || (b.getNum() > 8 && currPlayer.getType().equals("solid")))) {
                                Player temp = currPlayer;
                                currPlayer = watchPlayer;
                                watchPlayer = temp;
                                System.out.println("switched because wrong contact");
                                System.out.println("Current Player" + currPlayer.getPlayerNumber());
                                firstContact = true;
                            }

                        } else if (b.getNum() == 0) {
                            if ((a.getNum() < 8 && currPlayer.getType().equals("striped") || (a.getNum() > 8 && currPlayer.getType().equals("solid")))) {
                                Player temp = currPlayer;
                                currPlayer = watchPlayer;
                                watchPlayer = temp;
                                System.out.println("switched because wrong contact");
                                System.out.println("Current Player" + currPlayer.getPlayerNumber());
                                firstContact = true;
                            }
                        }
                    }
                }
            }
        }
        if (shot && !moving() && shotNum != 0) {
            if (!firstContact) {
                if (numPocketed == tempNum) {
                    Player temp = currPlayer;
                    currPlayer = watchPlayer;
                    watchPlayer = temp;
                    System.out.println("switched because nothing pocketed");
                    System.out.println("Current Player" + currPlayer.getPlayerNumber());
                }
            }
            shot = false;
            tempNum = numPocketed;
        }
        debugRenderer.render(world, debugMatrix);
    }

    /**
     * Resizes the screen
     * @param width width of the screen
     * @param height height of the screen
     */
    public void resize(int width, int height) {
        screenHeight = Gdx.graphics.getHeight();
        screenWidth = Gdx.graphics.getWidth();
    }


    private Ball currBall;
    /**
     * Creates new ball body and sprite
     *
     * @param i number of ball
     * @param x x position
     * @param y y position
     */
    public Body createBall(int i, float x, float y) {

        poolBalls.add(new Ball(i));
        poolBalls.get(i).setCenter(x, y);
        currBall = poolBalls.get(i);
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set((currBall.getX() + currBall.getWidth() / 2) /
                        PIXELS_TO_METERS,
                (currBall.getY() + currBall.getHeight() / 2) / PIXELS_TO_METERS);
        ballBodies.add(world.createBody(bodyDef)) ;
        CircleShape shape = new CircleShape();
        shape.setRadius(currBall.getWidth() / 2 / PIXELS_TO_METERS);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.2f;
        fixtureDef.friction = 0.1f;
        fixtureDef.restitution = 1f;
        ballBodies.get(i).createFixture(fixtureDef);
        ballBodies.get(i).setLinearDamping(0.75f);
        ballBodies.get(i).setAngularDamping(1);
        ballBodies.get(i).setUserData(poolBalls.get(i));
        shape.dispose();
        return ballBodies.get(i);

    }

    /**
     * Create walls in the view.
     */
    private void createWallModels() {
        final int pXT = 23 * (screenWidth / 800);
        final int pYT = 23 * (screenHeight / 480);
        float x = table.getTableInside().getX() * (screenWidth / 800);
        float y = table.getTableInside().getY() * (screenHeight / 480);
        float width = table.getTableInside().getWidth() * (screenWidth / 800);
        float height = table.getTableInside().getHeight() * (screenHeight / 480);
        System.out.println("Wall height " + height);
        t = new Rectangle();
        t.setSize(width + 30, height + 30);
        t.setPosition(-(screenWidth / 2 - x) - 15, -(screenHeight / 2 - y) - 15);

        addWall(-(screenWidth / 2 - x) + pXT, -(screenHeight / 2 - y), width / 2 - 2 * pXT, 0.1f);//bottomleft
        addWall(-(screenWidth / 2 - x) + pXT + width / 2, -(screenHeight / 2 - y), width / 2 - 2 * pXT, 0.1f);//bottomright
        addWall(-(screenWidth / 2 - x) + pXT, -(screenHeight / 2 - y) + height, width / 2 - 2 * pXT, 0.1f);//topleft
        addWall(-(screenWidth / 2 - x) + pXT + width / 2, -(screenHeight / 2 - y) + height, width / 2 - 2 * pXT, 0.1f);//topright
        addWall(-(screenWidth / 2 - x), -(screenHeight / 2 - y) + pYT, 0.1f, height - 2 * pYT);//left
        addWall(-(screenWidth / 2 - x - width), -(screenHeight / 2 - y) + pYT, 0.1f, height - 2 * pYT);//right
    }

    /**
     * Add wall to model.
     *
     * @param x      The bottom left hand corner of wall.
     * @param y      The bottom left hand corner of wall.
     * @param width  The wall width.
     * @param height The wall height.
     */
    private void addWall(float x, float y, float width, float height) {
        PolygonShape wallShape = new PolygonShape();

        float halfWidth = (width / PIXELS_TO_METERS) / 2;
        float halfHeight = (height / PIXELS_TO_METERS) / 2;
        wallShape.setAsBox(halfWidth, halfHeight);

        FixtureDef wallFixture = new FixtureDef();
        wallFixture.density = 1;
        wallFixture.friction = 1f;
        wallFixture.restitution = 1f;
        wallFixture.shape = wallShape;

        BodyDef wallBodyDef = new BodyDef();
        wallBodyDef.type = BodyDef.BodyType.StaticBody;

        float posX = x / PIXELS_TO_METERS;
        float posY = y / PIXELS_TO_METERS;
        wallBodyDef.position.set(posX + halfWidth, posY + halfHeight);

        Body wall = world.createBody(wallBodyDef);
        wall.createFixture(wallFixture);


    }//add wall


    int tempNum = 0;
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            System.out.println("Select Power");
        if (!moving()) {
            Gdx.input.setInputProcessor(table);
            aligned = true;
            shotNum++;
        }
        if (cuePocket) {
            world.destroyBody(ballBodies.get(0));
            ballBodies.set(0, createBall(0, poolBalls.get(0).getX(), poolBalls.get(0).getY()));
            cuePocket = false;
        }
        return true;
    }

    public boolean pocketed(Ball b) {

        if (!t.contains(b.getBoundingRectangle())) {
            System.out.println("pocketed " + b.getNum());


            if (b.getNum() == 8) {
                System.out.println("done");
                Gdx.app.exit();
            }
            if (numPocketed == 0) {
                if (b.getNum() < 8 && b.getNum() != 0) {
                    System.out.println(currPlayer.getPlayerNumber() + ": Solid");
                    System.out.println(watchPlayer.getPlayerNumber() + ": Striped");
                    currPlayer.setType("solid");
                    watchPlayer.setType("striped");
                    brake = false;

                } else if (b.getNum() > 8) {
                    System.out.println(currPlayer.getPlayerNumber() + ": Striped");
                    System.out.println(watchPlayer.getPlayerNumber() + ": Solid");
                    currPlayer.setType("striped");
                    watchPlayer.setType("solid");
                    brake = false;
                }

            } else if (!b.getType().equals(currPlayer.getType()) && !brake) {
                System.out.println("switched because wrong ball pocketed");
                Player temp = currPlayer;
                currPlayer = watchPlayer;
                watchPlayer = temp;
            }

            if (b.getNum() == 0) {
                cuePocket = true;
                //ballBodies.get(0).setTransform(-(Gdx.graphics.getWidth() / 2 - 537 * (screenWidth / 800)), -(Gdx.graphics.getHeight() / 2 - 197 * (screenHeight / 480)), 0);
                // poolBalls.get(0).setPosition(-(Gdx.graphics.getWidth() / 2 - 537 * (screenWidth / 800)), -(Gdx.graphics.getHeight() / 2 - 197 * (screenHeight / 480)));
            }
            numPocketed++;
            System.out.println("Current Player " + currPlayer.getPlayerNumber());
            return true;

        }
        return false;
    }

    public boolean moving() {
        Array<Body> bodies = new Array<Body>(world.getBodyCount());
        world.getBodies(bodies);
        for (Body b : bodies) {
            if ((b.getLinearVelocity().x != 0) && (b.getLinearVelocity().y != 0))
                return true;
        }
        return false;
    }


    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }
    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }


    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {

        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }//methods not used from Input Processor

    public void show() {

    }

    @Override
    public void pause() {

    }

    public void resume() {

    }

    public void hide() {

    }

    public void dispose() {

    }//methods not used from Screen

}







