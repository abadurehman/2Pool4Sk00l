package com.mygdx.Pool;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;


import java.util.HashMap;

/**
 * Created by SrinjoyMajumdar on 5/12/15.
 */
public class PoolTable extends Stage {
    private Table table;
    private Vector2[] pockets;
    Slider slider;
    Skin skin;
    TextureAtlas atlas;
    Slider.SliderStyle sliderStyle;
    private final Vector2 topLeft = new Vector2(97, 354);
    private int widthPockets = 294;
    private int heightPockets = 312;
    private Rectangle tableInside = new Rectangle(97, 44, 586, 313);

    public PoolTable() {
        table = new Table();
        table.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Background.png")))));
        table.setFillParent(true);
        super.addActor(table);
        atlas = new TextureAtlas("uiskin.atlas");
        skin = new Skin();
        skin.addRegions(atlas);
        sliderStyle = new Slider.SliderStyle();
        sliderStyle.knob = skin.getDrawable("default-slider-knob");
        sliderStyle.background = skin.getDrawable("default-slider");
        slider = new Slider((float) 0.0, (float) 10.0, (float) 0.1, true, sliderStyle);

        slider.setX(50);
        slider.setY(50);

        super.addActor(slider);
        pockets = new Vector2[6];
        for (int i = 0; i < 3; i++) {
            pockets[i] = new Vector2((topLeft.x + widthPockets * (i)), topLeft.y);
        }
        for (int i = 3; i < 6; i++) {
            pockets[i] = new Vector2((topLeft.x + widthPockets * (i)), topLeft.y + heightPockets);
        }
    }


//    public void setPosition(int index, Vector2 position)
//    {
//        positions[index] = position;
//    }

    public Vector2 getPocketIndex(int index) {
        return pockets[index];
    }

    public Vector2[] getPockets() {
        return pockets;
    }

    public Rectangle getTableInside() {
        return tableInside;
    }

    public Slider getSlider() {
        return slider;
    }

}
