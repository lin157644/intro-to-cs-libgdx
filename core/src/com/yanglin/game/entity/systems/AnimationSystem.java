package com.yanglin.game.entity.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.yanglin.game.GameAssetManager;
import com.yanglin.game.entity.EntityEngine;
import com.yanglin.game.entity.component.AnimationComponent;
import com.yanglin.game.entity.component.StateComponent;
import com.yanglin.game.entity.component.TextureComponent;

public class AnimationSystem extends IteratingSystem  {

    private ComponentMapper<TextureComponent> tm;
    private ComponentMapper<AnimationComponent> am;
    private ComponentMapper<StateComponent> sm;

    public AnimationSystem(GameAssetManager assetManager) {
        super(Family.all(TextureComponent.class, AnimationComponent.class, StateComponent.class).get());

        tm = EntityEngine.textureComponentMapper;
        am = EntityEngine.animationComponentMapper;
        sm = EntityEngine.stateComponentMapper;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        AnimationComponent ani = am.get(entity);
        StateComponent state = sm.get(entity);

        if (ani.animations.containsKey(state.get())) {
            TextureComponent tex = tm.get(entity);
            tex.region = (TextureRegion) ani.animations.get(state.get()).getKeyFrame(state.time, state.isLooping);
        } else if (ani.shouldClearOnBlankState) {
            TextureComponent tex = tm.get(entity);
            tex.setRegion(null);
        }

        if (!ani.isPaused) {
            state.time += deltaTime;
        }
    }
}
