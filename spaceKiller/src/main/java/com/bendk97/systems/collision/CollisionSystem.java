/*
 * Developed by Benjamin Lefèvre
 * Last modified 28/10/18 21:28
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.systems.collision;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bendk97.components.SpriteComponent;
import com.bendk97.components.helpers.ComponentMapperHelper;
import com.bendk97.components.helpers.Families;
import com.bendk97.listeners.CollisionListener;

import static com.bendk97.SpaceKillerGameConstants.SCREEN_HEIGHT;
import static com.bendk97.SpaceKillerGameConstants.SCREEN_WIDTH;
import static com.bendk97.pools.GamePools.poolCircle;
import static com.bendk97.pools.GamePools.poolRectangle;

public class CollisionSystem extends EntitySystem {

    private static final int FREQUENCY_MS = 50;
    private final CollisionListener collisionListener;
    private final CollisionHelper collisionHelper = new CollisionHelper();
    private final SpriteBatch spriteBatch;
    private FrameBuffer fbo;
    private final Viewport viewport;
    private float deltaCount = 0;

    public CollisionSystem(CollisionListener collisionListener, Viewport viewport, SpriteBatch spriteBatch, int priority) {
        super(priority);
        try {
            this.fbo = new FrameBuffer(Pixmap.Format.RGBA4444, (int) SCREEN_WIDTH, (int) SCREEN_HEIGHT, false);
        } catch (Exception e) {
            Gdx.app.log("Pixel Perfect Collision", "Unable on this device");
            this.fbo = null;
        }
        this.viewport = viewport;
        this.spriteBatch = spriteBatch;
        this.spriteBatch.enableBlending();
        this.collisionListener = collisionListener;
    }


    @Override
    public void update(float delta) {
        deltaCount += delta * 1000f;
        if (deltaCount < FREQUENCY_MS) {
            return;
        }
        deltaCount = 0f;

        detectCollisionWithPlayer();
        detectCollisionWithShields();
        detectCollisionWithPlayerVulnerable();
    }

    private void detectCollisionWithPlayer() {
        for (Entity player : getEngine().getEntitiesFor(Families.player)) {
            for (Entity bullet : getEngine().getEntitiesFor(Families.playerBullet)) {
                for (Entity enemy : getEngine().getEntitiesFor(Families.enemies)) {
                    if (!ComponentMapperHelper.enemy.get(enemy).isDead()
                            && isCollisionBetween(ComponentMapperHelper.sprite.get(enemy), ComponentMapperHelper.sprite.get(bullet))) {
                        collisionListener.enemyShoot(enemy, player, bullet);
                        return;
                    }
                }
            }

            for (Entity powerUp : getEngine().getEntitiesFor(Families.powerUp)) {
                if (isCollisionBetween(ComponentMapperHelper.sprite.get(player), ComponentMapperHelper.sprite.get(powerUp))) {
                    collisionListener.playerPowerUp(player, powerUp);
                    return;
                }
            }
            for (Entity shieldUp : getEngine().getEntitiesFor(Families.shieldUp)) {
                if (isCollisionBetween(ComponentMapperHelper.sprite.get(player), ComponentMapperHelper.sprite.get(shieldUp))) {
                    collisionListener.playerShieldUp(player, shieldUp);
                    return;
                }
            }
            for (Entity bombUp : getEngine().getEntitiesFor(Families.bombUp)) {
                if (isCollisionBetween(ComponentMapperHelper.sprite.get(player), ComponentMapperHelper.sprite.get(bombUp))) {
                    collisionListener.playerBombUp(player, bombUp);
                    return;
                }
            }
        }
    }

    private void detectCollisionWithPlayerVulnerable() {
        for (Entity player : getEngine().getEntitiesFor(Families.playerVulnerable)) {
            for (Entity enemy : getEngine().getEntitiesFor(Families.enemyBodies)) {
                if (isCollisionBetween(ComponentMapperHelper.sprite.get(enemy), ComponentMapperHelper.sprite.get(player))) {
                    collisionListener.playerHitByEnemyBody(player);
                    return;
                }

            }
            for (Entity bullet : getEngine().getEntitiesFor(Families.enemyBullet)) {
                SpriteComponent enemyBullet = ComponentMapperHelper.sprite.get(bullet);
                if (isCollisionBetween(enemyBullet, ComponentMapperHelper.sprite.get(player))) {
                    collisionListener.playerHitByEnemyBullet(player, bullet);
                    return;
                }
            }
        }
    }

    private void detectCollisionWithShields() {
        for (Entity shield : getEngine().getEntitiesFor(Families.shield)) {
            for (Entity bullet : getEngine().getEntitiesFor(Families.enemyBullet)) {
                SpriteComponent enemyBullet = ComponentMapperHelper.sprite.get(bullet);
                if (isCollisionBetween(enemyBullet, ComponentMapperHelper.sprite.get(shield))) {
                    collisionListener.bulletStoppedByShield(bullet);
                    return;
                }
            }
            for (Entity enemy : getEngine().getEntitiesFor(Families.enemyBodies)) {
                if (ComponentMapperHelper.boss.get(enemy) != null || ComponentMapperHelper.enemy.get(enemy).isLaserShip) {
                    break;
                }
                SpriteComponent enemySprite = ComponentMapperHelper.sprite.get(enemy);
                if (isCollisionBetween(enemySprite, ComponentMapperHelper.sprite.get(shield))) {
                    collisionListener.enemyShootByShield(enemy);
                    return;
                }
            }
        }
    }

    private boolean isCollisionBetween(SpriteComponent spriteComponent1, SpriteComponent spriteComponent2) {
        if (fbo != null && (spriteComponent1.pixelPerfectCollision || spriteComponent2.pixelPerfectCollision)) {
            return isPerfectPixelCollisionBetween(spriteComponent1.sprite, spriteComponent2.sprite);
        } else {
            return isBoundingCircleCollisionBetween(spriteComponent1.sprite, spriteComponent2.sprite);
        }
    }

    private boolean isBoundingCircleCollisionBetween(Sprite sprite1, Sprite sprite2) {
        Circle boundingCircle1 = collisionHelper.getBoundingCircle(sprite1);
        Circle boundingCircle2 = collisionHelper.getBoundingCircle(sprite2);
        try {
            return Intersector.overlaps(boundingCircle1, boundingCircle2);
        } finally {
            poolCircle.free(boundingCircle1);
            poolCircle.free(boundingCircle2);
        }
    }

    private boolean isPerfectPixelCollisionBetween(Sprite sprite1, Sprite sprite2) {
        Rectangle collision = poolRectangle.obtain();
        try {
            if (Intersector.intersectRectangles(sprite1.getBoundingRectangle(), sprite2.getBoundingRectangle(), collision)) {
                Pixmap pix1 = null;
                Pixmap pix2 = null;
                try {
                    viewport.apply();
                    spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
                    pix1 = transformToPixels(sprite1, collision);
                    pix2 = transformToPixels(sprite2, collision);
                    for (int i = 0; i < pix1.getWidth(); ++i) {
                        for (int j = 0; j < pix1.getHeight(); ++j) {
                            if (pix1.getPixel(i, j) != 255 && pix2.getPixel(i, j) != 255) {
                                return true;
                            }
                        }
                    }
                } finally {
                    if (pix1 != null) {
                        pix1.dispose();
                    }
                    if (pix2 != null) {
                        pix2.dispose();
                    }
                }
            }
            return false;
        } finally {
            poolRectangle.free(collision);
        }
    }

    private Pixmap transformToPixels(Sprite sprite, Rectangle collision) {
        fbo.begin();
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        spriteBatch.begin();
        sprite.draw(spriteBatch);
        spriteBatch.end();
        fbo.end();
        fbo.bind();
        Pixmap pixmap = ScreenUtils.getFrameBufferPixmap(
                (int) Math.floor(collision.x),
                (int) Math.floor(collision.y),
                (int) Math.ceil(collision.width),
                (int) Math.ceil(collision.height)
        );
        FrameBuffer.unbind();
        return pixmap;
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        if (fbo != null) {
            fbo.dispose();
        }
    }
}

