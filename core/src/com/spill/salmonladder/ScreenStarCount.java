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
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

public class ScreenStarCount implements Screen {

    // ADD THE CAMERA
    private OrthographicCamera camera;

    //ADD THE TABLE
    private Table awardTable;

    // ADD THE STAGE FOR USER INPUT
    private Stage stage;

    private int totalStarCount = getTotalStars();

    ScreenStarCount() {

        // SET THE STAGE TO THE SIZE OF THE PHONE SCREEN
        stage = new Stage(new FitViewport(SalmonLadderConstants.VIRTUAL_WIDTH, SalmonLadderConstants.VIRTUAL_HEIGHT));

        stage.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {

                if (SalmonLadderConstants.SETTINGS.isSoundEnabled()) {

                    SalmonLadderConstants.SOUND_CLICK.play();

                }

                stage.addAction(sequence(alpha(1), moveTo((-Gdx.graphics.getWidth() - stage.getWidth()), 0, 0.5f), run(new Runnable() {

                    @Override
                    public void run() {

                        ((Game) Gdx.app.getApplicationListener()).setScreen(new ScreenStart());

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

        awardTable = new Table();

        awardTable.setFillParent(true);

        awardTable.add(createTopLabel()).expand().center();

        awardTable.row().expand().center();

        awardTable.add(createStarCount()).expand().center();

        awardTable.row().expand().center();

        awardTable.add(createBotLabel()).fill().center();

        stage.addActor(awardTable);

    }

    @Override
    public void render(float delta) {

        // SET THE BACKGROUND COLOUR OF THE TITLE SCREEN
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);

        // ACT STAGE
        stage.act();
        stage.draw();

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

    }

    private Table createTopLabel() {

        Table labelTable = new Table();

        Label label = new Label("Congratulations!", SalmonLadderConstants.SKIN);

        Label label2 = new Label("You Win!", SalmonLadderConstants.SKIN);

        label.setFontScale(1.5f);

        label2.setFontScale(1.5f);

        labelTable.add(label).expand().center().bottom().pad(40);

        labelTable.row().expand().center();

        labelTable.add(label2).expand().center().top().pad(40);

        return labelTable;

    }

    private Table createStarCount() {

        Table starTable = new Table();

        // TEXTURE

        Texture Star = new Texture(Gdx.files.internal(SalmonLadderConstants.IMAGE_PATH_STAR));

        Image StarImage = new Image();

        StarImage.setDrawable(new TextureRegionDrawable(new TextureRegion(Star)));

        starTable.add(StarImage).width(Star.getWidth() / 3f).height(Star.getHeight() / 3f).expand().bottom().pad(40);

        // LABEL

        Label starCount = new Label(totalStarCount + " stars!", SalmonLadderConstants.SKIN);

        starCount.setFontScale(2f);

        starTable.row().expand().center();

        starTable.add(starCount).expand().center().top().pad(40);

        return starTable;

    }

    private Table createBotLabel() {

        Table labelTable = new Table();

        Label label = new Label("Thanks for playing!", SalmonLadderConstants.SKIN);

        Label label2;

        Label label3;

        if (getTotalStars() == SalmonLadderConstants.LEVEL_COUNT * 3) {

            label2 = new Label("Try to collect", SalmonLadderConstants.SKIN);

            label3 = new Label("all the stars!", SalmonLadderConstants.SKIN);

        } else {

            label2 = new Label("A true master", SalmonLadderConstants.SKIN);

            label3 = new Label("of Salmon Ladder!", SalmonLadderConstants.SKIN);

        }

        label.setFontScale(1.25f);

        label2.setFontScale(1.25f);

        label3.setFontScale(1.25f);

        labelTable.row().expand();

        labelTable.add(label).center();

        labelTable.row().expand();

        labelTable.add();

        labelTable.row().expand();

        labelTable.add();

        labelTable.row().expand();

        labelTable.add(label2).center();

        labelTable.row().expand();

        labelTable.add(label3).center();

        labelTable.row().expand();

        labelTable.add();

        labelTable.row().expand();

        labelTable.add();

        return labelTable;

    }

    private int getTotalStars() {

        int starCountInt = 0;

        for (int i = 0; i < SalmonLadderConstants.LEVEL_COUNT; i++) {

            starCountInt += SalmonLadderConstants.STARS.getStars(1);

        }

        return starCountInt;

    }
}
