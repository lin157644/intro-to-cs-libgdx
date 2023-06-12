package com.yanglin.game.entity;

import com.badlogic.gdx.utils.Array;

public class MapManager {
    private final Array<MapListener> mapListeners;

    public enum EMap {
        SCHOOL_GATE("tilemaps/school_gate.tmx"),
        DORM_AREA("tilemaps/.tmx"),
        ENGINEERING_BUILDING("tilemaps/.tmx"),
        OFFICE_STUDENT("tilemaps/.tmx"),
        DORM_INTERNAL("tilemaps/dorm.tmx"),
        LIBRARY_INTERNAL("tilemaps/.tmx");

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
        notifyMapListener(map);
    }

    public EMap getCurrentMap() {
        return currentMap;
    }

}
