package com.spill.salmonladder;

public abstract class EventTile extends LogicalTile{

    EventTile(int x, int y, boolean up, boolean right, boolean down, boolean left) {
        super(x, y, up, right, down, left);
    }

    public abstract void doEvent();
}
