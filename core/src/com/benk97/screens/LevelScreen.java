package com.benk97.screens;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.benk97.assets.Assets;
import com.benk97.components.*;
import com.benk97.inputs.TouchInputHandler;
import com.benk97.systems.*;

import java.util.Arrays;

import static com.benk97.SpaceKillerGameConstants.*;
import static com.benk97.assets.Assets.*;
import static com.benk97.components.Mappers.position;
import static com.benk97.components.Mappers.sprite;

public class LevelScreen extends ScreenAdapter {

    protected Viewport viewport;
    protected OrthographicCamera camera;
    protected PooledEngine engine;
    public Assets assets;


    public LevelScreen(Assets assets) {
        this.assets = assets;
        this.camera = new OrthographicCamera();
        viewport = new StretchViewport(SCREEN_WIDTH, SCREEN_HEIGHT, camera);
        camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);
        engine = new PooledEngine();
        engine.addEntityListener(new EntityListener() {
            @Override
            public void entityAdded(Entity entity) {
                Gdx.app.log("entity added", "size: " + engine.getEntities().size());
            }

            @Override
            public void entityRemoved(Entity entity) {
                Gdx.app.log("entity removed", "size " + engine.getEntities().size());
            }
        });
        Entity player = createEntityPlayer();
        createEntitiesInputController(player);
        createSystems(player);
    }

    private void createSystems(Entity player) {
        engine.addSystem(new RenderingSystem(camera, player));
        engine.addSystem(new MovementSystem());
        engine.addSystem(new RemovableSystem(engine));
        engine.addSystem(new AnimationSystem());
        engine.addSystem(new StateSystem());
    }


    private Rectangle createEntityInputController(Texture texture, float alpha, float rotation, float posX, float posY) {
        Entity entity = engine.createEntity();
        StaticSpriteComponent component = engine.createComponent(StaticSpriteComponent.class);
        component.setTexture(texture, alpha, rotation);
        component.setPosition(posX, posY);
        entity.add(component);
        engine.addEntity(entity);
        return component.getBounds();

    }

    private void createEntitiesInputController(Entity player) {
        Entity pad = engine.createEntity();
        StaticSpriteComponent component = engine.createComponent(StaticSpriteComponent.class);
        Texture texture = assets.get(GFX_PAD_ARROW);
        component.setTexture(texture, 0.2f, 0f);
        component.setPosition(PAD_X, PAD_Y);
        pad.add(component);
        engine.addEntity(pad);
        float heightTouch = texture.getHeight() / 3f, widthTouch = texture.getWidth() / 3f;
        Rectangle[] squareTouchesDirection = new Rectangle[8];
        squareTouchesDirection[0] = new Rectangle(PAD_X, PAD_Y + 2 * heightTouch, widthTouch, heightTouch);
        squareTouchesDirection[1] = new Rectangle(PAD_X + widthTouch, PAD_Y + 2 * heightTouch, widthTouch, heightTouch);
        squareTouchesDirection[2] = new Rectangle(PAD_X + 2 * widthTouch, PAD_Y + 2 * heightTouch, widthTouch, heightTouch);
        squareTouchesDirection[3] = new Rectangle(PAD_X, PAD_Y + heightTouch, widthTouch, heightTouch);
        squareTouchesDirection[4] = new Rectangle(PAD_X + 2 * widthTouch, PAD_Y + heightTouch, widthTouch, heightTouch);
        squareTouchesDirection[5] = new Rectangle(PAD_X, PAD_Y, widthTouch, heightTouch);
        squareTouchesDirection[6] = new Rectangle(PAD_X + widthTouch, PAD_Y, widthTouch, heightTouch);
        squareTouchesDirection[7] = new Rectangle(PAD_X + 2 * widthTouch, PAD_Y, widthTouch, heightTouch);

        Rectangle fireButton = createEntityInputController(assets.get(GFX_PAD_BUTTON_FIRE), 0.2f, 0f, FIRE_X, FIRE_Y);
        // input
        InputHandlerSystem inputHandlerSystem = new InputHandlerSystem(player, engine, this);
        TouchInputHandler touchInputHandler = new TouchInputHandler(inputHandlerSystem, camera, squareTouchesDirection, fireButton);
        Gdx.input.setInputProcessor(touchInputHandler);
        engine.addSystem(inputHandlerSystem);
        inputHandlerSystem.addedToEngine(engine);
    }

    private Entity createEntityPlayer() {
        Entity player = engine.createEntity();
        player.add(engine.createComponent(PositionComponent.class));
        player.add(engine.createComponent(VelocityComponent.class));
        AnimationComponent animationComponent = engine.createComponent(AnimationComponent.class);
        Texture texture = assets.get(GFX_SHIP_PLAYER);
        TextureRegion[] regions = TextureRegion.split(texture,
                texture.getWidth() / 4, texture.getHeight())[0];
        Sprite[] sprites = new Sprite[6];
        for (int i = 0; i < regions.length; ++i) {
            sprites[i] = new Sprite(regions[i]);
        }
        sprites[4] = new Sprite(regions[2]);
        sprites[5] = new Sprite(regions[3]);
        sprites[4].flip(true, false);
        sprites[5].flip(true, false);
        animationComponent.animations.put(GO_AHEAD, new Animation<Sprite>(FRAME_DURATION, Arrays.copyOfRange(sprites, 0, 2)));
        animationComponent.animations.put(GO_LEFT, new Animation<Sprite>(FRAME_DURATION, Arrays.copyOfRange(sprites, 2, 4)));
        animationComponent.animations.put(GO_RIGHT, new Animation<Sprite>(FRAME_DURATION, Arrays.copyOfRange(sprites, 4, 6)));
        player.add(animationComponent);
        SpriteComponent component = engine.createComponent(SpriteComponent.class);
        component.stayInBoundaries = true;
        player.add(component);
        player.add(engine.createComponent(StateComponent.class));
        engine.addEntity(player);
        return player;
    }


    @Override
    public void render(float delta) {
        engine.update(delta);
    }

    public void playerFires(Entity player) {
        Entity bullet = engine.createEntity();
        PositionComponent positionComponent = engine.createComponent(PositionComponent.class);
        bullet.add(positionComponent);
        VelocityComponent velocityComponent = engine.createComponent(VelocityComponent.class);
        bullet.add(velocityComponent);
        SpriteComponent spriteComponent = engine.createComponent(SpriteComponent.class);
        bullet.add(spriteComponent);
        bullet.add(engine.createComponent(RemovableComponent.class));
        engine.addEntity(bullet);
        PositionComponent playerPosition = position.get(player);
        positionComponent.x = playerPosition.x;
        positionComponent.y = playerPosition.y + sprite.get(player).sprite.getHeight();
        velocityComponent.y = PLAYER_BULLET_VELOCITY;
        spriteComponent.sprite = new Sprite(assets.get(GFX_BULLET));
    }

    @Override
    public void resize (int width, int height) {
        viewport.update(width, height, true);
    }
}
