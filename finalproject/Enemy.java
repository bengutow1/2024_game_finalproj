package finalproject;

import basicgraphics.BasicFrame;
import basicgraphics.Sprite;
import basicgraphics.SpriteComponent;
import basicgraphics.images.Picture;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import java.util.ArrayList;

/**
 *
 * @author hyprr
 */
public class Enemy extends Sprite {

    public double speed;
    public String type;
    public int health;
    public int damage;
    public int projectileCooldown = 0;
    public int scoreGiven;
    public int xpGiven;
    public boolean slowed = false;
    public Picture currentPic;
    public Picture imgFacingDown;
    public Picture imgFacingUp;
    public Picture imgFacingRight;
    public Picture imgFacingLeft;

    public Enemy(SpriteComponent sc, String type, double speed, int health, int damage, int scoreGiven, int xpGiven) {
        super(sc);
        EnemyDifficulty em = new EnemyDifficulty();
        freezeOrientation = true;
        this.speed = speed;
        this.type = type;
        this.health = health;
        this.damage = damage;
        this.scoreGiven = scoreGiven;
        this.xpGiven = xpGiven;
        this.slowed = false;
        if (type.equals("Private")) {
            setDrawingPriority(1);
            imgFacingDown = new Picture("finalproject/marine_private_down.png");
            imgFacingDown = imgFacingDown.resize(2.0);
            imgFacingUp = new Picture("finalproject/marine_private_up.png");
            imgFacingUp = imgFacingUp.resize(2.0);
            imgFacingRight = new Picture("finalproject/marine_private_right.png");
            imgFacingRight = imgFacingRight.resize(2.0);
            imgFacingLeft = new Picture("finalproject/marine_private_left.png");
            imgFacingLeft = imgFacingLeft.resize(2.0);
        }
        if (type.equals("Marksman")) {
            setDrawingPriority(1);
            projectileCooldown = em.marksman_bulletCooldown;
            imgFacingDown = new Picture("finalproject/marine_marksman_down.png");
            imgFacingDown = imgFacingDown.resize(2.0);
            imgFacingUp = new Picture("finalproject/marine_marksman_up.png");
            imgFacingUp = imgFacingUp.resize(2.0);
            imgFacingRight = new Picture("finalproject/marine_marksman_right.png");
            imgFacingRight = imgFacingRight.resize(2.0);
            imgFacingLeft = new Picture("finalproject/marine_marksman_left.png");
            imgFacingLeft = imgFacingLeft.resize(2.0);
        }
        if (type.equals("Grenadier")) {
            setDrawingPriority(1);
            projectileCooldown = em.grenadier_grenadeCooldown;
            imgFacingDown = new Picture("finalproject/marine_grenadier_down.png");
            imgFacingDown = imgFacingDown.resize(2.0);
            imgFacingUp = new Picture("finalproject/marine_grenadier_up.png");
            imgFacingUp = imgFacingUp.resize(2.0);
            imgFacingRight = new Picture("finalproject/marine_grenadier_right.png");
            imgFacingRight = imgFacingRight.resize(2.0);
            imgFacingLeft = new Picture("finalproject/marine_grenadier_left.png");
            imgFacingLeft = imgFacingLeft.resize(2.0);
        }
        currentPic = imgFacingDown;
        setPicture(currentPic);
    }

    //Sets the velocity of an enemy towards a player; returns the distance between the enemy & player
    public double setVelTowardsPlayer(Player plyr, double speed) {
        double[] posInfo = getPosTowardsSprite(plyr, speed);
        this.setVel(posInfo[0], posInfo[1]);
        if (Math.abs(posInfo[0]) > Math.abs(posInfo[1])) {
            if (posInfo[0] < 0) {
                this.currentPic = this.imgFacingLeft;
                this.setPicture(this.imgFacingLeft);
            } else {
                this.currentPic = this.imgFacingRight;
                this.setPicture(this.imgFacingRight);
            }
        } else {
            if (posInfo[1] < 0) {
                this.currentPic = this.imgFacingUp;
                this.setPicture(this.imgFacingUp);
            } else {
                this.currentPic = this.imgFacingDown;
                this.setPicture(this.imgFacingDown);
            }
        }
        return posInfo[2];
    }

