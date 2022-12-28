package com.unanglaro.topdown.main;

import com.badlogic.gdx.Game;

public class rpgGame extends Game {
	@Override
	public void create () {

		setScreen(new GameScreen(this));

	}
}
