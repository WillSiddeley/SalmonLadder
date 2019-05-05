package com.spill.salmonladder.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.spill.salmonladder.Fish;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.rotateBy;

public class LevelParser implements Screen, GestureListener {

    // THESE VARIABLES CONTROL THE STATE THE GAME IS IN (RUNNING, PAUSED, ETC)
    public static final int GAME_RUNNING = 0;
    public static final int GAME_PAUSED = 1;
    public static final int GAME_CUTSCENE = 2;
    public static final int GAME_OVER = 3;

    // THE STATE INT IS EVALUATED AS THE INTEGER ABOVE DEPENDING ON THE STATE OF THE GAME
    // THE STATE INT IS USED FOR SWITCH AND COMPARISONS
    public static int state;

    // CREATE A NEW STAGE
    private Stage stage;

    // CREATE A TILED MAP VARIABLE THAT IS USED TO GENERATE THE LEVEL'S GRAPHICS
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer tiledMapRenderer;

    // CREATE A NEW CAMERA
    private OrthographicCamera camera;
    private ExtendViewport viewport;

    // CREATE TEXTURES THAT WILL BE USED FOR BUTTONS (MOVEMENT, PAUSE, ETC.)
    private Texture texture;

    // DEBUG BOOLEAN USED FOR DEBUGGING ONLY.  SET TO TRUE TO BE ABLE TO SEE THE HITBOXES OF BUTTONS, AND OBJECTS
    private Boolean debug = true;

    private Fish fish;
    private boolean panMode = false;
    private MoveByAction[] moves;
    private RotateByAction[] rotate;
    private SequenceAction sequence = new SequenceAction();

    LevelParser(int LevelNumber) {

        XmlReader.Element root = new XmlReader().parse(Gdx.files.internal("Levels.xml"));
        XmlReader.Element LevelAttributes = root.getChildByName("Level" + LevelNumber);

        // CAMERA
        camera = new OrthographicCamera(640, (640f) * Gdx.graphics.getHeight() / Gdx.graphics.getWidth());
        camera.position.set(320, 320, 0);
        camera.update();

        // MAP PARSING
        tiledMap = new TmxMapLoader().load(LevelAttributes.get("path"));
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        //MapParser.parseMapLayers(world, tiledMap);

    }


    @Override
    public void show() {

        Gdx.input.setInputProcessor(new GestureDetector(this));
        fish = new Fish("Sprites/salmon.png");
        stage = new Stage(new ScreenViewport());

        moves = new MoveByAction[4];
        for(int i = 0; i < moves.length; i++){
            moves[i] = new MoveByAction();
        }
        for(MoveByAction i: moves){
            i.setDuration(0.3f);
        }
        moves[0].setAmountY(32f);
        moves[1].setAmountX(32f);
        moves[2].setAmountY(-32f);
        moves[3].setAmountX(-32f);

        rotate = new RotateByAction[3];
        for(int i = 0; i < rotate.length; i++){
            rotate[i] = new RotateByAction();
        }
        for(RotateByAction i: rotate){
            i.setDuration(0.2f);
        }
        rotate[0].setAmount(90f);
        rotate[1].setAmount(-90f);
        rotate[2].setAmount(180f);

        stage.addActor(fish);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        stage.act();
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {

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

        stage.dispose();

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
        if(!panMode) {
            if (Math.abs(velocityY) > Math.abs(velocityX)) {
                if (velocityY < 0) {
                    moveUp();
                }
                else{
                    moveDown();
                }
            }
            else{
                if(velocityX > 0){
                    moveRight();
                }
                else{
                    moveLeft();
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

    private void moveUp(){
       switch(fish.getOrientation()){
           case 1: sequence.addAction(rotate[1]);
           break;
           case 2: sequence.addAction(rotate[2]);
           break;
           case 3: sequence.addAction(rotate[0]);
       }
       sequence.addAction(moves[0]);
       fish.addAction(sequence);
       fish.setOrientation(0);
       System.out.println("here");
    }

    private void moveRight(){
        switch(fish.getOrientation()){
            case 0: sequence.addAction(rotate[0]);
                break;
            case 2: sequence.addAction(rotate[1]);
                break;
            case 3: sequence.addAction(rotate[2]);
        }
        sequence.addAction(moves[1]);
        fish.addAction(sequence);
        fish.setOrientation(1);

    }

    private void moveDown(){
        System.out.println("There");
        switch(fish.getOrientation()){
            case 0: sequence.addAction(rotate[2]);
                break;
            case 1: sequence.addAction(rotate[0]);
                break;
            case 3: sequence.addAction(rotate[1]);
        }
        sequence.addAction(moves[2]);
        fish.addAction(sequence);
        fish.setOrientation(2);

    }

    private void moveLeft(){
        switch(fish.getOrientation()){
            case 0: sequence.addAction(rotate[1]);
                break;
            case 1: sequence.addAction(rotate[2]);
                break;
            case 2: sequence.addAction(rotate[0]);
        }
        sequence.addAction(moves[3]);
        fish.addAction(sequence);
        fish.setOrientation(3);

    }
}
