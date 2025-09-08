package finalproject;

import basicgraphics.BasicContainer;
import basicgraphics.BasicFrame;
import basicgraphics.ClockWorker;
import basicgraphics.Sprite;
import basicgraphics.SpriteComponent;
import basicgraphics.SpriteSpriteCollisionListener;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JLabel;
import basicgraphics.Task;
import basicgraphics.images.Picture;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author bgutow1
 */
public class Shooter {

    public static void main(String[] args) {

        final int FRAME_WIDTH = 600;
        final int FRAME_HEIGHT = 600;

        //Difficulty names (just for personal reference): easy, normal
        final int[] DIFFICULTY_REQS = {0, 1000, 6000, 2147483647};
        final int[] DIFFICULTY_MAX_ENEMIES = {4, 6, 8, 500};
        final int[][] DIFFICULTY_SPAWNRATES_RATES = {{0, 700}, {0, 550, 900}, {0, 450, 800}};
        final String[][] DIFFICULTY_SPAWNRATES_TYPES = {
            {"Private", "Marksman"},
            {"Private", "Marksman", "Grenadier"},
            {"Private", "Marksman", "Grenadier"}
        };

        final Picture background800 = new Picture("finalproject/desert_background.png").resize(4.0);
        final Picture background600 = new Picture("finalproject/desert_background.png").resize(3.0);
        
        final ArrayList<Picture> punchHitFrames = new ArrayList<>(Arrays.asList(new Picture("finalproject/punch_effect_frame1.png"), new Picture("finalproject/punch_effect_frame2.png"), new Picture("finalproject/punch_effect_frame3.png"), new Picture("finalproject/punch_effect_frame4.png")));
        final ArrayList<Picture> bloodEffectFrames = new ArrayList<>(Arrays.asList(new Picture("finalproject/blood_effect_frame1.png"), new Picture("finalproject/blood_effect_frame2.png"), new Picture("finalproject/blood_effect_frame3.png"), new Picture("finalproject/blood_effect_frame4.png")));
        final ArrayList<Picture> gear2PunchHitFrames = new ArrayList<>(Arrays.asList(new Picture("finalproject/gear2_punch_effect_frame1.png"), new Picture("finalproject/gear2_punch_effect_frame2.png"), new Picture("finalproject/gear2_punch_effect_frame3.png"), new Picture("finalproject/gear2_punch_effect_frame4.png")));
        final ArrayList<Picture> arnaPunchHitFrames = new ArrayList<>(Arrays.asList(new Picture("finalproject/arna_punch_effect_frame1.png"), new Picture("finalproject/arna_punch_effect_frame2.png"), new Picture("finalproject/arna_punch_effect_frame3.png"), new Picture("finalproject/arna_punch_effect_frame4.png")));
        final ArrayList<Picture> blastCountdownFrames = new ArrayList<>(Arrays.asList(new Picture("finalproject/blast_effect_frame5.png"), new Picture("finalproject/blast_effect_frame4.png"), new Picture("finalproject/blast_effect_frame3.png"), new Picture("finalproject/blast_effect_frame2.png"), new Picture("finalproject/blast_effect_frame1.png")));
        final ArrayList<Picture> blastDefaultFrames = new ArrayList<>(Arrays.asList(new Picture("finalproject/blast_effect_frame1.png"), new Picture("finalproject/blast_effect_frame2.png"), new Picture("finalproject/blast_effect_frame3.png"), new Picture("finalproject/blast_effect_frame4.png"), new Picture("finalproject/blast_effect_frame5.png")));
        final ArrayList<Picture> blastExplodeFrames = new ArrayList<>(Arrays.asList(new Picture("finalproject/explode_effect_frame1.png"), new Picture("finalproject/explode_effect_frame2.png"), new Picture("finalproject/explode_effect_frame3.png"), new Picture("finalproject/explode_effect_frame4.png"), new Picture("finalproject/explode_effect_frame5.png")));

        ArrayList<Animation> animationStorage = new ArrayList<>();
        ArrayList<Enemy> enemyStorage = new ArrayList<>();
        ArrayList<Bullet> bulletStorage = new ArrayList<>();
        ArrayList<Blast> blastStorage = new ArrayList<>();
        ArrayList<Grenade> grenadeStorage = new ArrayList<>();

        //Setting up basic graphics functions
        BasicFrame bf = new BasicFrame("Shooter");
        SpriteComponent sc = new SpriteComponent() {
            @Override
            public void paintBackground(Graphics g) {
                Dimension d = getSize();
                if (d.height == 800 && d.width == 800) {
                    g.drawImage(background800.getImage(), 0, 0, null);
                } else {
                    g.drawImage(background600.getImage(), 0, 0, null);
                }
            }
        };
        Dimension dim = new Dimension(FRAME_WIDTH, FRAME_HEIGHT);
        sc.setPreferredSize(dim);
        bf.createBasicLayout(sc);
        ClockWorker.addTask(sc.moveSprites());

        Player pl = new Player(sc);
        EnemyDifficulty em = new EnemyDifficulty();

        //new ProgressBar(sc, int x, int y, int width, int height, int borderWidth, Color progressBarColor, String title, int currentProgress, int maxProgress)
        ProgressBar scoreBar = new ProgressBar(sc, 5, 5, 150, 50, 3, Color.white, "Score", pl.score, pl.score);
        ProgressBar healthBar = new ProgressBar(sc, 5, 60, 150, 50, 3, Color.red, "Health", pl.health, pl.MAX_HEALTH);
        ProgressBar difficultyBar = new ProgressBar(sc, (int) (dim.getWidth() - 255), 5, 250, 50, 3, Color.lightGray, "Difficulty", pl.score, DIFFICULTY_REQS[pl.difficulty + 1]);
        ProgressBar gear2Bar = new ProgressBar(sc, 160, (int) (dim.getHeight() - 70), 150, 30, 3, new Color(186, 77, 91), "Gear 2", pl.gear2Progress, 100);
        ProgressBar gear3Bar = new ProgressBar(sc, 160, (int) (dim.getHeight() - 35), 150, 30, 3, new Color(201, 164, 138), "Gear 3", pl.gear3Progress, 100);
        ProgressBar arnaHakiBar = new ProgressBar(sc, 5, (int) (dim.getHeight() - 70), 150, 30, 3, new Color(152, 117, 156), "Arna. Haki", pl.arnamentHakiCharge, 250);
        ProgressBar obsvHakiBar = new ProgressBar(sc, 5, (int) (dim.getHeight() - 35), 150, 30, 3, new Color(109, 115, 183), "Obsv. Haki", pl.observationHakiCharge, 250);

        final Container content = bf.getContentPane();
        final CardLayout cards = new CardLayout();
        content.setLayout(cards);
        BasicContainer bc1 = new BasicContainer();
        content.add(bc1, "Splash");
        BasicContainer bc2 = new BasicContainer();
        content.add(bc2, "Game");

        //Creating the menu
        String[][] splashLayout = {
            {"Title"},
            {"Image"},
            {"Button"},};
        bc1.setStringLayout(splashLayout);
        JLabel title = new JLabel("One Piece: Born in the East Blue (Freshman Year Final Project)");
        title.setFont(new Font("Serif", Font.BOLD, 30));
        bc1.add("Title", title);
        Picture icon = new Picture("finalproject/luffy_down.png");
        icon = icon.resize(8.0);
        ImageIcon icon1 = new ImageIcon(icon.getImage());
        bc1.add("Image", new JLabel(icon1));
        JButton jstart = new JButton("START");
        jstart.setFont(new Font("Serif", Font.PLAIN, 30));
        jstart.setPreferredSize(new Dimension(150, 100));
        jstart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cards.show(content, "Game");
                bc2.requestFocus();
                ClockWorker.initialize(5);
            }
        });
        bc1.add("Button", jstart);
        String[][] layout = {{
            "Shooter"
        }};
        bc2.setStringLayout(layout);
        bc2.add("Shooter", sc);
        bf.show();

        //Clockworker tasks
        //Handles powerups & their timers
        Task powerupHandler = new Task() {
            int gear2Timer = 0;
            int gear3Timer = 0;
            int arnaTimer = 0;
            int obsvTimer = 0;
            boolean gear2Set = false;
            boolean gear3Set = false;
            boolean arnaSet = false;
            boolean obsvSet = false;

            @Override
            public void run() {
                if (pl.inGear2) {
                    //Effects of going into gear 2
                    if (!gear2Set) {
                        gear2Set = true;
                        pl.bulletSpeed = pl.bulletSpeed * 2;
                        pl.bulletCooldownMax = pl.bulletCooldownMax / 2;
                        pl.bulletDamage = pl.bulletDamage * 2;
                        pl.speed = pl.speed + 0.2;
                    }
                    gear2Bar.setBorderColor(Color.red);
                    gear2Timer++;
                    if (gear2Timer >= pl.GEAR_2_USE_SPEED) {
                        pl.gear2Progress--;
                        gear2Timer = 0;
                        if (pl.gear2Progress <= 0) {
                            pl.inGear2 = false;
                        }
                    }
                } else {
                    gear2Bar.setBorderColor(Color.black);
                    if (pl.gear2Progress < pl.GEAR_2_REQ) {
                        gear2Bar.setColor(new Color(186, 77, 91));
                    }
                    if (gear2Set) {
                        gear2Set = false;
                        pl.bulletSpeed = pl.bulletSpeed / 2;
                        pl.bulletCooldownMax = pl.bulletCooldownMax * 2;
                        pl.bulletDamage = pl.bulletDamage / 2;
                        pl.speed = pl.speed - 0.2;
                        pl.resetVelocity();
                    }
                }
                if (pl.inGear3) {
                    //Effects of going into gear 3
                    if (!gear3Set) {
                        gear3Set = true;
                        pl.bulletDamage = 2 * pl.bulletDamage;
                        pl.bulletResizeAmount = 3 * pl.bulletResizeAmount;
                        pl.bulletCooldownMax = pl.bulletCooldownMax / 2;
                    }
                    gear3Bar.setBorderColor(Color.red);
                    gear3Timer++;
                    if (gear3Timer >= pl.GEAR_3_USE_SPEED) {
                        pl.gear3Progress--;
                        gear3Timer = 0;
                        if (pl.gear3Progress <= 0) {
                            pl.inGear3 = false;
                        }
                    }
                } else {
                    gear3Bar.setBorderColor(Color.black);
                    if (pl.gear3Progress < pl.GEAR_3_REQ) {
                        gear3Bar.setColor(new Color(201, 164, 138));
                    }
                    if (gear3Set) {
                        gear3Set = false;
                        pl.bulletDamage = pl.bulletDamage / 2;
                        pl.bulletResizeAmount = Math.round(pl.bulletResizeAmount / 3);
                        pl.bulletCooldownMax = pl.bulletCooldownMax * 2;
                    }
                }
                if (pl.inArnamentHaki) {
                    //Effects of using arnament haki
                    if (!arnaSet) {
                        arnaSet = true;
                        pl.bulletDamage = 2 * pl.bulletDamage;
                    }
                    arnaHakiBar.setBorderColor(Color.red);
                    arnaTimer++;
                    if (arnaTimer >= pl.ARNA_HAKI_USE_SPEED) {
                        pl.arnamentHakiCharge--;
                        arnaTimer = 0;
                        if (pl.arnamentHakiCharge <= 0) {
                            pl.inArnamentHaki = false;
                        }
                    }
                } else {
                    arnaHakiBar.setBorderColor(Color.black);
                    if (pl.arnamentHakiCharge < pl.ARNA_HAKI_REQ) {
                        arnaHakiBar.setColor(new Color(152, 117, 156));
                    }
                    if (arnaSet) {
                        arnaSet = false;
                        pl.bulletDamage = pl.bulletDamage / 2;
                    }
                }
                if (pl.inObservationHaki) {
                    //Effects of using observation haki
                    if (!obsvSet) {
                        obsvSet = true;
                        for (Enemy enemy : enemyStorage) {
                            enemy.speed = enemy.speed / pl.OBSV_HAKI_SLOWDOWN_MULTIPLIER;
                            enemy.projectileCooldown = enemy.projectileCooldown * (int) (pl.OBSV_HAKI_SLOWDOWN_MULTIPLIER);
                            enemy.slowed = true;
                        }
                        for (Bullet bullet : bulletStorage) {
                            if (!bullet.friendly) {
                                bullet.setVel(bullet.getVelX() / pl.OBSV_HAKI_SLOWDOWN_MULTIPLIER, bullet.getVelY() / pl.OBSV_HAKI_SLOWDOWN_MULTIPLIER);
                                bullet.slowed = true;
                            }
                        }
                        em.spawnCooldown = (int) (em.spawnCooldown * pl.OBSV_HAKI_SLOWDOWN_MULTIPLIER);

                    }
                    obsvHakiBar.setBorderColor(Color.red);
                    obsvTimer++;
                    if (obsvTimer >= pl.OBSV_HAKI_USE_SPEED) {
                        obsvTimer = 0;
                        pl.observationHakiCharge--;
                        if (pl.observationHakiCharge <= 0) {
                            pl.inObservationHaki = false;
                        }
                    }
                } else {
                    obsvHakiBar.setBorderColor(Color.black);
                    if (pl.observationHakiCharge < pl.OBSV_HAKI_REQ) {
                        obsvHakiBar.setColor(new Color(109, 115, 183));
                    }
                    if (obsvSet) {
                        obsvSet = false;
                        em.spawnCooldown = (int) (em.spawnCooldown / pl.OBSV_HAKI_SLOWDOWN_MULTIPLIER);
                        for (Enemy enemy : enemyStorage) {
                            if (enemy.slowed) {
                                enemy.speed = enemy.speed * pl.OBSV_HAKI_SLOWDOWN_MULTIPLIER;
                                enemy.projectileCooldown = enemy.projectileCooldown / (int) (pl.OBSV_HAKI_SLOWDOWN_MULTIPLIER);
                                enemy.slowed = false;
                            }
                        }
                        for (Bullet bullet : bulletStorage) {
                            if (bullet.slowed) {
                                bullet.setVel(bullet.getVelX() * pl.OBSV_HAKI_SLOWDOWN_MULTIPLIER, bullet.getVelY() * pl.OBSV_HAKI_SLOWDOWN_MULTIPLIER);
                                bullet.slowed = false;
                            }
                        }
                    }
                }
                gear2Bar.updateProgress(pl.gear2Progress, 100);
                gear3Bar.updateProgress(pl.gear3Progress, 100);
                arnaHakiBar.updateProgress(pl.arnamentHakiCharge, 250);
                obsvHakiBar.updateProgress(pl.observationHakiCharge, 250);
            }
        };
        ClockWorker.addTask(powerupHandler);

        //Advances cooldown timers
        Task cooldownUpdater = new Task() {
            @Override
            public void run() {
                pl.bulletCooldown = pl.bulletCooldown - 1;
                pl.gettingDamagedCooldown = pl.gettingDamagedCooldown - 1;
            }
        };
        ClockWorker.addTask(cooldownUpdater);

        //Updates variables with difficulty
        Task difficultyUpdater = new Task() {
            @Override
            public void run() {
                if (pl.difficulty == 1) {
                    em.private_speed = 1.8;
                    em.spawnCooldown = 200;
                }
                if (pl.difficulty == 2) {
                    em.private_speed = 2.0;
                    em.spawnCooldown = 180;
                    em.marksman_slowdownDistance = 200;
                    em.marksman_bulletDamage = 15;
                    em.private_health = 150;
                }
            }
        };
        ClockWorker.addTask(difficultyUpdater);

        //Handles grenades, their blasts, & their path
        Task grenadeHandler = new Task() {
            @Override
            public void run() {
                ArrayList<Integer> indexesToRemove = new ArrayList<>();
                int indexesAlreadyRemoved = 0;
                int currentIndex = 0;
                for (Grenade curGrenade : grenadeStorage) {
                    if (!curGrenade.launched) {
                        curGrenade.launched = true;
                        curGrenade.blast.setX(curGrenade.tracking.getCenterX());
                        curGrenade.blast.setY(curGrenade.tracking.getCenterY());
                    }
                    curGrenade.curAirTime++;
                    if (curGrenade.curAirTime < curGrenade.airDuration) {
                        if (curGrenade.curAirTime < curGrenade.airDuration / 2) {
                            curGrenade.ySlowdown = curGrenade.ySlowdown - ((50.0 / curGrenade.airDuration) / 100.0);
                            curGrenade.setY(curGrenade.initialY + (curGrenade.getVelY() * curGrenade.curAirTime) - (Math.pow(curGrenade.ySlowdown, 2) * curGrenade.yGain * (curGrenade.curAirTime / (curGrenade.airDuration / 2.0))));
                        } else {
                            curGrenade.ySlowdown = curGrenade.ySlowdown + ((50.0 / curGrenade.airDuration) / 100.0);
                            curGrenade.setY(curGrenade.initialY + (curGrenade.getVelY() * curGrenade.curAirTime) - (Math.pow(curGrenade.ySlowdown, 2) * curGrenade.yGain * ((curGrenade.airDuration - curGrenade.curAirTime) / (curGrenade.airDuration / 2.0))));
                        }
                    } else {
                        if (!curGrenade.landed) {
                            curGrenade.landed = true;
                            curGrenade.setPicture(curGrenade.groundPic);
                        }
                        if (curGrenade.curAirTime >= curGrenade.airDuration + curGrenade.landedTimer) {
                            curGrenade.setActive(false);
                        }
                    }
                    if (!curGrenade.isActive()) {
                        indexesToRemove.add(currentIndex);
                    }
                    currentIndex++;
                }
                for (int curInd : indexesToRemove) {
                    grenadeStorage.remove(curInd - indexesAlreadyRemoved++);
                }
            }
        };
        ClockWorker.addTask(grenadeHandler);

        //Handles animations & the animationStorage list.
        Task animationHandler = new Task() {
            @Override
            public void run() {
                ArrayList<Integer> indexesToRemove = new ArrayList<>();
                int indexesAlreadyRemoved = 0;
                int currentIndex = 0;
                for (Animation currentAnim : animationStorage) {
                    currentAnim.currentTick++;
                    if (currentAnim.currentTick >= currentAnim.frameInterval) {
                        currentAnim.currentTick = 0;
                        currentAnim.nextFrame();
                        if (!currentAnim.animationActive) {
                            indexesToRemove.add(currentIndex);
                        }
                    }
                    currentIndex++;
                }
                for (int curInd : indexesToRemove) {
                    animationStorage.remove(curInd - indexesAlreadyRemoved++);
                }
            }
        };
        ClockWorker.addTask(animationHandler);

        //Handles bullet cooldowns & the bulletStorage list
        Task bulletHandler = new Task() {
            @Override
            public void run() {
                ArrayList<Integer> indexesToRemove = new ArrayList<>();
                int indexesAlreadyRemoved = 0;
                int currentIndex = 0;
                for (Bullet currentBullet : bulletStorage) {
                    if (currentBullet.inDeathCooldown) {
                        currentBullet.deathCooldown--;
                        if (currentBullet.deathCooldown <= 0) {
                            currentBullet.setActive(false);
                        }
                    }
                    if (!currentBullet.isActive()) {
                        indexesToRemove.add(currentIndex);
                    }
                    currentIndex++;
                }
                for (int curInd : indexesToRemove) {
                    bulletStorage.remove(curInd - indexesAlreadyRemoved++);
                }
            }
        };
        ClockWorker.addTask(bulletHandler);

        //Handles blasts & what they effect
        Task blastHandler = new Task() {
            public ArrayList<Enemy> locateNearbyEnemies(Blast blast, double radius) {
                ArrayList<Enemy> nearbyEnemies = new ArrayList<>();
                for (Enemy curEnem : enemyStorage) {
                    double xDistance = curEnem.getX() - blast.getX();
                    double yDistance = curEnem.getY() - blast.getY();
                    double distance = Math.sqrt(Math.pow(Math.abs(xDistance), 2) + Math.pow(Math.abs(yDistance), 2));
                    if (distance <= radius) {
                        nearbyEnemies.add(curEnem);
                    }
                }
                return nearbyEnemies;
            }

            @Override
            public void run() {
                ArrayList<Integer> indexesToRemove = new ArrayList<>();
                int indexesAlreadyRemoved = 0;
                int currentIndex = 0;
                for (Blast curBlast : blastStorage) {
                    if (curBlast.curTime == 0) {
                        curBlast.countdownAnim.animate(curBlast.getX(), curBlast.getY(), animationStorage);
                    }
                    curBlast.curTime++;
                    if (curBlast.curTime >= curBlast.timer) {
                        if (!curBlast.exploding) {
                            curBlast.exploding = true;
                            curBlast.blastAnim.animate(curBlast.getX(), curBlast.getY(), animationStorage);
                        }
                        curBlast.curBlastTime++;
                        if (curBlast.curBlastTime >= curBlast.expandInterval) {
                            if (curBlast.friendly) {
                                for (Enemy curEnem : locateNearbyEnemies(curBlast, curBlast.radius * (curBlast.curBlastIteration / curBlast.blastFrames))) {
                                    if (!curBlast.spritesHit.contains(curEnem)) {
                                        double xDiff = Math.abs(curBlast.getX() - curEnem.getX());
                                        double yDiff = Math.abs(curBlast.getY() - curEnem.getY());
                                        double distance = Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2));
                                        Animation punchHitEffect = new Animation(sc, arnaPunchHitFrames, 15, 2, 25);
                                        punchHitEffect.animate(curEnem.getCenterX() + curEnem.getPosTowardsSprite(curBlast, distance)[0], curEnem.getCenterY() + curEnem.getPosTowardsSprite(curBlast, distance)[1], animationStorage);
                                        curEnem.damage(curBlast.damage, pl, enemyStorage, new ArrayList<>(Arrays.asList(scoreBar, healthBar, difficultyBar, gear2Bar, gear3Bar, arnaHakiBar, obsvHakiBar)), DIFFICULTY_REQS);
                                        curBlast.spritesHit.add(curEnem);
                                    }
                                }
                            } else {
                                if (!curBlast.spritesHit.contains(pl)) {
                                    double xDiff = Math.abs(curBlast.getX() - pl.getX());
                                    double yDiff = Math.abs(curBlast.getY() - pl.getY());
                                    double distance = Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2));
                                    if (distance <= curBlast.radius * (curBlast.curBlastIteration / curBlast.blastFrames)) {
                                        pl.gettingDamagedCooldown = pl.PLAYER_DAMAGE_COOLDOWN;
                                        pl.health = pl.health - curBlast.damage;
                                        healthBar.updateProgress(pl.health, pl.MAX_HEALTH);
                                        Animation bloodEffect = new Animation(sc, bloodEffectFrames, 15, 3, 25);
                                        bloodEffect.animate(pl.getCenterX(), pl.getCenterY(), animationStorage);
                                        if (pl.health <= 0 && pl.alive) {
                                            pl.alive = false;
                                            JOptionPane.showMessageDialog(sc, "You passed out! Game Over!\nScore: " + pl.score);
                                            System.exit(0);
                                        }
                                        curBlast.spritesHit.add(pl);
                                    }
                                }
                            }
                            if (curBlast.curBlastIteration >= curBlast.blastFrames) {
                                curBlast.setActive(false);
                                indexesToRemove.add(currentIndex);
                            }
                            curBlast.curBlastTime = 0;
                            curBlast.curBlastIteration++;
                            currentIndex++;
                        }

                    }
                }
                for (int curInd : indexesToRemove) {
                    blastStorage.remove(curInd - indexesAlreadyRemoved++);
                }
            }
        };
        ClockWorker.addTask(blastHandler);

        //Spawns enemies on the given wait time in enemySpawnCooldown
        Task enemySpawner = new Task() {
            int timeTillNextSpawn = em.spawnCooldown;
            Dimension d = sc.getPreferredSize();

            @Override
            public void run() {
                timeTillNextSpawn = timeTillNextSpawn - 1;
                if (timeTillNextSpawn <= 0 && enemyStorage.size() < DIFFICULTY_MAX_ENEMIES[pl.difficulty]) {
                    timeTillNextSpawn = em.spawnCooldown;
                    int enemyChooser = (int) (Math.random() * (1001));
                    String enemyType = "Private";
                    Enemy newEnemy;
                    for (int i = DIFFICULTY_SPAWNRATES_RATES[pl.difficulty].length - 1; i >= 0; i--) {
                        enemyType = DIFFICULTY_SPAWNRATES_TYPES[pl.difficulty][i];
                        if (enemyChooser >= DIFFICULTY_SPAWNRATES_RATES[pl.difficulty][i]) {
                            break;
                        }
                    }
                    if (enemyType.equals("Marksman")) {
                        newEnemy = new Enemy(sc, "Marksman", em.marksman_speed, em.marksman_health, em.marksman_damage, em.marksman_scoreGiven, em.marksman_xpGiven);
                    } else if (enemyType.equals("Grenadier")) {
                        newEnemy = new Enemy(sc, "Grenadier", em.grenadier_speed, em.grenadier_health, em.grenadier_damage, em.grenadier_scoreGiven, em.grenadier_xpGiven);
                    } else {
                        newEnemy = new Enemy(sc, "Private", em.private_speed, em.private_health, em.private_damage, em.private_scoreGiven, em.private_xpGiven);
                    }
                    if (pl.inObservationHaki) {
                        newEnemy.speed = newEnemy.speed / pl.OBSV_HAKI_SLOWDOWN_MULTIPLIER;
                        newEnemy.projectileCooldown = newEnemy.projectileCooldown * (int) (pl.OBSV_HAKI_SLOWDOWN_MULTIPLIER);
                        newEnemy.slowed = true;
                    }
                    int spawnPoint = (int) (Math.random() * (4) + 1);
                    if (spawnPoint == 1) {
                        newEnemy.setX((int) (Math.random() * d.getWidth()));
                        newEnemy.setY(0 - newEnemy.getHeight());
                    } else if (spawnPoint == 2) {
                        newEnemy.setX((int) (Math.random() * d.getWidth()));
                        newEnemy.setY(d.getHeight());
                    } else if (spawnPoint == 3) {
                        newEnemy.setX(0 - newEnemy.getWidth());
                        newEnemy.setY((int) (Math.random() * d.getHeight()));
                    } else {
                        newEnemy.setX(d.getWidth());
                        newEnemy.setY((int) (Math.random() * d.getHeight()));
                    }
                    System.out.println("Spawned at: " + spawnPoint);
                    enemyStorage.add(newEnemy);
                }
            }
        };
        ClockWorker.addTask(enemySpawner);

        //Handles enemy movement and abilities
        Task enemyHandler = new Task() {
            @Override
            public void run() {
                for (Enemy currentEnemy : enemyStorage) {
                    double Dist = currentEnemy.setVelTowardsPlayer(pl, currentEnemy.speed);
                    if (currentEnemy.type.equals("Marksman")) {
                        if (Dist <= 400) {
                            currentEnemy.setVelTowardsPlayer(pl, 0.4 * currentEnemy.speed);
                        }
                        currentEnemy.projectileCooldown--;
                        if (currentEnemy.projectileCooldown <= 0) {
                            currentEnemy.projectileCooldown = em.marksman_bulletCooldown;
                            Bullet bullet = new Bullet(sc, Color.black, 10, em.marksman_bulletDamage, em.marksman_bulletSpeed, false);
                            bullet.setX(currentEnemy.getCenterX());
                            bullet.setY(currentEnemy.getCenterY());
                            bullet.setVelTowards(pl, bullet.speed);
                            bulletStorage.add(bullet);
                            if (pl.inObservationHaki) {
                                bullet.setVel(bullet.getVelX() / pl.OBSV_HAKI_SLOWDOWN_MULTIPLIER, bullet.getVelY() / pl.OBSV_HAKI_SLOWDOWN_MULTIPLIER);
                                bullet.slowed = true;
                            }
                        }
                    }
                    if (currentEnemy.type.equals("Grenadier")) {
                        if (Dist <= 300) {
                            currentEnemy.setVelTowardsPlayer(pl, 0.4 * currentEnemy.speed);
                        }
                        currentEnemy.projectileCooldown--;
                        if (currentEnemy.projectileCooldown <= 0) {
                            currentEnemy.projectileCooldown = em.grenadier_grenadeCooldown;
                            Picture grenadePic = new Picture("finalproject/basic_grenade.png");
                            Blast grenBlast = new Blast(sc, new Sprite(sc), em.grenadier_grenadeBlastDamage, em.grenadier_grenadeAirTime, em.grenadier_grenadeBlastRadius, em.grenadier_grenadeBlastRadius / 11.0, false, 25, blastCountdownFrames, blastExplodeFrames);
                            new Grenade(sc, currentEnemy.getCenterX(), currentEnemy.getCenterY(), pl, grenadePic, grenadePic, 2.0, grenBlast, 0, em.grenadier_grenadeYGain, em.grenadier_grenadeAirTime).launchTowards(pl, grenadeStorage, blastStorage);
                        }
                    }
                }
            }
        };
        ClockWorker.addTask(enemyHandler);

        //Controls & key presses
        KeyAdapter ka = new KeyAdapter() {
            boolean qPressed = false;
            boolean twoPressed = false;
            boolean threePressed = false;
            boolean ePressed = false;

            @Override
            public void keyPressed(KeyEvent ke) {
                if (ke.getKeyCode() == KeyEvent.VK_Q && !qPressed) {
                    if (pl.arnamentHakiCharge >= pl.ARNA_HAKI_REQ && !pl.inArnamentHaki) {
                        qPressed = true;
                        pl.inArnamentHaki = true;
                    } else {
                        pl.inArnamentHaki = false;
                    }
                }
                if (ke.getKeyCode() == KeyEvent.VK_2 && !twoPressed) {
                    if (pl.gear2Progress >= pl.GEAR_2_REQ && !pl.inGear2) {
                        twoPressed = true;
                        pl.inGear2 = true;
                    } else {
                        pl.inGear2 = false;
                    }
                }
                if (ke.getKeyCode() == KeyEvent.VK_3 && !threePressed) {
                    if (pl.gear3Progress >= pl.GEAR_3_REQ && !pl.inGear3) {
                        threePressed = true;
                        pl.inGear3 = true;
                    } else {
                        pl.inGear3 = false;
                    }
                }
                if (ke.getKeyCode() == KeyEvent.VK_E && !ePressed) {
                    if (pl.observationHakiCharge >= pl.OBSV_HAKI_REQ && !pl.inObservationHaki) {
                        ePressed = true;
                        pl.inObservationHaki = true;
                    } else {
                        pl.inObservationHaki = false;
                    }
                }
                if (ke.getKeyCode() == KeyEvent.VK_W) {
                    pl.setVel(pl.getVelX(), pl.getVelY() - pl.speed);
                    pl.currentPic = pl.imgFacingUp;
                    pl.setPicture(pl.currentPic);
                    if (pl.getVelY() < -1 * pl.speed) {
                        pl.setVel(pl.getVelX(), -1 * pl.speed);
                    }
                }
                if (ke.getKeyCode() == KeyEvent.VK_S) {
                    pl.setVel(pl.getVelX(), pl.getVelY() + pl.speed);
                    pl.currentPic = pl.imgFacingDown;
                    pl.setPicture(pl.currentPic);
                    if (pl.getVelY() > pl.speed) {
                        pl.setVel(pl.getVelX(), pl.speed);
                    }
                }
                if (ke.getKeyCode() == KeyEvent.VK_A) {
                    pl.setVel(pl.getVelX() - pl.speed, pl.getVelY());
                    pl.currentPic = pl.imgFacingLeft;
                    pl.setPicture(pl.currentPic);
                    if (pl.getVelX() < -1 * pl.speed) {
                        pl.setVel(-1 * pl.speed, pl.getVelY());
                    }
                }
                if (ke.getKeyCode() == KeyEvent.VK_D) {
                    pl.setVel(pl.getVelX() + pl.speed, pl.getVelY());
                    pl.currentPic = pl.imgFacingRight;
                    pl.setPicture(pl.currentPic);
                    if (pl.getVelX() > pl.speed) {
                        pl.setVel(pl.speed, pl.getVelY());
                    }
                }
                if ((ke.getKeyCode() == KeyEvent.VK_UP || ke.getKeyCode() == KeyEvent.VK_DOWN || ke.getKeyCode() == KeyEvent.VK_RIGHT || ke.getKeyCode() == KeyEvent.VK_LEFT) && pl.bulletCooldown <= 0) {
                    Bullet bullet;
                    switch (ke.getKeyCode()) {
                        case KeyEvent.VK_UP:
                            bullet = new Bullet(sc, new Picture("finalproject/luffy_fist_up.png"), pl.bulletResizeAmount, (int) pl.bulletDamage, pl.bulletSpeed, true);
                            if (pl.inArnamentHaki) {
                                bullet = new Bullet(sc, new Picture("finalproject/arna_luffy_fist_up.png"), pl.bulletResizeAmount, (int) pl.bulletDamage, pl.bulletSpeed, true);
                            }
                            bullet.setCenterX(pl.getX() + (pl.currentPic.getWidth() / 2));
                            bullet.setCenterY(pl.getY());
                            bullet.setVel(0, -1 * bullet.speed);
                            break;
                        case KeyEvent.VK_DOWN:
                            bullet = new Bullet(sc, new Picture("finalproject/luffy_fist_down.png"), pl.bulletResizeAmount, (int) pl.bulletDamage, pl.bulletSpeed, true);
                            if (pl.inArnamentHaki) {
                                bullet = new Bullet(sc, new Picture("finalproject/arna_luffy_fist_down.png"), pl.bulletResizeAmount, (int) pl.bulletDamage, pl.bulletSpeed, true);
                            }
                            bullet.setCenterX(pl.getX() + (pl.currentPic.getWidth() / 2));
                            bullet.setCenterY(pl.getY() + pl.currentPic.getHeight());
                            bullet.setVel(0, bullet.speed);
                            break;
                        case KeyEvent.VK_RIGHT:
                            bullet = new Bullet(sc, new Picture("finalproject/luffy_fist_right.png"), pl.bulletResizeAmount, (int) pl.bulletDamage, pl.bulletSpeed, true);
                            if (pl.inArnamentHaki) {
                                bullet = new Bullet(sc, new Picture("finalproject/arna_luffy_fist_right.png"), pl.bulletResizeAmount, (int) pl.bulletDamage, pl.bulletSpeed, true);
                            }
                            bullet.setCenterX(pl.getX() + pl.currentPic.getWidth());
                            bullet.setCenterY(pl.getY() + (pl.currentPic.getHeight() / 2));
                            bullet.setVel(bullet.speed, 0);
                            break;
                        default:
                            bullet = new Bullet(sc, new Picture("finalproject/luffy_fist_left.png"), pl.bulletResizeAmount, (int) pl.bulletDamage, pl.bulletSpeed, true);
                            if (pl.inArnamentHaki) {
                                bullet = new Bullet(sc, new Picture("finalproject/arna_luffy_fist_left.png"), pl.bulletResizeAmount, (int) pl.bulletDamage, pl.bulletSpeed, true);
                            }
                            bullet.setCenterX(pl.getX());
                            bullet.setCenterY(pl.getY() + (pl.currentPic.getHeight() / 2));
                            bullet.setVel(-1 * bullet.speed, 0);
                            break;
                    }
                    pl.bulletCooldown = pl.bulletCooldownMax;
                    if (pl.inGear3) {
                        bullet.addDeathCooldown(pl.GEAR_3_BULLET_DEATH_COOLDOWN);
                    }
                    if (pl.inGear2) {
                        bullet.inGear2 = true;
                    }
                    if (pl.inArnamentHaki) {
                        bullet.inArna = true;
                    }
                    bulletStorage.add(bullet);
                }
            }

            @Override
            public void keyReleased(KeyEvent ke) {
                if (ke.getKeyCode() == KeyEvent.VK_Q) {
                    qPressed = false;
                }
                if (ke.getKeyCode() == KeyEvent.VK_2) {
                    twoPressed = false;
                }
                if (ke.getKeyCode() == KeyEvent.VK_3) {
                    threePressed = false;
                }
                if (ke.getKeyCode() == KeyEvent.VK_E) {
                    ePressed = false;
                }
                if (ke.getKeyCode() == KeyEvent.VK_W) {
                    if (!pl.upBorderCollision) {
                        pl.setVel(pl.getVelX(), pl.getVelY() + pl.speed);
                    } else {
                        pl.upBorderCollision = false;
                    }
                }
                if (ke.getKeyCode() == KeyEvent.VK_S) {
                    if (!pl.downBorderCollision) {
                        pl.setVel(pl.getVelX(), pl.getVelY() - pl.speed);
                    } else {
                        pl.downBorderCollision = false;
                    }
                }
                if (ke.getKeyCode() == KeyEvent.VK_A) {
                    if (!pl.leftBorderCollision) {
                        pl.setVel(pl.getVelX() + pl.speed, pl.getVelY());
                    } else {
                        pl.leftBorderCollision = false;
                    }
                }
                if (ke.getKeyCode() == KeyEvent.VK_D) {
                    if (!pl.rightBorderCollision) {
                        pl.setVel(pl.getVelX() - pl.speed, pl.getVelY());
                    } else {
                        pl.rightBorderCollision = false;
                    }
                }

            }
        };
        bc2.addKeyListener(ka);

        //Collisions (for attacks and stuff)
        sc.addSpriteSpriteCollisionListener(finalproject.Bullet.class, finalproject.Enemy.class, new SpriteSpriteCollisionListener<finalproject.Bullet, finalproject.Enemy>() {
            @Override
            public void collision(finalproject.Bullet bullet, finalproject.Enemy enemy) {
                if (bullet.friendly == true) {
                    enemy.damage(bullet.damage, pl, enemyStorage, new ArrayList<>(Arrays.asList(scoreBar, healthBar, difficultyBar, gear2Bar, gear3Bar, arnaHakiBar, obsvHakiBar)), DIFFICULTY_REQS);
                    Animation punchHitEffect;
                    if (bullet.inGear2) {
                        punchHitEffect = new Animation(sc, gear2PunchHitFrames, 15, 2, 25);
                    } else if (bullet.inArna) {
                        punchHitEffect = new Animation(sc, arnaPunchHitFrames, 15, 2, 25);
                    } else {
                        punchHitEffect = new Animation(sc, punchHitFrames, 15, 2, 25);
                    }
                    if (bullet.inArna) {
                        if (!bullet.setOffBlastYet) {
                            Blast arnaHakiBlast = new Blast(sc, bullet, pl.ARNA_HAKI_BLAST_DAMAGE, 0, pl.ARNA_HAKI_BLAST_RADIUS, pl.ARNA_HAKI_BLAST_RESIZE, true, 25, blastCountdownFrames, blastDefaultFrames);
                            arnaHakiBlast.spritesHit.add(enemy);
                            arnaHakiBlast.activate(blastStorage);
                            bullet.setOffBlastYet = true;
                        }
                    }
                    punchHitEffect.animate(bullet.getCenterX(), bullet.getCenterY(), animationStorage);
                    if (bullet.hasDeathCooldown) {
                        bullet.setDeathCooldown(bullet.deathCooldownMax);
                    } else {
                        bullet.setActive(false);
                    }

                }
            }
        });
        sc.addSpriteSpriteCollisionListener(finalproject.Player.class, finalproject.Enemy.class, new SpriteSpriteCollisionListener<finalproject.Player, finalproject.Enemy>() {
            @Override
            public void collision(finalproject.Player player, finalproject.Enemy enemy) {
                if (player.gettingDamagedCooldown <= 0 && player.health > 0) {
                    player.gettingDamagedCooldown = pl.PLAYER_DAMAGE_COOLDOWN;
                    player.health = player.health - enemy.damage;
                    healthBar.updateProgress(pl.health, pl.MAX_HEALTH);
                    double xDiff = Math.abs(player.centerX() - enemy.centerX());
                    double yDiff = Math.abs(player.centerY() - enemy.centerY());
                    double distance = Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2));
                    Animation bloodEffect = new Animation(sc, bloodEffectFrames, 15, 3, 25);
                    bloodEffect.animate(enemy.getCenterX() + enemy.getPosTowardsSprite(player, distance)[0], enemy.getCenterY() + enemy.getPosTowardsSprite(player, distance)[1], animationStorage);
                    if (player.health <= 0 && player.alive) {
                        player.alive = false;
                        JOptionPane.showMessageDialog(sc, "You passed out! Game Over!\nScore: " + pl.score);
                        System.exit(0);
                    }
                }
                int xDiff = (int) ((enemy.getX() - player.getX()) / Math.abs(enemy.getX() - player.getX()));
                int yDiff = (int) ((enemy.getY() - player.getY()) / Math.abs(enemy.getY() - player.getY()));
                if (Math.abs(enemy.getCenterY() - player.getCenterY()) < player.getHeight() / 2) {
                    yDiff = 0;
                }
                if (Math.abs(enemy.getCenterX() - player.getCenterX()) < player.getWidth() / 2) {
                    xDiff = 0;
                }
                enemy.setX(enemy.getX() + (xDiff * 5));
                enemy.setY(enemy.getY() + (yDiff * 5));
                enemy.setVel(0, 0);
            }
        });
        sc.addSpriteSpriteCollisionListener(finalproject.Player.class, finalproject.Bullet.class, new SpriteSpriteCollisionListener<finalproject.Player, finalproject.Bullet>() {
            @Override
            public void collision(finalproject.Player player, finalproject.Bullet bullet) {
                if (!bullet.friendly && player.gettingDamagedCooldown <= 0 && player.health > 0) {
                    bullet.setActive(false);
                    player.gettingDamagedCooldown = pl.PLAYER_DAMAGE_COOLDOWN;
                    player.health = player.health - bullet.damage;
                    healthBar.updateProgress(pl.health, pl.MAX_HEALTH);
                    Animation bloodEffect = new Animation(sc, bloodEffectFrames, 15, 3, 25);
                    bloodEffect.animate(bullet.getCenterX(), bullet.getCenterY(), animationStorage);
                    if (player.health <= 0 && player.alive) {
                        player.alive = false;
                        JOptionPane.showMessageDialog(sc, "You passed out! Game Over!\nScore: " + pl.score);
                        System.exit(0);
                    }
                }
            }
        });

        bf.show();
    }
}
