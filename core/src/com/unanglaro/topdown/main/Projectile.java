package com.unanglaro.topdown.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import java.lang.Math;

public class Projectile {
    private Vector2 velocity;
    private static Animation<TextureRegion> arrowAnimation;

    private float x, y, width, height, worldWidth, worldHeight, angle;
    private static float elapsedTime = 0;

    public boolean remove = false;

    public Projectile(float speed, float x, float y, float mouseX, float mouseY, float worldWidth, float worldHeight){
        System.out.println("clicked");
        arrowAnimation = AssetRenderer.arrowAnimation;
        this.x = x;
        this.y = y;
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;

        arrowAnimation = AssetRenderer.arrowAnimation;
        width = AssetRenderer.arrowTexture.getWidth()/2;
        height = AssetRenderer.arrowTexture.getHeight()/2;
        
        float diffX = mouseX*(worldWidth/Gdx.graphics.getWidth()) - x;
        float diffY = mouseY*(worldHeight/Gdx.graphics.getHeight()) - (worldHeight - y);
        angle = (float) Math.atan2(diffY, diffX);
        float velX = (float) (Math.cos(angle));
        float velY = (float) (Math.sin(angle));
        velocity = new Vector2(velX, -velY);
        velocity.nor();
        velocity.scl(speed);
    }

    public void update(float getDeltaTime){
        x += velocity.x*getDeltaTime;

        y += velocity.y*getDeltaTime;

        //detect collision within world coords
        if(x < 0 || x > worldWidth || y < 0 || y > worldHeight){// collision within world bounds
            remove = true;
        }
    }

    public void render(Batch batch){
        elapsedTime += Gdx.graphics.getDeltaTime();
        batch.draw(arrowAnimation.getKeyFrame(elapsedTime, true), x, y, 0, height, width, height, 1, 1, (float) (90 - Math.toDegrees(angle)));
    }
}
