package com.unanglaro.topdown.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class DialogueManager{
    // private DialogueConverter converter = new DialogueConverter();

    // private TextButton continueButton;
    // public Integer start;
    // public Integer end;
    // private List<String> conversation;
    // private Integer speaker;
    // private Integer words;
    // private Boolean closeOnEnd;

    // private Stage stage;
    // private rpgGame game;
    // private ScreenAdapter screen;
    // private String action;

    // DialogueManager(Stage stage, Integer start, Integer end, String action){
    //     System.out.println("dialogue opened");
    //     this.stage = stage;
    //     this.start = start;
    //     this.end = end;
    //     this.action = action;
    //     converter.convert();
    //     AssetRenderer.dialogueUILoad();

    //     closeOnEnd = false;
    //     speaker = 0;
    //     words = speaker + converter.elementsPerRow;
    // }
    // DialogueManager(rpgGame game, Stage stage, Integer start, Integer end, Boolean closeOnEnd, ScreenAdapter screen, String action){
    //     System.out.println("dialogue opened");
    //     this.game = game;
    //     this.screen = screen;
    //     this.stage = stage;
    //     this.start = start;
    //     this.end = end;
    //     this.closeOnEnd = closeOnEnd;
    //     this.action = action;//pass in null for nothing to happen

    //     converter.convert();
    //     AssetRenderer.dialogueUILoad();

    //     speaker = 0;
    //     words = speaker + converter.elementsPerRow;

    //     startConversation();
    // }

    // public void startConversation(){
    //     conversation = converter.getConversation(start, end);
    //     System.out.println(conversation.get(0) + "===");
        
    //     if(conversation.get(speaker).contains("player")){
    //         continueButton = new TextButton(DataStorage.playerName + "\n" + conversation.get(words), AssetRenderer.dialogueBox, "small");
    //     }else{
    //         continueButton = new TextButton(conversation.get(speaker) + "\n" + conversation.get(words), AssetRenderer.dialogueBox, "small");
    //     }
    //     continueButton.getLabel().setFontScale(0.75f);
    //     continueButton.getLabel().setWrap(true);
    //     continueButton.setSize(Gdx.graphics.getWidth()/3, Gdx.graphics.getHeight()/12);
    //     continueButton.setPosition(Gdx.graphics.getWidth()/4, Gdx.graphics.getHeight()/24);
    //     continueButton.setTransform(true);
    //     continueButton.scaleBy(0.5f);
    //     continueButton.addListener(new ClickListener(){
    //         @Override
    //         public void clicked (InputEvent event, float x, float y) {
    //             if(words + 1 < conversation.size() && conversation.get(speaker + 1).contains("player")){
    //                 speaker += 1;
    //                 words += 1;
    //                 continueButton.setText(DataStorage.playerName + "\n" + conversation.get(words));
    //             }
    //             else if(words + 1 < conversation.size() && !conversation.get(speaker).contains("ケイデン")){
    //                 speaker += 1;
    //                 words += 1;
    //                 continueButton.setText(conversation.get(speaker) + "\n" + conversation.get(words));
    //             }
    //             else{
    //                 if(closeOnEnd){
    //                     endConversationAndClose();
    //                 }
    //                 else{
    //                     endConversation();
    //                 }
    //             }
    //         }
    //     });

    //     continueButton.setFillParent(false);

    //     stage.addActor(continueButton);
    // }
    // public void endConversation(){
    //     performAction();
    //     continueButton.remove();
    // }
    // public void endConversationAndClose(){
    //     performAction();
    //     game.setScreen(screen);
    // }

    // public void performAction(){
    //     if(action == null){
    //         return;
    //     }
    //     else if(action == "initializePlayer"){
    //         DataStorage.playerName = null;
    //         DataStorage.playerLevel = 1;
    //         DataStorage.playerAttack = 10;
    //         DataStorage.playerDefense = 10;
    //         DataStorage.playerSpeed = 240;
    //         DataStorage.playerHealth = 100;
    //     }
    // }
    private TextButton continueButton;
    private Integer start;
    private Stage stage;
    private Integer end;
    private ArrayList<String> speaker = new ArrayList<String>();
    private String player = DataStorage.playerName;
    private ArrayList<String> words = new ArrayList<String>();
    private GameState.State state;
    private boolean doSomething;
    public Integer index;
    private static boolean firstTime = true;
    private float width, height;

    public DialogueManager(Stage stage, int start, int end, boolean doSomething, GameState.State state, float width, float height){
        this.stage = stage;
        this.start = start;
        this.end = end;
        this.doSomething = doSomething;
        this.state = state;
        this.width = width;
        this.height = height;
        index = start;
        if(firstTime){
            AssetRenderer.dialogueUILoad();
            firstTime = false;
        }
        initializeList();
        startConversation();
    }

    private void startConversation(){
        continueButton = new TextButton(speaker.get(index) + "\n" + words.get(index), AssetRenderer.dialogueBox, "small");
        continueButton.getLabel().setFontScale(0.5f);
        continueButton.getLabel().setWrap(true);
        continueButton.setSize(width/3, height/12);
        continueButton.setPosition(width/4, height/24);
        continueButton.setTransform(true);
        continueButton.scaleBy(0.5f);
        continueButton.addListener(new ClickListener(){
            @Override
            public void clicked (InputEvent event, float x, float y) {
                System.out.println("clicked");
                index++;
                if(index <= end){
                    continueButton.setText(speaker.get(index) + "\n" + words.get(index));
                }
                else{
                    endConversation();
                }
            }
        });
        continueButton.setFillParent(false);
        stage.addActor(continueButton);
    }

    private void endConversation(){
        continueButton.remove();
        if(doSomething){
            GameState.state = state;
            GameState.createdConversation = false;
        }
    }

    private void initializeList(){
        //convo 1 0 to 9
        speaker.add(player);
        words.add("Where am I? Who are you?");
        speaker.add("Goddess");
        words.add("You died in a car crash and now you're here with a goddess such as myself. Although in a few minutes I'll be sending you to a new world.");
        speaker.add(player);
        words.add("So... I'm being Isekai'd?");
        speaker.add("Goddess");
        words.add("Basically. You'll be reincarnated into a world you'd typically find in a roguelike.");
        speaker.add(player);
        words.add("So if I die that will be it?");
        speaker.add("Goddess");
        words.add("Pretty much.");
        speaker.add(player);
        words.add("Do I get special abilities since I'm being reincarnated?");
        speaker.add(player);
        words.add("I was pretty skilled with guns in my previous life.");
        speaker.add("Goddess");
        words.add("Hm. Got it. You'll be able to use more guns and infuse them with magic.");
        speaker.add("Goddess");
        words.add("Now go enjoy your new life.");

        //convo 2 10 to 12
        speaker.add(player);
        words.add("Wow.");
        speaker.add(player);
        words.add("First time in this world and I get attacked right away.");
        speaker.add(player);
        words.add("Might as well explore what's up ahead.");

        //convo 3 13 to 16
        speaker.add("Kaden");
        words.add("Thank you for playing this game!");
        speaker.add("Kaden");
        words.add("I spent so long on functionality that I forgot about the fun factor.");
        speaker.add("Kaden");
        words.add("If you would like to see me work on this game more,");
        speaker.add("Kaden");
        words.add("let me know and hopefully I won't get sick this time.");
    }
}
