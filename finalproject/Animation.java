package finalproject;

import basicgraphics.Sprite;
import basicgraphics.SpriteComponent;
import basicgraphics.images.Picture;
import java.util.ArrayList;

/**
 *
 * @author hyprr
 */
public class Animation extends Sprite{
    
    ArrayList<Picture> frames = new ArrayList<>();
    int frameInterval;
    int currentFrameIndex = 0;
    boolean animationActive = false;
    int currentTick = 0;
    
    public Animation(SpriteComponent sc, ArrayList<Picture> frames, int frameInterval, double resizeAmount, int drawingPriority) {
        super(sc);
        setDrawingPriority(drawingPriority);
        this.frameInterval = frameInterval;
        for (Picture curPic : frames) {
            curPic = curPic.resize(resizeAmount);
            this.frames.add(curPic);
        }
        setActive(false);
    }
    
    public void nextFrame() {
        if (currentFrameIndex + 1 >= frames.size()) {
            setActive(false);
            animationActive = false;
            return;
        }
        setPicture(frames.get(++currentFrameIndex));
    }
    
    public Animation animate(double x, double y, ArrayList<Animation> animationStorage) {
        animationActive = true;
        animationStorage.add(this);
        setPicture(frames.get(0));
        setCenterX(x);
        setCenterY(y);
        setActive(true);
        return this;
    }
}
