package com.unanglaro.topdown.main;

import com.badlogic.gdx.Game;

public class rpgGame extends Game {
	//screens

	@Override
	public void create () {
		AssetRenderer.mainMenuLoad();
		DialogueConverter.convert();

		setScreen(new GameScreen(this));

	}
}
