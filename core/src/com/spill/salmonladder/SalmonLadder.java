package com.spill.salmonladder;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class SalmonLadder extends Game implements ApplicationListener {

    public static final float PIXEL_PER_METER = 32f;

    public static Skin SKIN;

    public static BitmapFont FONT;

    @Override
    public void create() {

        SKIN = new Skin(Gdx.files.internal("Skins/uiskin.json"));

        FONT = new BitmapFont(Gdx.files.internal("Skins/MovesFont.fnt"));

        this.setScreen(new ScreenStart());

    }

    @Override
    public void render() {

        // SET THE BACKGROUND COLOUR OF THE TITLE SCREEN
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(255f, 255f, 255f, 0f);

        // CALL THE SUPERCLASS RENDER
        super.render();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void dispose() {

    }
}
