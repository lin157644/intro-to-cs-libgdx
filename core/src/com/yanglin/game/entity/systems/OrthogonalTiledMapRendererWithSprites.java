package com.yanglin.game.entity.systems;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class OrthogonalTiledMapRendererWithSprites extends OrthogonalTiledMapRenderer {
    public OrthogonalTiledMapRendererWithSprites(TiledMap map, float unitScale) {
        super(map, unitScale);
    }

    @Override
    public void renderObject(MapObject object) {
        if (object instanceof TextureMapObject textureObj) {
            if (textureObj.getTextureRegion() != null)
                this.getBatch().draw(textureObj.getTextureRegion(), textureObj.getX(), textureObj.getY(), 1, 2);
        }
    }
}
