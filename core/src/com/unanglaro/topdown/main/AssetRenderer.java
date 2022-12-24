package com.unanglaro.topdown.main;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Texture.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;

public class AssetRenderer {
    //main menu stuff
    public static Texture backgroundTexture;
    public static Sprite spriteBackground;
    public static Texture newGameTexture;
    public static Drawable newGameDrawable;
    public static Texture loadGameTexture;
    public static Drawable loadGameDrawable;
    public static Texture quitTexture;
    public static Drawable quitDrawable;
    

    public static void mainMenuLoad(){
        backgroundTexture = new Texture("MainMenu/background.png");
        backgroundTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        spriteBackground = new Sprite(backgroundTexture, 0, 0, 1920, 1080);
        spriteBackground.flip(false, true);
        newGameTexture = new Texture("MainMenu/newgame.png");
        newGameDrawable = new TextureRegionDrawable(new TextureRegion(newGameTexture));
        loadGameTexture = new Texture("MainMenu/loadgame.png");
        loadGameDrawable = new TextureRegionDrawable(new TextureRegion(loadGameTexture));
        quitTexture = new Texture("MainMenu/quit.png");
        quitDrawable = new TextureRegionDrawable(new TextureRegion(quitTexture));

    }
}
