package com.yanglin.game.entity;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

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

    public MapManager() {
        this.mapListeners = new Array<>();
    }

    public void addMapListener(final MapListener mapListener) {
        mapListeners.add(mapListener);
    }

    public void notifyMapListener(EMap EMap){
        Gdx.app.debug(TAG, "Notifying map listeners");
        for (MapListener mapListener : mapListeners){
            mapListener.mapChanged(EMap);
        }
    }

    public interface MapListener {
        void mapChanged(final EMap EMap);
    }

    public void setCurrentMap(EMap map){
        this.currentMap = map;
        notifyMapListener(map);
    }

    public EMap getCurrentMap() {
        return currentMap;
    }

}
