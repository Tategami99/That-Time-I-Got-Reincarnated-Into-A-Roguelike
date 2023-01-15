package com.unanglaro.topdown.main;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.FloatArray;

import java.lang.Math;

public class EntityManager {
    private float spawnAreaMinX = 100;
    private float spawnAreaMaxX = 1500;
    private float spawnAreaMinY = 100;
    private float spawnAreaMaxY = 800;

    //player
    private Player player;

    //game variables
    private TiledMapTileLayer collisionLayer;
    private rpgGame game;
    private Stage stage;
    private float worldWidth;
    private float worldHeight;

    //spiders
    private ArrayList<SpiderEnemy> spidersNormal = new ArrayList<SpiderEnemy>();
    private ArrayList<SpiderEnemy> spidersNormalToRemove = new ArrayList<SpiderEnemy>();
    private ArrayList<SpiderEnemy> spidersBig = new ArrayList<SpiderEnemy>();
    private ArrayList<SpiderEnemy> spidersBigToRemove = new ArrayList<SpiderEnemy>();

    //projectiles
    public ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
    private ArrayList<Projectile> projectilesToRemove = new ArrayList<Projectile>();


    public EntityManager(TiledMapTileLayer collisionLayer, rpgGame game, Stage stage, float worldWidth, float worldHeight){
        this.collisionLayer = collisionLayer;
        this.game = game;
        this.stage = stage;
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;

        AssetRenderer.bulletProjectileLoad();
        AssetRenderer.spiderEnemyLoad();
    }

    public void render(Batch batch, float deltaTime){
        player.render(batch, deltaTime);
        renderProjectiles(batch);
        renderNormal(batch, deltaTime);
        renderBig(batch, deltaTime);
    }

    public void update(float delta){
        player.update(delta);
        updateProjectiles(delta);
        updateNormal(delta);
        updateBig(delta);
    }

    public void createEntities(int normalSpiders, int bigSpiders){
        player = new Player(collisionLayer, game, stage, worldWidth, worldHeight, this, 20 * collisionLayer.getTileWidth(), 20 * collisionLayer.getTileHeight());
        Gdx.input.setInputProcessor(player);
        for (int index = 0; index < normalSpiders; index++) {
            float spawnX = (float) Math.floor(Math.random()*(spawnAreaMaxX-spawnAreaMinX+ 1) + spawnAreaMinX);
            float spawnY = (float) Math.floor(Math.random()*(spawnAreaMaxY-spawnAreaMinY+ 1) + spawnAreaMinY);
            spidersNormal.add(new SpiderEnemy(player, collisionLayer, false, spawnX, spawnY));
            System.out.println("enemy spawned");
            GameState.enemiesAlive += 1;
        }
        for (int index = 0; index < bigSpiders; index++) {
            float spawnX = (float) Math.floor(Math.random()*(spawnAreaMaxX-spawnAreaMinX+ 1) + spawnAreaMinX);
            float spawnY = (float) Math.floor(Math.random()*(spawnAreaMaxY-spawnAreaMinY+ 1) + spawnAreaMinY);
            spidersBig.add(new SpiderEnemy(player, collisionLayer, true, spawnX, spawnY));
            GameState.enemiesAlive += 1;
        }
    }

    public void dispose(){
        AssetRenderer.playerDispose();
        AssetRenderer.playerItemsDispose();
        AssetRenderer.spiderEnemyDispose();
        AssetRenderer.bulletProjectileDispose();
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
        //player
        if(player.health <= 0){
            System.out.println("died");
        }
        //normal sized spiders
        if(spidersNormal.size() > 0){
            for(SpiderEnemy spider : spidersNormal){
                spider.update(delta);
                if(projectiles.size() > 0){
                    if(spider.health <= 0){
                        spider.dead = true;
                        GameState.enemiesAlive -= 1;
                    }
                }
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
                if(projectiles.size() > 0){
                    if(spider.health <= 0){
                        spider.dead = true;
                        GameState.enemiesAlive -= 1;
                    }
                }
                if(spider.dead){
                    spidersBigToRemove.add(spider);
                }
            }
            spidersBig.removeAll(spidersBigToRemove);
        }
    }

    private void updateProjectiles(float delta){
        if (projectiles.size() > 0){
            for(Projectile projectile : projectiles){
                projectile.update(delta);
                if(collidedWithEnemy(projectile)){
                    projectile.remove = true;
                }
                if(projectile.remove){
                    projectilesToRemove.add(projectile);
                }
            }
            projectiles.removeAll(projectilesToRemove);
        }
    }

    private void renderProjectiles(Batch batch){
        if (projectiles.size() > 0){
            for (Projectile projectile : projectiles){
                projectile.render(batch);
            }
        }
    }

    private boolean collidedWithEnemy(Entity entity){
        boolean collided = false;
        for(Entity enemy : spidersNormal){
            float x = enemy.getX();
            float y = enemy.getY();
            float width = enemy.getWidth();
            float height = enemy.getHeight();
            float px = entity.getX();
            float pw = entity.getWidth();
            float py = entity.getY();
            float ph = entity.getHeight();
            collided = x < px + pw && x + width > px && y < py + ph && y + height > py;
            if(collided){
                if(entity.isPlayer){
                    entity.health -= enemy.attack;
                    System.out.println("Player Health: " + entity.health);
                }
                else{
                    enemy.health -= entity.attack;
                    System.out.println("hit and health is " + enemy.health);
                }
            }
        }
        return collided;
    }
}
