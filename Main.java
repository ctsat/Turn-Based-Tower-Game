import java.util.*;

public class Main {

    // Class variables for overall game status and difficulty multiplier
    static boolean keepPlaying = true;
    static double difficulty = 1.0;
    
    // Makes sure scanner doesn't error when getting incorrect values
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

    // Overloaded version that also checks the number is in range
    public static int getValidInt(Scanner scanner, String prompt, int min, int max) {
        while (true) {
            int value = getValidInt(scanner, prompt);
            if (value >= min && value <= max) {
                return value;
            }
            System.out.println("Please enter a number from " + min + " to " + max + ".");
        }
    }

    // Asks if the user wants to play again
    public static boolean askPlayAgain(Scanner scanner) {
        System.out.print("Would you like to play again? (yes/no): ");
        String response = scanner.nextLine().trim();
        return response.equalsIgnoreCase("yes") || response.equalsIgnoreCase("y");
    }

    // Averages visited floors (static, only uses the array)
    public static double averageVisited(int[] values) {
        int sum = 0;
        int count = 0;
        for (int value : values) {
            if (value > 0) {
                sum += value;
                count++;
            }
        }
        if (count == 0) {
            return 0.0;
        }
        return (double) sum / count;
    }

    // Returns the sum of an array (static, only uses the array)
    public static double sum(double[] values) {
        double total = 0.0;
        for (double value : values) {
            total += value;
        }
        return total;
    }

