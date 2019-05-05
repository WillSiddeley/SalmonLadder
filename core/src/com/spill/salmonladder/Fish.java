package com.spill.salmonladder;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.XmlReader;
import com.spill.salmonladder.Scenes.LevelParser;

public class Fish {

    private static final int FISH_SIZE = 32;

    private static XmlReader.Element root = new XmlReader().parse(Gdx.files.internal("Levels.xml"));
    private Body body;

    public Fish(World world) {

        createBody(world);

    }

    private void createBody(World world) {

        XmlReader.Element LevelAttributes = root.getChildByName("Level" + LevelParser.levelNumber);

        float playerStartX = LevelAttributes.getChildByName("StartCoords").getFloatAttribute("x");
        float playerStartY = LevelAttributes.getChildByName("StartCoords").getFloatAttribute("y");

        BodyDef bdef = new BodyDef();
        bdef.fixedRotation = false;
        bdef.type = BodyDef.BodyType.KinematicBody;
        bdef.position.set(playerStartX, playerStartY);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(FISH_SIZE / SalmonLadder.PIXEL_PER_METER / 2, FISH_SIZE / SalmonLadder.PIXEL_PER_METER / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;

        body = world.createBody(bdef);
        body.createFixture(fixtureDef).setUserData(this);

    }

    public Body getBody() {

        return body;

    }


}
