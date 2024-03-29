package com.unanglaro.topdown.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.unanglaro.topdown.main.GameState.World;
public class GameScreen extends ScreenAdapter{
    private rpgGame game;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Stage stage;
    private Table mainTable;

    GameScreen(rpgGame game){
        GameState.game = game;
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        batch = new SpriteBatch();
        
        AssetRenderer.mainMenuLoad();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1F, 1F, 1F, 1F);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | GL20.GL_STENCIL_BUFFER_BIT);

        stage.act(Gdx.graphics.getDeltaTime());

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        //render code
            batch.draw(AssetRenderer.spriteBackground, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        stage.draw();
        GameState.detectGameState();
    }

    @Override
    public void show() {
        GameState.world = World.MainMenu;
        stage = new Stage();
        mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);

        createButton(AssetRenderer.newGameDrawable).addListener(new ClickListener(){
            @Override
            public void clicked (InputEvent event, float x, float y) {
                System.out.println("clicked");
                game.setScreen(new OpeningScreen(game));
            }
        });
        createButton(AssetRenderer.loadGameDrawable).addListener(new ClickListener(){
            @Override
            public void clicked (InputEvent event, float x, float y) {
                DataManager.LoadData();
            }
        });;
        createButton(AssetRenderer.quitDrawable).addListener(new ClickListener(){
            @Override
            public void clicked (InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });;

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void hide() {
        dispose();
        AssetRenderer.mainMenuDispose();
    }
    @Override
    public void dispose() {
        batch.dispose();
        stage.dispose();
        
    }
    
    //my methods
    private ImageButton createButton(Drawable drawable){
        ImageButton button = new ImageButton(drawable);
        mainTable.add(button).fill().padBottom(10);
        mainTable.row();
        return button;
    }
}
