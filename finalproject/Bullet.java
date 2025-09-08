package finalproject;

import basicgraphics.BasicFrame;
import basicgraphics.Sprite;
import basicgraphics.SpriteCollisionEvent;
import basicgraphics.SpriteComponent;
import basicgraphics.images.Picture;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

/**
 *
 * @author bgutow1
 */
public class Bullet extends Sprite {
    
    boolean friendly;
    int size;
    int damage;
    double speed;
    Color color;
    Picture curPic;
    public int deathCooldown;
    public int deathCooldownMax;
    public boolean setOffBlastYet = false;
    public boolean inDeathCooldown = false;
    public boolean hasDeathCooldown = false;
    public boolean slowed = false;
    public boolean inGear2 = false;
    public boolean inArna = false;

    public static Picture makeBall(Color color, int size) {
        Image im = BasicFrame.createImage(size, size);
        Graphics g = im.getGraphics();
        g.setColor(color);
        g.fillOval(0, 0, size, size);
        return new Picture(im);
    }
    
    public Bullet(SpriteComponent sc, Color color, int size, int damage, double speed, boolean friendly) {
        super(sc);
        this.friendly = friendly;
        this.size = size;
        this.color = color;
        this.damage = damage;
        this.speed = speed;
        setPicture(makeBall(color, size));
    }
    
    public Bullet(SpriteComponent sc, Picture pic, double resizeAmount, int damage, double speed, boolean friendly) {
        super(sc);
        freezeOrientation = true;
        this.friendly = friendly;
        this.damage = damage;
        this.speed = speed;
        curPic = pic;
        curPic = curPic.resize(resizeAmount);
        setPicture(curPic);
    }
    
    @Override
    public void processEvent(SpriteCollisionEvent se) {
        setActive(false);
    }
    
    //Sets the velocity of a bullet towards a target; returns the distance between the target & player
    public double setVelTowards(Sprite target, double speed) {
        int xDirection = 1;
        int yDirection = 1;
        double xDist = target.getX() - this.getX();
        double yDist = target.getY() - this.getY();
        if (xDist < 0) {
            xDirection = -1;
        }
        if (yDist < 0) {
            yDirection = -1;
        }
        xDist = Math.abs(xDist);
        yDist = Math.abs(yDist);
        double Dist = Math.sqrt(Math.pow(xDist, 2) + Math.pow(yDist, 2));
        double theta = Math.atan(yDist / xDist);
        double velX = Math.pow(Math.cos(theta), 2) * speed;
        double velY = Math.pow(Math.sin(theta), 2) * speed;
        velX = xDirection * velX;
        velY = yDirection * velY;
        this.setVel(velX, velY);
        return Dist;
    }
    
    //Adds a death cooldown to the bullet.
    public void addDeathCooldown(int cooldownMax) {
        hasDeathCooldown = true;
        deathCooldownMax = cooldownMax;
    }
    
    //Sets the deathCooldown to a bullet as opposed to killing it. For bigger bullets, this will allow them to do damage to a little more enemies.
    public void setDeathCooldown(int cooldown) {
        if (!inDeathCooldown) {
            inDeathCooldown = true;
            deathCooldown = cooldown;
        }
    }
}
