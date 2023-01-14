package com.unanglaro.topdown.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import java.lang.Math;

public class Projectile{
    private Vector2 velocity;
    private static TextureRegion bulletTexture;

    private float x, y, oldX, oldY, width, height;
    public float angle;
    private float worldWidth = 1600;
    private float worldHeight = 900;
    private TiledMapTileLayer collisionLayer;

    //collisions
    private Polygon collider;
    private ShapeRenderer shapeRenderer = new ShapeRenderer();

    public boolean remove = false;
    public boolean addedToPosLog = false;

    public Projectile(float speed, float x, float y, float mouseX, float mouseY, TiledMapTileLayer collisionLayer){
        this.x = x;
        this.y = y;
        this.collisionLayer = collisionLayer;

        bulletTexture = AssetRenderer.bulletTextureRegion;
        width = AssetRenderer.bulletTexture.getWidth()/2;
        height = AssetRenderer.bulletTexture.getHeight()/2;
        
        float diffX = mouseX*(worldWidth/Gdx.graphics.getWidth()) - x;
        float diffY = mouseY*(worldHeight/Gdx.graphics.getHeight()) - (worldHeight - y);
        angle = (float) Math.atan2(diffY, diffX);
        float velX = (float) (Math.cos(angle));
        float velY = (float) (Math.sin(angle));
        velocity = new Vector2(velX, -velY);
        velocity.nor();
        velocity.scl(speed);
        float monitorX = x*(Gdx.graphics.getWidth()/worldWidth);
        float monitorY = y*(Gdx.graphics.getHeight()/worldHeight);
        collider = new Polygon(new float[]{monitorX, monitorY, monitorX + width, monitorY, monitorX + width, monitorY + height, monitorX, monitorY + height});
        collider.setOrigin((monitorX + width)/2, (monitorY + height)/2);
        //collider.setRotation((float) (90 - Math.toDegrees(angle)));
        //System.out.println(Math.toDegrees(angle));
    }

    public void update(float getDeltaTime){
        x += velocity.x*getDeltaTime;
        y += velocity.y*getDeltaTime;

        checkCollisions(getDeltaTime);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.polygon(collider.getTransformedVertices());
        shapeRenderer.end();
    }

    public void render(Batch batch){
        batch.draw(bulletTexture, x, y, 0, height, width, height, 1, 1, (float) (90 - Math.toDegrees(angle)));
    }

