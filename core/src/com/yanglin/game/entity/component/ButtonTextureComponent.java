package com.yanglin.game.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ButtonTextureComponent implements Component {
    public TextureRegion normal = null;
    public TextureRegion hover = null;
    public TextureRegion pressed = null;
}
