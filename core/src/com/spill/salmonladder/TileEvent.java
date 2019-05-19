package com.spill.salmonladder;

public abstract class TileEvent extends TileLogical {

    TileEvent(int x, int y, boolean up, boolean right, boolean down, boolean left) {
        super(x, y, up, right, down, left);
    }

    public abstract void doEvent();
}
