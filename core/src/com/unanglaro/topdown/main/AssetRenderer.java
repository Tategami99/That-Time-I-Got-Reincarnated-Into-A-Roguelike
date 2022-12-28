package com.unanglaro.topdown.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Texture.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;

public class AssetRenderer {
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
    public void openingScreenLoad(){
        //background
        openingBackgroundTexture = new Texture("OpeningScreen/ninoDarkMode.png");
        openingBackgroundTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        openingBackgroundSprite = new Sprite(openingBackgroundTexture);
        openingBackgroundSprite.flip(false, true);
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
}
