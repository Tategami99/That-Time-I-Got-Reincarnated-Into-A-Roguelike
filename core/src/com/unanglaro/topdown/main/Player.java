package com.unanglaro.topdown.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Player extends Entity implements InputProcessor{
    //player variables
    private float width, height;
    private Vector2 velocity = new Vector2();
    private float speed = DataStorage.playerSpeed;
    private float scaleAmount = 0.75f;
    private float diffX, diffY, angle;
    private Animation<TextureRegion> playerAnimation;

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
    public float worldWidth;
    public float worldHeight;

    Player(TiledMapTileLayer collisionLayer, rpgGame game, Stage stage, float worldWidth, float worldHeight, EntityManager entities, float xPos, float yPos){
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
        setX(xPos);
        setY(yPos);
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;

        pauseMenu = new PauseMenu(this, game);
        
        //scale(scaleAmount);
        width = width*scaleAmount;
        setWidth(width);
        height = height*scaleAmount;
        setHeight(height);
    }
    
    public void draw(Batch spriteBatch, float deltaTime){
        update(deltaTime);
        //System.out.println("x: " + getX() + " y: " + getY());
        elapsedTime += deltaTime;
        spriteBatch.draw(AssetRenderer.playerMoveAnimation.getKeyFrame(elapsedTime, true), getX(), getY());
        spriteBatch.draw(AssetRenderer.playerBowTextureRegion, getX() + width/2, getY() + height/2, AssetRenderer.playerBowTextureRegion.getRegionWidth(), 0, AssetRenderer.playerBowTextureRegion.getRegionWidth(), AssetRenderer.playerBowTextureRegion.getRegionHeight(), 0.5f, 0.5f,(float) (90 - Math.toDegrees(angle)));
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
    }

    private void checkCollisions(float delta){
        //save old position
        float oldX = getX(), oldY = getY(), tileWdith = collisionLayer.getTileWidth(), tileHeight = collisionLayer.getTileHeight();
        boolean collisionX = false, collisionY = false;

        //move x
        setX(getX()+velocity.x*delta);
        //float monitorX = getX()*(Gdx.graphics.getWidth()/worldWidth);
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
        //react to x collision
        if(collisionX){
            setX(oldX);
            //monitorX = getX()*(Gdx.graphics.getWidth()/worldWidth);
            velocity.x = 0;
        }

        //move y
        setY(getY()+velocity.y*delta);
        //float monitorY = Gdx.graphics.getHeight() - (getY()*(Gdx.graphics.getHeight()/worldHeight));
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
        //react to y collision
        if(collisionY){
            setY(oldY);
            //monitorY = Gdx.graphics.getHeight() - (getY()*(Gdx.graphics.getHeight()/worldHeight));
            velocity.y = 0;
        }
        // collider.setPosition(monitorX, monitorY);
        //collider.setPosition(getX(), getY());
        //System.out.println("cx: " + collider.getX() + "cy: " + collider.getY());
        // collider.setVertices(new float[]{monitorX, monitorY, monitorX + width, monitorY, monitorX + width, monitorY + height, monitorX, monitorY + height});
        // collider.setOrigin((monitorX + width)/2, (monitorY + height)/2);
    }

    public float getPlayerX(){
        return getX();
    }
    public float getPlayerY(){
        return getY();
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
                //System.out.println("mouseX is: " + screenX + " mouseY is: " + screenY + " x is: " + getX() + " y is: " + getY());
                entities.projectiles.add(new Projectile(shootingSpeed, getX() + (width/2), getY() + (height/2), screenX, screenY, collisionLayer, worldWidth, worldHeight));
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
        diffX = screenX*(worldWidth/Gdx.graphics.getWidth()) - getX();
        diffY = screenY*(worldHeight/Gdx.graphics.getHeight()) - (worldHeight - getY());
        angle = (float) Math.atan2(diffY, diffX);
        System.out.println("x: " + screenX + " y: " + screenY);
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
