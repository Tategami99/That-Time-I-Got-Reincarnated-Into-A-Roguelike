package com.unanglaro.topdown.main;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Overworld extends ScreenAdapter{
    private rpgGame game;
    private AssetRenderer renderer = new AssetRenderer();
    private Player player;

    private OrthographicCamera camera;
    private Stage stage;
    private SpriteBatch batch;
    
    Overworld(rpgGame game){
        this.game = game;
        
        camera = new OrthographicCamera();
        camera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        batch = new SpriteBatch();
        renderer.openingScreenLoad();
    }

    @Override
    public void render (float delta) {
        Gdx.gl.glClearColor(1F, 1F, 1F, 1F);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | GL20.GL_STENCIL_BUFFER_BIT);

        player.update();
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        //render code
        batch.draw(renderer.openingBackgroundSprite, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        stage.draw();
	}
    @Override
	public void show () {
        stage = new Stage();
        player = new Player(game, stage);
	}
    @Override
	public void hide () {
        dispose();
	}
    @Override
	public void dispose () {
        stage.dispose();
	}

    //my methods
}

