package com.spill.salmonladder;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

public class Menu extends Table {

    private SalmonLadderSettings preferences;

    private float widthCenter;

    private float heightCenter;

    public Menu(float heightModifier, float widthModifier, String ninePatchBG) {

        this.setVisible(false);

        this.setWidth(Gdx.graphics.getWidth() / widthModifier);

        this.setHeight(Gdx.graphics.getHeight() / heightModifier);

        this.widthCenter = (Gdx.graphics.getWidth() / 2f) - (this.getWidth() / 2f);

        this.heightCenter = (Gdx.graphics.getHeight() / 2f) - (this.getHeight() / 2f);

        this.setPosition(widthCenter, Gdx.graphics.getHeight() * 2f);

        setNinePatchBG(ninePatchBG);

    }

    private void setNinePatchBG(String internalPath) {

        NinePatch patch = new NinePatch(new Texture(Gdx.files.internal(internalPath)), 8, 8, 8, 8);

        NinePatchDrawable background = new NinePatchDrawable(patch);

        this.setBackground(background);

    }

    void bringToCenter(float duration, String menu) {

        this.setVisible(true);

        if (menu.equals("Death")) {

            LevelParser.inDeath = true;

        } else if (menu.equals("Pause")) {

            LevelParser.inMenu = true;

        } else if (menu.equals("Win")) {

            LevelParser.inWin = true;

        } else {

            System.out.println("Error: Specify menu type");

        }

        this.addAction(moveTo(widthCenter, heightCenter, duration));

    }

    void bringUp(float duration, String menu) {

        if (menu.equals("Death")) {

            LevelParser.inDeath = false;

        } else if (menu.equals("Pause")) {

            LevelParser.inMenu = false;

        } else if (menu.equals("Win")) {

            LevelParser.inWin = false;

        } else {

            System.out.println("Error: Specify menu type");

        }

        this.addAction(sequence(moveTo(widthCenter, Gdx.graphics.getHeight() * 2f, duration), run(new Runnable() {
            @Override
            public void run() {

                setVisible(false);

            }
        })));

    }

}
