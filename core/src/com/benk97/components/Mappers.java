package com.benk97.components;

import com.badlogic.ashley.core.ComponentMapper;

public class Mappers{
    public static ComponentMapper<BackgroundComponent> background = ComponentMapper.getFor(BackgroundComponent.class);
    public static ComponentMapper<AnimationComponent> animation = ComponentMapper.getFor(AnimationComponent.class);
    public static ComponentMapper<PositionComponent> position = ComponentMapper.getFor(PositionComponent.class);
    public static ComponentMapper<RemovableComponent> removable = ComponentMapper.getFor(RemovableComponent.class);
    public static ComponentMapper<SpriteComponent> sprite = ComponentMapper.getFor(SpriteComponent.class);
    public static ComponentMapper<StateComponent> state = ComponentMapper.getFor(StateComponent.class);
    public static ComponentMapper<VelocityComponent> velocity = ComponentMapper.getFor(VelocityComponent.class);
    public static ComponentMapper<EnnemyComponent> ennemy = ComponentMapper.getFor(EnnemyComponent.class);
    public static ComponentMapper<PlayerComponent> player = ComponentMapper.getFor(PlayerComponent.class);
}