package com.yanglin.game.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ToggleComponent implements Component {
    public Boolean isEnable = true;
    public TextureRegion onTexture = null;
    public TextureRegion offTexture = null;
}
