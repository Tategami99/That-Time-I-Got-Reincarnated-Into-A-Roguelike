package com.unanglaro.topdown.main;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class EntityManager {
    public TiledMapTileLayer collisionLayer;

    public ArrayList<SpiderEnemy> spidersNormal = new ArrayList<SpiderEnemy>();
    public ArrayList<SpiderEnemy> spidersNormalToRemove = new ArrayList<SpiderEnemy>();
    public ArrayList<SpiderEnemy> spidersBig = new ArrayList<SpiderEnemy>();
    public ArrayList<SpiderEnemy> spidersBigToRemove = new ArrayList<SpiderEnemy>();

    public EntityManager(TiledMapTileLayer collisionLayer){
        this.collisionLayer = collisionLayer;
    }

    public void render(Batch batch, float deltaTime){
        renderNormal(batch, deltaTime);
        renderBig(batch, deltaTime);
    }

    public void update(float delta){
        updateNormal(delta);
        updateBig(delta);
    }

    public void createEntities(int normalSpiders, int bigSpiders){
        for (int index = spidersNormal.size(); index < normalSpiders; index++) {
            spidersNormal.add(new SpiderEnemy(collisionLayer, false));
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
