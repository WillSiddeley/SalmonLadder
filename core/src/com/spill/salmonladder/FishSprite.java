package com.spill.salmonladder;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;


public class FishSprite extends Sprite implements GestureDetector.GestureListener {


    private int orientation = 0, frameIndex;


    private String event;


    private boolean isWaitingStart = false, isWaitingJump = false, isWaitingJumpDone = false, isWaitingWinDone;


    private Timer.Task[] movement = new Timer.Task[4], jumpMovement = new Timer.Task[4];


    private Array<Texture> fishTextures = new Array<Texture>();


    private Array<Texture> jumpLeftTextures = new Array<Texture>();


    private Array<Texture> jumpRightTextures = new Array<Texture>();


    private Array<Texture> winLeftTextures = new Array<Texture>();


    private Array<Texture> winRightTextures = new Array<Texture>();


    private TiledMapTileLayer map;


    private Animation<Texture> show, swim, jumpLeft, jumpRight, winLeft, winRight;


    private float elapsedTime = 0, waitTime = 0, jumpEndTime;


    public FishSprite(int skin, TiledMapTileLayer tiledMap) {


        super(new Texture("Sprites/Textures/ChinookStagnant_1.png"));


        switch (skin) {

            case 0:
                for (int i = 0; i < 26; i++) {

                    fishTextures.add(new Texture("Sprites/Textures/ChinookStagnant_" + (i + 1) + ".png"));


                }


                for (int i = 0; i < 27; i++) {

                    jumpLeftTextures.add(new Texture("Sprites/Textures/ChinookJumpLeft_" + (i + 1) + ".png"));


                }


                for (int i = 0; i < 27; i++) {

                    jumpRightTextures.add(new Texture("Sprites/Textures/ChinookJumpRight_" + (i + 1) + ".png"));


                }


                for (int i = 0; i < 8; i++) {

                    winLeftTextures.add(new Texture("Sprites/Textures/ChinookWinLeft_" + (i + 1) + ".png"));


                }


                for (int i = 0; i < 8; i++) {

                    winRightTextures.add(new Texture("Sprites/Textures/ChinookWinRight_" + (i + 1) + ".png"));


                }


                break;


        }


        setTexture(fishTextures.first());


        swim = new Animation<Texture>(1 / 20f, fishTextures, Animation.PlayMode.LOOP);


        jumpLeft = new Animation<Texture>(1 / 20f, jumpLeftTextures, Animation.PlayMode.LOOP);


        jumpRight = new Animation<Texture>(1 / 20f, jumpRightTextures, Animation.PlayMode.LOOP);


        winLeft = new Animation<Texture>(1 / 20f, winLeftTextures, Animation.PlayMode.LOOP);


        winRight = new Animation<Texture>(1 / 20f, winRightTextures, Animation.PlayMode.LOOP);


        show = swim;


        init();


        this.map = tiledMap;


    }


    private void init() {

        movement[0] = new Timer.Task() {

            @Override
            public void run() {

                translate(0f, 4f);


            }


        }

        ;


        movement[1] = new Timer.Task() {

            @Override
            public void run() {

                translate(4f, 0f);


            }


        }

        ;


        movement[2] = new Timer.Task() {

            @Override
            public void run() {

                translate(0f, -4f);


            }


        }

        ;


        movement[3] = new Timer.Task() {

            @Override
            public void run() {

                translate(-4f, 0f);


            }


        }

        ;


        jumpMovement[0] = new Timer.Task() {

            @Override
            public void run() {

                translate(0f, 2f);


            }


        }

        ;


        jumpMovement[1] = new Timer.Task() {

            @Override
            public void run() {

                translate(2f, 0f);


            }


        }

        ;


        jumpMovement[2] = new Timer.Task() {

            @Override
            public void run() {

                translate(0f, -2f);


            }


        }

        ;


        jumpMovement[3] = new Timer.Task() {

            @Override
            public void run() {

                translate(-2f, 0f);


            }


        }

        ;


    }


