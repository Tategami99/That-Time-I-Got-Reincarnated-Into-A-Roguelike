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

    //projectiles
    public ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
    private ArrayList<Projectile> projectilesToRemove = new ArrayList<Projectile>();
    private ArrayList<Float> projectileX = new ArrayList<Float>();
    private ArrayList<Float> projectileY = new ArrayList<Float>();


    public EntityManager(TiledMapTileLayer collisionLayer){
        this.collisionLayer = collisionLayer;

        AssetRenderer.spiderEnemyLoad();
    }

    public void render(Batch batch, float deltaTime){
        renderProjectiles(batch);
        renderNormal(batch, deltaTime);
        renderBig(batch, deltaTime);
    }

    public void update(Player player, float delta){
        updateProjectiles(delta);
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
                if(projectiles.size() > 0){
                    if(collidedWithProjectile(spider.getSpiderX(), spider.getSpiderY())){
                        spider.health -= 1;
                    }
                    if(spider.health == 0){
                        spider.dead = true;
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
                collidedWithProjectile(spider.getSpiderX(), spider.getSpiderY());
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

    private void updateProjectileStatus(Projectile projectile){
        float oldX = projectile.getOldX();
        float oldY = projectile.getOldY();
        if(!projectile.addedToPosLog){
            projectileX.add(oldX);
            projectileY.add(oldY);
            projectile.addedToPosLog = true;
        }
        if (projectile.remove){
            projectilesToRemove.add(projectile);
            projectileX.remove(projectileX.indexOf(oldX));
            projectileY.remove(projectileY.indexOf(oldY));
        }
        else{
            projectileX.set(projectileX.indexOf(oldX), projectile.getX());
            projectileY.set(projectileY.indexOf(oldY), projectile.getY());
        }
    }

    private boolean collidedWithProjectile(float enemyX, float enemyY){
        boolean collided = false;
        //left side of enemy
        for(Projectile projectile : projectiles){
            float angle =(float) -Math.toDegrees(projectile.angle);
            //check if enemy is within the size of the projectile
            
            if(projectile.getX() < enemyX && enemyX > projectile.getX() + AssetRenderer.bulletTexture.getWidth() && projectile.getY() < enemyY && enemyY > projectile.getY() + AssetRenderer.bulletTexture.getHeight()){
                projectilesToRemove.add(projectile);
                collided = true;
                break;
            }
            /*
            //30deg
            if(angle>0&&angle<=30){

            }

            //60deg
            if(angle>30&&angle<=60){

            }
            //90deg
            if(angle>60&&angle<=90){

            }
            */
        }
        return collided;
    }
}
