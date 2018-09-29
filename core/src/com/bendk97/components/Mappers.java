/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.components;

import com.badlogic.ashley.core.ComponentMapper;

public class Mappers {
    public static ComponentMapper<BackgroundComponent> background = ComponentMapper.getFor(BackgroundComponent.class);
    public static ComponentMapper<AnimationComponent> animation = ComponentMapper.getFor(AnimationComponent.class);
    public static ComponentMapper<PositionComponent> position = ComponentMapper.getFor(PositionComponent.class);
    public static ComponentMapper<com.bendk97.components.RemovableComponent> removable = ComponentMapper.getFor(com.bendk97.components.RemovableComponent.class);
    public static ComponentMapper<com.bendk97.components.SpriteComponent> sprite = ComponentMapper.getFor(com.bendk97.components.SpriteComponent.class);
    public static ComponentMapper<StateComponent> state = ComponentMapper.getFor(StateComponent.class);
    public static ComponentMapper<com.bendk97.components.VelocityComponent> velocity = ComponentMapper.getFor(com.bendk97.components.VelocityComponent.class);
    public static ComponentMapper<EnemyComponent> enemy = ComponentMapper.getFor(EnemyComponent.class);
    public static ComponentMapper<PlayerComponent> player = ComponentMapper.getFor(PlayerComponent.class);
    public static ComponentMapper<com.bendk97.components.SquadronComponent> squadron = ComponentMapper.getFor(com.bendk97.components.SquadronComponent.class);
    public static ComponentMapper<com.bendk97.components.ScoreSquadronComponent> scoreSquadron = ComponentMapper.getFor(com.bendk97.components.ScoreSquadronComponent.class);
    public static ComponentMapper<com.bendk97.components.GameOverComponent> gameover = ComponentMapper.getFor(com.bendk97.components.GameOverComponent.class);
    public static ComponentMapper<BossComponent> boss = ComponentMapper.getFor(BossComponent.class);
    public static ComponentMapper<TankComponent> tank = ComponentMapper.getFor(TankComponent.class);
    public static ComponentMapper<FollowPlayerComponent> follow = ComponentMapper.getFor(FollowPlayerComponent.class);
    public static ComponentMapper<com.bendk97.components.InvulnerableComponent> invulnerable = ComponentMapper.getFor(com.bendk97.components.InvulnerableComponent.class);
    public static ComponentMapper<LeveLFinishedComponent> levelFinished = ComponentMapper.getFor(LeveLFinishedComponent.class);
    public static ComponentMapper<com.bendk97.components.LightComponent> light = ComponentMapper.getFor(com.bendk97.components.LightComponent.class);
}