    private void checkCollisions(float delta){
        //save old position
        oldX = x;
        oldY = y;
        float tileWdith = collisionLayer.getTileWidth(), tileHeight = collisionLayer.getTileHeight();
        boolean collisionX = false, collisionY = false;

        //move x
        x += velocity.x*delta;
        if(velocity.x < 0){
            //top left tile
            if( collisionLayer.getCell((int) (x/tileWdith) ,(int) ((y + height)/tileHeight)) != null){
                collisionX = collisionLayer.getCell((int) (x/tileWdith) ,(int) ((y + height)/tileHeight)).getTile().getProperties().containsKey("blocked");
            }

            //middle left tile
            if(!collisionX && collisionLayer.getCell((int) (x/tileWdith) ,(int) ((y + height/2)/tileHeight)) != null){
                collisionX = collisionLayer.getCell((int) (x/tileWdith) ,(int) ((y + height/2)/tileHeight)).getTile().getProperties().containsKey("blocked");
            }

            //bottom left tile
            if(!collisionX && collisionLayer.getCell((int) (x/tileWdith) ,(int) (y/tileHeight)) != null){
                collisionX = collisionLayer.getCell((int) (x/tileWdith) ,(int) (y/tileHeight)).getTile().getProperties().containsKey("blocked");
            }
            // left side of game world
            if(x + velocity.x*delta < 0){
                collisionX = true;
            }
        }
        else if(velocity.x >0){
            //top right tile
            if(collisionLayer.getCell((int) ((x + width) / tileWdith), (int) ((y + height) / tileHeight)) != null){
                collisionX = collisionLayer.getCell((int) ((x + width) / tileWdith), (int) ((y + height) / tileHeight)).getTile().getProperties().containsKey("blocked");
            }
            //middle right tile
            if(!collisionX && collisionLayer.getCell((int) ((x + width) / tileWdith), (int) ((y + width/2) / tileHeight)) != null){
                collisionX = collisionLayer.getCell((int) ((x + width) / tileWdith), (int) ((y + width/2) / tileHeight)).getTile().getProperties().containsKey("blocked");
            }
            //bottom right tile
            if(!collisionX && collisionLayer.getCell((int) ((x + width) / tileWdith), (int) (y / tileHeight)) != null){
                collisionX = collisionLayer.getCell((int) ((x + width) / tileWdith), (int) (y / tileHeight)).getTile().getProperties().containsKey("blocked");
            }
            //right side of game world
            if(x + width + velocity.x*delta > worldWidth){
                collisionX = true;
            }
        }
        //react to x collision
        if(collisionX){
            //System.out.println("x: " + x + "  y: " + y);
            remove = true;
        }

        //move y
        y += velocity.y*delta;
        if(velocity.y < 0){
            //bottom left tile
            if(collisionLayer.getCell((int) (x / tileWdith),(int) (y / tileHeight)) != null){
                collisionY = collisionLayer.getCell((int) (x / tileWdith),(int) (y / tileHeight)).getTile().getProperties().containsKey("blocked");
            }
            //bottom middle tile
            if(!collisionY && collisionLayer.getCell((int) ((x + width / 2) / tileWdith),(int) (y / tileHeight)) != null){
                collisionY = collisionLayer.getCell((int) ((x + width / 2) / tileWdith),(int) (y / tileHeight)).getTile().getProperties().containsKey("blocked");
            }

            //bottom right tile
            if(!collisionY && collisionLayer.getCell((int) ((x + width) / tileWdith),(int) (y / tileHeight)) != null){
                collisionY = collisionLayer.getCell((int) ((x + width) / tileWdith),(int) (y / tileHeight)).getTile().getProperties().containsKey("blocked");
            }
            //bottom side of game world
            if(y + velocity.y*delta < 0){
                collisionY = true;
            }
        }
        else if(velocity.y >0){
            //top left tile
            if(collisionLayer.getCell((int) (x / tileWdith),(int) (y / tileHeight)) != null){
                collisionY = collisionLayer.getCell((int) (x / tileWdith),(int) (y / tileHeight)).getTile().getProperties().containsKey("blocked");
            }
            //top middle tile
            if(!collisionY && collisionLayer.getCell((int) (x / tileWdith),(int) ((y + height) / tileHeight)) != null){
                collisionY = collisionLayer.getCell((int) (x / tileWdith),(int) ((y + height) / tileHeight)).getTile().getProperties().containsKey("blocked");
            }

            //top right tile
            if(!collisionY && collisionLayer.getCell((int) ((x + width / 2) / tileWdith),(int) ((y + height / 2) / tileHeight)) != null){
                collisionY = collisionLayer.getCell((int) ((x + width / 2) / tileWdith),(int) ((y + height / 2) / tileHeight)).getTile().getProperties().containsKey("blocked");
            }
            //top side of game world
            if(y + height + velocity.y*delta > worldHeight){
                collisionY = true;
            }
        }
        //react to y collision
        if(collisionY){
            //System.out.println("x: " + x + "  y: " + y);
            remove = true;
        }

        updateCollider();
    }

    public float getOldX(){
        return oldX;
    }
    public float getOldY(){
        return oldY;
    }
    public float getX(){
        return x;
    }
    public float getY(){
        return y;
    }
    /*
    public float[] getPoints(){
        float bottomLeft, bottomRight, topLeft, topRight, center;
        float[] points = {bottomLeft, bottomRight, topLeft, topRight, center};
        return points;
    }
    */
    private void updateCollider(){
        float conversionFactor = (Gdx.graphics.getWidth()/worldWidth);
        float bottomLeftX = 0f;
        float bottomLeftY = 0f;
        float bottomRightX = 0f;
        float bottomRightY = 0f;
        float topRightX = 0f;
        float topRightY = 0f;
        float topLeftX = 0f;
        float topLeftY = 0f;

        
        collider.setVertices(new float[]{bottomLeftX, bottomLeftY, bottomRightX, bottomRightY, topRightX, topRightY, topLeftX, topLeftY});
        collider.setOrigin(0, height);
        System.out.println("x: " + x + " cx: " + collider.getX());
    }
}
