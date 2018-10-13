/*
 * Developed by Benjamin Lefèvre
 * Last modified 12/10/18 18:47
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.components.helpers;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.bendk97.components.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static com.bendk97.helpers.EntityTestHelper.createEntity;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class FamiliesTest {

    private final Engine engine = new Engine();

    @Test
    public void retrieving_all_entities_having_at_least_one_PlayerBulletComponent() {
        Entity entity1 = createEntity(engine, PlayerBulletComponent.class, SpriteComponent.class);
        Entity entity2 = createEntity(engine, SpriteComponent.class);
        Entity entity3 = createEntity(engine,PositionComponent.class, SpriteComponent.class, PlayerBulletComponent.class);

        ImmutableArray<Entity> playerBulletEntities = engine.getEntitiesFor(Families.playerBullet);

        assertThat(playerBulletEntities).hasSize(2);
        assertThat(playerBulletEntities).containsExactlyInAnyOrder(entity1, entity3);
        assertThat(playerBulletEntities).doesNotContain(entity2);
    }


    @Test
    public void retrieving_all_entities_having_at_least_one_ShieldComponent() {
        Entity entity1 = createEntity(engine,ShieldComponent.class, SpriteComponent.class);
        Entity entity2 = createEntity(engine,SpriteComponent.class);
        Entity entity3 = createEntity(engine,PositionComponent.class, SpriteComponent.class, ShieldComponent.class);

        ImmutableArray<Entity> shieldEntities = engine.getEntitiesFor(Families.shield);

        assertThat(shieldEntities).hasSize(2);
        assertThat(shieldEntities).containsExactlyInAnyOrder(entity1, entity3);
        assertThat(shieldEntities).doesNotContain(entity2);
    }

    @Test
    public void retrieving_all_entities_having_at_least_one_EnemyBulletComponent() {
        Entity entity1 = createEntity(engine,EnemyBulletComponent.class, SpriteComponent.class);
        Entity entity2 = createEntity(engine,SpriteComponent.class);
        Entity entity3 = createEntity(engine,PositionComponent.class, SpriteComponent.class, EnemyBulletComponent.class);

        ImmutableArray<Entity> enemyBulletEntitites = engine.getEntitiesFor(Families.enemyBullet);

        assertThat(enemyBulletEntitites).hasSize(2);
        assertThat(enemyBulletEntitites).containsExactlyInAnyOrder(entity1, entity3);
        assertThat(enemyBulletEntitites).doesNotContain(entity2);
    }

    @Test
    public void retrieving_all_entities_having_at_least_one_ShieldUpComponent() {
        Entity entity1 = createEntity(engine,ShieldUpComponent.class, SpriteComponent.class);
        Entity entity2 = createEntity(engine,SpriteComponent.class, ShieldUpComponent.class);
        Entity entity3 = createEntity(engine,PositionComponent.class, SpriteComponent.class, EnemyBulletComponent.class);

        ImmutableArray<Entity> shieldUpEntities = engine.getEntitiesFor(Families.shieldUp);

        assertThat(shieldUpEntities).hasSize(2);
        assertThat(shieldUpEntities).containsExactlyInAnyOrder(entity1, entity2);
        assertThat(shieldUpEntities).doesNotContain(entity3);
    }

    @Test
    public void retrieving_all_entities_having_at_least_one_PowerUpComponent() {
        Entity entity1 = createEntity(engine,PowerUpComponent.class, SpriteComponent.class);
        Entity entity2 = createEntity(engine,SpriteComponent.class, PowerUpComponent.class);
        Entity entity3 = createEntity(engine,PositionComponent.class, SpriteComponent.class, PowerUpComponent.class);

        ImmutableArray<Entity> powerUpEntities = engine.getEntitiesFor(Families.powerUp);

        assertThat(powerUpEntities).hasSize(3);
        assertThat(powerUpEntities).containsExactlyInAnyOrder(entity1, entity2, entity3);
    }

    @Test
    public void retrieving_all_entities_having_at_least_one_BombUpComponent() {
        Entity entity1 = createEntity(engine,BombUpComponent.class, SpriteComponent.class);
        Entity entity2 = createEntity(engine,SpriteComponent.class, BombUpComponent.class);
        Entity entity3 = createEntity(engine,PositionComponent.class, SpriteComponent.class, BombUpComponent.class);
        Entity entity4 = createEntity(engine,PowerUpComponent.class, ShieldUpComponent.class);

        ImmutableArray<Entity> bombUpEntities = engine.getEntitiesFor(Families.bombUp);

        assertThat(bombUpEntities).hasSize(3);
        assertThat(bombUpEntities).containsExactlyInAnyOrder(entity1, entity2, entity3);
        assertThat(bombUpEntities).doesNotContain(entity4);
    }

    @Test
    public void retrieving_all_enemies_entities_excluding_tanks() {
        Entity entity1 = createEntity(engine,EnemyComponent.class, SpriteComponent.class);
        Entity entity2 = createEntity(engine,EnemyComponent.class, GroundEnemyComponent.class);
        Entity entity3 = createEntity(engine,EnemyComponent.class, SpriteComponent.class, BombUpComponent.class);
        Entity entity4 = createEntity(engine,EnemyComponent.class, GroundEnemyComponent.class);

        ImmutableArray<Entity> enemyEntities = engine.getEntitiesFor(Families.enemyBodies);

        assertThat(enemyEntities).hasSize(2);
        assertThat(enemyEntities).containsExactlyInAnyOrder(entity1, entity3);
        assertThat(enemyEntities).doesNotContain(entity4, entity2);
    }

    @Test
    public void retrieving_all_enemies_entities() {
        Entity entity1 = createEntity(engine,EnemyComponent.class, SpriteComponent.class);
        Entity entity2 = createEntity(engine,EnemyComponent.class, GroundEnemyComponent.class);
        Entity entity3 = createEntity(engine,EnemyComponent.class, SpriteComponent.class, BombUpComponent.class);
        Entity entity4 = createEntity(engine,EnemyComponent.class, GroundEnemyComponent.class);

        ImmutableArray<Entity> enemyEntities = engine.getEntitiesFor(Families.enemies);

        assertThat(enemyEntities).hasSize(4);
        assertThat(enemyEntities).containsExactlyInAnyOrder(entity1, entity3, entity2, entity4);
    }

    @Test
    public void retrieving_player_vulnerable() {
        Entity player = createEntity(engine,PlayerComponent.class, SpriteComponent.class);

        assertThat(engine.getEntitiesFor(Families.playerVulnerable))
                .hasSize(1)
                .containsExactly(player);
    }

    @Test
    public void not_retrieving_player_vulnerable_when_gameover() {
        Entity player = createEntity(engine,PlayerComponent.class, GameOverComponent.class);

        assertThat(engine.getEntitiesFor(Families.playerVulnerable))
                .hasSize(0)
                .doesNotContain(player);
    }


    @Test
    public void not_retrieving_player_vulnerable_when_invulnerable() {
        Entity player = createEntity(engine,PlayerComponent.class, InvulnerableComponent.class);

        assertThat(engine.getEntitiesFor(Families.playerVulnerable))
                .hasSize(0)
                .doesNotContain(player);
    }

    @Test
    public void not_retrieving_player_vulnerable_when_invulnerable_gameover() {
        Entity player = createEntity(engine,PlayerComponent.class, GameOverComponent.class, InvulnerableComponent.class);

        assertThat(engine.getEntitiesFor(Families.playerVulnerable))
                .hasSize(0)
                .doesNotContain(player);
    }

    @Test
    public void retrieving_player_when_not_gameover() {
        Entity player = createEntity(engine,PlayerComponent.class, InvulnerableComponent.class);

        assertThat(engine.getEntitiesFor(Families.player))
                .hasSize(1)
                .containsExactly(player);
    }


    @Test
    public void not_retrieving_player_when_gameover() {
        Entity player = createEntity(engine, PlayerComponent.class, GameOverComponent.class);

        assertThat(engine.getEntitiesFor(Families.player))
                .hasSize(0)
                .doesNotContain(player);
    }

}
