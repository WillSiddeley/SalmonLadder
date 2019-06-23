package com.spill.salmonladder;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.repeat;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

public class ScreenStart implements Screen {

    // ADD THE CAMERA
    private OrthographicCamera camera;

    // ADD THE TEXTURE FOR THE TITLE SCREEN
    private Texture textureTitle;

    // ADD THE TEXTURE FOR THE SPLASH
    private Texture textureSplash;

    // ADD THE STAGE FOR USER INPUT
    private Stage stage;

    ScreenStart() {

        // SET THE STAGE TO THE SIZE OF THE PHONE SCREEN
        stage = new Stage(new FitViewport(SalmonLadderConstants.VIRTUAL_WIDTH, SalmonLadderConstants.VIRTUAL_HEIGHT));

        stage.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {

                if (SalmonLadderConstants.PREFERENCES.isSoundEnabled()) {

                    SalmonLadderConstants.SOUND_CLICK.play();

                }

                stage.addAction(sequence(alpha(1), moveTo((-Gdx.graphics.getWidth() - stage.getWidth()), 0, 0.5f), run(new Runnable() {

                    @Override
                    public void run() {

                        ((Game) Gdx.app.getApplicationListener()).setScreen(new ScreenLevelSelect());

                    }

                })));

            }

        });

        // ENABLE OUR STAGE TO BE ABLE TO ACCEPT TOUCH PRESSES AS A FORM OF INPUT
        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void show() {

        // INSTANTIATE THE CAMERA TO THE SIZE OF THE PHONE SCREEN AND UPDATE IT
        camera = new OrthographicCamera();

        // POSITION THE CAMERA TO THE MIDDLE OF THE SCREEN
        camera.position.set(SalmonLadderConstants.VIRTUAL_WIDTH / 2f, SalmonLadderConstants.VIRTUAL_HEIGHT / 2f, 0);

        // SET THE IMAGE TO A TEXTURE
        textureTitle = new Texture(Gdx.files.internal(SalmonLadderConstants.IMAGE_PATH_TITLE));
        textureSplash = new Texture(Gdx.files.internal(SalmonLadderConstants.IMAGE_PATH_SPLASH));

        // INSTANTIATE IMAGE
        Image imageTitle = new Image();
        Image imageSplash = new Image();

        // SET THE IMAGE TEXTURE TO THE TITLE SCREEN
        imageTitle.setDrawable(new TextureRegionDrawable(new TextureRegion(textureTitle)));
        imageSplash.setDrawable(new TextureRegionDrawable(new TextureRegion(textureSplash)));

        // SET THE SIZE OF THE IMAGE
        imageTitle.setSize(textureTitle.getWidth(), textureTitle.getHeight());
        imageSplash.setSize(textureSplash.getWidth(), textureSplash.getHeight());

        // CENTER THE IMAGE
        imageTitle.setPosition(camera.position.x - (textureTitle.getWidth() / 2f), camera.position.y - (textureTitle.getHeight() / 7f));
        imageSplash.setPosition(camera.position.x - (textureSplash.getWidth() / 2f), camera.position.y - (textureSplash.getHeight() * 1.25f));

        // ADD THE IMAGE TO THE STAGE
        stage.addActor(imageTitle);
        stage.addActor(imageSplash);

        // FADE IN THE STAGE
        imageTitle.addAction(sequence(alpha(0), parallel(fadeIn(0.5f), moveBy(0, 100, 1))));
        imageSplash.addAction(repeat(RepeatAction.FOREVER, sequence(fadeIn(0.5f), fadeOut(0.5f))));

    }

    @Override
    public void render(float delta) {

        // SET THE BACKGROUND COLOUR OF THE TITLE SCREEN
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);

        // ACT STAGE
        stage.act();
        stage.draw();

        if (SalmonLadderConstants.MUSIC_AMBIANT.isPlaying()) {

            SalmonLadderConstants.MUSIC_AMBIANT.stop();

        }

    }

    @Override
    public void resize(int width, int height) {

        camera.viewportWidth = width;
        camera.viewportHeight = height;
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

        dispose();

    }

    @Override
    public void dispose() {

        stage.dispose();

        textureTitle.dispose();

        textureSplash.dispose();

    }

}
