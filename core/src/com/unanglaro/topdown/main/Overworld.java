package com.unanglaro.topdown.main;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Overworld extends ScreenAdapter{
    private Integer worldWidth;
    private Integer worldHeight;

    private rpgGame game;
    private AssetRenderer renderer = new AssetRenderer();
    private Player player;

    private OrthographicCamera camera;
    private Stage stage;
    private Viewport viewport;
    
    Overworld(rpgGame game){
        this.game = game;

        renderer.playerLoad(4, 1, 0.05f);
        
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void render (float delta) {
        Gdx.gl.glClearColor(0F, 0F, 0F, 1F);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | GL20.GL_STENCIL_BUFFER_BIT);

        //tilemap
        renderer.overworldRenderer.setView(camera);
        renderer.overworldRenderer.render();

        //update stuff
        camera.update();
        renderer.overworldRenderer.getBatch().setProjectionMatrix(camera.combined);
        player.updateNonRender();

        renderer.overworldRenderer.getBatch().begin();
            player.draw(renderer.overworldRenderer.getBatch());
        renderer.overworldRenderer.getBatch().end();

        stage.draw();
	}
    @Override
	public void show () {
        renderer.overworldLoadShow();
        worldWidth = renderer.overworldMapProperties.get("width", Integer.class)*renderer.overworldMapProperties.get("tilewidth", Integer.class);
        worldHeight = renderer.overworldMapProperties.get("height", Integer.class)*renderer.overworldMapProperties.get("tileheight", Integer.class);

        viewport = new FitViewport(worldWidth, worldHeight, camera);

        stage = new Stage(viewport, renderer.overworldRenderer.getBatch());

        player = new Player(renderer.playerMoveAnimation.getKeyFrame(1), (TiledMapTileLayer) renderer.overworldMap.getLayers().get(1), game, stage);
        player.setPosition(20 * player.getCollisionsLayer().getTileWidth(), 20 * player.getCollisionsLayer().getTileHeight());

        Gdx.input.setInputProcessor(player);
	}
    @Override
	public void hide () {
        dispose();
	}
    @Override
	public void dispose () {
        stage.dispose();
        renderer.overworldDispose();
        renderer.playerDispose();
	}

    //my methods
}

