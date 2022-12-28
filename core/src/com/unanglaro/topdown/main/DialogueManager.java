package com.unanglaro.topdown.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import java.util.List;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class DialogueManager extends Group{
    private DialogueConverter converter = new DialogueConverter();
    private AssetRenderer renderer = new AssetRenderer();

    private TextButton continueButton;
    public Integer start;
    public Integer end;
    private List<String> conversation;
    private Integer speaker;
    private Integer words;

    private Stage stage;

    DialogueManager(Stage stage){
        System.out.println("dialogue opened");
        this.stage = stage;

        converter.convert();
        renderer.dialogueUILoad();

        speaker = 0;
        words = speaker + converter.elementsPerRow;
    }
    DialogueManager(Stage stage, Integer start, Integer end){
        System.out.println("dialogue opened");
        this.stage = stage;
        this.start = start;
        this.end = end;

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
                System.out.println("clicked");
                speaker += 1;
                words += 1;
                System.out.println(conversation.get(speaker).length());
                if(words < conversation.size() && conversation.get(speaker).contains("player")){
                    continueButton.setText("hello" + "\n" + conversation.get(words));
                }
                else if(words < conversation.size()){
                    continueButton.setText(conversation.get(speaker) + "\n" + conversation.get(words));
                }
                else{
                    System.out.println("end");
                }
            }
        });

        continueButton.setFillParent(false);

        this.addActor(continueButton);
        stage.addActor(continueButton);
    }
    public void endConversation(){
        this.remove();
    }
}
