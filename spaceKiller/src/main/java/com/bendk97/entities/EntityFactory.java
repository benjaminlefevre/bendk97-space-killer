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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;
import com.bendk97.SpaceKillerGame;
import com.bendk97.assets.GameAssets;
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

import static com.bendk97.assets.GameAssets.GFX_LEVEL_COMMON;

public class EntityFactory implements Disposable {

    private static final Color LIGHT_EXPLOSION_COLOR = new Color(0.5f, 0f, 0f, 0.3f);
    public PooledEngine engine;
    public final GameAssets assets;
    public TweenManager tweenManager;
    public final ScreenShake screenShake;
    public final TextureAtlasCache commonAtlas;
    public final TextureAtlasCache levelAtlas;
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

    public EntityFactory(SpaceKillerGame game, PooledEngine engine, GameAssets assets, TweenManager tweenManager, RayHandler rayHandler,
                         ScreenShake screenShake, Level level) {
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
        this.commonAtlas = new TextureAtlasCache(assets.get(GFX_LEVEL_COMMON));
        this.levelAtlas = new TextureAtlasCache(assets.get(level.sprites));
        initSubFactories(game, level);
    }

    private void initSubFactories(SpaceKillerGame game, Level level) {
        this.stageSetEntityFactory = new StageSetEntityFactory(engine);
        this.playerEntityFactory = new PlayerEntityFactory(this, game);
        this.playerActionsEntityFactory = new PlayerActionsEntityFactory(this);
        this.enemyActionEntityFactory = new EnemyActionEntityFactory(this, tweenManager);
        this.bonusEntityFactory = new BonusEntityFactory(this);
        this.enemyEntityFactory = new EnemyEntityFactory(this, level);
    }

    public void createLight(Entity entity) {
        createLight(entity, LIGHT_EXPLOSION_COLOR,
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
        light.setPosition(position.x() + sprite.sprite.getWidth() / 2f,
                position.y() + sprite.sprite.getHeight() / 2f);
        lightComponent.light = light;
        lightComponent.lights = lightPool;
        entity.add(lightComponent);
    }

    @Override
    public void dispose() {
        lightPool.clear();
        commonAtlas.dispose();
        levelAtlas.dispose();
    }


}
