package com.yanglin.game.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.yanglin.game.entity.MapManager;

public class PositionComponent implements Component {
    public Vector2 position = new Vector2();
    public float z;

    public MapManager.EMap map;
    public Vector2 velocity = new Vector2();
}
