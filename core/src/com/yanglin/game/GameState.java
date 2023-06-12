package com.yanglin.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.yanglin.game.entity.MapManager;
import com.yanglin.game.entity.component.ItemComponent;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class GameState {
    public MapManager.EMap map = MapManager.EMap.SCHOOL_GATE;
    public Array<ItemComponent.ItemType> items;
    // TODO: Should initialized in GameScreen/Map...
    public float x = 10;
    public float y = 10;
    public boolean hasEaten = false;
    public boolean hasWorshiped = false;
    public boolean hasPassEnglish = false;
    public boolean hasEnoughHours = false;
    public boolean hasFinishedOnlineProcedure = false;
    public int month = 5;
    public int date = 1;

    public static GameState loadState() {
        File file = new File("graduate.save");
        if (file.exists() && !file.isDirectory()) {
            try (Scanner scanner = new Scanner(file)){
                Json json = new Json();
                return json.fromJson(GameState.class, scanner.nextLine());
            } catch (Exception ignored) {
                return new GameState();
            }
        } else {
            return new GameState();
        }

    }

    public static Boolean saveExist() {
        File file = new File("graduate.save");
        return file.exists() && !file.isDirectory();
    }

    public static void saveState(GameState gs) {
        Json json = new Json();
        File file = new File("graduate.save");
        try (FileWriter fw = new FileWriter(file)) {
            fw.write(json.toJson(gs));
        } catch (Exception ignored) {
        }

    }
}
