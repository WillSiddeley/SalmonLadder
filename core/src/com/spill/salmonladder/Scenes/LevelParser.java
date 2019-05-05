package com.spill.salmonladder.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.spill.salmonladder.Fish;

public class LevelParser implements Screen {

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

        Fish fish = new Fish("Sprites/salmon.png");
        stage = new Stage(new ScreenViewport());

        stage.addActor(fish);

    }

    @Override
    public void render(float delta) {

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
}
