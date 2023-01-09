package com.unanglaro.topdown.main;

import java.io.*;

public class DataManager {
    DataManager(){

    }

    public static class Data implements java.io.Serializable{
        public static String playerName;
        public static int playerLevel,
        playerAttack,
        playerDefense,
        playerSpeed,
        playerHealth;
    }

    public static void SaveData(){
        try{
            FileOutputStream fos = new FileOutputStream("save.dat");
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            ObjectOutputStream oos = new ObjectOutputStream(bos);

            //Data
            Data.playerName = DataStorage.playerName;
            Data.playerLevel = DataStorage.playerLevel;
            Data.playerAttack = DataStorage.playerAttack;
            Data.playerDefense = DataStorage.playerDefense;
            Data.playerSpeed = DataStorage.playerSpeed;
            Data.playerHealth = DataStorage.playerHealth;

            System.out.println(Data.playerName);

            oos.writeObject(new Data());
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

            Data data = (Data)ois.readObject();
            

            //Data
            DataStorage.playerName = Data.playerName;
            DataStorage.playerLevel = Data.playerLevel;
            DataStorage.playerAttack = Data.playerAttack;
            DataStorage.playerDefense = Data.playerDefense;
            DataStorage.playerSpeed = Data.playerSpeed;
            DataStorage.playerHealth = Data.playerHealth;

            System.out.println(DataStorage.playerName);

            ois.close();
        }catch(IOException ex){
            ex.printStackTrace();
        }catch(ClassNotFoundException ex){
            ex.printStackTrace();
        }
    }
}
