package com.unanglaro.topdown.main;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.unanglaro.topdown.main.GameState.State;
import com.unanglaro.topdown.main.GameState.World;

public class Overworld extends ScreenAdapter{
    private Integer worldWidth;
    private Integer worldHeight;

    private rpgGame game;

    //entities in game
    private EntityManager entities;

    private OrthographicCamera camera;
    private Stage stage;
    private Viewport viewport;
    
    Overworld(rpgGame game){
        this.game = game;
        
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void render (float delta) {
        Gdx.gl.glClearColor(0F, 0F, 0F, 1F);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | GL20.GL_STENCIL_BUFFER_BIT);

        //tilemap
        AssetRenderer.overworldRenderer.setView(camera);
        AssetRenderer.overworldRenderer.render();

        //update stuff
        camera.update();
        AssetRenderer.overworldRenderer.getBatch().setProjectionMatrix(camera.combined);
        entities.update(delta);

        AssetRenderer.overworldRenderer.getBatch().begin();
            entities.render(AssetRenderer.overworldRenderer.getBatch(), delta);
        AssetRenderer.overworldRenderer.getBatch().end();

        stage.draw();
        GameState.detectGameState();
	}
    @Override
	public void show () {
        GameState.world = World.Overworld;
        GameState.state = State.BattlingEnemies;
        AssetRenderer.overworldLoadShow();
        worldWidth = AssetRenderer.overworldMapProperties.get("width", Integer.class)*AssetRenderer.overworldMapProperties.get("tilewidth", Integer.class);
        worldHeight = AssetRenderer.overworldMapProperties.get("height", Integer.class)*AssetRenderer.overworldMapProperties.get("tileheight", Integer.class);

        viewport = new FitViewport(worldWidth, worldHeight, camera);
        GameState.camera = camera;

        stage = new Stage(viewport, AssetRenderer.overworldRenderer.getBatch());
        GameState.stage = stage;

        entities = new EntityManager((TiledMapTileLayer) AssetRenderer.overworldMap.getLayers().get(1), game, stage, camera.viewportWidth, camera.viewportHeight);
        entities.createPlayer();
        entities.createEntities(3, 1);
	}
    @Override
	public void hide () {
        dispose();
	}
    @Override
	public void dispose () {
        stage.dispose();
        AssetRenderer.overworldDispose();
        entities.dispose();
	}

    //my methods
}

