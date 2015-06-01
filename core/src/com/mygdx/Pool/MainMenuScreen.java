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
import com.badlogic.gdx.utils.Align;


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
    PoolTable poolTable;
    BitmapFont font;
    public MainMenuScreen(final Pool gam) {
        game = gam;
        poolTable = new PoolTable();
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        camera = new OrthographicCamera();
        background = new Texture(Gdx.files.internal("Background.png"));
        camera.setToOrtho(false, 800, 480);
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("MarkerFelt.ttc"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 70;
        parameter.color = Color.WHITE;
        font12 = generator.generateFont(parameter); // font size 12 pixels
        generator.dispose(); // don't forget to dispose to avoid memory leaks!
        atlas = new TextureAtlas("buttonpack2.txt");
        skin = new Skin();
        skin.addRegions(atlas);
        float buttonScale = 0.7f;
        Table table = new Table();
        table.setDebug(true);
        table.setFillParent(true);
        table.setBackground(new TextureRegionDrawable(new TextureRegion(background)));
        table.add();
        table.row().width(Gdx.graphics.getWidth() / 3);


        table.add(createButton("play", Color.BLACK)).height(Gdx.graphics.getWidth() / 3);
//        table.row();
        table.add(createButton(("multi"), Color.GREEN)).height(Gdx.graphics.getWidth() / 3);// Row 0, column 1.
//        table.row();
        table.add(createButton(("settings"), Color.GREEN)).height(Gdx.graphics.getWidth() / 3);

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
        style.up = skin.getDrawable(text);
        style.down = skin.getDrawable(text);

        Button button = new Button(style);
//        button.s(1000,1000);
        button.pad(50);
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

