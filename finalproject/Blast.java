package finalproject;

import basicgraphics.Sprite;
import basicgraphics.SpriteComponent;
import basicgraphics.images.Picture;
import java.util.ArrayList;

/**
 *
 * @author hyprr
 */
public class Blast extends Sprite {
    
    int expandInterval = 15;
    int timer;
    int curTime = 0;
    int curBlastTime = 0;
    int curBlastIteration = 1;
    int blastFrames;
    int damage;
    int radius;
    Sprite tracking;
    double resizeDisplayAmount;
    boolean friendly;
    boolean exploding = false;
    Animation countdownAnim;
    Animation blastAnim;
    ArrayList<Sprite> spritesHit = new ArrayList<>(); 
    
    public Blast(SpriteComponent sc, Sprite tracking, int damage, int timer, int radius, double resizeDisplayAmount, boolean friendly, int effectDrawingPriority, ArrayList<Picture> countdownAnimFrames, ArrayList<Picture> blastAnimFrames) {
        super(sc);
        this.tracking = tracking;
        this.damage = damage;
        this.timer = timer;
        this.radius = radius;
        this.resizeDisplayAmount = resizeDisplayAmount;
        this.friendly = friendly;
        this.countdownAnim = new Animation(sc, countdownAnimFrames, (int) (Math.ceil(timer / 5.0)), resizeDisplayAmount, -1);
        this.blastFrames = blastAnimFrames.size();
        this.blastAnim = new Animation(sc, blastAnimFrames, expandInterval, resizeDisplayAmount, effectDrawingPriority);
    }
    
    //Sets the blast active by adding it to the blastStorage array.
    public void activate(ArrayList<Blast> arr) {
        arr.add(this);
        setX(tracking.getCenterX());
        setY(tracking.getCenterY());
    }
    
}
