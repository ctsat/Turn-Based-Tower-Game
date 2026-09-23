import java.util.*;

// Represents the player character and manages player stats like health and attack
public class Player {
    // Private instance variables to store player data safely
    private double health;
    private double maxHealth;
    private String name;
    private int strength;
    
    // Static scanner used across player methods for user input
    private static Scanner scanner = new Scanner(System.in);
    
    // Default constructor to make a player with default starting stats
    public Player(String name) {
        this.name = name;
        this.health = 20.0;
        this.strength = 3;
        this.maxHealth = 20.0;
    }

    // Overloaded constructor allowing custom max health and strength setup
    public Player(String name, int maxHealth, int strength) {
        this.name = name;
        this.health = maxHealth;
        this.maxHealth = maxHealth;
        this.strength = strength;
    }

    // Heals the player based on difficulty settings, making sure health does not exceed max health
    public void rest(double heal) {
        this.health += heal * (1.0 / Main.difficulty);
        if (this.health > this.maxHealth) {
            this.health = this.maxHealth;
        }
    }

    // Increases max health and adds health, capping at max health
    public void trainHealth(double amount) {
        this.maxHealth += amount;
        this.health += amount * (1.0 / (Main.difficulty+0.5));
        if (this.health>this.maxHealth)
            this.health = this.maxHealth;
    }

    // Increases player's attack strength
    public void trainAttack(int amount) {
        this.strength += amount;
    }

    // Calculates and returns remaining health as a decimal ratio
    public double healthPercent() { 
        return (double) this.health / this.maxHealth; 
    }

    // Asks player if they want to play again when game ends
    public static void restart() {
        System.out.print("Would you like to play again? (yes/no): ");
        String response = scanner.nextLine();
        if (!response.equalsIgnoreCase("yes")) {
            Main.keepPlaying = false;
            System.out.println("Thanks for playing!");
        }
    }

    // Checks if player health is greater than 0
    public boolean isAlive() { 
        return this.health > 0; 
    }

    // Subtracts damage from health and checks if game over is triggered
    public void takeDmg(double pain) {
        this.health -= pain;
        if (this.health < 0) {
            this.health = 0;
        }
        if (!this.isAlive()) {
            System.out.println("Game Over </3");
            restart();
        }
    }

    // Getter methods for accessing private fields
    public String getName() { return name; }
    public double getHealth() { return health; }
    public double getMaxHealth() { return maxHealth; }
    public int getStrength() { return strength; }

    // Returns a formatted string displaying current player stats
    public String playerStats() {
        return String.format("Player %s HP: %.1f/%.1f | ATK: %d", this.name, this.health, this.maxHealth, this.strength);
    }

    // Setter methods for modifying private fields
    public void setHealth(double health) { this.health = health; }
    public void setMaxHealth(double maxHealth) { this.maxHealth = maxHealth; }
    public void setStrength(int strength) { this.strength = strength; }
}