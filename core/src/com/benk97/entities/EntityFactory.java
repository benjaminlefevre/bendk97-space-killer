package com.benk97.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.benk97.Settings;
import com.benk97.assets.Assets;
import com.benk97.components.*;

import static com.benk97.SpaceKillerGameConstants.*;
import static com.benk97.assets.Assets.*;
import static com.benk97.components.Mappers.position;
import static com.benk97.components.Mappers.sprite;

public class EntityFactory {

    private PooledEngine engine;
    private Assets assets;

    public EntityFactory(PooledEngine engine, Assets assets) {
        this.engine = engine;
        this.assets = assets;
    }


    public Entity createBackground(Texture texture, float velocity) {
        Entity background = engine.createEntity();
        BackgroundComponent component = engine.createComponent(BackgroundComponent.class);
        component.setTexture(texture);
        background.add(component);
        background.add(engine.createComponent(PositionComponent.class));
        background.add(engine.createComponent(VelocityComponent.class));
        background.getComponent(VelocityComponent.class).y = velocity;
        engine.addEntity(background);
        return background;
    }

    public Entity createPlayerFire(Entity player) {
        Entity bullet = engine.createEntity();
        bullet.add(engine.createComponent(PlayerBulletComponent.class));
        PositionComponent positionComponent = engine.createComponent(PositionComponent.class);
        bullet.add(positionComponent);
        VelocityComponent velocityComponent = engine.createComponent(VelocityComponent.class);
        bullet.add(velocityComponent);
        SpriteComponent spriteComponent = engine.createComponent(SpriteComponent.class);
        spriteComponent.sprite = new Sprite(assets.get(GFX_BULLET));
        bullet.add(spriteComponent);
        bullet.add(engine.createComponent(RemovableComponent.class));
        engine.addEntity(bullet);
        PositionComponent playerPosition = position.get(player);
        positionComponent.x = playerPosition.x + Mappers.sprite.get(player).sprite.getWidth() / 2f - spriteComponent.sprite.getWidth() / 2f;
        positionComponent.y = playerPosition.y + sprite.get(player).sprite.getHeight();
        velocityComponent.y = PLAYER_BULLET_VELOCITY;
        return bullet;
    }


    public Entity createEnnemySoucoupe() {
        Entity enemy = engine.createEntity();
        EnemyComponent enemyComponent = engine.createComponent(EnemyComponent.class);
        enemyComponent.points = 100;
        enemy.add(enemyComponent);
        PositionComponent position = engine.createComponent(PositionComponent.class);
        enemy.add(position);
        enemy.add(engine.createComponent(VelocityComponent.class));
        AnimationComponent animationComponent = engine.createComponent(AnimationComponent.class);
        Texture texture = assets.get(Assets.GFX_SOUCOUPE);
        TextureRegion[][] regions = TextureRegion.split(texture,
                texture.getWidth(), texture.getHeight() / 6);
        Array<Sprite> sprites = new Array<Sprite>(6);
        for (int i = 0; i < regions.length; ++i) {
            sprites.add(new Sprite(regions[i][0]));
        }
        animationComponent.animations.put(ANIMATION_MAIN, new Animation<Sprite>(FRAME_DURATION, sprites, Animation.PlayMode.LOOP));
        enemy.add(animationComponent);
        SpriteComponent component = engine.createComponent(SpriteComponent.class);
        component.sprite = sprites.get(0);
        enemy.add(component);
        enemy.add(engine.createComponent(StateComponent.class));
        engine.addEntity(enemy);
        return enemy;
    }

