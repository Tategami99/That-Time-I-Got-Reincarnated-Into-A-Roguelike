package com.unanglaro.topdown.main;

import com.badlogic.gdx.Game;

public class rpgGame extends Game {
	private DialogueConverter converter = new DialogueConverter();
	@Override
	public void create () {
		converter.convert();
		setScreen(new GameScreen(this));

	}
}
