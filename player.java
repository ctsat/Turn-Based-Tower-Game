import java.util.*;

public class player {
    private double health;
    private String name;
    private int strength;
    
    private static Scanner scanner = new Scanner(System.in);

    public player(String name, int health, int strength) {
        this.name = name;
        this.health = health;
        this.strength = strength;
    }
    public player(String name) {
        this.name = name;
        health = 20;
        strength = 2;
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
        if (!(this.isAlive())) {
            System.out.println( "Game Over </3");
            restart();
        }



    }

}
