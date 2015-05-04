package com.mygdx.Pool;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by SrinjoyMajumdar on 5/4/15.
 */
public class MainMenuScreen implements Screen {
    final Pool game;
    Texture background;
    OrthographicCamera camera;
    public  MainMenuScreen(final Pool gam) {
        game = gam;
        camera = new OrthographicCamera();
        background = new Texture(Gdx.files.internal("pool_table.png"));
        camera.setToOrtho(false, 800,400);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        game.batch.begin();
        game.batch.draw(background,0,0);
    }

    @Override
    public void resize(int width, int height) {

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

