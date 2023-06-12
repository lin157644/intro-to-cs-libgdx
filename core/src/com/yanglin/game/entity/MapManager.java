package com.yanglin.game.entity;

import com.badlogic.gdx.utils.Array;

public class MapManager {
    private final Array<MapListener> mapListeners;

    public enum EMap {
        SCHOOL_GATE("tilemap/school_gate.tmx"),
        DORM_AREA("tilemap/.tmx"),
        ENGINEERING_BUILDING("tilemap/.tmx"),
        OFFICE_STUDENT("tilemap/.tmx"),
        DORM_INTERNAL("tilemap/dorm.tmx"),
        LIBRARY_INTERNAL("tilemap/.tmx");

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
        for (MapListener mapListener : mapListeners){
            mapListener.mapChanged(EMap);
        }
    }

    public interface MapListener {
        void mapChanged(final EMap EMap);
    }

    public void setCurrentMap(EMap map){
        this.currentMap = map;
    }

    public EMap getCurrentMap() {
        return currentMap;
    }

}
