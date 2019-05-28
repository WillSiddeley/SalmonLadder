package com.spill.salmonladder.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class PauseTable extends Table {

    private ClickListener pauseClickListener = new ClickListener() {

        @Override
        public void clicked(InputEvent event, float x, float y) {


        }
    };

    PauseTable() {

        int height = Gdx.graphics.getHeight();

        int width = Gdx.graphics.getWidth();

        this.setVisible(false);

        this.setHeight(height / 2f);

        this.setWidth(width / 1.25f);

        this.setPosition((Gdx.graphics.getWidth() / 2f) - (this.getWidth() / 2f), Gdx.graphics.getHeight() * 2f);

        this.row().expand();

        Drawable bg = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Images/white_color_texture.png"))));

        this.setBackground(bg);

    }


}
