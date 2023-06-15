package com.yanglin.game.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.ScreenUtils;
import com.rafaskoberg.gdx.typinglabel.TypingLabel;
import com.yanglin.game.IWantToGraduate;

public class BadEndScreen implements Screen {
    private static String TAG = BadEndScreen.class.getSimpleName();
    private IWantToGraduate game;
    private Stage stage = new Stage();
    private TypingLabel reasonLabel;
    public BadEndScreen(IWantToGraduate game) {
        this.game = game;

        Skin skin = game.assetManager.get("ui/skins/title_skin.json");

        // TODO: Positioning, typing effect, back to menu listener, music

        TypingLabel titleLabel = new TypingLabel("{SHAKE=1;1;1}大延畢{ENDSHAKE}", skin, "badEndTitle");
        reasonLabel = new TypingLabel("", skin, "badEndReason");
        Label label = new Label("大俠請重新來過", skin, "badEndReason");


        VerticalGroup vbox = new VerticalGroup();
        vbox.addActor(titleLabel);
        vbox.addActor(reasonLabel);
        vbox.addActor(label);
        vbox.space(30f);

        vbox.setPosition((float) (Gdx.graphics.getWidth() - vbox.getWidth()) / 2 , (float) Gdx.graphics.getHeight() / 2 + 300);

        stage.addActor(vbox);
    }
    @Override
    public void show() {

        Gdx.input.setInputProcessor(stage);

        String reasonText = "";
        switch (game.ending) {
            case DEFAULT_BAD -> {
                // Exit the school before finish all quest
                reasonText = "你因為沒完成畢業前需要的手續就離開學校，沒能拿到畢業證書，最終流落街頭。";
            }
            case TIME_OUT -> {
                reasonText = "很遺憾的，你來不及在時限前畢業，下一年會更好。";
            }
            case AWKWARD_STORE -> {
                // Did not bring money when go to store
                reasonText = "你因為沒帶錢包結帳時付不出錢，極度的尷尬感使你社會性死亡。\n你的意志消沉，來不急辦完剩下的畢業手續。";

            }
            case AWKWARD_BOOK -> {
                // Did not bring money when returning book
                reasonText = "你因為沒帶錢包繳不出逾期費，極度的尷尬感使你社會性死亡。\n你的意志消沉，來不急辦完剩下的畢業手續。";

            }
            case HUNGER -> {
                // gameState.hunger > THRESHOLD
                reasonText = "你因為極度挨餓倒在路上，醒來時已經來不急辦完剩下的畢業手續。";

            }
            case OVERSLEEP -> {
                reasonText = "睡過頭zzz";

            }
            case CAR_CRUSH -> {
                reasonText = "你在學校遭遇了車禍，雖然在醫院休養了一陣子並沒有大礙，\n但是仍然來不急辦完剩下的畢業手續。";

            }
            case DID_NOT_PASS -> {
                // !gameState.items.contains(Items.APPLE)
                reasonText = "教授覺平時老是翹課，誠摯地告訴你請大五再來。";

            }
            case LOW_ENG_SCORE -> {
                // gameState.hasWorshiped == false
                reasonText = "你與畢業門檻之間差了5分，下次記得請佛祖保佑。";
            }
            default -> Gdx.app.log(TAG, "Unimplemented ending or null");
        }
        reasonLabel.setText(reasonText);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        stage.act();
        stage.draw();
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
        stage.dispose();
    }
}
