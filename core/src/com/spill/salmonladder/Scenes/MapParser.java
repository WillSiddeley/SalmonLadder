package com.spill.salmonladder.Scenes;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.spill.salmonladder.SalmonLadder;

class MapParser {

    static void parseMapLayers(World world, TiledMap tiledMap) {

        MapObjects objects = tiledMap.getLayers().get("Ground").getObjects();

        Array<Body> bodies = new Array<Body>();

        for (MapObject object : objects) {

            Shape shape;

            if (object instanceof RectangleMapObject) {

                shape = getRectangle((RectangleMapObject) object);

            } else {

                continue;

            }

            BodyDef bd = new BodyDef();
            bd.type = BodyDef.BodyType.KinematicBody;
            Body body = world.createBody(bd);
            body.createFixture(shape, 1);

            bodies.add(body);

            shape.dispose();

        }

    }

    private static PolygonShape getRectangle(RectangleMapObject rectangleObject) {

        Rectangle rectangle = rectangleObject.getRectangle();
        PolygonShape polygon = new PolygonShape();

        Vector2 size = new Vector2((rectangle.x + rectangle.width * 0.5f) / SalmonLadder.PIXEL_PER_METER,(rectangle.y + rectangle.height * 0.5f) / SalmonLadder.PIXEL_PER_METER);
        polygon.setAsBox(rectangle.width * 0.5f / SalmonLadder.PIXEL_PER_METER,rectangle.height * 0.5f / SalmonLadder.PIXEL_PER_METER, size,0.0f);

        return polygon;

    }

}