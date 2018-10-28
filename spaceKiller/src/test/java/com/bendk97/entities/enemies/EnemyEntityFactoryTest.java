/*
 * Developed by Benjamin Lefèvre
 * Last modified 19/10/18 20:18
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.entities.enemies;

import aurelienribon.tweenengine.TweenManager;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.bendk97.SpaceKillerGame;
import com.bendk97.assets.Assets;
import com.bendk97.components.*;
import com.bendk97.components.helpers.ComponentMapperHelper;
import com.bendk97.entities.EntityFactory;
import com.bendk97.runner.GdxTestRunner;
import com.bendk97.screens.levels.Level;
import com.bendk97.screens.levels.utils.ScreenShake;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static com.bendk97.SpaceKillerGameConstants.STANDARD_RATE_SHOOT;
import static com.bendk97.entities.EntityFactoryIds.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(GdxTestRunner.class)
public class EnemyEntityFactoryTest {

    private EntityFactory entityFactory;
    private EnemyEntityFactory enemyEntityFactory;

    @Mock
    private Assets assets;

    @Mock
    private TextureAtlas sprites;

    @Mock
    private TextureAtlas noMaskSprites;

    @Mock
    private Texture texture;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        when(assets.get(Assets.GFX_LEVEL_COMMON)).thenReturn(noMaskSprites);
        when(assets.get(Level.Level2.sprites)).thenReturn(sprites);
        TextureAtlas.AtlasRegion atlasRegion = new TextureAtlas.AtlasRegion(texture, 0, 0, 100, 200);
        atlasRegion.setTexture(texture);
        Array<TextureAtlas.AtlasRegion> regionArray = new Array<>();
        regionArray.add(atlasRegion);
        when(sprites.findRegions(anyString())).thenReturn(regionArray);
        when(sprites.findRegion(anyString())).thenReturn(atlasRegion);
        PooledEngine engine = new PooledEngine();
        entityFactory = new EntityFactory(mock(SpaceKillerGame.class),
                engine, assets, mock(TweenManager.class), null, mock(ScreenShake.class),
                Level.Level2, mock(OrthographicCamera.class));
        enemyEntityFactory = new EnemyEntityFactory(entityFactory, Level.Level2, mock(OrthographicCamera.class));
    }

    @Test
    public void a_laser_ship_enemy_1_is_created() {
        Entity entity = enemyEntityFactory.createLaserShip(SHIP_LV2_LASER_SHIP1, 10f, 10f, 1,
                100, 1, true);
        assertThatEntityIsLaserShip(entity);
        verify(sprites).findRegions("staticEnemy1");
    }

    @Test
    public void a_laser_ship_enemy_2_is_created() {
        Entity entity = enemyEntityFactory.createLaserShip(SHIP_LV2_LASER_SHIP2, 10f, 10f, 1,
                100, 1, true);
        assertThatEntityIsLaserShip(entity);
        verify(sprites).findRegions("staticEnemy2");
    }

    @Test
    public void a_laser_ship_enemy_3_is_created() {
        Entity entity = enemyEntityFactory.createLaserShip(SHIP_LV2_LASER_SHIP3, 10f, 10f, 1,
                100, 1, true);
        assertThatEntityIsLaserShip(entity);
        verify(sprites).findRegions("staticEnemy3");
    }

    @Test
    public void a_laser_ship_enemy_4_is_created() {
        Entity entity = enemyEntityFactory.createLaserShip(SHIP_LV2_LASER_SHIP4, 10f, 10f, 1,
                100, 1, true);
        assertThatEntityIsLaserShip(entity);
        verify(sprites).findRegions("staticEnemy4");
    }

    @Test
    public void a_laser_ship_enemy_lark_is_created() {
        Entity entity = enemyEntityFactory.createLaserShip(SHIP_LV3_1, 10f, 10f, 1,
                100, 1, true);
        assertThatEntityIsLaserShip(entity);
        verify(sprites).findRegions("lark");
    }

    @Test
    public void a_laser_ship_enemy_stab_is_created() {
        Entity entity = enemyEntityFactory.createLaserShip(SHIP_LV3_2, 10f, 10f, 1,
                100, 1, true);
        assertThatEntityIsLaserShip(entity);
        verify(sprites).findRegions("stab");
    }

    @Test
    public void a_laser_ship_enemy_squid_is_created() {
        Entity entity = enemyEntityFactory.createLaserShip(SHIP_LV3_3, 10f, 10f, 1,
                100, 1, true);
        assertThatEntityIsLaserShip(entity);
        verify(sprites).findRegions("squid");
    }

    @Test
    public void a_laser_ship_enemy_bug_is_created() {
        Entity entity = enemyEntityFactory.createLaserShip(SHIP_LV3_4, 10f, 10f, 1,
                100, 1, true);
        assertThatEntityIsLaserShip(entity);
        verify(sprites).findRegions("bug");
    }

    @Test
    public void a_laser_ship_enemy_swarmer_is_created() {
        Entity entity = enemyEntityFactory.createLaserShip(SHIP_LV3_5, 10f, 10f, 1,
                100, 1, true);
        assertThatEntityIsLaserShip(entity);
        verify(sprites).findRegions("swarmer");
    }

    @Test
    public void a_laser_ship_enemy_stingray_is_created() {
        Entity entity = enemyEntityFactory.createLaserShip(SHIP_LV3_6, 10f, 10f, 1,
                100, 1, true);
        assertThatEntityIsLaserShip(entity);
        verify(sprites).findRegions("stingray");
    }

    @Test
    public void a_laser_ship_enemy_fish_is_created() {
        Entity entity = enemyEntityFactory.createLaserShip(SHIP_LV3_7, 10f, 10f, 1,
                100, 1, true);
        assertThatEntityIsLaserShip(entity);
        verify(sprites).findRegions("fish");
    }

    @Test
    public void a_laser_ship_enemy_podfish_is_created() {
        Entity entity = enemyEntityFactory.createLaserShip(SHIP_LV3_8, 10f, 10f, 1,
                100, 1, true);
        assertThatEntityIsLaserShip(entity);
        verify(sprites).findRegions("podfish");
    }

    @Test
    public void a_laser_ship_enemy_unknown_is_created() {
        Entity entity = enemyEntityFactory.createLaserShip(666, 10f, 10f, 1,
                100, 1, true);
        assertThatEntityIsLaserShip(entity);
        verify(sprites).findRegions("podfish");
    }

    @Test
    public void a_tank_is_created() {
        List<Entity> tank = enemyEntityFactory.createTank(TankComponent.TankLevel.MEDIUM, 10, 100);
        assertThat(tank).hasSize(2);
        assertThat(tank.get(0).getComponents()).hasOnlyElementsOfTypes(
                PositionComponent.class,
                SpriteComponent.class,
                GroundEnemyComponent.class);
        assertThat(tank.get(1).getComponents()).hasOnlyElementsOfTypes(
                PositionComponent.class,
                GroundEnemyComponent.class,
                TankComponent.class,
                FollowPlayerComponent.class,
                SpriteComponent.class,
                PositionComponent.class,
                EnemyComponent.class,
                StateComponent.class);
        verify(sprites).findRegion("tankCannon");
    }

    @Test
    public void a_soucoupe_is_created() {
        Entity soucoupe = enemyEntityFactory.createEnemySoucoupe(entityFactory.engine.createEntity(),
                true, 10f);
        assertThat(soucoupe).isNotNull();
        verify(sprites).findRegions("soucoupe");
        assertThat(soucoupe.getComponents()).hasOnlyElementsOfTypes(
                EnemyComponent.class,
                PositionComponent.class,
                VelocityComponent.class,
                SpriteComponent.class,
                AnimationComponent.class,
                StateComponent.class
        );
        EnemyComponent enemyComponent = ComponentMapperHelper.enemy.get(soucoupe);
        assertThat(enemyComponent.probabilityAttack).isEqualTo(STANDARD_RATE_SHOOT);
        assertThat(enemyComponent.points).isEqualTo(100);
        assertThat(enemyComponent.getLifeGauge()).isEqualTo(1);
    }


    @Test
    public void a_enemy_ship_1_is_created() {
        Entity entity = enemyEntityFactory.createEnemyShip(entityFactory.engine.createEntity(), true, 10f, 1,
                SHIP_1);
        assertThat(entity).isNotNull();
        assertThatEntityIsEnemyShip(entity);
        verify(sprites).findRegions("enemy");
        EnemyComponent enemyComponent = ComponentMapperHelper.enemy.get(entity);
        assertThat(enemyComponent.points).isEqualTo(200);
        assertThat(enemyComponent.getLifeGauge()).isEqualTo(1);
        assertThat(enemyComponent.attackCapacity).isEqualTo(1);
    }

    @Test
    public void a_enemy_ship_2_is_created() {
        Entity entity = enemyEntityFactory.createEnemyShip(entityFactory.engine.createEntity(), true, 10f, 1,
                SHIP_2);
        assertThat(entity).isNotNull();
        assertThatEntityIsEnemyShip(entity);
        verify(sprites).findRegions("enemy2");
        EnemyComponent enemyComponent = ComponentMapperHelper.enemy.get(entity);
        assertThat(enemyComponent.points).isEqualTo(200);
        assertThat(enemyComponent.getLifeGauge()).isEqualTo(1);
        assertThat(enemyComponent.attackCapacity).isEqualTo(1);
    }

    @Test
    public void a_enemy_ship_3_is_created() {
        Entity entity = enemyEntityFactory.createEnemyShip(entityFactory.engine.createEntity(), true, 10f, 1,
                SHIP_3);
        assertThat(entity).isNotNull();
        assertThatEntityIsEnemyShip(entity);
        verify(sprites).findRegions("enemy3");
        EnemyComponent enemyComponent = ComponentMapperHelper.enemy.get(entity);
        assertThat(enemyComponent.points).isEqualTo(200);
        assertThat(enemyComponent.getLifeGauge()).isEqualTo(1);
        assertThat(enemyComponent.attackCapacity).isEqualTo(1);
    }

    @Test
    public void a_enemy_ship_4_is_created() {
        Entity entity = enemyEntityFactory.createEnemyShip(entityFactory.engine.createEntity(), true, 10f, 1,
                SHIP_4);
        assertThat(entity).isNotNull();
        assertThatEntityIsEnemyShip(entity);
        verify(sprites).findRegions("enemy4");
        EnemyComponent enemyComponent = ComponentMapperHelper.enemy.get(entity);
        assertThat(enemyComponent.points).isEqualTo(200);
        assertThat(enemyComponent.getLifeGauge()).isEqualTo(1);
        assertThat(enemyComponent.attackCapacity).isEqualTo(1);
    }

    @Test
    public void a_enemy_ship_5_is_created() {
        Entity entity = enemyEntityFactory.createEnemyShip(entityFactory.engine.createEntity(), true, 10f, 1,
                SHIP_5);
        assertThat(entity).isNotNull();
        assertThatEntityIsEnemyShip(entity);
        verify(sprites).findRegions("enemy5");
        EnemyComponent enemyComponent = ComponentMapperHelper.enemy.get(entity);
        assertThat(enemyComponent.points).isEqualTo(200);
        assertThat(enemyComponent.getLifeGauge()).isEqualTo(1);
        assertThat(enemyComponent.attackCapacity).isEqualTo(1);
    }

    @Test
    public void a_enemy_ship_lark_is_created() {
        Entity entity = enemyEntityFactory.createEnemyShip(entityFactory.engine.createEntity(), true, 10f, 1,
                SHIP_LV3_1);
        assertThat(entity).isNotNull();
        assertThatEntityIsEnemyShip(entity);
        verify(sprites).findRegions("lark");
        EnemyComponent enemyComponent = ComponentMapperHelper.enemy.get(entity);
        assertThat(enemyComponent.points).isEqualTo(250);
        assertThat(enemyComponent.getLifeGauge()).isEqualTo(2);
        assertThat(enemyComponent.attackCapacity).isEqualTo(3);
    }

    @Test
    public void a_enemy_ship_stab_is_created() {
        Entity entity = enemyEntityFactory.createEnemyShip(entityFactory.engine.createEntity(), true, 10f, 1,
                SHIP_LV3_2);
        assertThat(entity).isNotNull();
        assertThatEntityIsEnemyShip(entity);
        verify(sprites).findRegions("stab");
        EnemyComponent enemyComponent = ComponentMapperHelper.enemy.get(entity);
        assertThat(enemyComponent.points).isEqualTo(200);
        assertThat(enemyComponent.getLifeGauge()).isEqualTo(1);
        assertThat(enemyComponent.attackCapacity).isEqualTo(2);
    }

    @Test
    public void a_enemy_ship_squid_is_created() {
        Entity entity = enemyEntityFactory.createEnemyShip(entityFactory.engine.createEntity(), true, 10f, 1,
                SHIP_LV3_3);
        assertThat(entity).isNotNull();
        assertThatEntityIsEnemyShip(entity);
        verify(sprites).findRegions("squid");
        EnemyComponent enemyComponent = ComponentMapperHelper.enemy.get(entity);
        assertThat(enemyComponent.points).isEqualTo(250);
        assertThat(enemyComponent.getLifeGauge()).isEqualTo(2);
        assertThat(enemyComponent.attackCapacity).isEqualTo(3);
    }

    @Test
    public void a_enemy_ship_bug_is_created() {
        Entity entity = enemyEntityFactory.createEnemyShip(entityFactory.engine.createEntity(), true, 10f, 1,
                SHIP_LV3_4);
        assertThat(entity).isNotNull();
        assertThatEntityIsEnemyShip(entity);
        verify(sprites).findRegions("bug");
        EnemyComponent enemyComponent = ComponentMapperHelper.enemy.get(entity);
        assertThat(enemyComponent.points).isEqualTo(200);
        assertThat(enemyComponent.getLifeGauge()).isEqualTo(1);
        assertThat(enemyComponent.attackCapacity).isEqualTo(2);
    }

    @Test
    public void a_enemy_ship_swarmer_is_created() {
        Entity entity = enemyEntityFactory.createEnemyShip(entityFactory.engine.createEntity(), true, 10f, 1,
                SHIP_LV3_5);
        assertThat(entity).isNotNull();
        assertThatEntityIsEnemyShip(entity);
        verify(sprites).findRegions("swarmer");
        EnemyComponent enemyComponent = ComponentMapperHelper.enemy.get(entity);
        assertThat(enemyComponent.points).isEqualTo(250);
        assertThat(enemyComponent.getLifeGauge()).isEqualTo(1);
        assertThat(enemyComponent.attackCapacity).isEqualTo(2);
    }

    @Test
    public void a_enemy_ship_stingray_is_created() {
        Entity entity = enemyEntityFactory.createEnemyShip(entityFactory.engine.createEntity(), true, 10f, 1,
                SHIP_LV3_6);
        assertThat(entity).isNotNull();
        assertThatEntityIsEnemyShip(entity);
        verify(sprites).findRegions("stingray");
        EnemyComponent enemyComponent = ComponentMapperHelper.enemy.get(entity);
        assertThat(enemyComponent.points).isEqualTo(200);
        assertThat(enemyComponent.getLifeGauge()).isEqualTo(1);
        assertThat(enemyComponent.attackCapacity).isEqualTo(2);
    }

    @Test
    public void a_enemy_ship_fish_is_created() {
        Entity entity = enemyEntityFactory.createEnemyShip(entityFactory.engine.createEntity(), true, 10f, 1,
                SHIP_LV3_7);
        assertThat(entity).isNotNull();
        assertThatEntityIsEnemyShip(entity);
        verify(sprites).findRegions("fish");
        EnemyComponent enemyComponent = ComponentMapperHelper.enemy.get(entity);
        assertThat(enemyComponent.points).isEqualTo(250);
        assertThat(enemyComponent.getLifeGauge()).isEqualTo(2);
        assertThat(enemyComponent.attackCapacity).isEqualTo(2);
    }

    @Test
    public void a_enemy_ship_podfish_is_created() {
        Entity entity = enemyEntityFactory.createEnemyShip(entityFactory.engine.createEntity(), true, 10f, 1,
                SHIP_LV3_8);
        assertThat(entity).isNotNull();
        assertThatEntityIsEnemyShip(entity);
        verify(sprites).findRegions("podfish");
        EnemyComponent enemyComponent = ComponentMapperHelper.enemy.get(entity);
        assertThat(enemyComponent.points).isEqualTo(200);
        assertThat(enemyComponent.getLifeGauge()).isEqualTo(1);
        assertThat(enemyComponent.attackCapacity).isEqualTo(2);
    }


    @Test
    public void a_enemy_ship_unknown_is_created() {
        Entity entity = enemyEntityFactory.createEnemyShip(entityFactory.engine.createEntity(), true, 10f, 1,
                666);
        assertThat(entity).isNotNull();
        assertThatEntityIsEnemyShip(entity);
        verify(sprites).findRegions("podfish");
        EnemyComponent enemyComponent = ComponentMapperHelper.enemy.get(entity);
        assertThat(enemyComponent.points).isEqualTo(200);
        assertThat(enemyComponent.getLifeGauge()).isEqualTo(1);
        assertThat(enemyComponent.attackCapacity).isEqualTo(2);
    }

    @Test
    public void boss_1_is_created() {
        Entity boss = enemyEntityFactory.createBoss(entityFactory.engine.createEntity(), 1f, 1f);
        assertThat(boss).isNotNull();
        assertThat(boss.getComponents()).hasOnlyElementsOfTypes(
                BossComponent.class,
                EnemyComponent.class,
                PositionComponent.class,
                SpriteComponent.class,
                StatusHealthComponent.class
        );
        verify(sprites).findRegion("boss-level1");
        EnemyComponent enemyComponent = ComponentMapperHelper.enemy.get(boss);
        assertThat(enemyComponent.getLifeGauge()).isEqualTo(300);
    }

    @Test
    public void boss_2_is_created() {
        Entity boss = enemyEntityFactory.createBoss2(entityFactory.engine.createEntity(), 1f, 1f, 1f);
        assertThat(boss).isNotNull();
        assertThat(boss.getComponents()).hasOnlyElementsOfTypes(
                BossComponent.class,
                EnemyComponent.class,
                PositionComponent.class,
                SpriteComponent.class,
                StatusHealthComponent.class
        );
        verify(sprites).findRegion("boss");
        EnemyComponent enemyComponent = ComponentMapperHelper.enemy.get(boss);
        assertThat(enemyComponent.getLifeGauge()).isEqualTo(400);
    }

    @Test
    public void boss_3_is_created() {
        Entity boss = enemyEntityFactory.createBoss3(entityFactory.engine.createEntity(), 1f, 1f, 1f);
        assertThat(boss).isNotNull();
        assertThat(boss.getComponents()).hasOnlyElementsOfTypes(
                BossComponent.class,
                EnemyComponent.class,
                PositionComponent.class,
                AnimationComponent.class,
                SpriteComponent.class,
                StateComponent.class,
                StatusHealthComponent.class
        );
        verify(sprites).findRegions("boss3");
        EnemyComponent enemyComponent = ComponentMapperHelper.enemy.get(boss);
        assertThat(enemyComponent.getLifeGauge()).isEqualTo(400);
    }

    @Test
    public void asteroid_is_created() {
        Entity asteroid = enemyEntityFactory.createAsteroid(entityFactory.engine.createEntity(), ASTEROID_1);
        assertThat(asteroid).isNotNull();
        assertThat(asteroid.getComponents()).hasOnlyElementsOfTypes(
                EnemyComponent.class,
                PositionComponent.class,
                AnimationComponent.class,
                SpriteComponent.class,
                StateComponent.class
        );
        verify(sprites).findRegions("asteroid");
    }

    @Test
    public void house_is_created() {
        Array<Entity> house = enemyEntityFactory.createHouse(entityFactory.engine.createEntity(), HOUSE_1);
        assertThat(house).hasSize(2);
        assertThat(house.get(0).getComponents()).hasOnlyElementsOfTypes(
                EnemyComponent.class,
                SpriteComponent.class,
                PositionComponent.class,
                GroundEnemyComponent.class
        );
        assertThat(house.get(1).getComponents()).hasOnlyElementsOfTypes(
                EnemyComponent.class,
                SpriteComponent.class,
                PositionComponent.class,
                GroundEnemyComponent.class
        );
        verify(sprites).findRegion("house-1");
        verify(sprites).findRegion("house-1_destroyed");
    }

    @Test
    public void explosion_is_created() {
        Array<TextureAtlas.AtlasRegion> regionArray = new Array<>();
        regionArray.add(new TextureAtlas.AtlasRegion(texture, 0, 0, 50, 50));
        when(noMaskSprites.findRegions(anyString())).thenReturn(regionArray);
        Entity explosion = enemyEntityFactory.createEntityExploding(10f, 10f);
        assertThat(explosion).isNotNull();
        assertThat(explosion.getComponents()).hasOnlyElementsOfTypes(
                PositionComponent.class,
                AnimationComponent.class,
                SpriteComponent.class,
                StateComponent.class
        );
    }

    @Test
    public void squadron_is_created() {
        Entity squadron = enemyEntityFactory.createSquadron(false, true, 100);
        assertThat(squadron).isNotNull();

        SquadronComponent squadronComponent = ComponentMapperHelper.squadron.get(squadron);
        assertThat(squadronComponent.powerUpAfterDestruction).isFalse();
        assertThat(squadronComponent.displayBonusSquadron).isTrue();
        assertThat(squadronComponent.scoreBonus).isEqualTo(100);
    }

    @Test
    public void score_squadron_is_created() {
        Entity squadron = entityFactory.engine.createEntity();
        SquadronComponent squadronComponent = entityFactory.engine.createComponent(SquadronComponent.class);
        squadron.add(squadronComponent);
        Entity entity = enemyEntityFactory.createScoreSquadron(squadron);
        assertThat(entity).isNotNull();
        assertThat(entity.getComponents()).hasOnlyElementsOfTypes(
                ScoreSquadronComponent.class,
                PositionComponent.class
        );
    }

    private void assertThatEntityIsLaserShip(Entity entity) {
        assertThat(entity).isNotNull();
        assertThat(entity.getComponents()).hasOnlyElementsOfTypes(
                PositionComponent.class,
                VelocityComponent.class,
                FollowPlayerComponent.class,
                AnimationComponent.class,
                SpriteComponent.class,
                PositionComponent.class,
                EnemyComponent.class,
                StateComponent.class);
    }

    private void assertThatEntityIsEnemyShip(Entity entity) {
        assertThat(entity).isNotNull();
        assertThat(entity.getComponents()).hasOnlyElementsOfTypes(
                PositionComponent.class,
                VelocityComponent.class,
                AnimationComponent.class,
                SpriteComponent.class,
                PositionComponent.class,
                EnemyComponent.class,
                StateComponent.class);
    }
}
