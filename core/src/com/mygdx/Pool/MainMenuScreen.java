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
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Created by SrinjoyMajumdar on 5/4/15.
 */
public class MainMenuScreen implements Screen {
    final Pool game;
    TextButton quit;
    Skin skin;
    TextureAtlas atlas;
    Stage stage;
    OrthographicCamera camera;
    Texture background;

    public MainMenuScreen(final Pool gam) {
        game = gam;

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        camera = new OrthographicCamera();
        background = new Texture(Gdx.files.internal("Background.png"));
        camera.setToOrtho(false, 800, 480);
    }


    public void show() {
        atlas = new TextureAtlas("data/buttonpack.txt");
        skin = new Skin();
        skin.addRegions(atlas);
    }


    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 0.1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        stage.draw();
        game.batch.end();

    }


    public void resize(int width, int height) {

        TextButton.TextButtonStyle styleQuit = new TextButton.TextButtonStyle();
        styleQuit.up = skin.getDrawable("green");
        styleQuit.down = skin.getDrawable("red");

        styleQuit.font = game.font;

        quit = new TextButton("testing", styleQuit);
        quit.setSize(100, 100);
        quit.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {

                return true;
            }

            public void touchUp(InputEvent event, float x, float y,
                                int pointer, int button) {
                game.setScreen(new Person_Comp(game));
            }
        });

        Table table = new Table();
        table.setFillParent(true);
        table.setBackground(new TextureRegionDrawable(new TextureRegion(background)));

        table.add(quit);    // Row 0, column 1.

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

