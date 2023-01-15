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
    public static Texture testingTexture;
    public static Sprite testingSprite;

    //main menu stuff
    public static Texture backgroundTexture;
    public static Sprite spriteBackground;
    public static Texture newGameTexture;
    public static Drawable newGameDrawable;
    public static Texture loadGameTexture;
    public static Drawable loadGameDrawable;
    public static Texture quitTexture;
    public static Drawable quitDrawable;

    //opening screen stuff
    public static Texture openingBackgroundTexture;
    public static Sprite openingBackgroundSprite;

    //option screen stuff
    public static Texture resumeTexture;
    public static Drawable resumeDrawable;
    public static Texture saveGameTexture;
    public static Drawable saveGameDrawable;
    public static Texture mainMenuTexture;
    public static Drawable mainMenuDrawable;
    
    //dialogue stuff
    public static Skin dialogueBox;
    public static BitmapFont font = new BitmapFont();

    //player stuff
    public static Texture playerMoveTexture;
    public static Animation<TextureRegion> playerMoveAnimation;
    
    //player items stuff
    public static Texture playerBowTexture;
    public static TextureRegion playerBowTextureRegion;

    //overworld stuff
    public static TiledMap overworldMap;
    public static OrthogonalTiledMapRenderer overworldRenderer;
    public static MapProperties overworldMapProperties;

    //dungeon1 stuff
    public static TiledMap dungeon1Map;
    public static OrthogonalTiledMapRenderer dungeon1Renderer;
    public static MapProperties dungeon1MapProperties;

    //bullet projectile stuff
    public static Texture bulletTexture;
    public static TextureRegion bulletTextureRegion;
    //spider enemy stuff
    public static Texture spiderTexture;
    public static Animation<TextureRegion> spiderAnimation;

  public AssetRenderer(){
        
    }  

    public static void testingLoad(){
        testingTexture = new Texture("MainMenu/nino6.jpg");
        testingTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        testingSprite = new Sprite(testingTexture);
        testingSprite.flip(false, true);
    }
    public static void testingDispose(){
        testingTexture.dispose();
    }

    public static void mainMenuLoad(){
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
    public static void mainMenuDispose(){
        backgroundTexture.dispose();
        newGameTexture.dispose();
        loadGameTexture.dispose();
        quitTexture.dispose();
    }

    public static void openingScreenLoad(){
        //background
        openingBackgroundTexture = new Texture("OpeningScreen/ninoDarkMode.png");
        openingBackgroundTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        openingBackgroundSprite = new Sprite(openingBackgroundTexture);
        openingBackgroundSprite.flip(false, true);
    }
    public static void openingScreenDispose(){
        openingBackgroundTexture.dispose();
    }

    public static void pauseScreenLoad(){
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
    public static void pauseScreenDispose(){
        resumeTexture.dispose();
        saveGameTexture.dispose();
        mainMenuTexture.dispose();
    }

    public static void dialogueUILoad(){
        //background
        dialogueBox = new Skin(Gdx.files.internal("skin/glassy-ui.json"));
    }
    
    public static void playerLoad(Integer FRAME_COLS, Integer FRAME_ROWS, Float frameInterval){
        playerMoveTexture = new Texture("Spritesheets/playermove.png");
        playerMoveAnimation = createAnimation(playerMoveTexture, FRAME_COLS, FRAME_ROWS, frameInterval);
    }
    public static void playerDispose(){
        playerMoveTexture.dispose();
    }

    public static void playerItemsLoad(){
        playerBowTexture = new Texture("Projectiles/pistol.png");
        playerBowTextureRegion = new TextureRegion(playerBowTexture);
        playerBowTextureRegion.flip(false, false);
    }
    public static void playerItemsDispose(){
        playerBowTexture.dispose();
    }

    public static void overworldLoadShow(){
        overworldMap = new TmxMapLoader().load("assets/Tiled/Overworld.tmx");
        overworldRenderer = new OrthogonalTiledMapRenderer(overworldMap);
        overworldMapProperties = overworldMap.getProperties();
    }
    public static void overworldDispose(){
        overworldMap.dispose();
        overworldRenderer.dispose();
    }
    public static void dungeon1LoadShow(){
        dungeon1Map = new TmxMapLoader().load("assets/Tiled/Dungeon1.tmx");
        dungeon1Renderer = new OrthogonalTiledMapRenderer(dungeon1Map);
        dungeon1MapProperties = dungeon1Map.getProperties();
    }
    public static void dungeon1Dispose(){
        dungeon1Map.dispose();
        dungeon1Renderer.dispose();
    }
    public static void bulletProjectileLoad(){
        bulletTexture = new Texture("Projectiles/bullet.png");
        bulletTextureRegion = new TextureRegion(bulletTexture);
    }
    public static void bulletProjectileDispose(){
        bulletTexture.dispose();
    }
    public static void spiderEnemyLoad(){
        spiderTexture = new Texture("Spritesheets/spidermove.png");
        spiderAnimation = createAnimation(spiderTexture, 4, 1, 0.1f);
    }
    public static void spiderEnemyDispose(){
        spiderTexture.dispose();
    }

    // non loading methods
    public static Animation<TextureRegion> createAnimation( Texture texture, Integer FRAME_COLS, Integer FRAME_ROWS, Float frameInterval){
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
