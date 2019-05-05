package com.spill.salmonladder;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

public class Fish extends Actor {

    private Sprite sprite;

    public Fish(String img) {

        this.sprite = new Sprite(new Texture(Gdx.files.internal(img)));

        this.sprite.setScale(2f);

        this.setOrigin(this.sprite.getWidth() / 2, this.sprite.getHeight() / 2);

        this.setTouchable(Touchable.enabled);

        this.addListener(new ActorGestureListener() {

            @Override
            public void fling(InputEvent event, float velocityX, float velocityY, int button) {

                move(1);

            }

        });

    }

    @Override
    public void draw(Batch batch, float alpha) {

        sprite.draw(batch);

    }

    @Override
    public void positionChanged() {

        sprite.setPosition(getX(), getY());

    }

    public void move(int direction) {

        switch (direction) {

            case 1:
                setPosition(getX(), getY() + 16);
                break;

            case 2:
                setPosition(getX(), getY() - 16);
                break;

            case 3:
                setPosition(getX() + 16, getY());
                break;

            case 4:
                setPosition(getX() - 16, getY());
                break;


        }

    }

}
