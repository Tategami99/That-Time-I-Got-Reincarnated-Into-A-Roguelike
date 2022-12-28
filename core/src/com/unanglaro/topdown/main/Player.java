package com.unanglaro.topdown.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Player {
    //player variables

    //game state variables
    public boolean isPaused = false;

    //variables dependent on player
    private PauseMenu pauseMenu;

    //variables received from game world
    private rpgGame game;
    private Stage stage;

    Player(rpgGame game,Stage stage){
        this.game = game;
        this.stage = stage;

        pauseMenu = new PauseMenu(this, game);

        Gdx.input.setInputProcessor(stage);
    }
    public void update(){
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            System.out.println("esc pressed");
            isPaused = !isPaused;
            if(isPaused){
                stage.addActor(pauseMenu);
            }
            else{
                pauseMenu.remove();
            }
        }
    }
}
