package com.benk97.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Intersector;
import com.benk97.assets.Assets;
import com.benk97.components.*;
import com.benk97.entities.EntityFactory;

import static com.benk97.assets.Assets.SOUND_EXPLOSION;

public class CollisionSystem extends EntitySystem {

    private ImmutableArray<Entity> playerBullets;
    private ImmutableArray<Entity> ennemies;

    private Assets assets;
    private EntityFactory entityFactory;

    public CollisionSystem(Assets assets, EntityFactory entityFactory, int priority){
        super(priority);
        this.assets = assets;
        this.entityFactory = entityFactory;
    }

    @Override
    public void addedToEngine(Engine engine) {
        playerBullets = engine.getEntitiesFor(Family.one(BulletComponent.class).get());
        ennemies = engine.getEntitiesFor(Family.all(EnnemyComponent.class).get());
    }


    @Override
    public void update(float delta) {
        for(Entity ennemy : ennemies){
            for(Entity bullet : playerBullets){
                SpriteComponent ennemySprite = Mappers.sprite.get(ennemy);
                SpriteComponent bulletSprite = Mappers.sprite.get(bullet);
                if(Intersector.overlaps(ennemySprite.sprite.getBoundingRectangle(), bulletSprite.sprite.getBoundingRectangle())){
                    assets.playSound(SOUND_EXPLOSION);
                    PositionComponent ennemyPosition = Mappers.position.get(ennemy);
                    entityFactory.createEntityExploding(ennemyPosition.x, ennemyPosition.y);
                    getEngine().removeEntity(ennemy);
                    getEngine().removeEntity(bullet);
                }
            }
        }
    }

}
