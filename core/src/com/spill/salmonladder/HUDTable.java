package com.spill.salmonladder;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

class HUDTable extends Table {

    private static int moves;

    private static Label movesLabel;

    HUDTable(int moves) {

        HUDTable.moves = moves;

        this.setFillParent(true);

        this.pad(50, 50, 50, 50);

        movesLabel = createMovesLabel(SalmonLadderConstants.SKIN);

        ImageButton pauseButton = createPauseButton();

        this.row().expand();

        this.add(movesLabel).top().left();

        this.add(pauseButton).top().right();

    }

    static int getMoves() {

        return moves;

    }

    static void setMoves(int newMoves) {

        movesLabel.setText("Moves: " + newMoves);

        moves = newMoves;

    }

    private Label createMovesLabel(Skin skin) {

        Label label = new Label("Moves: " + moves, skin);

        label.setStyle(new Label.LabelStyle(SalmonLadderConstants.FONT, Color.BLACK));

        label.setFontScale(1.5f);

        return label;

    }

    private ImageButton createPauseButton() {

        Drawable gearUp = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(SalmonLadderConstants.IMAGE_PATH_BUTTON_PAUSE))));

        ImageButton button = new ImageButton(gearUp);

        button.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {

                if (!LevelParser.inAnimation && !LevelParser.inDeath && !LevelParser.inMenu && !LevelParser.inWin) {

                    if (SalmonLadderConstants.PREFERENCES.isSoundEnabled()) {

                        SalmonLadderConstants.SOUND_MENU_OPEN.play();

                    }

                    LevelParser.inMenu = true;

                }
            }

        });

        return button;

    }


}
