package com.spill.salmonladder.Scenes;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class ScreenStart implements Screen {

    // CAMERA VARIABLE
    private OrthographicCamera camera;

    // SKINS AND TEXTURES FOR THE BUTTONS ON THE START SCREEN
    private Skin skin;
    private TextureAtlas atlas;

    // STAGE FOR ADDING THE BUTTONS, SO THEY CAN BE CLICKED
    private Stage stage;

    public ScreenStart() {

        // SET UP THE TEXTURES FOR THE BUTTONS
        atlas = new TextureAtlas("Skins/uiskin.atlas");
        skin = new Skin(Gdx.files.internal("Skins/uiskin.json"), atlas);

        // INSTANTIATE THE CAMERA TO THE SIZE OF THE PHONE SCREEN AND UPDATE IT
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
        camera.update();

        // SET THE STAGE TO THE SIZE OF THE PHONE SCREEN
        stage = new Stage(new ScreenViewport());

        // ENABLE THE APP TO LISTEN TO IF THE BACK KEY IS PRESSED ON ANDROID
        Gdx.input.setCatchBackKey(true);

    }

    @Override
    public void show() {

        // ENABLE OUR STAGE TO BE ABLE TO ACCEPT TOUCH PRESSES AS A FORM OF INPUT
        Gdx.input.setInputProcessor(stage);

        // CREATE A NEW TABLE TO HOLD THE BUTTONS, AND CENTER IT IN THE MIDDLE OF THE SCREEN
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.center();

        // CREATE 3 NEW BUTTONS FOR THE OPTIONS "PLAY", "SETTINGS" AND "EXIT"
        TextButton playButton = new TextButton("Play", skin);
        TextButton optionsButton = new TextButton("Settings", skin);
        TextButton exitButton = new TextButton("Exit", skin);

        // ADD A CLICK LISTENER ON TO THE PLAY BUTTONS SO THAT IF IT IS CLICKED IT MOVES THE PLAYER TO LEVEL SELECT
        playButton.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {

                // SET THE SCREEN TO THE LEVEL SELECT
                ((Game) Gdx.app.getApplicationListener()).setScreen(new ScreenLevelSelect());

            }
        });

        // ADD ALL 3 BUTTONS INTO THE TABLE AS WELL AS SET THEIR SIZE
        mainTable.add(playButton).expand(1, 1).fill(.5f, .5f).row();
        mainTable.add(optionsButton).expand(1, 1).fill(.5f, .5f).row();
        mainTable.add(exitButton).expand(1, 1).fill(.5f, .5f).row();

        // ADD THE TABLE TO THE STAGE
        stage.addActor(mainTable);


    }

    @Override
    public void render(float delta) {

        // SET THE BACKGROUND COLOUR OF THE TITLE SCREEN
        Gdx.gl.glClearColor(.1f, .12f, .16f, 0.5f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // EVERY FRAME, UPDATE THE STAGE FRAME
        stage.act();
        stage.draw();

        // IF THE PHYSICAL BACK BUTTON IS PRESSED ON THE PHONE, EXIT THE APP
        if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {

            Gdx.app.exit();

        }

    }

    @Override
    public void resize(int width, int height) {

        // IF THE PHONE SUPPORTS SPLITSCREEN APPS, AND THE APP IS RESIZED, UPDATE THE CAMERA TO THE NEW SIZE
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

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

        // DISPOSE OF UNUSED ASSESTS
        skin.dispose();
        atlas.dispose();


    }
}