    //gets the x and y value a certain distance (or in the case of setting velocity a certain speed), away from the enemy and towards the player.
    //for the return value: 1st index is x, 2nd index is y, third index is distance. (KEEP IN MIND VALUES FOR x AND y ARE RELATIVE TO THE ENEMIES X POS)
    public double[] getPosTowardsSprite(Sprite sprite, double distance) {
        int xDirection = 1;
        int yDirection = 1;
        double xDist = sprite.getX() - this.getX();
        double yDist = sprite.getY() - this.getY();
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
        double x = Math.pow(Math.cos(theta), 2) * distance;
        double y = Math.pow(Math.sin(theta), 2) * distance;
        x = xDirection * x;
        y = yDirection * y;
        double[] ret = {x, y, Dist};
        return ret;
    }

    public void damage(int damage, Player pl, ArrayList<Enemy> enemyStorage, ArrayList<ProgressBar> barStorage, int[] DIFFICULTY_REQS) {
        health = health - damage;
        if (health <= 0) {
            ProgressBar scoreBar = barStorage.get(0);
            ProgressBar healthBar = barStorage.get(1);
            ProgressBar difficultyBar = barStorage.get(2);
            ProgressBar gear2Bar = barStorage.get(3);
            ProgressBar gear3Bar = barStorage.get(4);
            ProgressBar arnaHakiBar = barStorage.get(5);
            ProgressBar obsvHakiBar = barStorage.get(6);
            setActive(false);
            enemyStorage.remove(this);
            pl.score += scoreGiven;
            pl.gear2Progress += xpGiven;
            if (pl.gear2Progress >= 100) {
                pl.gear2Progress = 100;
                gear2Bar.setColor(new Color(255, 141, 60));
            } else if (!pl.inGear2) {
                gear2Bar.setColor(new Color(186, 77, 91));
            }
            pl.gear3Progress += xpGiven;
            if (pl.gear3Progress >= 100) {
                pl.gear3Progress = 100;
                gear3Bar.setColor(new Color(255, 141, 60));
            } else if (!pl.inGear3) {
                gear3Bar.setColor(new Color(201, 164, 138));
            }
            pl.observationHakiCharge += xpGiven;
            if (pl.observationHakiCharge > 250) {
                pl.observationHakiCharge = 250;
            }
            if (pl.observationHakiCharge >= pl.OBSV_HAKI_REQ) {
                obsvHakiBar.setColor(new Color(255, 141, 60));
            } else if (!pl.inObservationHaki) {
                obsvHakiBar.setColor(new Color(109, 115, 183));
            }
            pl.arnamentHakiCharge += xpGiven;
            if (pl.arnamentHakiCharge > 250) {
                pl.arnamentHakiCharge = 250;
            }
            if (pl.arnamentHakiCharge >= pl.ARNA_HAKI_REQ) {
                arnaHakiBar.setColor(new Color(255, 141, 60));
            } else if (!pl.inArnamentHaki) {
                arnaHakiBar.setColor(new Color(152, 117, 156));
            }
            gear2Bar.updateProgress(pl.gear2Progress, 100);
            gear3Bar.updateProgress(pl.gear3Progress, 100);
            arnaHakiBar.updateProgress(pl.arnamentHakiCharge, 250);
            obsvHakiBar.updateProgress(pl.observationHakiCharge, 250);
            scoreBar.updateProgress(pl.score, pl.score);
            //Updating difficulty
            if (pl.score >= DIFFICULTY_REQS[pl.difficulty + 1]) {
                pl.difficulty++;
                pl.health = pl.MAX_HEALTH;
                healthBar.updateProgress(pl.health, pl.MAX_HEALTH);
            }
            difficultyBar.updateProgress(pl.score - DIFFICULTY_REQS[pl.difficulty], DIFFICULTY_REQS[pl.difficulty + 1] - DIFFICULTY_REQS[pl.difficulty]);
        }
    }

}
