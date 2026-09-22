import java.util.*;

public class Player {
    private double health;
    private double maxHealth;
    private String name;
    private int strength;
    
    private static Scanner scanner = new Scanner(System.in);
    
    public Player(String name) {
        this.name = name;
        this.health = 20.0;
        this.strength = 3;
        this.maxHealth = 20.0;
    }

    public Player(String name, int maxHealth, int strength) {
        this.name = name;
        this.health = maxHealth;
        this.maxHealth = maxHealth;
        this.strength = strength;
    }

    public void rest(double heal) {
        this.health += heal * (1.0 / Main.difficulty);
        if (this.health > this.maxHealth) {
            this.health = this.maxHealth;
        }
    }

    public void trainHealth(double amount) {
        this.maxHealth += amount;
        this.health += amount * (1.0 / (Main.difficulty+0.5));
        if (this.health>this.maxHealth)
            this.health = this.maxHealth;
    }

    public void trainAttack(int amount) {
        this.strength += amount;
    }

    public double healthPercent() { 
        return (double) this.health / this.maxHealth; 
    }

    public static void restart() {
        System.out.print("Would you like to play again? (yes/no): ");
        String response = scanner.nextLine();
        if (!response.equalsIgnoreCase("yes")) {
            Main.keepPlaying = false;
            System.out.println("Thanks for playing!");
        }
    }

    public boolean isAlive() { 
        return this.health > 0; 
    }

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

    public String getName() { return name; }
    public double getHealth() { return health; }
    public double getMaxHealth() { return maxHealth; }
    public int getStrength() { return strength; }

    public String playerStats() {
        return String.format("Player %s HP: %f/%f | ATK: %d", this.name, this.health, this.maxHealth, this.strength);
    }


    public void setHealth(double health) { this.health = health; }
    public void setMaxHealth(double maxHealth) { this.maxHealth = maxHealth; }
    public void setStrength(int strength) { this.strength = strength; }
}