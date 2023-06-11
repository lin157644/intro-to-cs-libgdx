package com.yanglin.game.entity.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.yanglin.game.MainAssetManager;
import com.yanglin.game.entity.EntityEngine;
import com.yanglin.game.entity.component.FontComponent;
import com.yanglin.game.entity.component.PositionComponent;
import com.yanglin.game.entity.component.RenderableComponent;
import com.yanglin.game.entity.component.TextureComponent;
import com.badlogic.gdx.utils.Array;

import java.util.Comparator;

public class RenderingSystem extends SortedIteratingSystem {
    private final ComponentMapper<TextureComponent> tm;
    private final ComponentMapper<FontComponent> fm;
    private final ComponentMapper<PositionComponent> pm;
    private final OrthogonalTiledMapRenderer tiledMapRenderer;
    private OrthographicCamera camera;
    private SpriteBatch batch;

    private PositionComponent playerPosComponent;

    private Array<Entity> renderQueue;

    public RenderingSystem(MainAssetManager assetManager, OrthographicCamera camera, SpriteBatch batch) {
        super(Family.all(PositionComponent.class, TextureComponent.class).get(), new ZComparator());

        this.camera = camera;
        this.batch = batch;

        tm = ComponentMapper.getFor(TextureComponent.class);
        fm = ComponentMapper.getFor(FontComponent.class);
        pm = ComponentMapper.getFor(PositionComponent.class);

        renderQueue = new Array<Entity>();

        TiledMap map = assetManager.get("tilemaps/school_gate.tmx", TiledMap.class);

        float unitScale = 1 / 16f;
        tiledMapRenderer = new OrthogonalTiledMapRenderer(map, unitScale); // uniScale is necessary
        // ImmutableArray<Component> components = entity.getComponents();
    }

    protected void processEntity(Entity entity, float deltaTime) {
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
        renderQueue.add(entity);
    }

    @Override
    public void update(float deltaTime){
        batch.setProjectionMatrix(camera.combined);
        super.update(deltaTime);
        batch.begin();
        for (Entity entity : renderQueue) {
            TextureComponent tex = tm.get(entity);
            PositionComponent pos = pm.get(entity);
            if(tex.region != null){
                batch.draw(tex.region, pos.position.x, pos.position.y, 1, 2);
            }
            // batch.draw(tex.region,
            //         t.position.x - originX + tex.offsetX,
            //         t.position.y - originY + tex.offsetY,
            //         originX, originY,
            //         width, height,
            //         PixelsToMeters(t.scale.x), PixelsToMeters(t.scale.y),
            //         t.rotation);
        }
        batch.end();
    }

    private static class ZComparator implements Comparator<Entity> {
        private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);

        @Override
        public int compare(Entity e1, Entity e2) {
            return (int) Math.signum(pm.get(e1).z - pm.get(e2).z);
        }
    }
}
