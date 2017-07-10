package com.benk97;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.benk97.screens.Level1Screen;

public class SpaceKillerGame extends Game {

	private Level1Screen level1Screen;

	public SpaceKillerGame() {
	}

	@Override
	public void create () {
		goToScreen(new Level1Screen());
	}

	public void goToScreen(Screen screen) {
		this.setScreen(screen);
	}


	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
	}
}
