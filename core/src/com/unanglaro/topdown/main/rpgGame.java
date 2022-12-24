package com.unanglaro.topdown.main;

import com.badlogic.gdx.Game;

public class rpgGame extends Game {
	//screens
	public GameScreen mainMenu;

	@Override
	public void create () {
		AssetRenderer.mainMenuLoad();
		mainMenu = new GameScreen(this);

		setScreen(mainMenu);

	}
}
