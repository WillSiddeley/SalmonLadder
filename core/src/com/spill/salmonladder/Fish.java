package com.spill.salmonladder;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Fish extends Actor {

    private Sprite sprite;

    public Fish(String img) {

        this.sprite = new Sprite(new Texture(Gdx.files.internal(img)));

        this.sprite.setScale(2f);

        this.setOrigin(this.sprite.getWidth() / 2, this.sprite.getHeight() / 2);

    }

    @Override
    public void draw(Batch batch, float alpha){

        sprite.draw(batch);

    }

    @Override
    public void positionChanged(){

        sprite.setPosition(getX(), getY());

    }

}
