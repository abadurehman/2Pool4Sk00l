package com.mygdx.Pool;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.TimeUtils;

import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Timer;

import sun.util.resources.cldr.ru.TimeZoneNames_ru;

/**
 * Created by SrinjoyMajumdar on 5/5/15.
 */
public class GameScreen implements Screen {

    final Pool game;
    Texture background;
    Sprite bgSprite;
    OrthographicCamera camera;
    Stage stage;
    Ball cue;
    Rectangle cueRect;
    Texture stick;
    Rectangle stickRect;
    Sprite sprite;
    Ball[] poolBalls;
    Sprite cueSprite;
    int change;
    long lastDropTime = 0;
    float angle = 0;
    float lastangle;
    GestureDetector detect;
    Vector2 lastPos;

    public GameScreen(final Pool gam) {
        game = gam;

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        background = new Texture(Gdx.files.internal("Background.png"));
        bgSprite = new Sprite(background);
        poolBalls = new Ball[16];
        for (int i = 0; i < poolBalls.length; i++) {
            poolBalls[i] = new Ball(i);
            poolBalls[i].setX((int) (Math.random() * 800));
            poolBalls[i].setY((int) (Math.random() * 480));
        }


//        stick = new Texture(Gdx.files.internal("stick.png"));
//        stickRect = new Rectangle();
//        stickRect.width = stick.getWidth();
//        stickRect.height = stick.getHeight();
//        stickRect.y = cue.getY() + cue.getHeight() / 2 - (stickRect.height / 2);
//        stickRect.x = cue.getX() + cue.getWidth();
//        // the bottom screen edge
//           sprite = new Sprite(stick);
//        lastPos = new Vector2(stickRect.x,stickRect.y);



    }

    @Override
    public void show() {

    }

    public void render(float delta) {


        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 0.1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        stage.draw();
        game.batch.end();

        game.batch.begin();
//            cueSprite.setPosition(cueRect.x,cueRect.y);
        //          cueSprite.draw(game.batch);
//        cue.draw(game.batch);

        for (int i = 0; i < poolBalls.length; i++) {
            poolBalls[i].draw(game.batch);
        }

//        sprite.setPosition(cueSprite.getX()+cueSprite.getWidth(), cueSprite.getY()+cueSprite.getHeight()/2-sprite.getHeight()/2);
//        sprite.setOrigin(-(cueSprite.getWidth()/2),sprite.getHeight()/2);

        if (Gdx.input.isTouched()) {
//            Vector2 touchPos = new Vector2();
//            touchPos.set(Gdx.input.getX(), Gdx.input.getY());
//            System.out.println(touchPos.x +" "+ touchPos.y);
//            sprite.setPosition(touchPos.x,stage.getHeight()-touchPos.y);
//            angle = touchPos.angle(new Vector2(sprite.getX(),sprite.getY()));
//            sprite.rotate(angle);
//            lastangle= angle;
//            lastPos = touchPos;
//            Vector3 touchpos = new Vector3();
//            touchpos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
//            camera.unproject(touchpos);
//            angle -= Gdx.input.getDeltaX();
//
//                    float xDistance = touchPos.x - cueSprite.getOriginX();
//                    float yDistance = touchPos.y - cueSprite.getOriginY();
//        //            sprite.setRotation((float)Math.toDegrees(Math.atan2(yDistance, xDistance)));
//
//                    sprite.setRotation ((int) (360 + Math.toDegrees(Math.atan2(yDistance, xDistance))) % 360);
               }
        TimeUtils
//        sprite.draw(game.batch);
        game.batch.end();
    }

    public void rotate(Vector2 touchPos) {
        double length = sprite.getWidth();
        sprite.getY();
    }

    public void resize(int width, int height) {
        Table table = new Table();
        table.setFillParent(true);
        table.setBackground(new TextureRegionDrawable(new TextureRegion(background)));
        stage.addActor(table);

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
    public void removeBall(int ballIndex)
    {
        ballBodies[ballIndex] = null;
    }
    public void contact()
    {
        float v1 = table.getTableInside().getX();
        float v2 = table.getTableInside().getY();
        Vector2 vertex1 = new Vector2(v1 + radius, v2 + radius);
        Vector2 vertex2 = new Vector2(v1 + radius, v2+ table.getTableInside().getHeight() - radius );
        Vector2 vertex3 = new Vector2(v1 + table.getTableInside().getWidth() - radius, v2 + radius);
        Vector2 vertex4 = new Vector2(v1 + table.getTableInside().getWidth() - radius, v2 + table.getTableInside().getHeight() - radius);

            for (int i = 0; i < ballBodies.length(); i++)
            {

                if (ballBodies[i].getPosition()== vertex1 || ballBodies[i].getPosition() == vertex2
                        || ballBodies[i].getPosition() == vertex3
                        || ballBodies[i].getPosition() == vertex4)
                {
                    removeBall(i);
                }
            }
        }

    }


}




