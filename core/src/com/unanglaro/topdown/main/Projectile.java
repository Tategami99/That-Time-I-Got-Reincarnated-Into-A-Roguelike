package com.unanglaro.topdown.main;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.lang.Math;

public class Projectile {
    private Vector2 velocity = new Vector2();
    private static TextureRegion projectileTexture;
    private static AssetRenderer renderer;

    float speed, x, y, mouseX, mouseY, worldWidth, worldHeight;

    public boolean remove = false;

    public Projectile(float speed, float x, float y, float mouseX, float mouseY, float worldWidth, float worldHeight){
        this.speed = speed;
        this.x = x;
        this.y = y;
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;

        if(renderer == null){
            renderer = new AssetRenderer();
            renderer.arrowProjectileLoad();
        }

        if(projectileTexture == null){
            projectileTexture = renderer.arrowAnimation.getKeyFrame(0);
        }

        float xComponent, yComponent;

        if(mouseX - x == 0){
            xComponent = 0;
        }
        else{
            xComponent = ((mouseX - x)/50)*speed;
        }
        
        if(mouseY - y == 0){
            yComponent = 0;
        }
        else{
            yComponent = ((mouseY - y)/30)*-speed;
        }

        velocity = new Vector2(xComponent,yComponent);

        System.out.println("mouseX is: " + mouseX + " mouseY is: " + mouseY + " x is: " + x + " y is: " + y + " velocityX is: " + velocity.x + " velocityY is: " + velocity.y);
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
        batch.draw(projectileTexture, x, y);
    }
}
