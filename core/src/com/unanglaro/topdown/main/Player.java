package com.unanglaro.topdown.main;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Player implements InputProcessor{
    //player variables
    private float width, height;
    private Vector2 velocity = new Vector2();
    private float speed = DataStorage.playerSpeed;
    private float scaleAmount = 0.75f;
    private float diffX, diffY, angle;
    private Animation<TextureRegion> playerAnimation;
    private float xPos, yPos;
    private Polygon collider;
    private ShapeRenderer shapeRenderer = new ShapeRenderer();

    //game state variables
    public boolean isPaused = false;
    private float elapsedTime = 0;

    //variables relative to player
    private PauseMenu pauseMenu;
    private float shootingSpeed = 300;

    //variables received from game world
    private Stage stage;
    private TiledMapTileLayer collisionLayer;
    private EntityManager entities;
    private float worldWidth = 1600;
    private float worldHeight = 960;

    Player(TiledMapTileLayer collisionLayer, rpgGame game, Stage stage, EntityManager entities, float xPos, float yPos){
        if(playerAnimation == null){
            AssetRenderer.playerLoad(4, 1, 0.075f);
            AssetRenderer.bulletProjectileLoad();
            AssetRenderer.playerItemsLoad();
            playerAnimation = AssetRenderer.playerMoveAnimation;
            width = AssetRenderer.playerMoveTexture.getWidth()/4;
            height = AssetRenderer.playerMoveTexture.getHeight()/1;
        }
        this.collisionLayer = collisionLayer;
        this.stage = stage;
        this.entities = entities;
        this.xPos = xPos;
        this.yPos = yPos;

        pauseMenu = new PauseMenu(this, game);
        collider = new Polygon();
        
        //scale(scaleAmount);
        width = width*scaleAmount;
        height = height*scaleAmount;

        float monitorX = xPos*(Gdx.graphics.getWidth()/worldWidth);
        float monitorY = yPos*(Gdx.graphics.getHeight()/worldHeight);
        collider = new Polygon(new float[]{monitorX, monitorY, monitorX + width, monitorY, monitorX + width, monitorY + height, monitorX, monitorY + height});
        collider.setOrigin((monitorX + width)/2, (monitorY + height)/2);
    }
    
    public void draw(Batch spriteBatch, float deltaTime){
        update(deltaTime);
        //System.out.println("x: " + xPos + " y: " + yPos);
        elapsedTime += deltaTime;
        spriteBatch.draw(AssetRenderer.playerMoveAnimation.getKeyFrame(elapsedTime, true), xPos, yPos);
        spriteBatch.draw(AssetRenderer.playerBowTextureRegion, xPos - width/2, yPos + height/2, AssetRenderer.playerBowTextureRegion.getRegionWidth()/2, 0, AssetRenderer.playerBowTextureRegion.getRegionWidth(), AssetRenderer.playerBowTextureRegion.getRegionHeight(), 1, 1,(float) (90 - Math.toDegrees(angle)));
    }

    public void update(float delta){
        //clamp velocity
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

    //debugging only
    public void updateNonRender(){
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.polygon(collider.getTransformedVertices());
        shapeRenderer.end();
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
        float monitorX = xPos*(Gdx.graphics.getWidth()/worldWidth);
        float monitorY = yPos*(Gdx.graphics.getHeight()/worldHeight);
        collider.setVertices(new float[]{monitorX, monitorY, monitorX + width, monitorY, monitorX + width, monitorY + height, monitorX, monitorY + height});
        collider.setOrigin((monitorX + width)/2, (monitorY + height)/2);
    }

    public float getPlayerX(){
        return xPos;
    }
    public float getPlayerY(){
        return yPos;
    }

    @Override
    public boolean keyDown(int keycode) {
        switch(keycode){
            case Keys.W:
                velocity.y = speed;
                break;
            case Keys.S:
                velocity.y = -speed;
                break;
            case Keys.A:
                velocity.x = -speed;
                break;
            case Keys.D:
                velocity.x = speed;
        }
        return true;
    }
    @Override
    public boolean keyUp(int keycode) {
        switch(keycode){
            case Keys.ESCAPE:
                toggleEscape();
                break;
            case Keys.W:
                velocity.y = 0;
                break;
            case Keys.S:
                velocity.y = 0;
                break;
            case Keys.A:
                velocity.x = 0;
                break;
            case Keys.D:
                velocity.x = 0;
        }
        return true;
    }
    @Override
    public boolean keyTyped(char character) {
        return false;
    }
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        switch(button){
            case Buttons.LEFT:
                //System.out.println("mouseX is: " + screenX + " mouseY is: " + screenY + " x is: " + xPos + " y is: " + yPos);
                entities.projectiles.add(new Projectile(shootingSpeed, xPos + (width/2), yPos - (height/2), screenX, screenY, collisionLayer));
        }
        return false;
    }
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        // System.out.println("x: " + screenX + " y: " + screenY);
        diffX = screenX*(worldWidth/Gdx.graphics.getWidth()) - xPos;
        diffY = screenY*(worldHeight/Gdx.graphics.getHeight()) - (worldHeight - yPos);
        angle = (float) Math.atan2(diffY, diffX);
        return false;
    }
    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
    
    //
    public void toggleEscape(){
        isPaused = !isPaused;
        if(!isPaused){
            Gdx.input.setInputProcessor(stage);
            stage.addActor(pauseMenu);
        }else{
            Gdx.input.setInputProcessor(this);
            pauseMenu.remove();
        }
    }
}
