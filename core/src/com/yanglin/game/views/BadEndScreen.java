package com.yanglin.game.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.yanglin.game.IWantToGraduate;

public class BadEndScreen implements Screen {
    private static String TAG = BadEndScreen.class.getSimpleName();
    private IWantToGraduate game;
    public BadEndScreen(IWantToGraduate game) {
        this.game = game;
    }
    @Override
    public void show() {
        switch (game.ending) {
            case DEFAULT_BAD -> {
                // Exit the school before finish all quest
                String text = "你因為沒完成畢業前需要的手續就離開學校，沒能達到畢業證書，最終流落街頭。";
            }
            case AWKWARD_STORE -> {
                // Did not bring money when go to store
                String text = "你因為沒帶錢包結帳時付不出錢，極度的尷尬感使你社會性死亡。{BR}你的意志消沉，來不急辦完剩下的畢業手續。";

            }
            case AWKWARD_BOOK -> {
                // Did not bring money when returning book
                String text = "你因為沒帶錢包繳不出逾期費，極度的尷尬感使你社會性死亡。{BR}你的意志消沉，來不急辦完剩下的畢業手續。";

            }
            case HUNGER -> {
                // gameState.hunger > THRESHOLD
                String text = "你因為極度挨餓倒在路上，醒來時已經來不急辦完剩下的畢業手續。";

            }
            case OVERSLEEP -> {
                String text = "睡過頭zzz";

            }
            case CAR_CRUSH -> {
                String text = "你在學校遭遇了車禍，雖然在醫院休養了一陣子並沒有大礙，但是來不急辦完剩下的畢業手續。";

            }
            case DID_NOT_PASS -> {
                // !gameState.items.contains(Items.APPLE)
                String text = "教授覺平時老是翹課，誠摯地告訴你請大五再來。";

            }
            case LOW_ENG_SCORE -> {
                // gameState.hasWorshiped == false
                String text = "你與畢業門檻之間差了5分，下次記得請佛祖保佑。";

            }
            default -> Gdx.app.log(TAG, "Unimplemented ending or null");
        }

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
