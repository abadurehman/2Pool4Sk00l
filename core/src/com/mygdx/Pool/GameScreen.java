package com.mygdx.Pool;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.util.Timer;

/**
 * Created by SrinjoyMajumdar on 5/5/15.
 */
public class GameScreen implements Screen {

    final Pool game;
    Texture background;
    Sprite bgSprite;
    OrthographicCamera camera;
    Stage stage;
    Texture cue;
    Rectangle cueRect;
    Texture stick;
    Rectangle stickRect;
    int change;

    public GameScreen(final Pool gam) {
        game = gam;
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        camera = new OrthographicCamera();
        background = new Texture(Gdx.files.internal("Background.png"));
        bgSprite = new Sprite(background);

        cue = new Texture(Gdx.files.internal("cue.png"));
        cueRect = new Rectangle();
        cueRect.x = 1600 / 2 - 50 / 2; // center the bucket horizontally
        cueRect.y = 800 / 2 - 50 / 2; // bottom left corner of the bucket is 20 pixels above
        // the bottom screen edge
        cueRect.width = 50;
        cueRect.height = 50;


        stick = new Texture(Gdx.files.internal("stick.png"));
        stickRect = new Rectangle();
        stickRect.width = 749;
        stickRect.height = 29;
        stickRect.y = cueRect.y + cueRect.height / 2 - stickRect.height / 2;
        stickRect.x = cueRect.x + cueRect.width;
        // the bottom screen edge


        camera.setToOrtho(false, 1600, 900);
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
        game.batch.draw(cue, cueRect.x, cueRect.y);
        game.batch.draw(stick, stickRect.x, stickRect.y);
        game.batch.end();


        rotate(new Vector2(cueRect.x + cueRect.width / 2, cueRect.y + cueRect.height / 2), new Vector2(stickRect.x, stickRect.y - stickRect.height / 2), 0.01);


        //  System.out.println(newPoint[0]+" "+newPoint[1]);
//        stickRect.x=newPoint[0];
//        stickRect.y = Math.abs(newPoint[1]);


    }


    public void rotate(Vector2 center, Vector2 stick, double angle) {

        float curAngle = stick.angle(center);
        curAngle += angle;
        float curDist = stick.dst(center);

        stickRect.x = (float) (Math.cos(curAngle) * curDist) + center.x;
        stickRect.y = (float) (Math.sin(curAngle) * curDist) + center.y;


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


}




