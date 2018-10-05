/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.bendk97.components.AnimationComponent;
import com.bendk97.components.Mappers;
import com.bendk97.components.SpriteComponent;
import com.bendk97.components.StateComponent;

public class AnimationSystem extends IteratingSystem {

    public AnimationSystem(int priority) {
        super(Family.all(SpriteComponent.class, AnimationComponent.class, StateComponent.class).get(), priority);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        SpriteComponent sprite = Mappers.sprite.get(entity);
        AnimationComponent anim = Mappers.animation.get(entity);
        StateComponent state = Mappers.state.get(entity);

        Animation<Sprite> animation = anim.animations.get(state.get());

        if (animation != null) {
            if (animation.getPlayMode().equals(Animation.PlayMode.NORMAL) && animation.isAnimationFinished(state.time)) {
                getEngine().removeEntity(entity);
            } else {
                sprite.sprite = animation.getKeyFrame(state.time);
            }
        }
    }
}