package com.unanglaro.topdown.main;

import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;

public class PauseMenu extends Group{
    private Player player;
    private rpgGame game;

    private AssetRenderer renderer = new AssetRenderer();
    private Table pausedTable;
    private Integer worldWidth = 1600;
    private Integer worldHeight = 960;

    PauseMenu(Player player, rpgGame game){
        System.out.println("opened");
        this.player = player;
        this.game = game;
    }

    {
        System.out.println("called");
        renderer.pauseScreenLoad();
        pausedTable = new Table();
        
        pausedTable.setFillParent(true);
        this.addActor(pausedTable);

        createButton(renderer.resumeDrawable).addListener(new ClickListener(){
            @Override
            public void clicked (InputEvent event, float x, float y) {
                System.out.println("clicked");
                player.toggleEscape();
            }
        });
        createButton(renderer.saveGameDrawable).addListener(new ClickListener(){
            @Override
            public void clicked (InputEvent event, float x, float y) {
                DataManager.SaveData();
            }
        });
        createButton(renderer.mainMenuDrawable).addListener(new ClickListener(){
            @Override
            public void clicked (InputEvent event, float x, float y) {
                System.out.println("clicked");
                renderer.pauseScreenDispose();
                game.setScreen(new GameScreen(game));
            }
        });

        this.setPosition(worldWidth/2, worldHeight/2);
    }

    //my methods
    private ImageButton createButton(Drawable drawable){
        ImageButton button = new ImageButton(drawable);
        pausedTable.add(button).fill().padBottom(10);
        pausedTable.row();
        return button;
    }
}
