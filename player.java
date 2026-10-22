// Represents the player character and manages player stats like health and attack
public class Player {
    // Private instance variables to store player data safely
    private double health;
    private double maxHealth;
    private String name;
    private int strength;
    private Item[] inventory;

    public static final int MAX_INVENTORY = 5;
    
    // Default constructor: this(...) calls the overloaded constructor below
    // with the default starting stats (20 HP, 3 attack)
    public Player(String name) {
        this(name, 20, 3);
    }

    // Overloaded constructor allowing custom max health and strength setup
    public Player(String name, int maxHealth, int strength) {
        this.name = name;
        this.health = maxHealth;
        this.maxHealth = maxHealth;
        this.strength = strength;
        this.inventory = new Item[MAX_INVENTORY];
    }

    public boolean addItem(Item item) {
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] == null) {
                inventory[i] = item;
                return true;
            }
        }
        return false;
    }

    public Item getItem(int slot) {
        if (slot >= 0 && slot < inventory.length) {
            return inventory[slot];
        }
        return null;
    }

    public Item removeItem(int slot) {
        Item removed = getItem(slot);
        if (removed != null) {
            inventory[slot] = null;
        }
        return removed;
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

    // Checks if player health is greater than 0
    public boolean isAlive() { 
        return this.health > 0; 
    }

    // Subtracts damage from health, making sure health stays at or above 0.
    // Game over is handled in Main so this method only changes the player's state.
    public void takeDmg(double pain) {
        this.health -= pain;
        if (this.health < 0) {
            this.health = 0;
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