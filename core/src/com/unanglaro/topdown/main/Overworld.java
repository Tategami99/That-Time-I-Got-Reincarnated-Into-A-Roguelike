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
    private Player player;

    private OrthographicCamera camera;
    private Stage stage;
    private Viewport viewport;
    
    Overworld(rpgGame game){
        this.game = game;

        AssetRenderer.playerLoad(4, 1, 0.05f);
        
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
        player.updateNonRender();

        AssetRenderer.overworldRenderer.getBatch().begin();
            player.draw(AssetRenderer.overworldRenderer.getBatch());
        AssetRenderer.overworldRenderer.getBatch().end();

        stage.draw();
	}
    @Override
	public void show () {
        AssetRenderer.overworldLoadShow();
        worldWidth = AssetRenderer.overworldMapProperties.get("width", Integer.class)*AssetRenderer.overworldMapProperties.get("tilewidth", Integer.class);
        worldHeight = AssetRenderer.overworldMapProperties.get("height", Integer.class)*AssetRenderer.overworldMapProperties.get("tileheight", Integer.class);

        viewport = new FitViewport(worldWidth, worldHeight, camera);

        stage = new Stage(viewport, AssetRenderer.overworldRenderer.getBatch());

        player = new Player(AssetRenderer.playerMoveAnimation.getKeyFrame(1), (TiledMapTileLayer) AssetRenderer.overworldMap.getLayers().get(1), game, stage);
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
        AssetRenderer.overworldDispose();
        AssetRenderer.playerDispose();
	}

    //my methods
}

