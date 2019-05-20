package com.spill.salmonladder;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.spill.salmonladder.Scenes.LevelParser;
import com.badlogic.gdx.graphics.g2d.Animation;

public class FishSprite extends Sprite implements GestureDetector.GestureListener {

    private TiledMapTileLayer collisionCheck;

    private Animation<Texture> swimUp, swimRight, swimDown, swimLeft;

    private float elapsedTime = 0;

    private float unitScale;

    public FishSprite(Texture[] fishTextures, TiledMapTileLayer tiledMap, float unitScale) {

        super(fishTextures[0]);

        swimUp = new Animation(1/20f, fishTextures);

        this.collisionCheck = tiledMap;

        this.unitScale = unitScale;

    }

    @Override
    public void draw(Batch batch) {

        elapsedTime += Gdx.graphics.getDeltaTime();

        setTexture(swimUp.getKeyFrame(elapsedTime, true));

        super.draw(batch);

    }

    private boolean CheckCollision(String direction) {

        if (direction.equals("up")) {

            return collisionCheck.getCell((int) (getX() / SalmonLadder.PIXEL_PER_METER), (int) ((getY() + getHeight()) / SalmonLadder.PIXEL_PER_METER)).getTile().getProperties().get("CanGoOn", boolean.class);

        } else if (direction.equals("down")) {

            return collisionCheck.getCell((int) (getX() / SalmonLadder.PIXEL_PER_METER), (int) ((getY() - getHeight()) / SalmonLadder.PIXEL_PER_METER)).getTile().getProperties().get("CanGoOn", boolean.class);

        } else if (direction.equals("right")) {

            return collisionCheck.getCell((int) ((getX() + getWidth()) / SalmonLadder.PIXEL_PER_METER), (int) (getY() / SalmonLadder.PIXEL_PER_METER)).getTile().getProperties().get("CanGoOn", boolean.class);

        } else {

            return collisionCheck.getCell((int) ((getX() - getWidth()) / SalmonLadder.PIXEL_PER_METER), (int) (getY() / SalmonLadder.PIXEL_PER_METER)).getTile().getProperties().get("CanGoOn", boolean.class);

        }

    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {

        if (!LevelParser.panMode && !LevelParser.screenLock) {

            if (Math.abs(velocityY) > Math.abs(velocityX)) {

                if (velocityY < 0) {

                    if (CheckCollision("up")) {

                        this.setPosition(getX(), getY() + 32f * unitScale);

                    }

                } else {

                    if (CheckCollision("down")) {

                        this.setPosition(getX(), getY() - 32f * unitScale);

                    }

                }

            } else {

                if (velocityX > 0) {

                    if (CheckCollision("right")) {

                        this.setPosition(getX() + 32f * unitScale, getY());

                    }

                } else {

                    if (CheckCollision("left")) {

                        this.setPosition(getX() - 32f * unitScale, getY());

                    }
                }
            }
        }

        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {
    }

}
