package com.yanglin.game.entity.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.yanglin.game.GameAssetManager;
import com.yanglin.game.entity.EntityEngine;
import com.yanglin.game.entity.MapManager;
import com.yanglin.game.entity.component.PositionComponent;
import com.yanglin.game.entity.component.TextureComponent;

import java.util.Comparator;

public class RenderingSystem extends SortedIteratingSystem implements MapManager.MapListener, Disposable {
    private static final String TAG = RenderingSystem.class.getSimpleName();
    private final ComponentMapper<TextureComponent> tm;
    private final ComponentMapper<PositionComponent> pm;
    private OrthogonalTiledMapRendererWithSprites tiledMapRenderer;
    private OrthographicCamera camera;
    private SpriteBatch batch;

    private PositionComponent playerPosComponent;

    private Array<Entity> renderQueue;
    private float unitScale;

    private GameAssetManager assetManager;
    private MapManager mapManager;

    private TextureMapObject playerTextureObj = new TextureMapObject();
    ;

    public RenderingSystem(GameAssetManager assetManager, MapManager mapManager, OrthographicCamera camera, SpriteBatch batch) {
        // TODO: RenderableComponent cause white screen
        super(Family.all(PositionComponent.class, TextureComponent.class).get(), new ZComparator());

        this.camera = camera;
        this.batch = batch;
        this.assetManager = assetManager;
        this.mapManager = mapManager;

        mapManager.addMapListener(this);

        tm = EntityEngine.textureComponentMapper;
        pm = EntityEngine.positionComponentMapper;

        renderQueue = new Array<Entity>();

        TiledMap map = assetManager.get(mapManager.getCurrentMap().getFileName(), TiledMap.class);
        // unitScale = 1 / 16f; // Assume width and height are the same
        MapLayer objectLayer = map.getLayers().get("playerLayer");
        if (objectLayer != null) {
            objectLayer.getObjects().add(playerTextureObj);
        } else {
            Gdx.app.log(TAG, "playerLayer is null in map: " + mapManager.getCurrentMap().name());
        }
        unitScale = 1 / (float) map.getProperties().get("tilewidth", Integer.class);
        tiledMapRenderer = new OrthogonalTiledMapRendererWithSprites(map, unitScale); // uniScale is necessary
        // ImmutableArray<Component> components = entity.getComponents();
    }

    protected void processEntity(Entity entity, float deltaTime) {
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
        renderQueue.add(entity);
    }

    @Override
    public void update(float deltaTime) {
        batch.setProjectionMatrix(camera.combined);
        super.update(deltaTime);
        batch.begin();

        for (Entity entity : renderQueue) {
            TextureComponent tex = tm.get(entity);
            PositionComponent pos = pm.get(entity);
            if (EntityEngine.playerComponentMapper.get(entity) != null) {
                playerTextureObj.setTextureRegion(tm.get(entity).region);
                playerTextureObj.setX(pos.position.x);
                playerTextureObj.setY(pos.position.y);
            }
            if (tex.region != null) {
                // batch.draw(tex.region, pos.position.x, pos.position.y, 1, 2);
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

    @Override
    public void mapChanged(MapManager.EMap EMap, float x, float y) {
        // Gdx.app.debug(TAG, "Changing map to: " + EMap.name());
        MapLayer oldObjectLayer = tiledMapRenderer.getMap().getLayers().get("playerLayer");
        if (oldObjectLayer != null) {
            oldObjectLayer.getObjects().remove(playerTextureObj);
        }

        TiledMap map = assetManager.get(mapManager.getCurrentMap().getFileName(), TiledMap.class);
        unitScale = 1 / (float) map.getProperties().get("tilewidth", Integer.class);

        MapLayer objectLayer = map.getLayers().get("playerLayer");
        if (objectLayer != null) {
            objectLayer.getObjects().add(playerTextureObj);
        } else {
            Gdx.app.log(TAG, "playerLayer is null in map: " + mapManager.getCurrentMap().name());
        }

        tiledMapRenderer.setMap(map);

    }

    @Override
    public void dispose() {
        // This is redundant since we already do clean up when map changed.
        // for(MapManager.EMap emap : MapManager.EMap.values()){
        //     TiledMap map = assetManager.get(emap.getFileName(), TiledMap.class);
        //     MapLayer objectLayer = map.getLayers().get("playerLayer");
        //     if(objectLayer != null) {
        //         objectLayer.getObjects().remove(playerTextureObj);
        //     }
        // }

        TiledMap map = assetManager.get(mapManager.getCurrentMap().getFileName(), TiledMap.class);
        MapLayer objectLayer = map.getLayers().get("playerLayer");
        if (objectLayer != null) {
            objectLayer.getObjects().remove(playerTextureObj);
        }
    }

    private static class ZComparator implements Comparator<Entity> {
        private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);

        @Override
        public int compare(Entity e1, Entity e2) {
            return (int) Math.signum(pm.get(e1).z - pm.get(e2).z);
        }
    }
}
