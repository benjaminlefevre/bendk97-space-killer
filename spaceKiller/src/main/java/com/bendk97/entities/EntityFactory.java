/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 22:06
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.entities;

import aurelienribon.tweenengine.TweenManager;
import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;
import com.bendk97.SpaceKillerGame;
import com.bendk97.assets.Assets;
import com.bendk97.components.LightComponent;
import com.bendk97.components.PositionComponent;
import com.bendk97.components.SpriteComponent;
import com.bendk97.components.helpers.ComponentMapperHelper;
import com.bendk97.entities.enemies.EnemyActionEntityFactory;
import com.bendk97.entities.enemies.EnemyEntityFactory;
import com.bendk97.entities.player.BonusEntityFactory;
import com.bendk97.entities.player.PlayerActionsEntityFactory;
import com.bendk97.entities.player.PlayerEntityFactory;
import com.bendk97.screens.levels.Level;
import com.bendk97.screens.levels.utils.ScreenShake;

import static com.bendk97.assets.Assets.GFX_LEVEL_ALL_ATLAS_NO_MASK;

public class EntityFactory implements Disposable {

    public final PooledEngine engine;
    public final Assets assets;
    public final TweenManager tweenManager;
    public final ScreenShake screenShake;
    public final TextureAtlas atlasNoMask;
    public final TextureAtlas atlasMask;
    public final RayHandler rayHandler;
    private final Pool<PointLight> lightPool = new Pool<PointLight>() {
        @Override
        protected PointLight newObject() {
            PointLight light = new PointLight(rayHandler, 5);
            light.setXray(true);
            return light;
        }
    };
    public StageSetEntityFactory stageSetEntityFactory;
    public PlayerEntityFactory playerEntityFactory;
    public PlayerActionsEntityFactory playerActionsEntityFactory;
    public EnemyEntityFactory enemyEntityFactory;
    public EnemyActionEntityFactory enemyActionEntityFactory;
    public BonusEntityFactory bonusEntityFactory;

    public EntityFactory(SpaceKillerGame game, PooledEngine engine, Assets assets, TweenManager tweenManager, RayHandler rayHandler,
                         ScreenShake screenShake, Level level, OrthographicCamera camera) {
        this.engine = engine;
        this.screenShake = screenShake;
        this.rayHandler = rayHandler;
        if (rayHandler != null) {
            Array<PointLight> poolObjects = new Array<>(30);
            for (int i = 0; i < 50; ++i) {
                PointLight light = lightPool.obtain();
                light.setActive(false);
                poolObjects.add(light);
            }
            lightPool.freeAll(poolObjects);
        }
        this.assets = assets;
        this.tweenManager = tweenManager;
        this.atlasNoMask = assets.get(GFX_LEVEL_ALL_ATLAS_NO_MASK);
        this.atlasMask = assets.get(level.sprites);
        initSubFactories(game, level, camera);
    }

    private void initSubFactories(SpaceKillerGame game, Level level, OrthographicCamera camera) {
        this.stageSetEntityFactory = new StageSetEntityFactory(engine);
        this.playerEntityFactory = new PlayerEntityFactory(this, game);
        this.playerActionsEntityFactory = new PlayerActionsEntityFactory(this);
        this.enemyActionEntityFactory = new EnemyActionEntityFactory(this);
        this.bonusEntityFactory = new BonusEntityFactory(this);
        this.enemyEntityFactory = new EnemyEntityFactory(this, level, camera);
    }

    public void createLight(Entity entity) {
        createLight(entity, new Color(0.5f, 0f, 0f, 0.3f),
                ComponentMapperHelper.sprite.get(entity).sprite.getHeight() * 20f);
    }

    public void createLight(Entity entity, Color color, float distance) {
        SpriteComponent sprite = ComponentMapperHelper.sprite.get(entity);
        PositionComponent position = ComponentMapperHelper.position.get(entity);
        LightComponent lightComponent = engine.createComponent(LightComponent.class);
        PointLight light = lightPool.obtain();
        light.setActive(true);
        light.setColor(color);
        light.setDistance(distance);
        light.setPosition(position.x + sprite.sprite.getWidth() / 2f,
                position.y + sprite.sprite.getHeight() / 2f);
        lightComponent.light = light;
        lightComponent.lights = lightPool;
        entity.add(lightComponent);
    }

    @Override
    public void dispose() {
        lightPool.clear();
    }


}
