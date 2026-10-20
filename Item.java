public class Item {
    private static int itemsCreated = 0;

    public static final String weapon = "weapon";
    public static final String potion = "potion";

    private String name;
    private String type;
    private int power;

    public Item(String name, String type, int power) {
        this.name = name;
        this.type = type;
        this.power = power;
        itemsCreated++;
    }

    public Item(String name, int power) {
        this(name, potion, power);
    }

    public static int getItemsCreated() {
        return itemsCreated;
    }

    public static Item randomDrop(int level) {
        if (Math.random() < 0.5) {
            return new Item("Iron Blade", weapon, level + 1);
        } else {
            return new Item("Health Potion", 5 + level * 5);
        }
    }

    public boolean isBetterThan(Item other) {
        return this.power > other.power;
    }

    public boolean isWeapon() {
        return this.type.equals(weapon);
    }

    public String getName() { return name; }
    public String getType() { return type; }
    public int getPower() { return power; }

    public void upgrade(int amount) {
        this.power += amount;
        if (this.power < 0) {
            this.power = 0;
        }
    }

    public String toString() {
        if (isWeapon()) {
            return name + " (+" + power + " ATK)";
        }
        return name + " (heals " + power + " HP)";
    }
}
