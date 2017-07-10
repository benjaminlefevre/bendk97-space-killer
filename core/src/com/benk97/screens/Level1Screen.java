package com.benk97.screens;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.benk97.components.BackgroundComponent;
import com.benk97.components.PositionComponent;
import com.benk97.components.VelocityComponent;

import static com.benk97.SpaceKillerGameConstants.BGD_PARALLAX_VELOCITY;
import static com.benk97.SpaceKillerGameConstants.BGD_VELOCITY;

public class Level1Screen extends LevelScreen {
    public Level1Screen() {
        super();

        createBackground(new Texture(Gdx.files.internal("gfx/backgrounds/level1.gif")), -BGD_VELOCITY);
        createBackground(new Texture(Gdx.files.internal("gfx/backgrounds/starfield.png")), -BGD_PARALLAX_VELOCITY);
    }

    private Entity createBackground(Texture texture, float velocity) {
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

    @Override
    public void render(float delta) {
        super.render(delta);
    }
}
