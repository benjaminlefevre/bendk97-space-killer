/*
 * Developed by Benjamin Lefèvre
 * Last modified 12/10/18 18:23
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.components.helpers;

import com.badlogic.ashley.core.ComponentMapper;
import com.bendk97.components.*;

public class ComponentMapperHelper {
    public static final ComponentMapper<BackgroundComponent> background = ComponentMapper.getFor(BackgroundComponent.class);
    public static final ComponentMapper<AnimationComponent> animation = ComponentMapper.getFor(AnimationComponent.class);
    public static final ComponentMapper<PositionComponent> position = ComponentMapper.getFor(PositionComponent.class);
    public static final ComponentMapper<RemovableComponent> removable = ComponentMapper.getFor(RemovableComponent.class);
    public static final ComponentMapper<SpriteComponent> sprite = ComponentMapper.getFor(SpriteComponent.class);
    public static final ComponentMapper<StateComponent> state = ComponentMapper.getFor(StateComponent.class);
    public static final ComponentMapper<VelocityComponent> velocity = ComponentMapper.getFor(VelocityComponent.class);
    public static final ComponentMapper<EnemyComponent> enemy = ComponentMapper.getFor(EnemyComponent.class);
    public static final ComponentMapper<PlayerComponent> player = ComponentMapper.getFor(PlayerComponent.class);
    public static final ComponentMapper<SquadronComponent> squadron = ComponentMapper.getFor(SquadronComponent.class);
    public static final ComponentMapper<ScoreSquadronComponent> scoreSquadron = ComponentMapper.getFor(ScoreSquadronComponent.class);
    public static final ComponentMapper<BossComponent> boss = ComponentMapper.getFor(BossComponent.class);
    public static final ComponentMapper<TankComponent> tank = ComponentMapper.getFor(TankComponent.class);
    public static final ComponentMapper<FollowPlayerComponent> follow = ComponentMapper.getFor(FollowPlayerComponent.class);
    public static final ComponentMapper<InvulnerableComponent> invulnerable = ComponentMapper.getFor(InvulnerableComponent.class);
    public static final ComponentMapper<LevelFinishedComponent> levelFinished = ComponentMapper.getFor(LevelFinishedComponent.class);
    public static final ComponentMapper<LightComponent> light = ComponentMapper.getFor(LightComponent.class);
    public static final ComponentMapper<StatusHealthComponent> healthBar = ComponentMapper.getFor(StatusHealthComponent.class);
}