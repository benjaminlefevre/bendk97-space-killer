package com.benk97.screens;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.benk97.assets.Assets;
import com.benk97.components.Mappers;
import com.benk97.entities.EntityFactory;
import com.benk97.inputs.TouchInputHandler;
import com.benk97.systems.*;

import static com.benk97.SpaceKillerGameConstants.*;

public class LevelScreen extends ScreenAdapter {

    protected Viewport viewport;
    protected OrthographicCamera camera;
    protected PooledEngine engine;
    protected EntityFactory entityFactory;
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
        entityFactory = new EntityFactory(engine, assets);
        Entity player = entityFactory.createEntityPlayer();
        entityFactory.createEntityPlayerLives();
        createSystems(player);
    }

    private void createSystems(Entity player) {
        engine.addSystem(createInputHandlerSystem(player, 0));
        engine.addSystem(new AnimationSystem(0));
        engine.addSystem(new StateSystem(1));
        engine.addSystem(new MovementSystem(2));
        engine.addSystem(new RenderingSystem(camera, player, assets, 3));
        engine.addSystem(new CollisionSystem(player, assets, entityFactory, 4));
        engine.addSystem(new RemovableSystem(5));
    }

    private InputHandlerSystem createInputHandlerSystem(Entity player, int priority) {
        Entity fireButton = entityFactory.createEntityFireButton(0.2f, FIRE_X, FIRE_Y);
        Entity padController = entityFactory.createEntitiesPadController(0.2f, PAD_X, PAD_Y);
        // define touch area as rectangles
        Sprite padSprite = Mappers.staticSprite.get(padController).sprite;
        float heightTouch = padSprite.getHeight() / 3f, widthTouch = padSprite.getWidth() / 3f;
        Rectangle[] squareTouchesDirection = new Rectangle[8];
        squareTouchesDirection[0] = new Rectangle(PAD_X, PAD_Y + 2 * heightTouch, widthTouch, heightTouch);
        squareTouchesDirection[1] = new Rectangle(PAD_X + widthTouch, PAD_Y + 2 * heightTouch, widthTouch, heightTouch);
        squareTouchesDirection[2] = new Rectangle(PAD_X + 2 * widthTouch, PAD_Y + 2 * heightTouch, widthTouch, heightTouch);
        squareTouchesDirection[3] = new Rectangle(PAD_X, PAD_Y + heightTouch, widthTouch, heightTouch);
        squareTouchesDirection[4] = new Rectangle(PAD_X + 2 * widthTouch, PAD_Y + heightTouch, widthTouch, heightTouch);
        squareTouchesDirection[5] = new Rectangle(PAD_X, PAD_Y, widthTouch, heightTouch);
        squareTouchesDirection[6] = new Rectangle(PAD_X + widthTouch, PAD_Y, widthTouch, heightTouch);
        squareTouchesDirection[7] = new Rectangle(PAD_X + 2 * widthTouch, PAD_Y, widthTouch, heightTouch);
        // input
        InputHandlerSystem inputHandlerSystem = new InputHandlerSystem(player, entityFactory, assets, priority);
        TouchInputHandler touchInputHandler = new TouchInputHandler(inputHandlerSystem, camera, squareTouchesDirection, Mappers.staticSprite.get(fireButton).getBounds());
        Gdx.input.setInputProcessor(touchInputHandler);
        return inputHandlerSystem;
    }


    @Override
    public void render(float delta) {
        engine.update(delta);
    }


    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }
}
