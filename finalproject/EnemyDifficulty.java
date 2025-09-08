package finalproject;

/**
 *
 * @author hyprr
 */
//This class is necessary for updating difficulty as methods in the main method don't allow non-final local variables.
public class EnemyDifficulty {
    
    public int spawnCooldown = 250;
    public double private_speed = 1.5;
    public int private_damage = 20;
    public int private_health = 100;
    public int private_scoreGiven = 50;
    public int private_xpGiven = 5;
    public double marksman_speed = 1.3;
    public int marksman_damage = 10;
    public int marksman_health = 150;
    public int marksman_bulletCooldown = 200;
    public int marksman_bulletDamage = 10;
    public int marksman_bulletSpeed = 3;
    public int marksman_slowdownDistance = 300;
    public int marksman_scoreGiven = 50;
    public int marksman_xpGiven = 5;
    public double grenadier_speed = 1.0;
    public int grenadier_damage = 10;
    public int grenadier_health = 200;
    public int grenadier_grenadeCooldown = 280;
    public int grenadier_grenadeBlastDamage = 20;
    public int grenadier_grenadeBlastRadius = 132;
    public int grenadier_grenadeAirTime = 400;
    public int grenadier_grenadeYGain = 250;
    public int grenadier_slowdownDistance = 250;
    public int grenadier_scoreGiven = 75;
    public int grenadier_xpGiven = 7;
    
    public EnemyDifficulty() {}
    
}
