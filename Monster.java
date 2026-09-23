// Represents monsters in the game with stats scaled by difficulty
public class Monster {
    // Private instance variables for monster stats
    private String name;
    private double health;
    private double maxHealth;
    private int attack;
    private double exp;

    // Constructor that sets up monster stats scaled by difficulty
    public Monster(String name, double health, int attack, double exp) {
        this.name = name;
        this.health = health * Main.difficulty;
        this.maxHealth = this.health;
        this.attack = (int) (attack * Main.difficulty);
        this.exp = exp * Main.difficulty * 0.85;
    }

    // Returns formatted string display of monster stats
    public String monsterStats() {
        return String.format("Monster %s HP: %.1f/%.1f | ATK: %d", this.name, this.health, this.maxHealth, this.attack);
    }

    // Subtracts damage from monster health, making sure health stays at or above 0
    public void takeDmg(double pain) {
        this.health -= pain;
        if (this.health < 0) {
            this.health = 0;
        }
    }

    // Checks if monster is alive
    public boolean isAlive() {
        return this.health > 0;
    }

    // Calculates current monster health percentage as a decimal
    public double healthPercent() {
        return (double) this.health / this.maxHealth;
    }

    // Getter methods for accessing private monster instance variables
    public String getName() { return name; }
    public double getHealth() { return health; }
    public double getMaxHealth() { return maxHealth; }
    public int getAttack() { return attack; }
    public double getExp() { return exp; }

    // Setter methods for updating private monster instance variables
    public void setHealth(double health) { this.health = health; }
    public void setMaxHealth(double maxHealth) { this.maxHealth = maxHealth; }
    public void setAttack(int attack) { this.attack = attack; }
    public void setExp(double exp) { this.exp = exp; }
}