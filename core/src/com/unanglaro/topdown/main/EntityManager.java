package com.unanglaro.topdown.main;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import java.lang.Math;

public class EntityManager {
    private float spawnAreaMinX = 100;
    private float spawnAreaMaxX = 1500;
    private float spawnAreaMinY = 100;
    private float spawnAreaMaxY = 800;

    private TiledMapTileLayer collisionLayer;

    //spiders
    private ArrayList<SpiderEnemy> spidersNormal = new ArrayList<SpiderEnemy>();
    private ArrayList<SpiderEnemy> spidersNormalToRemove = new ArrayList<SpiderEnemy>();
    private ArrayList<Float> normalSpiderX = new ArrayList<Float>();
    private ArrayList<Float> normalSpiderY = new ArrayList<Float>();
    private ArrayList<SpiderEnemy> spidersBig = new ArrayList<SpiderEnemy>();
    private ArrayList<SpiderEnemy> spidersBigToRemove = new ArrayList<SpiderEnemy>();
    private ArrayList<Float> bigSpiderX = new ArrayList<Float>();
    private ArrayList<Float> bigSpiderY = new ArrayList<Float>();


    public EntityManager(TiledMapTileLayer collisionLayer){
        this.collisionLayer = collisionLayer;

        AssetRenderer.spiderEnemyLoad();
    }

    public void render(Batch batch, float deltaTime){
        renderNormal(batch, deltaTime);
        renderBig(batch, deltaTime);
    }

    public void update(float delta){
        updateNormal(delta);
        updateBig(delta);
    }

    public void createEntities(Player player, int normalSpiders, int bigSpiders){
        for (int index = 0; index < normalSpiders; index++) {
            float spawnX = (float) Math.floor(Math.random()*(spawnAreaMaxX-spawnAreaMinX+ 1) + spawnAreaMinX);
            float spawnY = (float) Math.floor(Math.random()*(spawnAreaMaxY-spawnAreaMinY+ 1) + spawnAreaMinY);
            spidersNormal.add(new SpiderEnemy(player, collisionLayer, false, spawnX, spawnY));
            System.out.println("enemy spawned");
        }
        for (int index = 0; index < bigSpiders; index++) {
            float spawnX = (float) Math.floor(Math.random()*(spawnAreaMaxX-spawnAreaMinX+ 1) + spawnAreaMinX);
            float spawnY = (float) Math.floor(Math.random()*(spawnAreaMaxY-spawnAreaMinY+ 1) + spawnAreaMinY);
            spidersBig.add(new SpiderEnemy(player, collisionLayer, true, spawnX, spawnY));
            System.out.println("x: " + spawnX + "  y: " + spawnY);
        }
    }

    private void renderNormal(Batch batch, float deltaTime){
        //normal sized spiders
        if(spidersNormal.size() > 0){
            for(SpiderEnemy spider : spidersNormal){
                spider.render(batch, deltaTime);
            }
        }
    }

    private void renderBig(Batch batch, float deltaTime){
        //big spiders
        if(spidersBig.size() > 0){
            for(SpiderEnemy spider : spidersBig){
                spider.render(batch, deltaTime);
            }
        }
    }

    private void updateNormal(float delta){
        //normal sized spiders
        if(spidersNormal.size() > 0){
            for(SpiderEnemy spider : spidersNormal){
                spider.update(delta);
                float oldX = spider.getSpiderOldX();
                //System.out.println("oldX: " + oldX);
                normalSpiderX.add(oldX);
                normalSpiderX.set(normalSpiderX.indexOf(oldX), spider.getSpiderX());
                //System.out.println("newX: " + spider.getSpiderX());
                if(spider.dead){
                    spidersNormalToRemove.add(spider);
                }
            }
            spidersNormal.removeAll(spidersNormalToRemove);
        }
    }
    
    private void updateBig(float delta){
        //big sized spiders
        if(spidersBig.size() > 0){
            for(SpiderEnemy spider : spidersBig){
                spider.update(delta);
                if(spider.dead){
                    spidersBigToRemove.add(spider);
                }
            }
            spidersBig.removeAll(spidersBigToRemove);
        }
    }
}
