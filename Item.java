// Represents an item (weapon or potion) dropped by monsters
public class Item {
    // static: shared count of all items created
    private static int itemsCreated = 0;

    // static: item types are the same for every item
    public static final String weapon = "weapon";
    public static final String potion = "potion";

    // Private instance variables for item data
    private String name;
    private String type;
    private int power;

    // Constructor using this to set fields
    public Item(String name, String type, int power) {
        this.name = name;
        this.type = type;
        this.power = power;
        itemsCreated++;
    }

    // Overloaded constructor for potions using this(...)
    public Item(String name, int power) {
        this(name, potion, power);
    }

    // static: only uses static data
    public static int getItemsCreated() {
        return itemsCreated;
    }

    // Makes a random item for a floor (static). Pre: level >= 1. Post: returns new Item
    public static Item randomDrop(int level) {
        if (Math.random() < 0.5) {
            return new Item("Iron Blade", weapon, level + 1);
        } else {
            return new Item("Health Potion", 5 + level * 5);
        }
    }

    // Compares power with another item. Pre: other not null. Post: true if stronger
    public boolean isBetterThan(Item other) {
        return this.power > other.power;
    }

    // Checks if item is a weapon
    public boolean isWeapon() {
        return this.type.equals(weapon);
    }

    // Getter methods for accessing private fields
    public String getName() { return name; }
    public String getType() { return type; }
    public int getPower() { return power; }

    // Increases or decreases power, minimum 0
    public void upgrade(int amount) {
        this.power += amount;
        if (this.power < 0) {
            this.power = 0;
        }
    }

    // Returns a formatted string of the item
    public String toString() {
        if (isWeapon()) {
            return name + " (+" + power + " ATK)";
        }
        return name + " (heals " + power + " HP)";
    }
}
