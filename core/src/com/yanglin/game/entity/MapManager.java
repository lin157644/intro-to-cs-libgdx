package com.yanglin.game.entity;

import com.badlogic.gdx.utils.Array;

public class MapManager {
    private final Array<MapListener> mapListeners;
    public enum Map {
        SCHOOL_GATE,
        DORM_AREA,
        ENGINEERING_BUILDING,
        OFFICE_STUDENT,
        LANGUAGE_CENT,
        DORM_INTERNAL,
        LIBRARY_INTERNAL
    }
    private int currentMap;
    public MapManager() {
        this.mapListeners = new Array<>();
    }
    public void addMapListener(final MapListener mapListener) {
        mapListeners.add(mapListener);
    }

    public interface MapListener {
        void mapChanged(final Map map);
    }

}