    @Override
    public void draw(Batch batch) {

        elapsedTime += Gdx.graphics.getDeltaTime();

        setTexture(show.getKeyFrame(elapsedTime, true));

        super.draw(batch);

        if (isWaitingStart) {

            waitTime += Gdx.graphics.getDeltaTime();

            if (waitTime > 0.05f) {

                waitTime = 0;

                isWaitingStart = false;

                isWaitingJump = true;


            }


        }

        if (isWaitingJump && jumpRight.getKeyFrameIndex(elapsedTime) == 11) {

            isWaitingJump = false;

            Timer.schedule(jumpMovement[orientation], 0, 1 / 15f, 15);

            isWaitingJumpDone = true;


        }


        if (isWaitingJumpDone && jumpRight.isAnimationFinished(elapsedTime)) {

            jumpEndTime = elapsedTime;


        }


        if (isWaitingJumpDone && jumpRight.isAnimationFinished(jumpEndTime)) {

            if (getX() % 32 == 0 && getY() % 32 == 0) {

                isWaitingJumpDone = false;

                elapsedTime = 0;

                if (event.equals("EventWin")) {

                    if (frameIndex == 3) {

                        show = winLeft;


                    } else if (frameIndex == 16) {

                        show = winRight;


                    }


                    Timer.schedule(movement[orientation], 0, 1 / 20f, 7);

                    isWaitingWinDone = true;


                } else if (event.substring(0, 11).equals("EventLadder") || event.substring(0, 14).equals("EventWaterfall")) {

                    show = swim;

                    if (frameIndex == 3) {

                        elapsedTime = 9 / 20f;


                    } else if (frameIndex == 16) {

                        elapsedTime = 11 / 10f;


                    }


                    LevelParser.inAnimation = false;


                }


            }


        }


        if (isWaitingWinDone && winRight.isAnimationFinished(elapsedTime)) {


            isWaitingWinDone = false;


            LevelParser.unlockNext();


            LevelParser.WinTable.updateStarDrawable(LevelParser.awardStars());


            LevelParser.inWin = true;


        }


    }


    private boolean CheckCollisionIn() {


        if (orientation == 0) {


            return map.getCell((int) (getX() / SalmonLadderConstants.PIXEL_PER_METER), (int) ((getY() + getHeight()) / SalmonLadderConstants.PIXEL_PER_METER)).getTile().getProperties().get("CanGoUpIn", boolean.class);


        } else if (orientation == 2) {


            return map.getCell((int) (getX() / SalmonLadderConstants.PIXEL_PER_METER), (int) ((getY() - getHeight()) / SalmonLadderConstants.PIXEL_PER_METER)).getTile().getProperties().get("CanGoDownIn", boolean.class);


        } else if (orientation == 1) {


            return map.getCell((int) ((getX() + getWidth()) / SalmonLadderConstants.PIXEL_PER_METER), (int) (getY() / SalmonLadderConstants.PIXEL_PER_METER)).getTile().getProperties().get("CanGoRightIn", boolean.class);


        } else {


            return map.getCell((int) ((getX() - getWidth()) / SalmonLadderConstants.PIXEL_PER_METER), (int) (getY() / SalmonLadderConstants.PIXEL_PER_METER)).getTile().getProperties().get("CanGoLeftIn", boolean.class);


        }


    }


    private boolean CheckCollisionOut() {


        if (orientation == 0) {


            return map.getCell((int) (getX() / SalmonLadderConstants.PIXEL_PER_METER), (int) (getY() / SalmonLadderConstants.PIXEL_PER_METER)).getTile().getProperties().get("CanGoUpOut", boolean.class);


        } else if (orientation == 2) {


            return map.getCell((int) (getX() / SalmonLadderConstants.PIXEL_PER_METER), (int) (getY() / SalmonLadderConstants.PIXEL_PER_METER)).getTile().getProperties().get("CanGoDownOut", boolean.class);


        } else if (orientation == 1) {


            return map.getCell((int) (getX() / SalmonLadderConstants.PIXEL_PER_METER), (int) (getY() / SalmonLadderConstants.PIXEL_PER_METER)).getTile().getProperties().get("CanGoRightOut", boolean.class);


        } else {


            return map.getCell((int) (getX() / SalmonLadderConstants.PIXEL_PER_METER), (int) (getY() / SalmonLadderConstants.PIXEL_PER_METER)).getTile().getProperties().get("CanGoLeftOut", boolean.class);


        }


    }


