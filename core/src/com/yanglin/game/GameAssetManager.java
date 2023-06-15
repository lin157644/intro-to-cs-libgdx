package com.yanglin.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ObjectMap;
import com.ray3k.stripe.FreeTypeSkinLoader;
import com.yanglin.game.entity.MapManager;

import java.util.EnumMap;

public class GameAssetManager extends AssetManager {
    public enum PlayerAnimation {
        IDLE_FRONT("IDLE", "FRONT"),
        IDLE_BACK("IDLE", "BACK"),
        IDLE_LEFT("IDLE", "LEFT"),
        IDLE_RIGHT("IDLE", "RIGHT"),
        WALK_FRONT("WALK", "FRONT"),
        WALK_BACK("WALK", "BACK"),
        WALK_LEFT("WALK", "LEFT"),
        WALK_RIGHT("WALK", "RIGHT"),
        ;

        private final String type;
        private final String facing;

        PlayerAnimation(String type, String facing) {
            this.type = type;
            this.facing = facing;
        }

        public String getType() {
            return type + "_" + facing;
        }
    }

    public final EnumMap<PlayerAnimation, TextureRegion[]> playerAnimationFrames = new EnumMap<>(PlayerAnimation.class);

    public GameAssetManager() {
        super();
        FileHandleResolver resolver = new InternalFileHandleResolver();
        setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));

        setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
        setLoader(BitmapFont.class, ".otf", new FreetypeFontLoader(resolver));

        setLoader(Skin.class, new FreeTypeSkinLoader(getFileHandleResolver()));


    }

    public void loadTiledMap() {
        load("tilemaps/school_gate.tmx", TiledMap.class);
        load("tilemaps/dorm.tmx", TiledMap.class);
        for (MapManager.EMap map : MapManager.EMap.values()) {
            load(map.getFileName(), TiledMap.class);
        }
    }

    public void loadImages() {
        load("sprites/player.png", Texture.class);
        load("badlogic.jpg", Texture.class);
        load("tilesets/Modern_UI_Style_2_48x48.png", Texture.class);
        load("ui/ui_pause_bg.png", Texture.class);
        load("ui/time_display.png", Texture.class);
        load("ui/apple_basket.png", Texture.class);
        load("ui/book.png", Texture.class);
        load("ui/wallet.png", Texture.class);
        load("ui/english.png", Texture.class);
        load("ui/dialog_box.png", Texture.class);

        load("img/happy_man.png", Texture.class);
        load("img/cert.png", Texture.class);
        load("img/the_wall.jpg", Texture.class);
    }

    public void loadFonts(){
        load("fonts/title_font.otf", FreeTypeFontGenerator.class);
    }
    public void loadSkins() {
        load("ui/skins/title_skin.json", Skin.class);
    }

    public void loadAudios() {
        load("audio/music/menu_bgm.ogg", Music.class);
        load("audio/music/game_bgm.ogg", Music.class);
        // load("audio/music/ge_bgm.ogg", Music.class);
        load("audio/music/be_bgm.ogg", Music.class);
        load("audio/music/june_bgm.ogg", Music.class);

        // load("audio/music/dorm_bgm.ogg, Music.class);
        // load("audio/music/library_bgm.ogg, Music.class);

        load("audio/effect/menu_select.wav", Sound.class);

        load("audio/effect/get_item.wav", Sound.class);

        load("audio/effect/dialog.wav", Sound.class);

        // load("audio/effect/open_menu.ogg", Music.class);
        // load("audio/effect/close_menu.ogg", Music.class);
        // load("audio/effect/change_map.ogg", Music.class);

    }

    public void buildPlayerAnimationFrames() {
        TextureRegion[][] tmp = TextureRegion.split(get("sprites/player.png"), 16, 32);
        // 18-1 ~ 23-1
        TextureRegion[] idleFrontFrames = new TextureRegion[6];
        TextureRegion[] idleBackFrames = new TextureRegion[6];
        TextureRegion[] idleLeftFrames = new TextureRegion[6];
        TextureRegion[] idleRightFrames = new TextureRegion[6];
        TextureRegion[] walkFrontFrames = new TextureRegion[6];
        TextureRegion[] walkBackFrames = new TextureRegion[6];
        TextureRegion[] walkLeftFrames = new TextureRegion[6];
        TextureRegion[] walkRightFrames = new TextureRegion[6];
        int i = 1;
        int index = 0;
        for (int j = 0; j < 6; j++) {
            idleRightFrames[index++] = tmp[i][j];
        }
        index = 0;
        for (int j = 6; j < 12; j++) {
            idleBackFrames[index++] = tmp[i][j];
        }
        index = 0;
        for (int j = 12; j < 18; j++) {
            idleLeftFrames[index++] = tmp[i][j];
        }
        index = 0;
        for (int j = 18; j < 24; j++) {
            idleFrontFrames[index++] = tmp[i][j];
        }
        i = 2;
        index = 0;
        for (int j = 0; j < 6; j++) {
            walkRightFrames[index++] = tmp[i][j];
        }
        index = 0;
        for (int j = 6; j < 12; j++) {
            walkBackFrames[index++] = tmp[i][j];
        }
        index = 0;
        for (int j = 12; j < 18; j++) {
            walkLeftFrames[index++] = tmp[i][j];
        }
        index = 0;
        for (int j = 18; j < 24; j++) {
            walkFrontFrames[index++] = tmp[i][j];
        }
        playerAnimationFrames.put(PlayerAnimation.IDLE_FRONT, idleFrontFrames);
        playerAnimationFrames.put(PlayerAnimation.IDLE_BACK, idleBackFrames);
        playerAnimationFrames.put(PlayerAnimation.IDLE_LEFT, idleLeftFrames);
        playerAnimationFrames.put(PlayerAnimation.IDLE_RIGHT, idleRightFrames);
        playerAnimationFrames.put(PlayerAnimation.WALK_FRONT, walkFrontFrames);
        playerAnimationFrames.put(PlayerAnimation.WALK_BACK, walkBackFrames);
        playerAnimationFrames.put(PlayerAnimation.WALK_LEFT, walkLeftFrames);
        playerAnimationFrames.put(PlayerAnimation.WALK_RIGHT, walkRightFrames);
    }

    public void loadAudio() {
        // libGDX support mp3, ogg and wav.
        // ogg is preferred in this project, since we are not targeting iOS platform.
    }

    public void dispose(){
        // Should dispose font generator
    }


}
