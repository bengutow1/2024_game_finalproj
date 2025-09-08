package finalproject;

import basicgraphics.BasicFrame;
import basicgraphics.Sprite;
import basicgraphics.SpriteComponent;
import basicgraphics.images.Picture;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 *
 * @author hyprr
 */
public class ProgressBar extends Sprite {

    int width;
    int height;
    int borderWidth;
    int currentProgress;
    int maxProgress;
    String title;
    Color progressBarColor;
    Color progressBarBorderColor = Color.black;
    Picture image;
    
    public ProgressBar(SpriteComponent sc, int x, int y, int width, int height, int borderWidth, Color progressBarColor, String title, int currentProgress, int maxProgress) {
        super(sc);
        setDrawingPriority(50);
        this.width = width;
        this.height = height;
        setX(x);
        setY(y);
        this.progressBarColor = progressBarColor;
        this.borderWidth = borderWidth;
        this.title = title;
        updateProgress(currentProgress, maxProgress);
        
    }
    
    public void updateProgress(int cur, int max) {
        Image im = BasicFrame.createImage(width, height);
        Graphics g = im.getGraphics();
        g.setColor(progressBarBorderColor);
        g.fillRect(0,0, width, height);
        g.setColor(Color.white);
        g.fillRect(borderWidth, borderWidth, width - (borderWidth * 2), height - (borderWidth * 2));
        g.setColor(progressBarColor);
        g.fillRect(borderWidth, borderWidth, (int) (((1.0 * cur) / max) *(width - (borderWidth * 2))), height - (borderWidth * 2));
        g.setColor(Color.black);
        g.setFont(new Font("Serif", Font.PLAIN, height / 3));
        int fontLoc = (int) (height / 2);
        //Special cases for text:
        if (title.equals("Gear 2") || title.equals("Gear 3") || title.equals("Arna. Haki") || title.equals("Obsv. Haki")) {
            g.setFont(new Font("Serif", Font.PLAIN, (int) (height / 2)));
            fontLoc = (int) (height / 1.5);
        }
        if (title.equals("Score")) {
            g.drawString(title + ": " + cur, 10, (fontLoc));
        } else if (title.equals("Difficulty")) {
            if (max >= 214748200) {
                 g.drawString(title + ": MAX", 50, (fontLoc));
            } else {
                g.drawString(title + ": " + cur + "/" + max, 50, (fontLoc));
            }
            //pixel art for this image (based on difficulty level) should be 40x40 pixels vvvv
            if (max == 1000) {
                g.drawImage(new Picture("finalproject/difficulty_easy.png").getImage(), 5, 5, image);
            } else if (max == 5000) {
                 g.drawImage(new Picture("finalproject/difficulty_medium.png").getImage(), 5, 5, image);
            } else {
                g.drawImage(new Picture("finalproject/difficulty_hard.png").getImage(), 5, 5, image);
            }
        } else {
            g.drawString(title + ": " + cur + "/" + max, 10, (fontLoc));
        }
        //System.out.println("Original Rect: x=" + x + ", y=" + y + ", width=" + width + ", height=" + height);
        //System.out.println("Secondary Rect: x=" + (x + borderWidth) + ", y=" + (y + borderWidth) + ", width=" + (width - (borderWidth * 8)) + ", height=" + (height - (borderWidth * 8)));
        image = new Picture(im);
        setPicture(image);
    }
    
    public void setColor(Color color) {
        this.progressBarColor = color;
    }
    
    public void setBorderColor(Color color) {
        this.progressBarBorderColor = color;
    }
    
}
