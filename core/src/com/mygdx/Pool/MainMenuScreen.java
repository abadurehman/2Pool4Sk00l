package com.mygdx.Pool;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;


/**
 * Created by SrinjoyMajumdar on 5/4/15.
 */
public class MainMenuScreen implements Screen {
    /**
     * Your current game
     */
    final Pool game;
    /**
     * Button to quit
     */
    TextButton quit;
    /**
     * Your screen skin that everything else is built on
     */
    Skin skin;
    /**
     * Region of the packed image
     */
    TextureAtlas atlas;
    /**
     * The current stage
     */
    Stage stage;
    /**
     * Your view of the game
     */
    OrthographicCamera camera;
    /**
     * Background textures
     */
    Texture background;
    /**
     * Font of the text
     */
    BitmapFont font12;
    /**
     * The table
     */
    PoolTable poolTable;
    /**
     * Second font
     */
    BitmapFont font;

    /**
     * Table to which buttons are added
     */
    Table table;

    /**
     * Constructs the Main Menu Screen
     * @param gam Current Game
     */
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
        table = new Table();
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

    /**
     * Renders the Main Menu Screen
     * @param delta
     */
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

    /**
     * Creates the 3 buttons on the Main Menu
     * @param text What's written in the button
     * @param color The button color
     * @return a Button
     */
    public Button createButton(String text, Color color) {
        final String texthere = text;
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
                if (texthere.equals("play"))
                game.setScreen(new Person_Comp(game));
                else if (texthere.equals("multi"))
                    game.setScreen(new Multiplayer(game));
                else {
                }
//                    game.setScreen(new Settings(game));
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

