package com.yanglin.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.yanglin.game.GameState;
import com.yanglin.game.IWantToGraduate;

public class MapManager {
    private static final String TAG = MapManager.class.getSimpleName();
    private final Array<MapListener> mapListeners;
    public enum EMap {
        SCHOOL_GATE("tilemaps/school_gate.tmx"),
        DORM_EXTER("tilemaps/dorm_exterior.tmx"),
        ENGINEER_V("tilemaps/engineering_building.tmx"),
        ACAD_OFFICE("tilemaps/academic_office.tmx"),
        LIBRARY_EXTER("tilemaps/library_exterior.tmx"),
        INTERSEC("tilemaps/intersection.tmx"),
        DORM_INTER("tilemaps/dorm.tmx"),
        LIBRARY_INTER("tilemaps/library_interior.tmx");

        private final String fileName;

        EMap(String fileName) {
            this.fileName = fileName;
        }

        public String getFileName() {
            return fileName;
        }
    }

    private EMap currentMap;
    private IWantToGraduate game;

    public MapManager(IWantToGraduate game) {
        this.mapListeners = new Array<>();
        this.game = game;
    }

    public void addMapListener(final MapListener mapListener) {
        mapListeners.add(mapListener);
    }

    public void notifyMapListener(EMap EMap, float x, float y){
        Gdx.app.debug(TAG, "Notifying map listeners");
        for (MapListener mapListener : mapListeners){
            mapListener.mapChanged(EMap, x, y);
        }
    }
    public void clearMapListeners(){
        mapListeners.clear();
    }

    public interface MapListener {
        void mapChanged(final EMap EMap, float x, float y);
    }

    public void setCurrentMap(EMap map, float x, float y){
        Gdx.app.debug(TAG, "Setting current map: " + map.name());
        currentMap = map;
        game.gameState.map = map;
        Gdx.app.debug(TAG, game.gameState.map.name());
        notifyMapListener(map, x, y);
    }

    public EMap getCurrentMap() {
        return currentMap;
    }

}
