import javax.swing.*;
import java.util.*;

public class Main {

    // Class variables for overall game status and difficulty multiplier
    static boolean keepPlaying = true;
    static double difficulty = 1.0;
    
    // Returns a new Monster object based on current floor level
    private static Monster spawnMonsterForLevel(int level) {

        if (level == 1) 
            return new Monster("Goblin Runt", 15.0, 4, 10.0);
        if (level == 2) 
            return new Monster("Cave Spider", 30.0, 7, 25.0);
        if (level == 3) 
            return new Monster("Orc Guard", 50.0, 11, 45.0);
        if (level == 4) 
            return new Monster("Stone Golem", 80.0, 16, 70.0);
        if (level == 5) 
            return new Monster("Tower Golem", 130.0, 24, 150.0); 
        else
             return new Monster("Zenon", 1000.0, 2, 9999.99);
    }
    
    //Makes sure scanner doesnt error when getting incorrect values
    public static int getValidInt(Scanner scanner, String prompt) {
    while (true) {
        System.out.print(prompt);
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter a whole number.");
        }
    }
}

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Main game loop that keeps running games until user stops
        while (keepPlaying) {
            // Displays game title and reads player name
            System.out.println("======================== \nWelcome to the tower \nReach floor 5 \nGood Luck! \n======================= \nEnter player name:");
            String playerName = scanner.nextLine();

            // Creates a new Player instance
            Player player = new Player(playerName);

            // Prompts user for game difficulty selection
            System.out.println("\nSelect Difficulty(1-3):");
            System.out.println("1. Easy (0.5x)");
            System.out.println("2. Normal (1.0x)");
            System.out.println("3. Hard (1.5x)");
            
            int diffChoice = getValidInt(scanner, "Choice: ");

            // Assigns difficulty modifier according to choice
            if (diffChoice == 1) {
                difficulty = 0.5;
            } else if (diffChoice == 3) {
                difficulty = 1.5;
            } else {
                difficulty = 1.0;
            }

            // Loop iterating through tower levels 1 to 5
            for (int level = 1; level <= 5; level++) {
                // Exits loop if player died or stopped playing
                if (!player.isAlive() || !keepPlaying) {
                    break;
                }

                // Generates floor monster and prints entrance message
                Monster monster = spawnMonsterForLevel(level);
                System.out.println("\n============================");
                System.out.println("   ENTERING TOWER LEVEL " + level);
                System.out.println("============================");
                System.out.println("A wild " + monster.getName() + " appears!");

                boolean fled = false;
                // Combat turn loop active while player and monster live, player hasn't fled, and game continues
                while (monster.isAlive() && player.isAlive() && !fled && keepPlaying) {
                    System.out.println("\n--- COMBAT vs " + monster.getName() + " ---");
                    System.out.println(player.playerStats() + " | " + monster.getName());

                    // Display combat action menu
                    System.out.println("1. Attack");
                    System.out.println("2. Analyze");
                    System.out.println("3. Flee");

                    int combatChoice = getValidInt(scanner, "Combat Choice: ");

                    // Option 1: Player attacks monster; monster counterattacks if alive
                    if (combatChoice == 1) {
                        monster.takeDmg(player.getStrength());
                        System.out.println(player.getName() + " hit " + monster.getName() + " for " + player.getStrength() + " damage!");

                        if (monster.isAlive()) {
                            player.takeDmg(monster.getAttack());
                            System.out.println(monster.getName() + " struck back for " + monster.getAttack() + " damage!");
                        }
                    // Option 2: Inspect stats; monster strikes back
                    } else if (combatChoice == 2) {
                        System.out.println("[ANALYZE] " + player.playerStats() + String.format(" (%.1f%% HP)", player.healthPercent() * 100.0));
                        System.out.println("[ANALYZE] " + monster.monsterStats() + String.format(" | EXP Value: %.1f", monster.getExp()));
                       
                        if (monster.isAlive()) {
                            player.takeDmg(monster.getAttack());
                            System.out.println(monster.getName() + " struck back for " + monster.getAttack() + " damage!");
                        }
                        
                    // Option 3: Attempt 66% chance flee; takes counterattack on failure
                    } else if (combatChoice == 3) {
                        if (Math.random() < 0.66) {
                            System.out.println(player.getName() + " successfully fled down the stairs!");
                            fled = true;
                            level--;
                        } else {
                            System.out.println(player.getName() + " failed to flee!");
                            if (monster.isAlive()) {
                                player.takeDmg(monster.getAttack());
                                System.out.println(monster.getName() + " struck back for " + monster.getAttack() + " damage!");
                            }
                        }
                    }
                }

                // Skips post-combat phase if combat ended by dying, fleeing, or quitting
                if (!player.isAlive() || fled || !keepPlaying) {
                    continue;
                }

                System.out.println("\nVictory! Defeated " + monster.getName() + "!");

                // Post-combat room choice loop for floors 1 to 4
                if (level < 5) {
                    boolean inRoomPhase = true;
                    while (inRoomPhase && player.isAlive() && keepPlaying) {
                        System.out.println("\n--- POST-COMBAT CHAMBER (Floor " + level + " Cleared) ---");
                        System.out.println(player.playerStats());
                        System.out.println("1. Train Room");
                        System.out.println("2. Rest Room");
                        System.out.println("3. Fight Room (Ascend)");

                        int hubChoice = getValidInt(scanner, "Room Choice: ");

                        // Option 1: Train to boost Max HP or Attack
                        if (hubChoice == 1) {
                            System.out.println("  [1] +10 Max HP  |  [2] +3 Attack");
                            int trainChoice = getValidInt(scanner, "  Select focus: ");

                            if (trainChoice == 1) {
                                player.trainHealth(10.0);
                                System.out.println("Max HP boosted!");
                                inRoomPhase = false;
                            } else if (trainChoice == 2) {
                                player.trainAttack(3);
                                System.out.println("Attack boosted!");
                                inRoomPhase = false;
                            }
                        // Option 2: Rest to heal HP
                        } else if (hubChoice == 2) {
                            player.rest(15.0);
                            System.out.println("Rested. " + player.playerStats());
                            inRoomPhase = false;
                        // Option 3: Ascend directly to next floor
                        } else if (hubChoice == 3) {
                            inRoomPhase = false;
                        }
                    }
                }
            }

            // Victory screen if all 5 levels cleared while alive
            if (player.isAlive() && keepPlaying) {
                System.out.println("\nCongratulations " + player.getName() + "! You cleared all 5 levels of the tower!");
                Player.restart();
            }

            scanner.nextLine();
        }
    }
/*
    Turn based tower game
    5 levels
    1- weaker mosnerter
    2- stronger 
    3- stronger
    4- stronger
    5- boss

    // 3 rooms types
    forage
    rest
    fight

    chance to have option not work or not find anything

    -items would provide modifiers to the player 
    -fighting should increase stats but should either be extremely difficult without items 
        or you should need to fight in forageing and for stats (training and at end of training stats increase)

    different weapons???

    different visuals??
    
    player will have 4 actions
       attack
       analyze
       item
       flee

*/
}