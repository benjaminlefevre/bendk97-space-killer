package com.benk97.screens;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.benk97.components.*;
import com.benk97.inputs.TouchInputHandler;
import com.benk97.systems.InputHandlerSystem;
import com.benk97.systems.MovementSystem;
import com.benk97.systems.RemovableSystem;
import com.benk97.systems.RenderingSystem;

import static com.benk97.SpaceKillerGameConstants.SCREEN_HEIGHT;
import static com.benk97.SpaceKillerGameConstants.SCREEN_WIDTH;

public class LevelScreen implements Screen {

    protected Viewport viewport;
    protected OrthographicCamera camera;
    protected PooledEngine engine;


    public LevelScreen() {
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
        RenderingSystem renderingSystem = new RenderingSystem(camera, player);
        engine.addSystem(renderingSystem);
        MovementSystem movementSystem = new MovementSystem(Family.all(PositionComponent.class, VelocityComponent.class).get());
        engine.addSystem(movementSystem);
        RemovableSystem removableSystem = new RemovableSystem(engine,
                Family.all(RemovableComponent.class, PositionComponent.class, DynamicSpriteComponent.class).get());
        engine.addSystem(removableSystem);
    }


    private Rectangle createEntityInputController(Entity player, Texture texture, float alpha, float rotation, float posX, float posY) {
        Entity arrow = engine.createEntity();
        StaticSpriteComponent component = engine.createComponent(StaticSpriteComponent.class);
        component.setTexture(texture, alpha, rotation);
        component.setPosition(posX, posY);
        arrow.add(component);
        engine.addEntity(arrow);
        return component.getBounds();
    }

    private void createEntitiesInputController(Entity player) {
        Rectangle[] squareTouchesDirection = new Rectangle[8];
        float x = 10f, y = 120f, rotation = 45f, width, height;
        Texture texture = new Texture(Gdx.files.internal("gfx/arrow.png"));
        squareTouchesDirection[0] = createEntityInputController(player, texture, 0f, 45f, x, y);
        width = squareTouchesDirection[0].getWidth();
        height = squareTouchesDirection[0].getHeight();
        squareTouchesDirection[1] = createEntityInputController(player, texture, 0.2f, 0f, x + width, y);
        squareTouchesDirection[2] = createEntityInputController(player, texture, 0f, -45f, x + 2 * width, y);
        squareTouchesDirection[3] = createEntityInputController(player, texture, 0.2f, 90f, x, y - height);
        squareTouchesDirection[4] = createEntityInputController(player, texture, 0.2f, -90f, x + 2 * width, y - height);
        squareTouchesDirection[5] = createEntityInputController(player, texture, 0f, 135f, x, y - 2 * height);
        squareTouchesDirection[6] = createEntityInputController(player, texture, 0.2f, 180f, x + width, y - 2 * height);
        squareTouchesDirection[7] = createEntityInputController(player, texture, 0f, -135f, x + 2 * width, y - 2 * height);

        Rectangle fireButton = createEntityInputController(player, new Texture(Gdx.files.internal("gfx/fire_button.png")), 0.2f, 0f, 275f, y - 2 * height);
        // input
        InputHandlerSystem inputHandlerSystem = new InputHandlerSystem(player, engine);
        TouchInputHandler touchInputHandler = new TouchInputHandler(inputHandlerSystem, camera, squareTouchesDirection, fireButton);
        Gdx.input.setInputProcessor(touchInputHandler);
        engine.addSystem(inputHandlerSystem);
        inputHandlerSystem.addedToEngine(engine);
    }

    private Entity createEntityPlayer() {
        Entity player = engine.createEntity();
        player.add(engine.createComponent(PositionComponent.class));
        player.add(engine.createComponent(VelocityComponent.class));
        DynamicSpriteComponent component = engine.createComponent(DynamicSpriteComponent.class);
        component.stayInBoundaries = true;
        component.setTexture(new Texture(Gdx.files.internal("gfx/player.png")), 4, new int[]{0, 1}, new int[]{2, 3});
        player.add(component);
        engine.addEntity(player);
        return player;
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        engine.update(delta);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
