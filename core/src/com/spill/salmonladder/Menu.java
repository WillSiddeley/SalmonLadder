package com.spill.salmonladder;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;

class Menu extends Table {

    private float widthCenter;

    private float heightCenter;

    Menu(float heightModifier, float widthModifier, String ninePatchBG) {

        this.setWidth(SalmonLadderConstants.VIRTUAL_WIDTH / widthModifier);

        this.setHeight(SalmonLadderConstants.VIRTUAL_HEIGHT / heightModifier);

        this.widthCenter = (SalmonLadderConstants.VIRTUAL_WIDTH / 2f) - (this.getWidth() / 2f);

        this.heightCenter = (SalmonLadderConstants.VIRTUAL_HEIGHT / 2f) - (this.getHeight() / 2f);

        this.setPosition(widthCenter, Gdx.graphics.getHeight() * 2f);

        setNinePatchBG(ninePatchBG);

    }

    private void setNinePatchBG(String internalPath) {

        NinePatch patch = new NinePatch(new Texture(Gdx.files.internal(internalPath)), 8, 8, 8, 8);

        NinePatchDrawable background = new NinePatchDrawable(patch);

        this.setBackground(background);

    }

    void bringToCenter(String menu) {

        if (menu.equals(SalmonLadderConstants.MENU_TYPE_DEATH)) {

            LevelParser.inDeath = true;

        } else if (menu.equals(SalmonLadderConstants.MENU_TYPE_PAUSE)) {

            LevelParser.inMenu = true;

        } else if (menu.equals(SalmonLadderConstants.MENU_TYPE_TUTORIAL)) {

            LevelParser.inTutorial = true;

        } else if (menu.equals(SalmonLadderConstants.MENU_TYPE_WIN)) {

            LevelParser.inWin = true;

        } else {

            System.out.println("Error: Specify menu type");

        }

        this.addAction(moveTo(widthCenter, heightCenter, SalmonLadderConstants.ANIMATION_DURATION));

    }

    void bringUp(String menu) {

        if (menu.equals(SalmonLadderConstants.MENU_TYPE_DEATH)) {

            LevelParser.inDeath = false;

        } else if (menu.equals(SalmonLadderConstants.MENU_TYPE_PAUSE)) {

            LevelParser.inMenu = false;

        } else if (menu.equals(SalmonLadderConstants.MENU_TYPE_TUTORIAL)) {

            LevelParser.inTutorial = false;

            SalmonLadderConstants.TUTORIAL.setTutorialCompleted(LevelParser.levelNumber, true);

        } else if (menu.equals(SalmonLadderConstants.MENU_TYPE_WIN)) {

            LevelParser.inWin = false;

        } else {

            System.out.println("Error: Specify menu type");

        }

        this.addAction(moveTo(widthCenter, Gdx.graphics.getHeight() * 2f, SalmonLadderConstants.ANIMATION_DURATION));

    }

}
