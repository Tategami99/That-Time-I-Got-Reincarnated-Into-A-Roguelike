package com.unanglaro.topdown.main;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

public class SpiderEnemy{
    private static Animation<TextureRegion> spiderAnimation;
    public int health;
    public boolean dead = false;
    private float scaleAmount = 2;
    private float width, height;
    private float xPos, yPos;
    private float elapsedTime = 0f;
    private Vector2 velocity;
    private float speed;
    private TiledMapTileLayer collisionLayer;
    private float worldWidth = 1600;
    private float worldHeight = 900;

    public SpiderEnemy(TiledMapTileLayer collisionLayer, boolean big){
        this.collisionLayer = collisionLayer;

        if(spiderAnimation == null){
            AssetRenderer.spiderEnemyLoad();
            spiderAnimation = AssetRenderer.spiderAnimation;
            width = AssetRenderer.spiderTexture.getWidth()/4;
            height = AssetRenderer.spiderTexture.getHeight()/1;
        }

        if(big){
            health = 2;
            //scale(scaleAmount);
            width *= scaleAmount;
            height *= scaleAmount;
        }
        else{
            health = 1;
        }
    }

    public void render(Batch spriteBatch, float deltaTime){
        elapsedTime += deltaTime;
        spriteBatch.draw(spiderAnimation.getKeyFrame(elapsedTime, true), xPos, yPos);
    }

    public void update(float delta){
        pathfinding();

        //limit speed
        if (velocity.y > speed){
            velocity.y = speed;
        }
        else if (velocity.y == 0){
            velocity.y = 0;
        }
        else if (velocity.y < speed){
            velocity.y = -speed;
        }
        if(velocity.x > speed){
            velocity.x = speed;
        }
        else if (velocity.x == 0){
            velocity.x = 0;
        }
        else if (velocity.x < speed){
            velocity.x = -speed;
        }

        checkCollisions(delta);
    }

    private void checkCollisions(float delta){
        //save old position
        float oldX = xPos, oldY = yPos, tileWdith = collisionLayer.getTileWidth(), tileHeight = collisionLayer.getTileHeight();
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

    private void pathfinding(){

    }
}
