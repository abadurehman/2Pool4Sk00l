package com.mygdx.Pool;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

/**
 * Created by SrinjoyMajumdar on 5/5/15.
 */
public class GameScreen implements Screen, InputProcessor {

    final Pool game;
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
    Player player1;
    Player player2;
    Player currPlayer;
    Player watchPlayer;
    boolean aligned = false;
    int shotNum = 0;
    int numPocketed = 0;
    int temp = 0;
    boolean cuePocket = false;
    public GameScreen(final Pool gam) {

        game = gam;
        screenHeight = Gdx.graphics.getHeight();
        screenWidth = Gdx.graphics.getWidth();
        world = new World(new Vector2(0, 0), true); //create world for physics
        table = new PoolTable();
        Gdx.input.setInputProcessor(this);
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.
                getHeight());

        poolBalls = new ArrayList<Ball>(16);
        ballBodies = new ArrayList<Body>(16);
        createBall(0, -(Gdx.graphics.getWidth() / 2 - 537 * (screenWidth / 800)), -(Gdx.graphics.getHeight() / 2 - 197 * (screenHeight / 480)));
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


        game.batch.setProjectionMatrix(camera.combined);
        debugMatrix = game.batch.getProjectionMatrix().cpy().scale(PIXELS_TO_METERS,
                PIXELS_TO_METERS, 0);

        game.batch.begin();

        for (int i = poolBalls.size() - 1; i > -1; i--) {
            Ball b = poolBalls.get(i);
            b.setPosition((ballBodies.get(i).getPosition().x) * PIXELS_TO_METERS -
                    poolBalls.get(i).getHeight() / 2, (ballBodies.get(i).getPosition().y) * PIXELS_TO_METERS
                    - b.getHeight() / 2);
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
            touchPos.set(-(screenWidth / 2 - Gdx.input.getX()), (screenHeight / 2 - Gdx.input.getY()));
            System.out.println(touchPos);
            lineX = touchPos.x;
            lineY = touchPos.y;
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(1, 1, 0, 1);

            shapeRenderer.line((float) poolBalls.get(0).getX() + poolBalls.get(0).getWidth() / 2, (float) poolBalls.get(0).getY() + poolBalls.get(0).getHeight() / 2, (float) lineX, (float) lineY);
            shapeRenderer.end();


        }
        if (aligned && table.getSlider().getValue() != 1 && !table.getSlider().isDragging()) {
            System.out.println("shot");
            double val = table.getSlider().getValue();
            table.getSlider().setValue(1);
            int temp;
            temp = POWER_REDUCTION;
            POWER_REDUCTION *= val;

            Vector2 line = new Vector2(lineX - cue.getHeight() / 2, lineY - cue.getHeight() / 2);
            Vector2 power = new Vector2(-(poolBalls.get(0).getX() - line.x) / POWER_REDUCTION,
                    -(poolBalls.get(0).getY() - line.y) / POWER_REDUCTION);
            Vector2 position = new Vector2(ballBodies.get(0).getPosition().x + cue.getWidth() / 2,
                    ballBodies.get(0).getPosition().y + cue.getWidth() / 2);
            ballBodies.get(0).applyLinearImpulse(power, position, true);
            Gdx.input.setInputProcessor(this);
            aligned = false;
            shot = true;
            POWER_REDUCTION = temp;
        }
        if (shot) {
            for (Contact c : world.getContactList()) {
                if (c.isTouching()) {
                    Ball a = (Ball) c.getFixtureA().getBody().getUserData();
                    Ball b = (Ball) c.getFixtureB().getBody().getUserData();
                    if (a != null && b != null && currPlayer.getType() != null) {
                        if (a.getNum() == 0) {
                            if (b.getNum() < 8 && currPlayer.getType().equals("striped") || b.getNum() > 8 && currPlayer.getType().equals("solid")) {
                                Player temp = currPlayer;
                                currPlayer = watchPlayer;
                                watchPlayer = temp;
                                System.out.println("switched");
                                shot = false;
                            }

                        } else if (b.getNum() == 0) {
                            if (a.getNum() < 8 && currPlayer.getType().equals("striped") || a.getNum() > 8 && currPlayer.getType().equals("solid")) {
                                Player temp = currPlayer;
                                currPlayer = watchPlayer;
                                watchPlayer = temp;
                                System.out.println("switched");
                                shot = false;
                            }
                        }
                    }
                }
            }
        }
        debugRenderer.render(world, debugMatrix);
    }

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
    public void createBall(int i, float x, float y) {

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
        wallFixture.restitution = 1.1f;
        wallFixture.shape = wallShape;

        BodyDef wallBodyDef = new BodyDef();
        wallBodyDef.type = BodyDef.BodyType.StaticBody;

        float posX = x / PIXELS_TO_METERS;
        float posY = y / PIXELS_TO_METERS;
        wallBodyDef.position.set(posX + halfWidth, posY + halfHeight);

        Body wall = world.createBody(wallBodyDef);
        wall.createFixture(wallFixture);


    }//add wall

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        Array<Body> bodies = new Array<Body>(world.getBodyCount());
        world.getBodies(bodies);
        boolean moving = false;
        for (Body b : bodies) {
            if ((b.getLinearVelocity().x != 0) && (b.getLinearVelocity().y != 0))
                moving = true;
            System.out.println(b.getLinearVelocity());
        }
        if (!moving) {
            if (numPocketed == temp) {
                Player tempPlayer = currPlayer;
                currPlayer = watchPlayer;
                watchPlayer = tempPlayer;
                System.out.println("switched");
                temp = numPocketed;
            }
            System.out.println("Select Power");
            Gdx.input.setInputProcessor(table);
            aligned = true;
            shotNum++;
        }

        return true;
    }

    public boolean pocketed(Ball b) {

        if (!t.contains(b.getBoundingRectangle())) {
            System.out.println("pocketed " + b.getNum());

            if (numPocketed == 0) {
                if (b.getNum() < 8 && b.getNum() != 0) {
                    System.out.println(currPlayer.getPlayerNumber() + ": Solid");
                    System.out.println(watchPlayer.getPlayerNumber() + ": Striped");
                    currPlayer.setType("solid");
                    watchPlayer.setType("striped");
                } else if (b.getNum() > 8) {
                    System.out.println(currPlayer.getPlayerNumber() + ": Striped");
                    System.out.println(watchPlayer.getPlayerNumber() + ": Solid");
                    currPlayer.setType("striped");
                    watchPlayer.setType("solid");
                }

            } else if (!b.getType().equals(currPlayer.getType())) {
                System.out.println("fail");
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







