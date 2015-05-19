package com.mygdx.Pool;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;

/**
 * Created by SrinjoyMajumdar on 5/5/15.
 */
public class GameScreen implements Screen, InputProcessor {

    final Pool game;
    final float PIXELS_TO_METERS = 100f;
    protected MouseJoint mouseJoint = null;
    Sprite stick;
    Ball[] poolBalls;
    Body[] ballBodies;
    PoolTable table;
    Behavior behave;
    SpriteBatch batch;
    Sprite sprite;
    Texture img;
    World world;
    Body body;
    Body body3;
    //    Body wallBody;
    Body bodyEdgeScreen;
    Ball currBall;
    Fixture bottomFixture;
    Fixture ballFixture;
    Box2DDebugRenderer debugRenderer;
    OrthographicCamera camera;
    BitmapFont font;
    Matrix4 debugMatrix;

    public GameScreen(final Pool gam) {

        game = gam;
        world = new World(new Vector2(0, 0), true);
        table = new PoolTable();
        Gdx.input.setInputProcessor(table);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        poolBalls = new Ball[16];
        ballBodies = new Body[16];
        poolBalls[0] = new Ball(0);
        poolBalls[0].setCenter(537, 197);
        createBall(0, -(400 - poolBalls[0].getX()), -(240 - poolBalls[0].getY()));
        float x = -156;
        float y = -43;
        int count2 = 1;
        for (int i = 1; i <= 5; i++) {

            for (int z = i; z > 0; z--) {
                createBall(count2, x, y);
                if (count2 != 1)
                    y -= poolBalls[0].getWidth();
                count2++;
            }
            y = (poolBalls[count2 - i].getY() + poolBalls[0].getWidth());
            x -= poolBalls[0].getWidth();
        }
        stick = new Stick();
        createWallModels();


        Gdx.input.setInputProcessor(this);
        debugRenderer = new Box2DDebugRenderer();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.
                getHeight());

    }

    public void createBall(int i, float x, float y) {
        poolBalls[i] = new Ball(i);
        poolBalls[i].setCenter(x, y);
        currBall = poolBalls[i];
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set((currBall.getX() + currBall.getWidth() / 2) /
                        PIXELS_TO_METERS,
                (currBall.getY() + currBall.getHeight() / 2) / PIXELS_TO_METERS);
        body = world.createBody(bodyDef);
        CircleShape shape = new CircleShape();
        shape.setRadius(currBall.getWidth() / 2 / PIXELS_TO_METERS);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.3f;
        fixtureDef.friction = 0.3f;
        fixtureDef.restitution = 0.5f;
        body.createFixture(fixtureDef);
        shape.dispose();
        ballBodies[i] = body;
    }

    /**
     * Create walls in the view.
     */
    private void createWallModels() {
        addWall(-(400 - table.getTableInside().getX()), -(240 - table.getTableInside().getY()), table.getTableInside().getWidth(), 0.1f);//bottom
        addWall(-(400 - table.getTableInside().getX()), -(240 - table.getTableInside().getY()), 0.1f, table.getTableInside().getHeight());//left
        addWall(-(400 - table.getTableInside().getX()), -(240 - table.getTableInside().getY()) + table.getTableInside().getHeight(), table.getTableInside().getWidth() - 0.1f, 0.1f);//right
        addWall(-(400 - table.getTableInside().getX()) + table.getTableInside().getWidth(), -(240 - table.getTableInside().getY()) - 0.1f, 0.1f, (float) table.getTableInside().getHeight());//top
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
        wall.createFixture(wallFixture);
    }

    public void show() {

    }

    public void render(float delta) {
        camera.update();
        debugRenderer.render(world, camera.combined);
        world.step(1 / 60f, 6, 2);

        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 0.1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        table.draw();
        game.batch.end();


        game.batch.setProjectionMatrix(camera.combined);
        debugMatrix = game.batch.getProjectionMatrix().cpy().scale(PIXELS_TO_METERS,
                PIXELS_TO_METERS, 0);

        game.batch.begin();
        for (int i = 0; i < poolBalls.length; i++) {
            poolBalls[i]
            poolBalls[i].draw(game.batch);
            poolBalls[i].draw(game.batch);
        }
//        poolBalls[0].draw(game.batch);
//        poolBalls[0].setPosition((body.getPosition().x * PIXELS_TO_METERS) - poolBalls[0].
//                getWidth() / 2, (body.getPosition().y * PIXELS_TO_METERS) - poolBalls[0].getHeight() / 2);
//        		stick.setPosition((body3.getPosition().x * PIXELS_TO_METERS) - stick.
//                                getWidth() / 2,
//                        (body3.getPosition().y * PIXELS_TO_METERS) - stick.getHeight() / 2)
//		;
//        		stick.setRotation((float) Math.toDegrees(body3.getAngle()));
//        stick.setPosition(poolBalls[0].getX() + poolBalls[0].getWidth(), poolBalls[0].getY() + poolBalls[0].getHeight() / 2 - stick.getHeight() / 2);
//       stick.setOrigin(-(poolBalls[0].getWidth() / 2), stick.getHeight() / 2);
//
        if (Gdx.input.isTouched() && !table.getSlider().isDragging()) {

            Vector2 touchPos = new Vector2();
            //    touchPos.set((Gdx.input.getX()*800)/(Gdx.graphics.getWidth()),(Gdx.input.getY()*480)/(Gdx.graphics.getHeight()));
            touchPos.set(Gdx.input.getX(), Gdx.input.getY());

//            stick.setPosition(body3.getPosition().x, body3.getPosition().y);
//            float angle = MathUtils.radiansToDegrees * MathUtils.atan2(touchPos.y - (poolBalls[0].getY()+poolBalls[0].getHeight()/2), touchPos.x - (poolBalls[0].getX()+poolBalls[0].getHeight()/2));
//            System.out.println(touchPos.x + " " + touchPos.y);
//            if(angle < 0){
//                angle += 360;
//            }
//            stick.setRotation(-angle);
//            System.out.println(angle);
        }
        if (!table.getSlider().isDragging()) {
            table.getSlider().setValue(10);
        }

//        stick.draw(game.batch);
        game.batch.end();

        debugRenderer.render(world, debugMatrix);

    }

    public void resize(int width, int height) {


    }

    @Override
    public void pause() {

    }

    public void resume() {

    }

    public void hide() {

    }

    public void dispose() {

    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.RIGHT)
            body.setLinearVelocity(10f, 0f);
        if (keycode == Input.Keys.LEFT)
            body.setLinearVelocity(-10f, 0f);

        if (keycode == Input.Keys.UP)
            body.setLinearVelocity(0, 10f);
        if (keycode == Input.Keys.DOWN)
            body.setLinearVelocity(0f, -10f);
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    // On touch we apply force from the direction of the users touch.
    // This could result in the object "spinning"
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector2 touchPos = new Vector2();
        touchPos.set(screenX, screenY);
        float angle = MathUtils.radiansToDegrees * MathUtils.atan2(touchPos.y - (poolBalls[0].getY() + poolBalls[0].getHeight() / 2), touchPos.x - (poolBalls[0].getX() + poolBalls[0].getHeight() / 2));
        if (angle < 0) {
            angle += 360;
        }

        System.out.println(angle);

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
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
    }
}




