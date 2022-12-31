package com.unanglaro.topdown.main;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class OpeningScreen extends ScreenAdapter{
    private rpgGame game;
    private AssetRenderer renderer;
    private DialogueManager dialoguer;

    private OrthographicCamera camera;
    private Stage stage;
    private SpriteBatch batch;

    OpeningScreen(rpgGame game){
        this.game = game;

        renderer = new AssetRenderer();
        
        camera = new OrthographicCamera();
        camera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        batch = new SpriteBatch();
        renderer.openingScreenLoad();
    }

    @Override
    public void show(){
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        dialoguer = new DialogueManager(game, stage, 0, 17, true);
    }
    @Override
    public void render(float delta){
        Gdx.gl.glClearColor(1F, 1F, 1F, 1F);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | GL20.GL_STENCIL_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        //render code
        batch.draw(renderer.openingBackgroundSprite, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        stage.draw();
    }
    @Override
    public void hide(){
        dispose();
        renderer.openingScreenDispose();
    }
    @Override
    public void dispose(){
        batch.dispose();
        stage.dispose();
    }
}