    private boolean waterfallJump() {

        if (orientation == 0) {

            return map.getCell((int) (getX() / SalmonLadderConstants.PIXEL_PER_METER), (int) ((getY() + getHeight()) / SalmonLadderConstants.PIXEL_PER_METER)).getTile().getProperties().get("CanGoDownOut", boolean.class);


        } else if (orientation == 2) {

            return map.getCell((int) (getX() / SalmonLadderConstants.PIXEL_PER_METER), (int) ((getY() - getHeight()) / SalmonLadderConstants.PIXEL_PER_METER)).getTile().getProperties().get("CanGoUpOut", boolean.class);


        } else if (orientation == 1) {

            return map.getCell((int) ((getX() + getHeight()) / SalmonLadderConstants.PIXEL_PER_METER), (int) (getY() / SalmonLadderConstants.PIXEL_PER_METER)).getTile().getProperties().get("CanGoLeftOut", boolean.class);


        } else {

            return map.getCell((int) ((getX() - getHeight()) / SalmonLadderConstants.PIXEL_PER_METER), (int) (getY() / SalmonLadderConstants.PIXEL_PER_METER)).getTile().getProperties().get("CanGoRightOut", boolean.class);


        }


    }


    private String nextTile() {


        if (orientation == 0) {


            return map.getCell((int) (getX() / SalmonLadderConstants.PIXEL_PER_METER), (int) ((getY() + getHeight()) / SalmonLadderConstants.PIXEL_PER_METER)).getTile().getProperties().get("Name", String.class);


        } else if (orientation == 2) {


            return map.getCell((int) (getX() / SalmonLadderConstants.PIXEL_PER_METER), (int) ((getY() - getHeight()) / SalmonLadderConstants.PIXEL_PER_METER)).getTile().getProperties().get("Name", String.class);


        } else if (orientation == 1) {


            return map.getCell((int) ((getX() + getWidth()) / SalmonLadderConstants.PIXEL_PER_METER), (int) (getY() / SalmonLadderConstants.PIXEL_PER_METER)).getTile().getProperties().get("Name", String.class);


        } else {


            return map.getCell((int) ((getX() - getWidth()) / SalmonLadderConstants.PIXEL_PER_METER), (int) (getY() / SalmonLadderConstants.PIXEL_PER_METER)).getTile().getProperties().get("Name", String.class);


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


        if (canMove()) {


            LevelParser.inAnimation = true;


            if (Math.abs(velocityY) > Math.abs(velocityX)) {


                if (velocityY < 0) {


                    this.orientation = 0;


                    this.setRotation(0);


                } else {


                    this.orientation = 2;


                    this.setRotation(180);


                }


            } else {


                if (velocityX > 0) {


                    this.orientation = 1;


                    this.setRotation(270);


                } else {


                    this.orientation = 3;


                    this.setRotation(90);


                }


            }


            if (CheckCollisionIn() && CheckCollisionOut()) {


                HUDTable.setMoves(HUDTable.getMoves() + 1);


                if (map.getCell((int) (getX() / SalmonLadderConstants.PIXEL_PER_METER), (int) (getY() / SalmonLadderConstants.PIXEL_PER_METER)).getTile().getProperties().get("Name", String.class).substring(0, 5).equals("Event")) {

                    event = map.getCell((int) (getX() / SalmonLadderConstants.PIXEL_PER_METER), (int) (getY() / SalmonLadderConstants.PIXEL_PER_METER)).getTile().getProperties().get("Name", String.class);

                    if (event.equals("EventLadder")) {

                        isWaitingStart = true;


                    } else if (!nextTile().substring(0, 5).equals("Event")) {

                        Timer.schedule(movement[orientation], 0, 1 / 64f, 7);

                        LevelParser.inAnimation = false;


                    }


                }


                if (nextTile().substring(0, 5).equals("Event")) {

                    event = nextTile();

                    if (event.equals("EventWin") || event.equals("EventLadder") || (event.equals("EventWaterfall") && !waterfallJump())) {

                        isWaitingStart = true;


                    } else {

                        Timer.schedule(movement[orientation], 0, 1 / 64f, 7);

                        LevelParser.inAnimation = false;


                    }


                }


                if (!map.getCell((int) (getX() / SalmonLadderConstants.PIXEL_PER_METER), (int) (getY() / SalmonLadderConstants.PIXEL_PER_METER)).getTile().getProperties().get("Name", String.class).substring(0, 5).equals("Event") && !nextTile().substring(0, 5).equals("Event")) {

                    Timer.schedule(movement[orientation], 0, 1 / 64f, 7);

                    LevelParser.inAnimation = false;


                }


            } else {

                LevelParser.inAnimation = false;


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


    public boolean canMove() {


        return !LevelParser.inAnimation && !LevelParser.inDeath && !LevelParser.inMenu && !LevelParser.inWin && getX() % 32 == 0 && getY() % 32 == 0;


    }


}