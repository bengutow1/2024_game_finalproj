package finalproject;

import basicgraphics.Sprite;
import basicgraphics.SpriteCollisionEvent;
import basicgraphics.SpriteComponent;
import basicgraphics.Task;
import basicgraphics.images.Picture;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;

/**
 *
 * @author bgutow1
 */
public class Player extends Sprite {
    
    final public double INITIAL_SPEED = 2.3;
    final public int INITIAL_BULLET_COOLDOWN = 20;
    final public double INITIAL_BULLET_SPEED = 5;
    final public double INITIAL_BULLET_DAMAGE = 50;
    final double INITIAL_BULLET_RESIZE_AMOUNT = 1.0;
    final public int MAX_HEALTH = 100;
    final int PLAYER_DAMAGE_COOLDOWN = 100;
    final int GEAR_2_REQ = 100;
    final int GEAR_2_USE_SPEED = 10;
    final int GEAR_3_REQ = 100;
    final int GEAR_3_USE_SPEED = 3;
    final int GEAR_3_BULLET_DEATH_COOLDOWN = 5;
    final int OBSV_HAKI_REQ = 100;
    final int OBSV_HAKI_USE_SPEED = 6;
    final double OBSV_HAKI_SLOWDOWN_MULTIPLIER = 4.0;
    final int ARNA_HAKI_REQ = 50;
    final int ARNA_HAKI_USE_SPEED = 6;
    final int ARNA_HAKI_BLAST_DAMAGE = 50;
    final int ARNA_HAKI_BLAST_RADIUS = 110;
    final double ARNA_HAKI_BLAST_RESIZE = 10.0;
    
    public double speed = INITIAL_SPEED;
    public double bulletSpeed = INITIAL_BULLET_SPEED;
    public double bulletResizeAmount = INITIAL_BULLET_RESIZE_AMOUNT;
    public int bulletCooldownMax = INITIAL_BULLET_COOLDOWN;
    public double bulletDamage = INITIAL_BULLET_DAMAGE;
    public int health = MAX_HEALTH;
    public int score = 0;
    public boolean alive = true;
    public int difficulty = 0;
    public int gear2Progress;
    public boolean inGear2 = false;
    public int gear3Progress;
    public boolean inGear3 = false;
    public int arnamentHakiCharge = 0;
    public boolean inArnamentHaki = false;
    public int observationHakiCharge = 0;
    public boolean inObservationHaki = false;
    
    public Picture imgFacingDown;
    public Picture imgFacingUp;
    public Picture imgFacingRight;
    public Picture imgFacingLeft;
    public Picture currentPic;
    public boolean leftBorderCollision = false;
    public boolean rightBorderCollision = false;
    public boolean upBorderCollision = false;
    public boolean downBorderCollision = false;
    public int bulletCooldown = 0;
    public int gettingDamagedCooldown = 0;

    public Player(SpriteComponent sc) {
        super(sc);
        setDrawingPriority(0);
        freezeOrientation = true;
        imgFacingDown = new Picture("finalproject/luffy_down.png");
        imgFacingDown = imgFacingDown.resize(2.0);
        imgFacingUp = new Picture("finalproject/luffy_up.png");
        imgFacingUp = imgFacingUp.resize(2.0);
        imgFacingRight = new Picture("finalproject/luffy_right.png");
        imgFacingRight = imgFacingRight.resize(2.0);
        imgFacingLeft = new Picture("finalproject/luffy_left.png");
        imgFacingLeft = imgFacingLeft.resize(2.0);
        currentPic = imgFacingDown;

        Dimension d = sc.getPreferredSize();
        setX(d.width / 2 - (currentPic.getWidth() / 2));
        setY(d.height / 2 - (currentPic.getHeight() / 2));

        setPicture(currentPic);

    }

    public void processEvent(SpriteCollisionEvent sce) {
        SpriteComponent sc = getSpriteComponent();
        Dimension d = sc.getPreferredSize();
        if (getX() < 0) {
            setX(0);
            setVel(0, getVelY());
            leftBorderCollision = true;
        }
        if (getX() > d.width - currentPic.getWidth()) {
            setX(d.width - currentPic.getWidth());
            setVel(0, getVelY());
            rightBorderCollision = true;
        }
        if (getY() < 0) {
            setY(0);
            setVel(getVelX(), 0);
            upBorderCollision = true;
        }
        if (getY() > d.height - currentPic.getHeight()) {
            setY(d.height - currentPic.getHeight());
            setVel(getVelX(), 0);
            downBorderCollision = true;
        }
    }
    
    //Should be called after re-lowering player speed to ensure the player is not still being carried in one direction.
    public void resetVelocity() {
        if (Math.abs(getVelX()) > INITIAL_SPEED) {
            setVel((getVelX() / Math.abs(getVelX())) * INITIAL_SPEED, getVelY());
        }
        if (Math.abs(getVelY()) > INITIAL_SPEED) {
            setVel(getVelX(), (getVelY() / Math.abs(getVelY())) * INITIAL_SPEED);
        }
    }
}
