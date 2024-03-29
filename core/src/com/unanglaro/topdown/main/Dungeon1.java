package com.unanglaro.topdown.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.unanglaro.topdown.main.GameState.State;
import com.unanglaro.topdown.main.GameState.World;

public class Dungeon1 extends ScreenAdapter{
    private Integer worldWidth;
    private Integer worldHeight;

    private rpgGame game; 

    //entities in game
    private EntityManager entities;
    private int wave = 0;

    private OrthographicCamera camera;
    private Stage stage;
    private Viewport viewport;

    public Dungeon1(rpgGame game){

        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0F, 0F, 0F, 1F);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | GL20.GL_STENCIL_BUFFER_BIT);

        //tilemap
        AssetRenderer.dungeon1Renderer.setView(camera);
        AssetRenderer.dungeon1Renderer.render();

        //update stuff
        camera.update();
        AssetRenderer.dungeon1Renderer.getBatch().setProjectionMatrix(camera.combined);
        entities.update(delta);

        AssetRenderer.dungeon1Renderer.getBatch().begin();
            entities.render(AssetRenderer.dungeon1Renderer.getBatch(), delta);
        AssetRenderer.dungeon1Renderer.getBatch().end();

        stage.draw();
        GameState.detectGameState();

        //wave stuff
        startWave();
    }

    @Override
    public void show() {
        GameState.world = World.Dungeon1;
        GameState.state = State.Exploring;
        AssetRenderer.dungeon1LoadShow();
        worldWidth = AssetRenderer.dungeon1MapProperties.get("width", Integer.class)*AssetRenderer.dungeon1MapProperties.get("tilewidth", Integer.class);
        worldHeight = AssetRenderer.dungeon1MapProperties.get("height", Integer.class)*AssetRenderer.dungeon1MapProperties.get("tileheight", Integer.class);

        viewport = new FitViewport(worldWidth, worldHeight, camera);
        GameState.camera = camera;

        stage = new Stage(viewport, AssetRenderer.dungeon1Renderer.getBatch());
        GameState.stage = stage;

        entities = new EntityManager((TiledMapTileLayer) AssetRenderer.dungeon1Map.getLayers().get(0), game, stage, camera.viewportWidth, camera.viewportHeight);
        entities.createPlayer();
        entities.spawnAreaMinX = 700;
        entities.spawnAreaMaxX = 1000;
        entities.spawnAreaMinY = 100;
        entities.spawnAreaMaxY = 850;
        wave = 1;
    }

    @Override
    public void hide() {
        hide();
    }

    @Override
    public void dispose() {
        stage.dispose();
        AssetRenderer.dungeon1Dispose();
        entities.dispose();
    }

    private void startWave(){
        if(GameState.state == State.Exploring && wave == 1){
            entities.createEntities(3, 1);
            wave++;
        }
        if(GameState.state == State.Exploring && wave == 2){
            entities.createEntities(2, 2);
            wave++;
        }
        if(GameState.state == State.Exploring && wave == 3){
            entities.createEntities(1, 3);
            wave++;
        }
        if(GameState.state == State.Exploring && wave == 4){
            GameState.state = State.SpecialMessage;
        }
    }
}
