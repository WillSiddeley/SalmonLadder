package com.spill.salmonladder.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.XmlReader;
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

    // CREATE A NEW WORLD AND STAGE
    private World world;
    private Stage stage;
    private Fish fish;

    // CREATE A TILED MAP VARIABLE THAT IS USED TO GENERATE THE LEVEL'S GRAPHICS
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer tiledMapRenderer;

    // CREATE A NEW SPRITE BATCH THAT DRAWS ONTO THE SCREEN
    private SpriteBatch batch = new SpriteBatch();

    // CREATE A NEW CAMERA
    private OrthographicCamera camera = new OrthographicCamera();

    // CREATE TEXTURES THAT WILL BE USED FOR BUTTONS (MOVEMENT, PAUSE, ETC.)
    private Texture texture;

    // DEBUG BOOLEAN USED FOR DEBUGGING ONLY.  SET TO TRUE TO BE ABLE TO SEE THE HITBOXES OF BUTTONS, AND OBJECTS
    private Boolean debug = true;

    // LEVEL NUMBER VARIABLE IS USED TO DETERMINE THE MAP THAT WILL BE LOADED WHEN PARSED IN FROM SCREENLEVELSELECT
    public static int levelNumber;

    public LevelParser(int LevelNumber) {

        XmlReader.Element root = new XmlReader().parse(Gdx.files.internal("Levels.xml"));
        XmlReader.Element LevelAttributes = root.getChildByName("Level" + LevelNumber);

        levelNumber = LevelNumber;

        // CAMERA
        camera.setToOrtho(false, Gdx.graphics.getWidth() / 1.7f, Gdx.graphics.getHeight() / 1.7f);
        camera.update();

        // MAP PARSING
        tiledMap = new TmxMapLoader().load(LevelAttributes.get("path"));
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        //MapParser.parseMapLayers(world, tiledMap);

        // PLAYER
        fish = new Fish(world);
        texture = new Texture("Skins/e.");

        // HUD
        stage = new Stage(new ScreenViewport());
        //InputTable.createInputTable(stage, debug);

    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

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

    }
}
