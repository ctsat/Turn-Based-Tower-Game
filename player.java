// Represents the player character and manages player stats like health and attack
public class Player {
    // Private instance variables to store player data safely
    private double health;
    private double maxHealth;
    private String name;
    private int strength;
    private Item[] inventory;
// PRAISE: I like the use of an array to store all the items in the inventory instead of keeping them seperate
    // static: inventory size is the same for every player
    public static final int MAX_INVENTORY = 5;
    
    // Default constructor with 20 HP and 3 attack
    public Player(String name) {
        this(name, 20, 3);
    }
// SUGGESTION:Maybe add a choice to create a custom player that calls this overlaoded constructor - Akaran
    // Overloaded constructor allowing custom max health and strength setup
    public Player(String name, int maxHealth, int strength) {
        this.name = name;
        this.health = maxHealth;
        this.maxHealth = maxHealth;
        this.strength = strength;
        this.inventory = new Item[MAX_INVENTORY];
    }

    // Adds item to first empty slot. Pre: item not null. Post: true if added
    public boolean addItem(Item item) {
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] == null) {
                inventory[i] = item;
                return true;
            }
        }
        return false;
    }

    // Returns item in a slot, or null if empty or invalid
    public Item getItem(int slot) {
        if (slot >= 0 && slot < inventory.length) {
            return inventory[slot];
        }
        return null;
    }

    // Removes and returns item in a slot
    public Item removeItem(int slot) {
        Item removed = getItem(slot);
        if (removed != null) {
            inventory[slot] = null;
        }
        return removed;
    }

    // Counts how many items are in the inventory
    public int countItems() {
        int count = 0;
        for (Item item : inventory) {
            if (item != null) {
                count++;
            }
        }
        return count;
    }

    // Finds first item of a type. Pre: type not null. Post: index or -1
    public int findItemSlot(String type) {
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] != null && inventory[i].getType().equals(type)) {
                return i;
            }
        }
        return -1;
    }

    // Adds up weapon power (static, only uses the array)
    public static int totalWeaponPower(Item[] items) {
        int total = 0;
        for (Item item : items) {
            if (item != null && item.isWeapon()) {
                total += item.getPower();
            }
        }
        return total;
    }
//QUESTION When would finding the strongest weapon be needed?
    // Returns the strongest weapon, or null if none
    public Item findStrongestWeapon() {
        Item best = null;
        for (Item item : inventory) {
            if (item != null && item.isWeapon() && (best == null || item.isBetterThan(best))) {
                best = item;
            }
        }
        return best;
    }

    // Drinks first potion. Pre: none. Post: potion removed, health raised
    public Item drinkPotion() {
        int slot = findItemSlot(Item.potion);
        if (slot == -1) {
            return null;
        }
        Item potion = removeItem(slot);
        this.health += potion.getPower();
        if (this.health > this.maxHealth) {
            this.health = this.maxHealth;
        }
        return potion;
    }

    // Removes a random item when fleeing
    public Item loseRandomItem() {
        int count = countItems();
        if (count == 0) {
            return null;
        }
        int target = (int) (Math.random() * count);
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] != null) {
                if (target == 0) {
                    return removeItem(i);
                }
                target--;
            }
        }
        return null;
    }

    // Returns inventory as a string
    public String inventoryString() {
        String result = "Inventory:";
        for (Item item : inventory) {
            if (item == null) {
                result += " [empty]";
            } else {
                result += " [" + item + "]";
            }
        }
        return result;
    }

    // Returns strength plus weapon power
    public int getAttack() {
        return this.strength + totalWeaponPower(this.inventory);
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

    // Subtracts damage and returns damage actually taken
    public double takeDmg(double pain) {
        double before = this.health;
        this.health -= pain;
        if (this.health < 0) {
            this.health = 0;
        }
        return before - this.health;
    }

    // Getter methods for accessing private fields
    public String getName() { return name; }
    public double getHealth() { return health; }
    public double getMaxHealth() { return maxHealth; }
    public int getStrength() { return strength; }

    // Returns a formatted string displaying current player stats
    public String playerStats() {
        return String.format("Player %s HP: %.1f/%.1f | ATK: %d", this.name, this.health, this.maxHealth, getAttack());
    }

    // Setter methods for modifying private fields
    public void setHealth(double health) { this.health = health; }
    public void setMaxHealth(double maxHealth) { this.maxHealth = maxHealth; }
    public void setStrength(int strength) { this.strength = strength; }
}