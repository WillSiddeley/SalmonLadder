package com.spill.salmonladder;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;


public class SalmonLadder extends Game implements ApplicationListener {

    @Override
    public void create() {

        this.setScreen(new ScreenStart());

    }

    @Override
    public void render() {

        super.render();

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void dispose() {

    }
}
