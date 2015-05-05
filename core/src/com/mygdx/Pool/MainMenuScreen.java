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
    Texture background;
    Sprite bgSprite;
    OrthographicCamera camera;
    TextButton quit;
    Stage stage;
    Skin skin;
    TextureAtlas atlas;


    private static final float BUTTON_WIDTH = 300f;
    private static final float BUTTON_HEIGHT = 60f;
    private static final float BUTTON_SPACING = 10f;

    TextButton.TextButtonStyle textButtonStyle;

    public MainMenuScreen(final Pool gam) {
//         skin = new Skin(Gdx.files.internal("uiskin.json"));
        stage = new Stage();

        game = gam;
        Gdx.input.setInputProcessor(stage);


        camera = new OrthographicCamera();
        background = new Texture(Gdx.files.internal("Background.png"));
//        button = new TextButton("Test",);
//        Image img = new Image(background);


        bgSprite = new Sprite(background);
        camera.setToOrtho(false, 1600, 900);
    }


    @Override
    public void show() {
        atlas = new TextureAtlas("data/buttonpack.txt");
        skin = new Skin();
        skin.addRegions(atlas);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 0.1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        stage.draw();


//        game.font.draw(game.batch, "2Pool4Skool", 800, 900 - game.font.getCapHeight());
        game.batch.end();
    }

    @Override
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

            }
        });


        NinePatchDrawable patch = new NinePatchDrawable();

        Table table = new Table();
        table.setFillParent(true);
        table.setBackground(new TextureRegionDrawable(new TextureRegion(background)));

//        table.add(nameLabel);              // Row 0, column 0.


        table.add(quit);    // Row 0, column 1.

        stage.addActor(table);


    }

    private NinePatch getNinePatch(String fname) {

        // Get the image
        final Texture t = new Texture(Gdx.files.internal(fname));

        // create a new texture region, otherwise black pixels will show up too, we are simply cropping the image
        // last 4 numbers respresent the length of how much can each corner can draw,
        // for example if your image is 50px and you set the numbers 50, your whole image will be drawn in each corner
        // so what number should be good?, well a little less than half would be nice
        return new NinePatch(new TextureRegion(t, 1, 1, t.getWidth() - 2, t.getWidth() - 2), 10, 10, 10, 10);
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

