package com.spill.salmonladder;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.spill.salmonladder.Scenes.LevelParser;

public class FishSprite extends Sprite implements GestureDetector.GestureListener {

    private int orientation = 0;

    private boolean isWaitingStart = false, isWaitingJump = false, isWaitingJumpDone = false, isWaitingWinDone;

    private String event;

    private Thread threadExit, threadEnter;

    private Timer.Task[] movement = new Timer.Task[4];

    private Texture[] fishTextures = new Texture[26], jumpLeftTextures = new Texture[28], jumpRightTextures = new Texture[28], winLeftTextures = new Texture[8], winRightTextures = new Texture[8];

    private TiledMapTileLayer map;

    private Animation<Texture> show, swim, jumpLeft, jumpRight, winLeft, winRight;

    private float elapsedTime = 0, unitScale;

    public FishSprite(int skin, TiledMapTileLayer tiledMap, float unitScale) {

        super(new Texture("Sprites/Textures/ChinookStagnant_1.png"));

        switch(skin){
            case 0: for(int i = 0; i < fishTextures.length; i++){
                        fishTextures[i] = new Texture("Sprites/Textures/ChinookStagnant_" + (i+1) + ".png");
                    }

                    for(int i = 0; i < jumpLeftTextures.length; i++){
                        jumpLeftTextures[i] = new Texture("Sprites/Textures/ChinookJumpLeft_" + (i+1) + ".png");
                    }

                    for(int i = 0; i < jumpRightTextures.length; i++){
                        jumpRightTextures[i] = new Texture("Sprites/Textures/ChinookJumpRight_" + (i+1) + ".png");
                    }

                    for(int i = 0; i < winLeftTextures.length; i++){
                        winLeftTextures[i] = new Texture("Sprites/Textures/ChinookWinLeft_" + (i+1) + ".png");
                    }

                    for(int i = 0; i < winRightTextures.length; i++){
                        winRightTextures[i] = new Texture("Sprites/Textures/ChinookWinRight_" + (i+1) + ".png");
                    }
            break;
        }

        setTexture(fishTextures[0]);

        swim = new Animation(1 / 20f, fishTextures);

        jumpLeft = new Animation(1/20f, jumpLeftTextures);

        jumpRight = new Animation(1/20f, jumpRightTextures);

        winLeft = new Animation(1/20f, winLeftTextures);

        winRight = new Animation(1/20f, winRightTextures);

        show = swim;

        init();

        this.map = tiledMap;

        this.unitScale = unitScale;

        threadExit = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    doExitEvent(event);
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        threadEnter = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    doEntranceEvent(event);
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void init(){
        movement[0] = new Timer.Task() {
            @Override
            public void run() {
                translate(0f, 4f);
            }
        };

        movement[1] = new Timer.Task() {
            @Override
            public void run() {
                translate(4f, 0f);
            }
        };

        movement[2] = new Timer.Task() {
            @Override
            public void run() {
                translate(0f, -4f);
            }
        };

        movement[3] = new Timer.Task() {
            @Override
            public void run() {
                translate(-4f, 0f);
            }
        };
    }

    @Override
    public void draw(Batch batch) {
        elapsedTime += Gdx.graphics.getDeltaTime();

        setTexture(show.getKeyFrame(elapsedTime, true));

        super.draw(batch);

        System.out.println(isWaitingStart);
        System.out.println(swim.getKeyFrame(elapsedTime).equals(fishTextures[3]));//ALWAYS RETURNS FALSE
        System.out.println(swim.getKeyFrame(elapsedTime).equals(fishTextures[16]));//ALWAYS RETURNS FALSE

        if(isWaitingStart){
            if(swim.getKeyFrame(elapsedTime).equals(fishTextures[3]) || swim.getKeyFrame(elapsedTime).equals(fishTextures[16])){
                System.out.println("c");
                isWaitingStart = false;
                synchronized (this){
                    notifyAll();
                }
            }
        }
        else if(isWaitingJump){
            if(elapsedTime >= 1/2){
                isWaitingJump = false;
                synchronized (this){
                    notifyAll();
                }
            }
        }
        else if(isWaitingJumpDone){
            if(jumpRight.isAnimationFinished(elapsedTime)){
                isWaitingJumpDone = false;
                synchronized (this){
                    notifyAll();
                }
            }
        }
        else if(isWaitingWinDone){
            if(winRight.isAnimationFinished(elapsedTime)){
                synchronized (this){
                    notifyAll();
                }
            }
        }
    }

    private boolean CheckCollisionIn() {

        if (orientation == 0) {

            return map.getCell((int) (getX() / SalmonLadder.PIXEL_PER_METER), (int) ((getY() + getHeight()) / SalmonLadder.PIXEL_PER_METER)).getTile().getProperties().get("CanGoUpIn", boolean.class);

        } else if (orientation == 2) {

            return map.getCell((int) (getX() / SalmonLadder.PIXEL_PER_METER), (int) ((getY() - getHeight()) / SalmonLadder.PIXEL_PER_METER)).getTile().getProperties().get("CanGoDownIn", boolean.class);

        } else if (orientation == 1) {

            return map.getCell((int) ((getX() + getWidth()) / SalmonLadder.PIXEL_PER_METER), (int) (getY() / SalmonLadder.PIXEL_PER_METER)).getTile().getProperties().get("CanGoRightIn", boolean.class);

        } else {

            return map.getCell((int) ((getX() - getWidth()) / SalmonLadder.PIXEL_PER_METER), (int) (getY() / SalmonLadder.PIXEL_PER_METER)).getTile().getProperties().get("CanGoLeftIn", boolean.class);

        }

    }

    private boolean CheckCollisionOut(){

        if (orientation == 0) {

            return map.getCell((int) (getX() / SalmonLadder.PIXEL_PER_METER), (int) (getY() / SalmonLadder.PIXEL_PER_METER)).getTile().getProperties().get("CanGoUpOut", boolean.class);

        } else if (orientation == 2) {

            return map.getCell((int) (getX() / SalmonLadder.PIXEL_PER_METER), (int) (getY() / SalmonLadder.PIXEL_PER_METER)).getTile().getProperties().get("CanGoDownOut", boolean.class);

        } else if (orientation == 1) {

            return map.getCell((int) (getX() / SalmonLadder.PIXEL_PER_METER), (int) (getY() / SalmonLadder.PIXEL_PER_METER)).getTile().getProperties().get("CanGoRightOut", boolean.class);

        } else {

            return map.getCell((int) (getX() / SalmonLadder.PIXEL_PER_METER), (int) (getY() / SalmonLadder.PIXEL_PER_METER)).getTile().getProperties().get("CanGoLeftOut", boolean.class);

        }
    }

    private String nextTile(){

        if (orientation == 0) {

            return map.getCell((int) (getX() / SalmonLadder.PIXEL_PER_METER), (int) ((getY() + getHeight()) / SalmonLadder.PIXEL_PER_METER)).getTile().getProperties().get("Name", String.class);

        } else if (orientation == 2) {

            return map.getCell((int) (getX() / SalmonLadder.PIXEL_PER_METER), (int) ((getY() - getHeight()) / SalmonLadder.PIXEL_PER_METER)).getTile().getProperties().get("Name", String.class);

        } else if (orientation == 1) {

            return map.getCell((int) ((getX() + getWidth()) / SalmonLadder.PIXEL_PER_METER), (int) (getY() / SalmonLadder.PIXEL_PER_METER)).getTile().getProperties().get("Name", String.class);

        } else {

            return map.getCell((int) ((getX() - getWidth()) / SalmonLadder.PIXEL_PER_METER), (int) (getY() / SalmonLadder.PIXEL_PER_METER)).getTile().getProperties().get("Name", String.class);

        }
    }

    private void doExitEvent(String event) throws InterruptedException{
        if(event.equals("EventLadder")){
            isWaitingStart = true;
            synchronized (this){
                wait();
            }

            if(swim.getKeyFrame(elapsedTime).equals(fishTextures[3])){
                show = jumpLeft;
                elapsedTime = 0;
                isWaitingJump = true;
                synchronized (this){
                    wait();
                }

                Timer.schedule(movement[orientation], 0, 1/20f, 7);

                isWaitingJumpDone = true;
                synchronized (this){
                    wait();
                }

                elapsedTime = 1/2f;
                show = swim;
            }
            else if(swim.getKeyFrame(elapsedTime).equals(fishTextures[16])){
                show = jumpRight;
                elapsedTime = 0;
                isWaitingJump = true;
                synchronized (this){
                    wait();
                }

                Timer.schedule(movement[orientation], 0, 1/20f, 7);

                isWaitingJumpDone = true;
                synchronized (this){
                    wait();
                }

                elapsedTime = 23/20f;
                show = swim;
            }
        }
        threadExit.join();
    }

    private void doEntranceEvent(String event) throws InterruptedException{
        if(event.equals("EventWin")){
            if(!swim.getKeyFrame(elapsedTime).equals(fishTextures[3]) && !swim.getKeyFrame(elapsedTime).equals(fishTextures[16])){
                isWaitingStart = true;
                synchronized (this){
                    wait();
                }
            }

            if(swim.getKeyFrame(elapsedTime).equals(fishTextures[3])){
                show = jumpLeft;
                elapsedTime = 0;
                isWaitingJump = true;
                synchronized (this){
                    wait();
                }

                Timer.schedule(movement[orientation], 0, 1/20f, 7);

                isWaitingJumpDone = true;
                synchronized (this){
                    wait();
                }

                Timer.schedule(movement[orientation], 0, 1/20f, 7);

                show = winLeft;
                elapsedTime = 0;
                isWaitingWinDone = true;
                synchronized (this){
                    wait();
                }

                while(0 < 1){

                }

                //LEVEL WIN MENU METHOD HERE

            }
            else if(swim.getKeyFrame(elapsedTime).equals(fishTextures[16])){
                show = jumpRight;
                elapsedTime = 0;
                isWaitingJump = true;
                synchronized (this){
                    wait();
                }

                Timer.schedule(movement[orientation], 0, 1/20f, 7);

                isWaitingJumpDone = true;
                synchronized (this){
                    wait();
                }

                Timer.schedule(movement[orientation], 0, 1/20f, 7);

                show = winRight;
                elapsedTime = 0;
                isWaitingWinDone = true;
                synchronized (this){
                    wait();
                }

                while(0 < 1){

                }

                //LEVEL WIN MENU METHOD HERE

            }
        }
        else if(event.substring(0, 11).equals("EventLadder")){
            isWaitingStart = true;
            synchronized (this){
                System.out.println("a");
                wait();
            }
            System.out.println("d");

            if(swim.getKeyFrame(elapsedTime).equals(fishTextures[3])){
                show = jumpLeft;
                elapsedTime = 0;
                isWaitingJump = true;
                synchronized (this){
                    wait();
                }

                Timer.schedule(movement[orientation], 0, 1/20f, 7);

                isWaitingJumpDone = true;
                synchronized (this){
                    wait();
                }

                elapsedTime = 1/2f;
                show = swim;
            }
            else if(swim.getKeyFrame(elapsedTime).equals(fishTextures[16])){
                show = jumpRight;
                elapsedTime = 0;
                isWaitingJump = true;
                synchronized (this){
                    wait();
                }

                Timer.schedule(movement[orientation], 0, 1/20f, 7);

                isWaitingJumpDone = true;
                synchronized (this){
                    wait();
                }

                elapsedTime = 23/20f;
                show = swim;
            }
        }
        threadEnter.join();
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
                if(map.getCell((int) (getX() / SalmonLadder.PIXEL_PER_METER), (int) (getY()/ SalmonLadder.PIXEL_PER_METER)).getTile().getProperties().get("Name", String.class).substring(0, 5).equals("Event")){
                    try{
                        event = map.getCell((int) (getX() / SalmonLadder.PIXEL_PER_METER), (int) (getY() / SalmonLadder.PIXEL_PER_METER)).getTile().getProperties().get("Name", String.class);
                        threadExit.start();
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
                if(nextTile().substring(0, 5).equals("Event")){
                    try{
                        event = nextTile();
                        threadEnter.start();
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
                if(!map.getCell((int) (getX() / SalmonLadder.PIXEL_PER_METER), (int) (getY()/ SalmonLadder.PIXEL_PER_METER)).getTile().getProperties().get("Name", String.class).substring(0, 5).equals("Event") && !nextTile().substring(0, 5).equals("Event")){
                    Timer.schedule(movement[orientation], 0, 1 / 64f, 7);
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
