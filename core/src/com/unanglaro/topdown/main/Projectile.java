package com.unanglaro.topdown.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import java.lang.Math;

public class Projectile extends Entity{
    private Vector2 velocity;
    private static TextureRegion bulletTexture;

    private float oldX, oldY, width, height;
    private float scaleAmount = 1;
    public float angle;
    private float worldWidth;
    private float worldHeight;
    private TiledMapTileLayer collisionLayer;

    //collisions

    public boolean remove = false;
    public boolean addedToPosLog = false;

    public Projectile(int projectileAttack, float speed, float xPos, float yPos, float mouseX, float mouseY, TiledMapTileLayer collisionLayer, float worldWidth, float worldHeight){
        setX(xPos);
        setY(yPos);
        this.collisionLayer = collisionLayer;
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;

        attack = projectileAttack;

        bulletTexture = AssetRenderer.bulletTextureRegion;
        width = AssetRenderer.bulletTexture.getWidth()/2*scaleAmount;
        setWidth(width);
        height = AssetRenderer.bulletTexture.getHeight()/2*scaleAmount;
        setHeight(height);
        
        float diffX = mouseX*(worldWidth/Gdx.graphics.getWidth()) - getX();
        float diffY = mouseY*(worldHeight/Gdx.graphics.getHeight()) - (worldHeight - getY());
        angle = (float) Math.atan2(diffY, diffX);
        float velX = (float) (Math.cos(angle));
        float velY = (float) (Math.sin(angle));
        velocity = new Vector2(velX, -velY);
        velocity.nor();
        velocity.scl(speed);
    }

    public void update(float getDeltaTime){
        checkCollisions(getDeltaTime);
    }

    public void render(Batch batch){
        batch.draw(bulletTexture, getX(), getY(), 0, height, width, height, scaleAmount, scaleAmount, (float) (90 - Math.toDegrees(angle)));
    }

    private void checkCollisions(float delta){
        //save old position
        oldX = getX();
        oldY = getY();
        float tileWdith = collisionLayer.getTileWidth(), tileHeight = collisionLayer.getTileHeight();
        boolean collisionX = false, collisionY = false;

        //move getX()
        setX(getX()+velocity.x*delta);
        if(velocity.x < 0){
            //top left tile
            if( collisionLayer.getCell((int) (getX()/tileWdith) ,(int) ((getY() + height)/tileHeight)) != null){
                collisionX = collisionLayer.getCell((int) (getX()/tileWdith) ,(int) ((getY() + height)/tileHeight)).getTile().getProperties().containsKey("blocked");
            }

            //middle left tile
            if(!collisionX && collisionLayer.getCell((int) (getX()/tileWdith) ,(int) ((getY() + height/2)/tileHeight)) != null){
                collisionX = collisionLayer.getCell((int) (getX()/tileWdith) ,(int) ((getY() + height/2)/tileHeight)).getTile().getProperties().containsKey("blocked");
            }

            //bottom left tile
            if(!collisionX && collisionLayer.getCell((int) (getX()/tileWdith) ,(int) (getY()/tileHeight)) != null){
                collisionX = collisionLayer.getCell((int) (getX()/tileWdith) ,(int) (getY()/tileHeight)).getTile().getProperties().containsKey("blocked");
            }
            // left side of game world
            if(getX() + velocity.x*delta < 0){
                collisionX = true;
            }
        }
        else if(velocity.x >0){
            //top right tile
            if(collisionLayer.getCell((int) ((getX() + width) / tileWdith), (int) ((getY() + height) / tileHeight)) != null){
                collisionX = collisionLayer.getCell((int) ((getX() + width) / tileWdith), (int) ((getY() + height) / tileHeight)).getTile().getProperties().containsKey("blocked");
            }
            //middle right tile
            if(!collisionX && collisionLayer.getCell((int) ((getX() + width) / tileWdith), (int) ((getY() + width/2) / tileHeight)) != null){
                collisionX = collisionLayer.getCell((int) ((getX() + width) / tileWdith), (int) ((getY() + width/2) / tileHeight)).getTile().getProperties().containsKey("blocked");
            }
            //bottom right tile
            if(!collisionX && collisionLayer.getCell((int) ((getX() + width) / tileWdith), (int) (getY() / tileHeight)) != null){
                collisionX = collisionLayer.getCell((int) ((getX() + width) / tileWdith), (int) (getY() / tileHeight)).getTile().getProperties().containsKey("blocked");
            }
            //right side of game world
            if(getX() + width + velocity.x*delta > worldWidth){
                collisionX = true;
            }
        }
        //react to getX() collision
        if(collisionX){
            //SgetY()stem.out.println("getX(): " + getX() + "  getY(): " + getY());
            remove = true;
        }

        //move getY()
        setY(getY()+velocity.y*delta);
        if(velocity.y < 0){
            //bottom left tile
            if(collisionLayer.getCell((int) (getX() / tileWdith),(int) (getY() / tileHeight)) != null){
                collisionY = collisionLayer.getCell((int) (getX() / tileWdith),(int) (getY() / tileHeight)).getTile().getProperties().containsKey("blocked");
            }
            //bottom middle tile
            if(!collisionY && collisionLayer.getCell((int) ((getX() + width / 2) / tileWdith),(int) (getY() / tileHeight)) != null){
                collisionY = collisionLayer.getCell((int) ((getX() + width / 2) / tileWdith),(int) (getY() / tileHeight)).getTile().getProperties().containsKey("blocked");
            }

            //bottom right tile
            if(!collisionY && collisionLayer.getCell((int) ((getX() + width) / tileWdith),(int) (getY() / tileHeight)) != null){
                collisionY = collisionLayer.getCell((int) ((getX() + width) / tileWdith),(int) (getY() / tileHeight)).getTile().getProperties().containsKey("blocked");
            }
            //bottom side of game world
            if(getY() + velocity.y*delta < 0){
                collisionY = true;
            }
        }
        else if(velocity.y >0){
            //top left tile
            if(collisionLayer.getCell((int) (getX() / tileWdith),(int) (getY() / tileHeight)) != null){
                collisionY = collisionLayer.getCell((int) (getX() / tileWdith),(int) (getY() / tileHeight)).getTile().getProperties().containsKey("blocked");
            }
            //top middle tile
            if(!collisionY && collisionLayer.getCell((int) (getX() / tileWdith),(int) ((getY() + height) / tileHeight)) != null){
                collisionY = collisionLayer.getCell((int) (getX() / tileWdith),(int) ((getY() + height) / tileHeight)).getTile().getProperties().containsKey("blocked");
            }

            //top right tile
            if(!collisionY && collisionLayer.getCell((int) ((getX() + width / 2) / tileWdith),(int) ((getY() + height / 2) / tileHeight)) != null){
                collisionY = collisionLayer.getCell((int) ((getX() + width / 2) / tileWdith),(int) ((getY() + height / 2) / tileHeight)).getTile().getProperties().containsKey("blocked");
            }
            //top side of game world
            if(getY() + height + velocity.y*delta > worldHeight){
                collisionY = true;
            }
        }
        //react to getY() collision
        if(collisionY){
            //SgetY()stem.out.println("getX(): " + getX() + "  getY(): " + getY());
            remove = true;
        }

    }

    /*
    public float[] getPoints(){
        float bottomLeft, bottomRight, topLeft, topRight, center;
        float[] points = {bottomLeft, bottomRight, topLeft, topRight, center};
        return points;
    }
    */
}
