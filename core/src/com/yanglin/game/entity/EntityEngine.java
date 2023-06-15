package com.yanglin.game.entity;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.PooledEngine;
import com.yanglin.game.entity.component.*;

public class EntityEngine extends PooledEngine {
    public static final ComponentMapper<PlayerComponent> playerComponentMapper = ComponentMapper.getFor(PlayerComponent.class);
    public static final ComponentMapper<PositionComponent> positionComponentMapper = ComponentMapper.getFor(PositionComponent.class);
    public static final ComponentMapper<AnimationComponent> animationComponentMapper = ComponentMapper.getFor(AnimationComponent.class);
    public static final ComponentMapper<FacingComponent> facingComponentMapper = ComponentMapper.getFor(FacingComponent.class);
    public static final ComponentMapper<StateComponent> stateComponentMapper = ComponentMapper.getFor(StateComponent.class);
    public static final ComponentMapper<TextureComponent> textureComponentMapper = ComponentMapper.getFor(TextureComponent.class);
    public static final ComponentMapper<RenderableComponent> renderableComponentMapper = ComponentMapper.getFor(RenderableComponent.class);
    public static final ComponentMapper<ItemComponent> itemComponentMapper = ComponentMapper.getFor(ItemComponent.class);

}
