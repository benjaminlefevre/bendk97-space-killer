package com.benk97.screens;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.benk97.assets.Assets;
import com.benk97.components.BackgroundComponent;
import com.benk97.components.PositionComponent;
import com.benk97.components.VelocityComponent;

import static com.benk97.SpaceKillerGameConstants.BGD_PARALLAX_VELOCITY;
import static com.benk97.SpaceKillerGameConstants.BGD_VELOCITY;
import static com.benk97.assets.Assets.GFX_BGD_LEVEL1;
import static com.benk97.assets.Assets.GFX_BGD_STARS;

public class Level1Screen extends LevelScreen {
    public Level1Screen(Assets assets) {
        super(assets);
        createBackground(assets.get(GFX_BGD_LEVEL1), -BGD_VELOCITY);
        createBackground(assets.get(GFX_BGD_STARS), -BGD_PARALLAX_VELOCITY);
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

    @Override
    public void dispose() {
        assets.unloadResources(this.getClass());
    }
}
