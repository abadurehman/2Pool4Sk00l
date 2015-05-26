package com.mygdx.Pool;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeType;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
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
    BitmapFont font12;

    BitmapFont font;
    public MainMenuScreen(final Pool gam) {
        game = gam;

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        camera = new OrthographicCamera();
        background = new Texture(Gdx.files.internal("Background.png"));
        camera.setToOrtho(false, 800, 480);
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("/System/Library/Fonts/MarkerFelt.ttc"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 70;
        parameter.color = Color.WHITE;
        font12 = generator.generateFont(parameter); // font size 12 pixels
        generator.dispose(); // don't forget to dispose to avoid memory leaks!
        atlas = new TextureAtlas("buttonpack2.txt");
        skin = new Skin();
        skin.addRegions(atlas);
        Table table = new Table();
        table.setFillParent(true);
        table.setBackground(new TextureRegionDrawable(new TextureRegion(background)));

        Button button = createButton("", Color.BLACK);
        table.add(createButton(("Person VS Person"), Color.GREEN));
        table.row();
        table.add(createButton(("Person VS Computer"), Color.GREEN));// Row 0, column 1.
        table.row();
        table.add(createButton(("Multiplayer"), Color.GREEN));
        table.row();
        table.add(createButton(("Settings"), Color.GREEN));
        stage.addActor(table);
    }


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
        font12.draw(game.batch, "2Pool4Skool", 220, 465);
        game.batch.end();

    }


    public void resize(int width, int height) {


    }

    public Button createButton(String text, Color color) {
        Button.ButtonStyle style = new Button.ButtonStyle();
        style.up = skin.getDrawable("multi");
        style.down = skin.getDrawable("play");

        Button button = new Button(style);
        button.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {

                return true;
            }

            public void touchUp(InputEvent event, float x, float y,
                                int pointer, int button) {
                game.setScreen(new Person_Comp(game));
            }

        });
        return button;
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