    public Entity createEntityPlayer() {
        Entity player = engine.createEntity();
        PlayerComponent playerComponent = engine.createComponent(PlayerComponent.class);
        playerComponent.setHighScore(Settings.getHighscore());
        player.add(playerComponent);
        player.add(engine.createComponent(PositionComponent.class));
        player.add(engine.createComponent(VelocityComponent.class));
        AnimationComponent animationComponent = engine.createComponent(AnimationComponent.class);
        Texture texture = assets.get(GFX_SHIP_PLAYER);
        TextureRegion[] regions = TextureRegion.split(texture,
                texture.getWidth() / 4, texture.getHeight())[0];
        Array<Sprite> spritesMAIN = new Array<Sprite>(2);
        for (int i = 0; i < 2; ++i) {
            spritesMAIN.add(new Sprite(regions[i]));
        }
        Array<Sprite> spritesLEFT = new Array<Sprite>(2);
        for (int i = 2; i < 4; ++i) {
            spritesLEFT.add(new Sprite(regions[i]));
        }
        Array<Sprite> spritesRIGHT = new Array<Sprite>(2);
        spritesRIGHT.add(new Sprite(regions[2]));
        spritesRIGHT.add(new Sprite(regions[3]));
        spritesRIGHT.get(0).flip(true, false);
        spritesRIGHT.get(1).flip(true, false);
        animationComponent.animations.put(ANIMATION_MAIN, new Animation<Sprite>(FRAME_DURATION, spritesMAIN, Animation.PlayMode.LOOP));
        animationComponent.animations.put(GO_LEFT, new Animation<Sprite>(FRAME_DURATION, spritesLEFT, Animation.PlayMode.LOOP));
        animationComponent.animations.put(GO_RIGHT, new Animation<Sprite>(FRAME_DURATION, spritesRIGHT, Animation.PlayMode.LOOP));
        player.add(animationComponent);
        SpriteComponent component = engine.createComponent(SpriteComponent.class);
        component.stayInBoundaries = true;
        player.add(component);
        player.add(engine.createComponent(StateComponent.class));
        engine.addEntity(player);
        Mappers.position.get(player).setPosition(PLAYER_ORIGIN_X, PLAYER_ORIGIN_Y);
        return player;
    }

    public Array<Entity> createEntityPlayerLives(Entity player) {
        PlayerComponent playerComponent = Mappers.player.get(player);
        Array<Entity> entities = new Array<Entity>(playerComponent.lives);
        for (int i = 0; i < playerComponent.lives - 1; ++i) {
            Entity life = engine.createEntity();
            Texture texture = assets.get(GFX_SHIP_PLAYER);
            TextureRegion tr = TextureRegion.split(texture,
                    texture.getWidth() / 4, texture.getHeight())[0][1];
            SpriteComponent component = engine.createComponent(SpriteComponent.class);
            component.setTexture(new Sprite(tr), 1f, 0f, 0.5f);
            component.setPosition(LIVES_X + 20f * i, LIVES_Y - texture.getHeight());
            life.add(component);
            engine.addEntity(life);
            entities.add(life);
        }
        return entities;
    }

    public Entity createEntityExploding(float x, float y) {
        Entity explosion = engine.createEntity();
        PositionComponent position = engine.createComponent(PositionComponent.class);
        explosion.add(position);
        position.setPosition(x, y);
        explosion.add(engine.createComponent(VelocityComponent.class));
        AnimationComponent animationComponent = engine.createComponent(AnimationComponent.class);
        Texture texture = assets.get(Assets.GFX_EXPLOSION);
        TextureRegion[][] regions = TextureRegion.split(texture,
                texture.getWidth() / 9, texture.getHeight() / 9);
        Array<Sprite> sprites = new Array<Sprite>(81);
        for (int i = 0; i < regions.length; ++i) {
            for (int j = 0; j < regions[i].length; ++j) {
                sprites.add(new Sprite(regions[i][j]));
            }
        }
        sprites.removeRange(74, 80);
        animationComponent.animations.put(ANIMATION_MAIN, new Animation<Sprite>(FRAME_DURATION_EXPLOSION, sprites, Animation.PlayMode.NORMAL));
        animationComponent.playMode = Animation.PlayMode.NORMAL;
        explosion.add(animationComponent);
        SpriteComponent component = engine.createComponent(SpriteComponent.class);
        explosion.add(component);
        explosion.add(engine.createComponent(StateComponent.class));
        engine.addEntity(explosion);
        return explosion;
    }

    public Entity createEntityFireButton(float alpha, float posX, float posY) {
        Entity entity = engine.createEntity();
        SpriteComponent component = engine.createComponent(SpriteComponent.class);
        component.setTexture(assets.get(GFX_PAD_BUTTON_FIRE), alpha, 0, 1f);
        component.setPosition(posX, posY);
        entity.add(component);
        engine.addEntity(entity);
        return entity;

    }

    public Entity createEntitiesPadController(float alpha, float posX, float posY) {
        Entity pad = engine.createEntity();
        SpriteComponent component = engine.createComponent(SpriteComponent.class);
        component.setTexture(assets.get(GFX_PAD_ARROW), 0.2f, 0f, 1f);
        component.setPosition(posX, posY);
        pad.add(component);
        engine.addEntity(pad);
        return pad;
    }

}