    // Prints the end-of-run report
    public static void printRunReport(Player player, int[] turnsPerFloor, double[] damageTaken, double totalExp) {
        System.out.println("\n=========== RUN REPORT ===========");
        for (int i = 0; i < turnsPerFloor.length; i++) {
            if (turnsPerFloor[i] > 0) {
                System.out.println(String.format("Floor %d: %d turns, %.1f damage taken", i + 1, turnsPerFloor[i], damageTaken[i]));
            }
        }
        System.out.println(String.format("Average turns per floor: %.1f", averageVisited(turnsPerFloor)));
        System.out.println(String.format("Total damage taken: %.1f", sum(damageTaken)));
        System.out.println(String.format("Score (EXP earned): %.1f", totalExp));
        System.out.println("Final ATK: " + player.getAttack());
        Item best = player.findStrongestWeapon();
        if (best != null) {
            System.out.println("Strongest weapon: " + best);
        } else {
            System.out.println("Strongest weapon: none");
        }
        System.out.println(player.inventoryString());
        System.out.println("Items dropped (all games): " + Item.getItemsCreated());
        System.out.println("==================================");
    }
// STYLE:printing a line of equal signs makes the games announcment clear and easy to read

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Main game loop that keeps running games until user stops
        while (keepPlaying) {
            // Displays game title and reads player name
            System.out.println("======================== \nWelcome to the tower \nReach floor 5 \nGood Luck! \n======================= \nEnter player name:");
            String playerName = scanner.nextLine();

            // Prompts user for game difficulty selection
            System.out.println("\nSelect Difficulty(1-3):");
            System.out.println("1. Easy (0.5x)");
            System.out.println("2. Normal (1.0x)");
            System.out.println("3. Hard (1.5x)");
            
            int diffChoice = getValidInt(scanner, "Choice: ", 1, 3);
// SUGGESTION:maybe make difficulty affect the strenght of enemies as well and the odds to get items from a victory

            // Assigns difficulty and starting stats according to choice
            Player player;
            if (diffChoice == 1) {
                difficulty = 0.5;
                player = new Player(playerName, 30, 4);
            } else if (diffChoice == 3) {
                difficulty = 1.5;
                player = new Player(playerName, 25, 5);
            } else {
                difficulty = 1.0;
                player = new Player(playerName);
            }
// PRAISE: I like the use of a variety of monsters with a variety of stats
            // Monster for each floor (initializer list)
            Monster[] floors = {
                new Monster("Goblin Runt", 15.0, 4, 10.0),
                new Monster("Cave Spider", 30.0, 7, 25.0),
                new Monster("Orc Guard", 50.0, 11, 45.0),
                new Monster("Stone Golem", 80.0, 16, 70.0),
                new Monster("Tower Golem", 130.0, 24, 150.0)
            };
            // Per-floor stats for the run report
            int[] turnsPerFloor = new int[floors.length];
            double[] damageTaken = new double[floors.length];
            boolean[] cleared = new boolean[floors.length];
            // Total EXP earned, shown as the score
            double totalExp = 0.0;

            // Loop iterating through tower levels 1 to 5
            for (int level = 1; level <= floors.length; level++) {
                // Exits loop if player died or stopped playing
                if (!player.isAlive() || !keepPlaying) {
                    break;
                }

                // Generates floor monster and prints entrance message
                Monster monster = floors[level - 1];
                // Revives a defeated monster if returning after fleeing
                if (!monster.isAlive()) {
                    monster.setHealth(monster.getMaxHealth());
                }
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
                    System.out.println("4. Drink Potion");

                    int combatChoice = getValidInt(scanner, "Combat Choice: ", 1, 4);
                    // No potions means no turn is used
                    if (combatChoice == 4 && player.findItemSlot(Item.potion) == -1) {
                        System.out.println("You don't have any potions!");
                        continue;
                    }
                    turnsPerFloor[level - 1]++;

                    // Option 1: Player attacks monster; monster counterattacks if alive
                    if (combatChoice == 1) {
                        monster.takeDmg(player.getAttack());
                        System.out.println(player.getName() + " hit " + monster.getName() + " for " + player.getAttack() + " damage!");

                        if (monster.isAlive()) {
                            damageTaken[level - 1] += player.takeDmg(monster.getAttack());
                            System.out.println(monster.getName() + " struck back for " + monster.getAttack() + " damage!");
                        }
                    // Option 2: Inspect stats; monster strikes back
                    } else if (combatChoice == 2) {
                        System.out.println("[ANALYZE] " + player.playerStats() + String.format(" (%.1f%% HP)", player.healthPercent() * 100.0));
                        System.out.println("[ANALYZE] " + monster.monsterStats() + String.format(" | EXP Value: %.1f", monster.getExp()));
                        System.out.println("[ANALYZE] " + player.inventoryString());

                        if (monster.isAlive()) {
                            damageTaken[level - 1] += player.takeDmg(monster.getAttack());
                            System.out.println(monster.getName() + " struck back for " + monster.getAttack() + " damage!");
                        }

                    // Option 3: Attempt 66% chance flee; takes counterattack on failure
                    } else if (combatChoice == 3) {
                        if (Math.random() < 0.66) {
                            System.out.println(player.getName() + " successfully fled down the stairs!");
                            // Fleeing costs a random item, or some HP if inventory is empty
                            Item lost = player.loseRandomItem();
                            if (lost != null) {
                                System.out.println("In the rush, " + player.getName() + " dropped " + lost + "!");
                            } else {
                                double stumble = Math.min(player.getMaxHealth() * 0.15, player.getHealth() - 1);
                                if (stumble > 0) {
                                    damageTaken[level - 1] += player.takeDmg(stumble);
                                    System.out.println(player.getName() + String.format(" tripped on the stairs and lost %.1f HP!", stumble));
                                }
                            }
                            fled = true;
                            level = Math.max(0, level - 2);
                        } else {
                            System.out.println(player.getName() + " failed to flee!");
                            if (monster.isAlive()) {
                                damageTaken[level - 1] += player.takeDmg(monster.getAttack());
                                System.out.println(monster.getName() + " struck back for " + monster.getAttack() + " damage!");
                            }
                        }
                    // Option 4: Drink a potion; monster strikes back
                    } else if (combatChoice == 4) {
                        Item potion = player.drinkPotion();
                        System.out.println(player.getName() + " drank a " + potion + "! " + player.playerStats());

                        if (monster.isAlive()) {
                            damageTaken[level - 1] += player.takeDmg(monster.getAttack());
                            System.out.println(monster.getName() + " struck back for " + monster.getAttack() + " damage!");
                        }
                    }
                }

                // Skips post-combat phase if combat ended by dying, fleeing, or quitting
                if (!player.isAlive() || fled || !keepPlaying) {
                    continue;
                }

                System.out.println("\nVictory! Defeated " + monster.getName() + "!");

                // First clear gives EXP and a chance at an item
                if (!cleared[level - 1]) {
                    cleared[level - 1] = true;
                    totalExp += monster.getExp();
                    System.out.println(String.format("+%.1f EXP (Score: %.1f)", monster.getExp(), totalExp));
                    if (Math.random() < 0.6) {
                        Item drop = Item.randomDrop(level);
                        if (player.addItem(drop)) {
                            System.out.println(monster.getName() + " dropped " + drop + "!");
                        } else {
                            System.out.println(monster.getName() + " dropped " + drop + ", but your inventory is full.");
                        }
                    }
                }

                // Post-combat room choice loop for floors 1 to 4
                if (level < floors.length) {
                    boolean inRoomPhase = true;
                    while (inRoomPhase && player.isAlive() && keepPlaying) {
                        System.out.println("\n--- POST-COMBAT CHAMBER (Floor " + level + " Cleared) ---");
                        System.out.println(player.playerStats());
                        System.out.println(player.inventoryString());
                        System.out.println("1. Train Room");
                        System.out.println("2. Rest Room");
                        System.out.println("3. Fight Room (Ascend)");

                        int hubChoice = getValidInt(scanner, "Room Choice: ", 1, 3);

                        // Option 1: Train to boost Max HP or Attack
                        if (hubChoice == 1) {
                            System.out.println("  [1] +10 Max HP  |  [2] +3 Attack");
                            int trainChoice = getValidInt(scanner, "  Select focus: ", 1, 2);

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

            // Victory or game over screen
            if (player.isAlive()) {
                System.out.println("\nCongratulations " + player.getName() + "! You cleared all 5 levels of the tower!");
            } else {
                System.out.println("\nGame Over </3");
            }

            printRunReport(player, turnsPerFloor, damageTaken, totalExp);

            // Asks to play again
            keepPlaying = askPlayAgain(scanner);
        }
        System.out.println("Thanks for playing!");
        scanner.close();
    }
}