package com.unanglaro.topdown.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;

public class NewOpeningScreen extends ScreenAdapter{
    private rpgGame game;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    
    NewOpeningScreen(rpgGame game){
        this.game = game;
        
        camera = new OrthographicCamera();
        camera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        batch = new SpriteBatch();
    }

    @Override
    public void render (float delta) {
        Gdx.gl.glClearColor(1F, 1F, 1F, 1F);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        //render code
        batch.end();
	}
    @Override
	public void show () {
	}
    @Override
	public void hide () {
	}
    @Override
	public void dispose () {
	}

    //my methods
}

