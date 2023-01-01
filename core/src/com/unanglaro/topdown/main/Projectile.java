package com.unanglaro.topdown.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import java.lang.Math;

public class Projectile {
    private Vector2 velocity;
    private static TextureRegion projectileTexture;
    private static AssetRenderer renderer;

    float x, y, worldWidth, worldHeight, angle;

    public boolean remove = false;

    public Projectile(float speed, float x, float y, float mouseX, float mouseY, float worldWidth, float worldHeight){
        this.x = x;
        this.y = y;
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;

        if(renderer == null){
            renderer = new AssetRenderer();
            renderer.arrowProjectileLoad();
        }

        if(projectileTexture == null){
            projectileTexture = renderer.arrowAnimation.getKeyFrame(0);
        }
        
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

        if(x < 0 || x > worldWidth || y < 0 || y > worldHeight){
            remove = true;
            System.out.println("remove called");
        }
    }

    public void render(Batch batch){
        batch.draw(projectileTexture, x, y, 0, projectileTexture.getRegionHeight(), projectileTexture.getRegionWidth(), projectileTexture.getRegionHeight(), 1, 1, (float) (90 - Math.toDegrees(angle)));
    }
}
