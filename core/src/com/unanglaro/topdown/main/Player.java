package com.unanglaro.topdown.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Player extends Sprite implements InputProcessor{
    //player variables
    public Float playerSpriteWidth;
    public Float playerSpriteHeight;
    private Vector2 velocity = new Vector2();
    private Float speed = 120f;

    //game state variables
    public boolean isPaused = false;
    public float elapsedTime = 0f;

    //variables relative to player
    private PauseMenu pauseMenu;

    //variables received from game world
    private rpgGame game;
    private AssetRenderer renderer;
    private Stage stage;
    private SpriteBatch batch;
    private TiledMapTileLayer collisionLayer;

    Player(TextureRegion still, TiledMapTileLayer collisionLayer){
        super(still);
        this.collisionLayer = collisionLayer;
    }
    /*
    public void updateInput(){
        elapsedTime += Gdx.graphics.getDeltaTime();
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            System.out.println("esc pressed");
            isPaused = !isPaused;
            if(isPaused){
                stage.addActor(pauseMenu);
            }
            else{
                pauseMenu.remove();
            }
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.W)){

        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.S)){

        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.A)){

        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.D)){

        }
    }

    public void updateBatch(){
        batch.draw(renderer.playerMoveAnimation.getKeyFrame(elapsedTime, true), 50, 50, playerSpriteWidth, playerSpriteHeight*-1);
    }
    */
    public void draw(Batch spriteBatch){
        update(Gdx.graphics.getDeltaTime());
        super.draw(spriteBatch);
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
        
        //save old position
        float oldX = getX(), oldY = getY(), tileWdith = collisionLayer.getTileWidth(), tileHeight = collisionLayer.getTileHeight();
        boolean collisionX = false, collisionY = false;

        //move x
        setX(getX() + velocity.x*delta);
        if(velocity.x < 0){
            //top left tile
            if( collisionLayer.getCell((int) (getX()/tileWdith) ,(int) ((getY() + getHeight())/tileHeight)) != null){
                collisionX = collisionLayer.getCell((int) (getX()/tileWdith) ,(int) ((getY() + getHeight())/tileHeight)).getTile().getProperties().containsKey("blocked");
            }

            //middle left tile
            if(!collisionX && collisionLayer.getCell((int) (getX()/tileWdith) ,(int) ((getY() + getHeight()/2)/tileHeight)) != null){
                collisionX = collisionLayer.getCell((int) (getX()/tileWdith) ,(int) ((getY() + getHeight()/2)/tileHeight)).getTile().getProperties().containsKey("blocked");
            }

            //bottom left tile
            if(!collisionX && collisionLayer.getCell((int) (getX()/tileWdith) ,(int) (getY()/tileHeight)) != null){
                collisionX = collisionLayer.getCell((int) (getX()/tileWdith) ,(int) (getY()/tileHeight)).getTile().getProperties().containsKey("blocked");
            }

        }
        else if(velocity.x >0){
            //top right tile
            if(collisionLayer.getCell((int) ((getX() + getWidth()) / tileWdith), (int) ((getY() + getHeight()) / tileHeight)) != null){
                collisionX = collisionLayer.getCell((int) ((getX() + getWidth()) / tileWdith), (int) ((getY() + getHeight()) / tileHeight)).getTile().getProperties().containsKey("blocked");
            }
            //middle right tile
            if(!collisionX && collisionLayer.getCell((int) ((getX() + getWidth()) / tileWdith), (int) ((getY() + getWidth()/2) / tileHeight)) != null){
                collisionX = collisionLayer.getCell((int) ((getX() + getWidth()) / tileWdith), (int) ((getY() + getWidth()/2) / tileHeight)).getTile().getProperties().containsKey("blocked");
            }
            //bottom right tile
            if(!collisionX && collisionLayer.getCell((int) ((getX() + getWidth()) / tileWdith), (int) (getY() / tileHeight)) != null){
                collisionX = collisionLayer.getCell((int) ((getX() + getWidth()) / tileWdith), (int) (getY() / tileHeight)).getTile().getProperties().containsKey("blocked");
            }
        }
        //react to x collision
        if(collisionX){
            setX(oldX);
            velocity.x = 0;
        }

        //move y
        setY(getY() + velocity.y*delta);
        if(velocity.y < 0){
            //bottom left tile
            if(collisionLayer.getCell((int) (getX() / tileWdith),(int) (getY() / tileHeight)) != null){
                collisionY = collisionLayer.getCell((int) (getX() / tileWdith),(int) (getY() / tileHeight)).getTile().getProperties().containsKey("blocked");
            }
            //bottom middle tile
            if(!collisionY && collisionLayer.getCell((int) ((getX() + getWidth() / 2) / tileWdith),(int) (getY() / tileHeight)) != null){
                collisionY = collisionLayer.getCell((int) ((getX() + getWidth() / 2) / tileWdith),(int) (getY() / tileHeight)).getTile().getProperties().containsKey("blocked");
            }

            //bottom right tile
            if(!collisionY && collisionLayer.getCell((int) ((getX() + getWidth()) / tileWdith),(int) (getY() / tileHeight)) != null){
                collisionY = collisionLayer.getCell((int) ((getX() + getWidth()) / tileWdith),(int) (getY() / tileHeight)).getTile().getProperties().containsKey("blocked");
            }
        }
        else if(velocity.y >0){
            //top left tile
            if(collisionLayer.getCell((int) (getX() / tileWdith),(int) (getY() / tileHeight)) != null){
                collisionY = collisionLayer.getCell((int) (getX() / tileWdith),(int) (getY() / tileHeight)).getTile().getProperties().containsKey("blocked");
            }
            //top middle tile
            if(!collisionY && collisionLayer.getCell((int) (getX() / tileWdith),(int) ((getY() + getHeight()) / tileHeight)) != null){
                collisionY = collisionLayer.getCell((int) (getX() / tileWdith),(int) ((getY() + getHeight()) / tileHeight)).getTile().getProperties().containsKey("blocked");
            }

            //top right tile
            if(!collisionY && collisionLayer.getCell((int) ((getX() + getWidth() / 2) / tileWdith),(int) ((getY() + getHeight() / 2) / tileHeight)) != null){
                collisionY = collisionLayer.getCell((int) ((getX() + getWidth() / 2) / tileWdith),(int) ((getY() + getHeight() / 2) / tileHeight)).getTile().getProperties().containsKey("blocked");
            }
        }
        //react to y collision
        if(collisionY){
            setY(oldY);
            velocity.y = 0;
        }
        System.out.println("collisionx is: " + collisionX + " and collision y is: " + collisionY);
    }

    public TiledMapTileLayer getCollisionsLayer(){
        return collisionLayer;
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
        // TODO Auto-generated method stub
        return false;
    }
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // TODO Auto-generated method stub
        return false;
    }
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        // TODO Auto-generated method stub
        return false;
    }
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        // TODO Auto-generated method stub
        return false;
    }
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        // TODO Auto-generated method stub
        return false;
    }
    @Override
    public boolean scrolled(float amountX, float amountY) {
        // TODO Auto-generated method stub
        return false;
    }
}
