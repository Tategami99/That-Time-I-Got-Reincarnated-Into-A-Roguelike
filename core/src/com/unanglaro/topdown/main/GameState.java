package com.unanglaro.topdown.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class GameState {
    public static rpgGame game;
    public static Stage stage;
    public static World world;
    public static State state;
    public static OrthographicCamera camera;

    public static DialogueManager dialoguer;


    public static int enemiesAlive = 0;
    public static boolean pausable = true;

    public static enum World{
        MainMenu,
        Heaven,
        Overworld,
        Dungeon1
    }

    public static enum State{
        Idle,
        InitializePlayer,
        BattlingEnemies,
        Exploring,
        TransferingWorlds,
    }

    public static void detectGameState(){
        //System.out.println(world);
        if(enemiesAlive>0){
            state = State.BattlingEnemies;
        }
        switch(world){
            case MainMenu:
                mainMenu();
                break;
            case Heaven:
                heaven();
                break;
            case Overworld:
                overworld();
                break;
            case Dungeon1:
                dungeon1();
                break;
        }
    }

    public static void mainMenu(){
        state = State.Idle;
    }
    public static void heaven(){
        //System.out.println(state);
        switch(state){
            case InitializePlayer:
                DataStorage.playerName = null;
                DataStorage.playerLevel = 1;
                DataStorage.playerAttack = 10;
                DataStorage.playerDefense = 10;
                DataStorage.playerSpeed = 240;
                DataStorage.playerHealth = 100;
                state = State.TransferingWorlds;
                break;
            case TransferingWorlds:
                game.setScreen(new Overworld(game));
                break;
            default:
                break;
        }
    }
    public static void overworld(){
        switch(state){
            case BattlingEnemies:
                if(enemiesAlive > 0){
                    state = State.Exploring;
                }
                break;
            case Exploring:
                //System.out.println("being called");
                Gdx.input.setInputProcessor(stage);
                if(dialoguer == null){
                    dialoguer = new DialogueManager(stage, 10, 12, true, State.TransferingWorlds, camera.viewportWidth, camera.viewportHeight);
                }
                break;
            case TransferingWorlds:
                game.setScreen(new Dungeon1(game));
            default:
                break;

        }
    }
    public static void dungeon1(){

    }
}
