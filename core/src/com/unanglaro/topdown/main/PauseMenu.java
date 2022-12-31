package com.unanglaro.topdown.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;

public class PauseMenu extends Group{
    private Player player;
    private rpgGame game;

    private AssetRenderer renderer = new AssetRenderer();
    private Table pausedTable;

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
                player.isPaused = false;
                //player.updateInput();
            }
        });
        createButton(renderer.saveGameDrawable).addListener(new ClickListener(){
            @Override
            public void clicked (InputEvent event, float x, float y) {
                System.out.println("clicked and saved");
            }
        });
        createButton(renderer.mainMenuDrawable).addListener(new ClickListener(){
            @Override
            public void clicked (InputEvent event, float x, float y) {
                System.out.println("clicked");
                game.setScreen(new GameScreen(game));
            }
        });

        this.setPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
    }

    //my methods
    private ImageButton createButton(Drawable drawable){
        ImageButton button = new ImageButton(drawable);
        pausedTable.add(button).fill().padBottom(10);
        pausedTable.row();
        return button;
    }
}
