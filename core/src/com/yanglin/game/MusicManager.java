package com.yanglin.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class MusicManager {

    private Music currBGM;
    private Sound currEffect;
    private Sound dialogEffect;

    public enum BGM {
        MENU("audio/music/menu_bgm.ogg"),
        GAME("audio/music/game_bgm.ogg"),
        GAME_JUNE("audio/music/june_bgm.ogg"),
        GOOD_END("audio/music/ge_bgm.ogg"),
        BAD_END("audio/music/be_bgm.ogg");
        private final String path;

        BGM(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }

    public enum Effect {
        MENU_SELECT("audio/effect/menu_select.wav"),
        GET_ITEM("audio/effect/get_item.wav"),
        OPEN_MENU("audio/effect/open_menu.ogg"),
        CLOSE_MENU("audio/effect/close_menu.ogg"),
        CHANGE_MAP("audio/effect/change_map.ogg"),
        DIALOG("audio/effect/dialog.wav");

        private final String path;

        Effect(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }

    private IWantToGraduate game;

    public MusicManager(IWantToGraduate game) {
        this.game = game;
    }

    public void setBGM(BGM bgm) {
        setBGM(bgm, false);
    }

    public void setBGM(BGM bgm, boolean playBGM) {
        if(currBGM!=null) {
            currBGM.stop();
        }
        Music newBGM = game.assetManager.get(bgm.getPath());
        newBGM.setLooping(true);
        currBGM = newBGM;
        if (playBGM)
            playBGM();
    }

    public void stopBGM() {
        if (currBGM != null)
            currBGM.stop();
    }

    public void pauseBGM() {
        if (currBGM != null)
            currBGM.pause();
    }

    public void playBGM() {
        if (currBGM != null)
            currBGM.play();
    }

    public void playDialog() {
        if (dialogEffect == null) {
            dialogEffect = game.assetManager.get(Effect.DIALOG.getPath());
        }
        dialogEffect.loop();
    }

    public void stopDialog() {
        if(dialogEffect != null)
            dialogEffect.stop();
    }

    public void playEffect(Effect effect) {
        currEffect = game.assetManager.get(effect.getPath());
        currEffect.play();
    }
}
