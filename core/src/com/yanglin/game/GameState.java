package com.yanglin.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.yanglin.game.entity.MapManager;
import com.yanglin.game.entity.component.ItemComponent;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class GameState {
    private static final String TAG = GameState.class.getSimpleName();
    public MapManager.EMap map = MapManager.EMap.SCHOOL_GATE;
    private final Array<ItemComponent.ItemType> items = new Array<>();
    // TODO: Should initialized in GameScreen/Map...
    public float x = 24;
    public float y = 20;
    public int appleCount = 0;
    public boolean hasEaten = false;
    public boolean hasWorshiped = false;
    public boolean hasPassEnglish = false;
    public boolean hasEnoughHours = false;
    public boolean hasFinishedOnlineProcedure = false;
    public int month = 5;
    public int date = 1;

    public void addItem(ItemComponent.ItemType itemType){
        if (!items.contains(itemType, true))
            items.add(itemType);
        Gdx.app.log(TAG, "New item added: " + itemType + " Current items: " + items);
    }

    public static GameState loadState() {
        File file = new File("graduate.save");
        if (file.exists() && !file.isDirectory()) {
            try (Scanner scanner = new Scanner(file)){
                Json json = new Json();
                return json.fromJson(GameState.class, scanner.nextLine());
            } catch (Exception ignored) {
                Gdx.app.log(TAG, "Error trying to parse old save, ignore one save.");
                return new GameState();
            }
        } else {
            Gdx.app.debug(TAG, "Old save does not exist, creating a new one.");
            return new GameState();
        }

    }

    public static Boolean saveExist() {
        File file = new File("graduate.save");
        return file.exists() && !file.isDirectory();
    }

    public static Boolean clearSavedState(){
        File file = new File("graduate.save");
        return file.delete();
    }

    public static void saveState(GameState gs) {
        Json json = new Json();

        json.setTypeName(null);
        json.setUsePrototypes(false);
        json.setIgnoreUnknownFields(true);
        json.setOutputType(JsonWriter.OutputType.json);

        File file = new File("graduate.save");
        try (FileWriter fw = new FileWriter(file)) {
            Gdx.app.debug(TAG, json.toJson(gs));
            fw.write(json.toJson(gs, GameState.class));
        } catch (Exception ignored) {
            Gdx.app.log(TAG, "Error when trying to write savefile");
        }
    }
}
