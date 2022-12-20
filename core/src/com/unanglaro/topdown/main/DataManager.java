package com.unanglaro.topdown.main;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DataManager {
    DataManager(){

    }

    //data
    public static String pName;
    public static int pLevel, pAttack, pDefense, pSpeed, pHealth;

    public static void SaveData(){
        try{
            FileOutputStream fos = new FileOutputStream("save.dat");
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            ObjectOutputStream oos = new ObjectOutputStream(bos);

            DataStorage dataStorage = new DataStorage();

            //data
            dataStorage.playerName = pName;
            dataStorage.playerLevel = pLevel;
            dataStorage.playerAttack = pAttack;
            dataStorage.playerDefense = pDefense;
            dataStorage.playerSpeed = pSpeed;
            dataStorage.playerHealth = pHealth;

            oos.writeObject(dataStorage);
            oos.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public static void LoadData(){
        try{
            FileInputStream fis = new FileInputStream("save.dat");
            BufferedInputStream bis = new BufferedInputStream(fis);
            ObjectInputStream ois = new ObjectInputStream(bis);

            DataStorage dataStorage = (DataStorage)ois.readObject();
            
            //data
            pName = dataStorage.playerName;
            pLevel = dataStorage.playerLevel;
            pAttack = dataStorage.playerAttack;
            pDefense = dataStorage.playerDefense;
            pSpeed = dataStorage.playerSpeed;
            pHealth = dataStorage.playerHealth;

            ois.close();
        }catch(IOException ex){
            ex.printStackTrace();
        }catch(ClassNotFoundException ex){
            ex.printStackTrace();
        }
    }
}
