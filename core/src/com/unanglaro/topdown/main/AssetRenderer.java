package com.unanglaro.topdown.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Texture.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.*;

public class AssetRenderer {
    //testing
    public Texture testingTexture;
    public Sprite testingSprite;

    //main menu stuff
    public Texture backgroundTexture;
    public Sprite spriteBackground;
    public Texture newGameTexture;
    public Drawable newGameDrawable;
    public Texture loadGameTexture;
    public Drawable loadGameDrawable;
    public Texture quitTexture;
    public Drawable quitDrawable;

    //opening screen stuff
    public Texture openingBackgroundTexture;
    public Sprite openingBackgroundSprite;

    //option screen stuff
    public Texture resumeTexture;
    public Drawable resumeDrawable;
    public Texture saveGameTexture;
    public Drawable saveGameDrawable;
    public Texture mainMenuTexture;
    public Drawable mainMenuDrawable;
    
    //dialogue stuff
    public Skin dialogueBox;
    public BitmapFont font = new BitmapFont();

    //player stuff
    public Texture playerMoveTexture;
    public Animation<TextureRegion> playerMoveAnimation;

    //overworld stuff
    public TiledMap overworldMap;
    public OrthogonalTiledMapRenderer overworldRenderer;
    MapProperties overworldMapProperties;

    //arrow projectile stuff
    public Texture arrowTexture;
    public Animation<TextureRegion> arrowAnimation;

  AssetRenderer(){
        
    }  

    public void testingLoad(){
        testingTexture = new Texture("MainMenu/nino6.jpg");
        testingTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        testingSprite = new Sprite(testingTexture);
        testingSprite.flip(false, true);
    }
    public void testingDispose(){
        testingTexture.dispose();
    }

    public void mainMenuLoad(){
        //background
        backgroundTexture = new Texture("MainMenu/background.png");
        backgroundTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        spriteBackground = new Sprite(backgroundTexture);
        spriteBackground.flip(false, true);

        //new game button
        newGameTexture = new Texture("MainMenu/newgame.png");
        newGameDrawable = new TextureRegionDrawable(new TextureRegion(newGameTexture));

        //load game button
        loadGameTexture = new Texture("MainMenu/loadgame.png");
        loadGameDrawable = new TextureRegionDrawable(new TextureRegion(loadGameTexture));

        //quit game button
        quitTexture = new Texture("MainMenu/quit.png");
        quitDrawable = new TextureRegionDrawable(new TextureRegion(quitTexture));

    }
    public void mainMenuDispose(){
        backgroundTexture.dispose();
        newGameTexture.dispose();
        loadGameTexture.dispose();
        quitTexture.dispose();
    }

    public void openingScreenLoad(){
        //background
        openingBackgroundTexture = new Texture("OpeningScreen/ninoDarkMode.png");
        openingBackgroundTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        openingBackgroundSprite = new Sprite(openingBackgroundTexture);
        openingBackgroundSprite.flip(false, true);
    }
    public void openingScreenDispose(){
        openingBackgroundTexture.dispose();
    }

    public void pauseScreenLoad(){
        //resume button
        resumeTexture = new Texture("UserInterface/resume.png");
        resumeDrawable = new TextureRegionDrawable(new TextureRegion(resumeTexture));

        //save game button
        saveGameTexture = new Texture("UserInterface/savegame.png");
        saveGameDrawable = new TextureRegionDrawable(new TextureRegion(saveGameTexture));

        //main menu button
        mainMenuTexture = new Texture("UserInterface/mainmenu.png");
        mainMenuDrawable = new TextureRegionDrawable(new TextureRegion(mainMenuTexture));
    }
    public void pauseScreenDispose(){
        resumeTexture.dispose();
        saveGameTexture.dispose();
        mainMenuTexture.dispose();
    }

    public void dialogueUILoad(){
        //background
        dialogueBox = new Skin(Gdx.files.internal("skin/glassy-ui.json"));
    }
    
    public void playerLoad(Integer FRAME_COLS, Integer FRAME_ROWS, Float frameInterval){
        playerMoveTexture = new Texture("Spritesheets/playermove.png");
        playerMoveAnimation = createAnimation(playerMoveTexture, FRAME_COLS, FRAME_ROWS, frameInterval);
    }
    public void playerDispose(){
        playerMoveTexture.dispose();
    }

    public void overworldLoadShow(){
        overworldMap = new TmxMapLoader().load("assets/Tiled/Overworld.tmx");
        overworldRenderer = new OrthogonalTiledMapRenderer(overworldMap);
        overworldMapProperties = overworldMap.getProperties();
    }
    public void overworldDispose(){
        overworldMap.dispose();
        overworldRenderer.dispose();
    }
    public void arrowProjectileLoad(){
        arrowTexture = new Texture("Projectiles/arrow.png");
        arrowAnimation = createAnimation(arrowTexture, 2, 2, 0.05f);
    }

    // non loading methods
    public Animation<TextureRegion> createAnimation( Texture texture, Integer FRAME_COLS, Integer FRAME_ROWS, Float frameInterval){
        TextureRegion[][] tmp = TextureRegion.split(texture, texture.getWidth()/FRAME_COLS, texture.getHeight()/FRAME_ROWS);
        TextureRegion[] frames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++){
            for (int j = 0; j < FRAME_COLS; j++){
                frames[index++] = tmp[i][j];
            }
        }

        Animation<TextureRegion> animation = new Animation<TextureRegion>(frameInterval, frames);
        return animation;
    }
}
