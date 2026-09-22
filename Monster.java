public class Monster {
    private String name;
    private double health;
    private double maxHealth;
    private int attack;
    private double exp;

    public Monster(String name, double health, int attack, double exp) {
        this.name = name;
        this.health = health * Main.difficulty;
        this.maxHealth = this.health;
        this.attack = (int) (attack * Main.difficulty);
        this.exp = exp * Main.difficulty * 0.85;
    }

    public String monsterStats() {
        return String.format("Monster %s HP: %.1f/%.1f | ATK: %d", this.name, this.health, this.maxHealth, this.attack);
    }

    public void takeDmg(double pain) {
        this.health -= pain;
        if (this.health < 0) {
            this.health = 0;
        }
    }

    public boolean isAlive() {
        return this.health > 0;
    }

    public double healthPercent() {
        return (double) this.health / this.maxHealth;
    }

    public String getName() { return name; }
    public double getHealth() { return health; }
    public double getMaxHealth() { return maxHealth; }
    public int getAttack() { return attack; }
    public double getExp() { return exp; }

    public void setHealth(double health) { this.health = health; }
    public void setMaxHealth(double maxHealth) { this.maxHealth = maxHealth; }
    public void setAttack(int attack) { this.attack = attack; }
    public void setExp(double exp) { this.exp = exp; }
}