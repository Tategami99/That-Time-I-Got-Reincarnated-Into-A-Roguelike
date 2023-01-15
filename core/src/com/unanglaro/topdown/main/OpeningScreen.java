package com.unanglaro.topdown.main;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.unanglaro.topdown.main.GameState.State;
import com.unanglaro.topdown.main.GameState.World;

public class OpeningScreen extends ScreenAdapter{
    private rpgGame game;

    private OrthographicCamera camera;
    private Stage stage;
    private SpriteBatch batch;

    OpeningScreen(rpgGame game){
        this.game = game;
        
        camera = new OrthographicCamera();
        camera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        batch = new SpriteBatch();
        AssetRenderer.openingScreenLoad();
    }

    @Override
    public void show(){
        GameState.world = World.Heaven;
        stage = new Stage();
        GameState.camera = camera;
        Gdx.input.setInputProcessor(stage);
        new DialogueManager(stage, 0, 9, true, State.InitializePlayer, camera.viewportWidth, camera.viewportHeight);
    }
    @Override
    public void render(float delta){
        Gdx.gl.glClearColor(1F, 1F, 1F, 1F);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | GL20.GL_STENCIL_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        //render code
        batch.draw(AssetRenderer.openingBackgroundSprite, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        stage.draw();
        GameState.detectGameState();
    }
    @Override
    public void hide(){
        dispose();
        AssetRenderer.openingScreenDispose();
    }
    @Override
    public void dispose(){
        batch.dispose();
        stage.dispose();
    }
}
