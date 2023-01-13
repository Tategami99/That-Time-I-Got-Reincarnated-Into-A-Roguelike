package com.unanglaro.topdown.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class SpiderEnemy{
    private static Animation<TextureRegion> spiderAnimation;
    public int health;
    public boolean dead = false;
    private float scaleAmount = 2;
    private float width, height;
    private float oldX, oldY;
    private float xPos, yPos;
    private Player player;
    private static float playerX, playerY;
    private static TiledMapTile playerTile;
    private float elapsedTime = 0f;
    private Vector2 velocity = new Vector2();
    private float speed;
    private TiledMapTileLayer collisionLayer;
    private float worldWidth = 1600;
    private float worldHeight = 900;
    private boolean runPathfinding = true;

    public SpiderEnemy(Player player, TiledMapTileLayer collisionLayer, boolean big, float xPos, float yPos){
        this.player = player;
        this.collisionLayer = collisionLayer;
        this.xPos = xPos;
        this.yPos = yPos;

        spiderAnimation = AssetRenderer.spiderAnimation;
        width = AssetRenderer.spiderTexture.getWidth()/4;
        height = AssetRenderer.spiderTexture.getHeight()/1;

        if(big){
            health = 2;
            //scale(scaleAmount);
            width *= scaleAmount;
            height *= scaleAmount;
            speed = DataStorage.playerSpeed*1f;
        }
        else{
            health = 1;
            speed = DataStorage.playerSpeed*1.1f;
        }
    }

    public void render(Batch spriteBatch, float deltaTime){
        elapsedTime += deltaTime;
        spriteBatch.draw(spiderAnimation.getKeyFrame(elapsedTime, true), xPos, yPos, width, height);
    }

    public void update(float delta){
        pathfinding();
        checkCollisions(delta);
    }

    private void checkCollisions(float delta){
        //save old position
        oldX = xPos;
        oldY = yPos;
        float tileWdith = collisionLayer.getTileWidth(), tileHeight = collisionLayer.getTileHeight();
        boolean collisionX = false, collisionY = false;

        //move x
        xPos += velocity.x*delta;
        if(velocity.x < 0){
            //top left tile
            if( collisionLayer.getCell((int) (xPos/tileWdith) ,(int) ((yPos + height)/tileHeight)) != null){
                collisionX = collisionLayer.getCell((int) (xPos/tileWdith) ,(int) ((yPos + height)/tileHeight)).getTile().getProperties().containsKey("blocked");
            }

            //middle left tile
            if(!collisionX && collisionLayer.getCell((int) (xPos/tileWdith) ,(int) ((yPos + height/2)/tileHeight)) != null){
                collisionX = collisionLayer.getCell((int) (xPos/tileWdith) ,(int) ((yPos + height/2)/tileHeight)).getTile().getProperties().containsKey("blocked");
            }

            //bottom left tile
            if(!collisionX && collisionLayer.getCell((int) (xPos/tileWdith) ,(int) (yPos/tileHeight)) != null){
                collisionX = collisionLayer.getCell((int) (xPos/tileWdith) ,(int) (yPos/tileHeight)).getTile().getProperties().containsKey("blocked");
            }
            // left side of game world
            if(xPos + velocity.x*delta < 0){
                collisionX = true;
            }
        }
        else if(velocity.x >0){
            //top right tile
            if(collisionLayer.getCell((int) ((xPos + width) / tileWdith), (int) ((yPos + height) / tileHeight)) != null){
                collisionX = collisionLayer.getCell((int) ((xPos + width) / tileWdith), (int) ((yPos + height) / tileHeight)).getTile().getProperties().containsKey("blocked");
            }
            //middle right tile
            if(!collisionX && collisionLayer.getCell((int) ((xPos + width) / tileWdith), (int) ((yPos + width/2) / tileHeight)) != null){
                collisionX = collisionLayer.getCell((int) ((xPos + width) / tileWdith), (int) ((yPos + width/2) / tileHeight)).getTile().getProperties().containsKey("blocked");
            }
            //bottom right tile
            if(!collisionX && collisionLayer.getCell((int) ((xPos + width) / tileWdith), (int) (yPos / tileHeight)) != null){
                collisionX = collisionLayer.getCell((int) ((xPos + width) / tileWdith), (int) (yPos / tileHeight)).getTile().getProperties().containsKey("blocked");
            }
            //right side of game world
            if(xPos + width + velocity.x*delta > worldWidth){
                collisionX = true;
            }
        }
        //react to x collision
        if(collisionX){
            xPos = oldX;
            velocity.x = 0;
        }

        //move y
        yPos += velocity.y*delta;
        if(velocity.y < 0){
            //bottom left tile
            if(collisionLayer.getCell((int) (xPos / tileWdith),(int) (yPos / tileHeight)) != null){
                collisionY = collisionLayer.getCell((int) (xPos / tileWdith),(int) (yPos / tileHeight)).getTile().getProperties().containsKey("blocked");
            }
            //bottom middle tile
            if(!collisionY && collisionLayer.getCell((int) ((xPos + width / 2) / tileWdith),(int) (yPos / tileHeight)) != null){
                collisionY = collisionLayer.getCell((int) ((xPos + width / 2) / tileWdith),(int) (yPos / tileHeight)).getTile().getProperties().containsKey("blocked");
            }

            //bottom right tile
            if(!collisionY && collisionLayer.getCell((int) ((xPos + width) / tileWdith),(int) (yPos / tileHeight)) != null){
                collisionY = collisionLayer.getCell((int) ((xPos + width) / tileWdith),(int) (yPos / tileHeight)).getTile().getProperties().containsKey("blocked");
            }
            //bottom side of game world
            if(yPos + velocity.y*delta < 0){
                collisionY = true;
            }
        }
        else if(velocity.y >0){
            //top left tile
            if(collisionLayer.getCell((int) (xPos / tileWdith),(int) (yPos / tileHeight)) != null){
                collisionY = collisionLayer.getCell((int) (xPos / tileWdith),(int) (yPos / tileHeight)).getTile().getProperties().containsKey("blocked");
            }
            //top middle tile
            if(!collisionY && collisionLayer.getCell((int) (xPos / tileWdith),(int) ((yPos + height) / tileHeight)) != null){
                collisionY = collisionLayer.getCell((int) (xPos / tileWdith),(int) ((yPos + height) / tileHeight)).getTile().getProperties().containsKey("blocked");
            }

            //top right tile
            if(!collisionY && collisionLayer.getCell((int) ((xPos + width / 2) / tileWdith),(int) ((yPos + height / 2) / tileHeight)) != null){
                collisionY = collisionLayer.getCell((int) ((xPos + width / 2) / tileWdith),(int) ((yPos + height / 2) / tileHeight)).getTile().getProperties().containsKey("blocked");
            }
            //top side of game world
            if(yPos + height + velocity.y*delta > worldHeight){
                collisionY = true;
            }
        }
        //react to y collision
        if(collisionY){
            yPos = oldY;
            velocity.y = 0;
        }
    }

    public float getSpiderX(){
        return xPos;
    }
    public float getSpiderY(){
        return yPos;
    }

    private void pathfinding(){
        playerX = player.getPlayerX();
        playerY = player.getPlayerY();

        velocity.x = playerX - xPos;
        velocity.y = playerY - yPos;
        velocity.nor();
        velocity.scl(speed);
    }
}
