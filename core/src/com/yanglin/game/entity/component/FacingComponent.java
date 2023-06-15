package com.yanglin.game.entity.component;

import com.badlogic.ashley.core.Component;

public class FacingComponent implements Component {
    public enum Facing {
        FRONT,
        BACK,
        LEFT,
        RIGHT,
    }
    public Facing facing;
}

