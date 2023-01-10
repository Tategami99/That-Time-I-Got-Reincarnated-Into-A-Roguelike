package com.unanglaro.topdown.main;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Player extends Sprite implements InputProcessor{
    //player variables
    private float width, height;
    private float colliderX, colliderY;
    private Vector2 velocity = new Vector2();
    private float speed = DataStorage.playerSpeed;
    private float scaleAmount = 0.75f;
    private float diffX, diffY, angle;

    //game state variables
    public boolean isPaused = false;

    //variables relative to player
    private PauseMenu pauseMenu;
    private ArrayList<Projectile> projectiles;
    private ArrayList<Projectile> projectilesToRemove;
    private float shootingSpeed = 300;

    //variables received from game world
    private Stage stage;
    private TiledMapTileLayer collisionLayer;
    private float worldWidth = 1600;
    private float worldHeight = 960;

    Player(TextureRegion still, TiledMapTileLayer collisionLayer, rpgGame game, Stage stage){
        super(still);
        this.collisionLayer = collisionLayer;
        this.stage = stage;

        pauseMenu = new PauseMenu(this, game);
        projectiles = new ArrayList<Projectile>();
        projectilesToRemove = new ArrayList<Projectile>();

        AssetRenderer.playerItemsLoad();
        
        scale(scaleAmount);
        width = getWidth()*scaleAmount;
        height = getHeight()*scaleAmount;
        colliderX = width;
        colliderY = height*0.25f;
    }

    public void updateNonRender(){
        //update arrows
        if (projectiles.size() > 0){
            for(Projectile projectile : projectiles){
                projectile.update(Gdx.graphics.getDeltaTime());
                if (projectile.remove){
                    projectilesToRemove.add(projectile);
                }
            }
            projectiles.removeAll(projectilesToRemove);
        }
    }
    
    public void draw(Batch spriteBatch){
        update(Gdx.graphics.getDeltaTime());
        //System.out.println("x: " + getX() + " y: " + getY());
        super.draw(spriteBatch);
        spriteBatch.draw(AssetRenderer.playerBowTextureRegion, getX() - width/2, getY() + height/2, AssetRenderer.playerBowTextureRegion.getRegionWidth()/2, 0, AssetRenderer.playerBowTextureRegion.getRegionWidth(), AssetRenderer.playerBowTextureRegion.getRegionHeight(), 1, 1,(float) (90 - Math.toDegrees(angle)));

        //render arrows
        if (projectiles.size() > 0){
            for (Projectile projectile : projectiles){
                projectile.render(spriteBatch);
            }
        }
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
            velocity.y = 0;
        }
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
                projectiles.add(new Projectile(shootingSpeed, getX() + (width/2), getY() - (height/2), screenX, screenY, worldWidth, worldHeight));
        }
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
        // System.out.println("x: " + screenX + " y: " + screenY);
        diffX = screenX*(worldWidth/Gdx.graphics.getWidth()) - getX();
        diffY = screenY*(worldHeight/Gdx.graphics.getHeight()) - (worldHeight - getY());
        angle = (float) Math.atan2(diffY, diffX);
        return false;
    }
    @Override
    public boolean scrolled(float amountX, float amountY) {
        // TODO Auto-generated method stub
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
