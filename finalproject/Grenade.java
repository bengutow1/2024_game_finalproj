package finalproject;

import basicgraphics.Sprite;
import basicgraphics.SpriteComponent;
import basicgraphics.images.Picture;
import java.util.ArrayList;

/**
 *
 * @author hyprr
 */
public class Grenade extends Sprite {
    
    public int yGain;
    public int airDuration;
    public int curAirTime;
    public Picture pic;
    public double resizeAmount;
    public Blast blast;
    public Picture groundPic;
    public double gravityAccel;
    public double maxAlt;
    public boolean landed = false;
    public int landedTimer;
    public double initialY;
    public double ySlowdown = 0.0;
    public boolean launched = false;
    Sprite tracking;
    
    public Grenade(SpriteComponent sc, double x, double y, Sprite tracking, Picture pic, Picture groundPic, double resizeAmount, Blast blast, int landedTimer, int yGain, int airDuration) {
        super(sc);
        this.groundPic = groundPic;
        this.pic = pic;
        this.resizeAmount = resizeAmount;
        this.blast = blast;
        this.blast.tracking = this;
        this.landedTimer = landedTimer;
        this.tracking = tracking;
        this.yGain = yGain;
        this.airDuration = airDuration;
        setY(y);
        setX(x);
        initialY = y;
    }
    
    //Lunches the grenade towards the sprite factoring in yGain and airDuration 
    public void launchTowards(Sprite sprite, ArrayList<Grenade> arr, ArrayList<Blast> blastArr) {
        setPicture(pic.resize(resizeAmount));
        double xDist = sprite.getX() - this.getX();
        double velX = xDist / airDuration;
        double yDist = sprite.getY() - this.getY();
        double velY = yDist / airDuration;
        blast.activate(blastArr);
        setVel(velX, velY);
        arr.add(this);
    }
    
}
