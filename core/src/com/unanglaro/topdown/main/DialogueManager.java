package com.unanglaro.topdown.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import java.util.List;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class DialogueManager{
    private DialogueConverter converter = new DialogueConverter();
    private AssetRenderer renderer = new AssetRenderer();

    private TextButton continueButton;
    public Integer start;
    public Integer end;
    private List<String> conversation;
    private Integer speaker;
    private Integer words;
    private Boolean closeOnEnd;

    private Stage stage;
    private rpgGame game;
    private ScreenAdapter screen;
    private String action;

    DialogueManager(Stage stage, Integer start, Integer end, String action){
        System.out.println("dialogue opened");
        this.stage = stage;
        this.start = start;
        this.end = end;
        this.action = action;
        converter.convert();
        renderer.dialogueUILoad();

        closeOnEnd = false;
        speaker = 0;
        words = speaker + converter.elementsPerRow;
    }
    DialogueManager(rpgGame game, Stage stage, Integer start, Integer end, Boolean closeOnEnd, ScreenAdapter screen, String action){
        System.out.println("dialogue opened");
        this.game = game;
        this.screen = screen;
        this.stage = stage;
        this.start = start;
        this.end = end;
        this.closeOnEnd = closeOnEnd;
        this.action = action;//pass in null for nothing to happen

        converter.convert();
        renderer.dialogueUILoad();

        speaker = 0;
        words = speaker + converter.elementsPerRow;

        startConversation();
    }

    public void startConversation(){
        conversation = converter.getConversation(start, end);
        System.out.println(conversation.get(0) + "===");
        
        continueButton = new TextButton(conversation.get(speaker) + "\n" + conversation.get(words), renderer.dialogueBox, "small");
        continueButton.getLabel().setFontScale(0.75f);
        continueButton.getLabel().setWrap(true);
        continueButton.setSize(Gdx.graphics.getWidth()/3, Gdx.graphics.getHeight()/12);
        continueButton.setPosition(Gdx.graphics.getWidth()/4, Gdx.graphics.getHeight()/24);
        continueButton.setTransform(true);
        continueButton.scaleBy(0.5f);
        continueButton.addListener(new ClickListener(){
            @Override
            public void clicked (InputEvent event, float x, float y) {
                if(words + 1 < conversation.size() && conversation.get(speaker + 1).contains("player")){
                    speaker += 1;
                    words += 1;
                    continueButton.setText("hello" + "\n" + conversation.get(words));
                }
                else if(words + 1 < conversation.size()){
                    speaker += 1;
                    words += 1;
                    continueButton.setText(conversation.get(speaker) + "\n" + conversation.get(words));
                }
                else{
                    if(closeOnEnd){
                        endConversationAndClose();
                    }
                    else{
                        endConversation();
                    }
                }
            }
        });

        continueButton.setFillParent(false);

        stage.addActor(continueButton);
    }
    public void endConversation(){
        performAction();
        continueButton.remove();
    }
    public void endConversationAndClose(){
        performAction();
        game.setScreen(screen);
    }

    public void performAction(){
        if(action == "initializePlayer"){
            DataStorage.playerName = null;
            DataStorage.playerLevel = 1;
            DataStorage.playerAttack = 10;
            DataStorage.playerDefense = 10;
            DataStorage.playerSpeed = 240;
            DataStorage.playerHealth = 100;
        }
    }
}
