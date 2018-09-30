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
    public static ComponentMapper<RemovableComponent> removable = ComponentMapper.getFor(RemovableComponent.class);
    public static ComponentMapper<SpriteComponent> sprite = ComponentMapper.getFor(SpriteComponent.class);
    public static ComponentMapper<StateComponent> state = ComponentMapper.getFor(StateComponent.class);
    public static ComponentMapper<VelocityComponent> velocity = ComponentMapper.getFor(VelocityComponent.class);
    public static ComponentMapper<EnemyComponent> enemy = ComponentMapper.getFor(EnemyComponent.class);
    public static ComponentMapper<PlayerComponent> player = ComponentMapper.getFor(PlayerComponent.class);
    public static ComponentMapper<SquadronComponent> squadron = ComponentMapper.getFor(SquadronComponent.class);
    public static ComponentMapper<ScoreSquadronComponent> scoreSquadron = ComponentMapper.getFor(ScoreSquadronComponent.class);
    public static ComponentMapper<GameOverComponent> gameover = ComponentMapper.getFor(GameOverComponent.class);
    public static ComponentMapper<BossComponent> boss = ComponentMapper.getFor(BossComponent.class);
    public static ComponentMapper<TankComponent> tank = ComponentMapper.getFor(TankComponent.class);
    public static ComponentMapper<FollowPlayerComponent> follow = ComponentMapper.getFor(FollowPlayerComponent.class);
    public static ComponentMapper<InvulnerableComponent> invulnerable = ComponentMapper.getFor(InvulnerableComponent.class);
    public static ComponentMapper<LeveLFinishedComponent> levelFinished = ComponentMapper.getFor(LeveLFinishedComponent.class);
    public static ComponentMapper<LightComponent> light = ComponentMapper.getFor(LightComponent.class);
}