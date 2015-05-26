package com.mygdx.Pool;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by SrinjoyMajumdar on 5/5/15.
 */
public class GameScreen implements Screen, InputProcessor {

    final Pool game;
    final double margin = 0.1;
    final float PIXELS_TO_METERS = 100f;
    protected MouseJoint mouseJoint = null;
    Sprite stick;
    ArrayList<Ball> poolBalls;
    ArrayList<Body> ballBodies;
    PoolTable table;
    World world;
    Ball currBall;
    Box2DDebugRenderer debugRenderer;
    OrthographicCamera camera;
    Matrix4 debugMatrix;
    int screenHeight;
    int screenWidth;
    Ball cue;
    float lineX;
    float lineY;
    Texture texture;
    Rectangle t;
    ShapeRenderer shapeRenderer;
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
        float y = -43 * (screenHeight / 800);
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

        stick = new Stick(); //new cue
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
                    poolBalls.remove(i);
                    world.destroyBody(ballBodies.get(i));
                    ballBodies.remove(i);
                    createBall(0, -(Gdx.graphics.getWidth() / 2 - 537 * (screenWidth / 800)), -(Gdx.graphics.getHeight() / 2 - 197 * (screenHeight / 480)));

                } else {
                    poolBalls.remove(i);
                    world.destroyBody(ballBodies.get(i));
                    ballBodies.remove(i);
                }
            }
        }

//        stick.setPosition(cue.getX() + cue.getWidth(), cue.getY()
//                + cue.getHeight() / 2 - stick.getHeight() / 2);
////        stick.setOrigin(-(cue.getWidth()/2),stick.getHeight()/2);
//        stick.setOrigin(cue.getX(), cue.getY());
//        float x = -(400 - cue.getX());
//        float y = -(240 - cue.getY());
        // stick.setPosition(cue.getX()+cue.getHeight()/2-stick.getHeight()/2,
        // cue.getY()+cue.getHeight()/2-stick.getHeight()/2 );


//        stick.draw(game.batch);
        game.batch.end();
        if (Gdx.input.isTouched() && !table.getSlider().isDragging()) {
            Vector2 touchPos = new Vector2();
            touchPos.set(-(screenWidth / 2 - Gdx.input.getX()), (screenHeight / 2 - Gdx.input.getY()));
            lineX = touchPos.x;
            lineY = touchPos.y;
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(1, 1, 0, 1);
            shapeRenderer.line((float) poolBalls.get(0).getX() + poolBalls.get(0).getWidth() / 2, (float) poolBalls.get(0).getY() + poolBalls.get(0).getHeight() / 2, (float) lineX, (float) lineY);
            shapeRenderer.end();

        }
        debugRenderer.render(world, debugMatrix);

    }

    public void resize(int width, int height) {
        screenHeight = Gdx.graphics.getHeight();
        screenWidth = Gdx.graphics.getWidth();

    }


    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.RIGHT)
            ballBodies.get(0).setLinearVelocity(10f, 0f);
        if (keycode == Input.Keys.LEFT)
            ballBodies.get(0).setLinearVelocity(-10f, 0f);
        if (keycode == Input.Keys.UP)
            ballBodies.get(0).setLinearVelocity(0, 10f);
        if (keycode == Input.Keys.DOWN)
            ballBodies.get(0).setLinearVelocity(0f, -10f);
        return true;
    }


    /**
     * Creates new ball body and sprite
     *
     * @param i number of ball
     * @param x x position
     * @param y y position
     */
    public void createBall(int i, float x, float y) {
        if (i == 0)
            poolBalls.add(0, new Ball(i));
        else
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
        fixtureDef.density = 0.3f;
        fixtureDef.friction = 0.3f;
        fixtureDef.restitution = 0.5f;
        ballBodies.get(i).createFixture(fixtureDef);
        shape.dispose();

    }

    /**
     * Create walls in the view.
     */
    private void createWallModels() {
        final int pXT = 23 * (screenWidth / 800);
        final int pYT = 23 * (screenHeight / 480);
        float x = table.getTableInside().getX();
        float y = table.getTableInside().getY();
        float width = table.getTableInside().getWidth() * (screenWidth / 800);
        float height = table.getTableInside().getHeight() * (screenHeight / 480);
        float lineThickness = 0.1f;
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
        wallFixture.friction = 1;
        wallFixture.restitution = 0.7f;
        wallFixture.shape = wallShape;

        BodyDef wallBodyDef = new BodyDef();
        wallBodyDef.type = BodyDef.BodyType.StaticBody;

        float posX = x / PIXELS_TO_METERS;
        float posY = y / PIXELS_TO_METERS;
        wallBodyDef.position.set(posX + halfWidth, posY + halfHeight);

        Body wall = world.createBody(wallBodyDef);
//        wall.
        wall.createFixture(wallFixture);
    }//add wall

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        table.getSlider().setValue(10);
        if (!table.getSlider().isDragging()) {
            Vector2 ball = new Vector2(poolBalls.get(0).getX(), poolBalls.get(0).getY());
            Vector2 line = new Vector2((float) lineX, (float) lineY);
            float power = ball.dst(line) / (PIXELS_TO_METERS * 100);
            float angle = MathUtils.radiansToDegrees * MathUtils.atan2(lineY -
                    (ball.x + cue.getHeight() / 2), line.x -
                    (ball.y + cue.getHeight() / 2));
            float y1 = (cue.getHeight() / 2 * line.y) / ball.dst(line);
            float x1 = (float) Math.sqrt(Math.pow(y1, 2) - Math.pow(cue.getHeight() / 2, 2));
            System.out.println((float) x1 + "  " + (float) y1);
            System.out.println("cue center" + (float) poolBalls.get(0).getX() + cue.getHeight() / 2 + " " + (float) poolBalls.get(0).getY() + cue.getHeight() / 2);
            ballBodies.get(0).applyLinearImpulse(new Vector2(((poolBalls.get(0).getX() - line.x) / 1000), (poolBalls.get(0).getY() - line.y) / 1000), ballBodies.get(0).getPosition(), true);

        }
        return false;
    }

    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        System.out.println(screenX + " " + screenY);
        float x = -(400 - cue.getX());
        float y = -(240 - cue.getY());
        float angle = MathUtils.radiansToDegrees * MathUtils.atan2(screenY -
                (y + cue.getHeight() / 2), screenX -
                (x + cue.getHeight() / 2));
        if (angle < 0) {
            angle += 360;
        }
        stick.setRotation(-angle);
//        if (table.getTableInside().contains(screenX, screenY))
//            Gdx.input.setInputProcessor(this);
//        else
//            Gdx.input.setInputProcessor(table);
        return true;
    }

    public boolean pocketed(Ball b) {

        if (!t.contains(b.getBoundingRectangle())) {
            System.out.println("pcketed");

            return true;
        }
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







