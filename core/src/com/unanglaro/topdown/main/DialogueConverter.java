package com.unanglaro.topdown.main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DialogueConverter {
    public static BufferedReader br;
    public static List<String> dialogue = new ArrayList<>();

    DialogueConverter(){

    }
    public static String filePath = "assets/Dialogue/dialogue.csv";
    public static String line;

    public static void convert(){
        try{
            br = new BufferedReader(new FileReader(filePath));
            while((line = br.readLine()) != null){
                String[] values = line.split(",");
                //System.out.println(values[1] + "++++");
                for(int i=0; i<values.length; i++){
                    for (String string : values[i].split("\n")) {
                        dialogue.add(string);
                        //System.out.println(string + "===");
                    }
                }
            }
            //System.out.println(dialogue.size());
            System.out.println("worked");
        } catch(FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        } finally{
            try{
                if (br != null){
                    br.close();
                }
            } catch(IOException e){
                e.printStackTrace();
            }
        }
    }
    public static List<String> getConversation(int start, int finish){
        List<String> convo = dialogue.subList(start, finish);
        return convo;
    }
}

