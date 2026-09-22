
import javax.swing.*;
import java.util.*;

public class Main {


    static boolean keepPlaying = true;
    static double difficulty = 1.0;
    
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


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (keepPlaying) {
            System.out.println("======================== \nWelcome to the tower \nReach floor 5 \nGood Luck! \n======================= \nEnter player name:");
            String playerName = scanner.nextLine();

            Player player = new Player(playerName);

            System.out.println("\nSelect Difficulty(1-3):");
            System.out.println("1. Easy (0.5x)");
            System.out.println("2. Normal (1.0x)");
            System.out.println("3. Hard (1.5x)");
            System.out.print("Choice: ");
            
            int diffChoice = scanner.nextInt();
            scanner.nextLine(); 

            if (diffChoice == 1) {
                difficulty = 0.5;
            } else if (diffChoice == 3) {
                difficulty = 1.5;
            } else {
                difficulty = 1.0;
            }

            for (int level = 1; level <= 5; level++) {
                if (!player.isAlive() || !keepPlaying) {
                    break;
                }

                Monster monster = spawnMonsterForLevel(level);
                System.out.println("\n============================");
                System.out.println("   ENTERING TOWER LEVEL " + level);
                System.out.println("============================");
                System.out.println("A wild " + monster.getName() + " appears!");

                boolean fled = false;
                while (monster.isAlive() && player.isAlive() && !fled && keepPlaying) {
                    System.out.println("\n--- COMBAT vs " + monster.getName() + " ---");
                    System.out.println(player.playerStats() + " | " + monster.getName());

                    System.out.println("1. Attack");
                    System.out.println("2. Analyze");
                    System.out.println("3. Flee");
                    System.out.print("Combat Choice: ");

                    int combatChoice = scanner.nextInt();

                    if (combatChoice == 1) {
                        monster.takeDmg(player.getStrength());
                        System.out.println(player.getName() + " hit " + monster.getName() + " for " + player.getStrength() + " damage!");

                        if (monster.isAlive()) {
                            player.takeDmg(monster.getAttack());
                            System.out.println(monster.getName() + " struck back for " + monster.getAttack() + " damage!");
                        }
                    } else if (combatChoice == 2) {
                        System.out.println("[ANALYZE] " + player.playerStats() + String.format(" (%.1f%% HP)", player.healthPercent() * 100.0));
                        System.out.println("[ANALYZE] " + monster.monsterStats() + String.format(" | EXP Value: %.1f", monster.getExp()));
                       
                        if (monster.isAlive()) {
                            player.takeDmg(monster.getAttack());
                            System.out.println(monster.getName() + " struck back for " + monster.getAttack() + " damage!");
                        }
                        
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

                if (!player.isAlive() || fled || !keepPlaying) {
                    continue;
                }

                System.out.println("\nVictory! Defeated " + monster.getName() + "!");

                if (level < 5) {
                    boolean inRoomPhase = true;
                    while (inRoomPhase && player.isAlive() && keepPlaying) {
                        System.out.println("\n--- POST-COMBAT CHAMBER (Floor " + level + " Cleared) ---");
                        System.out.println(player.playerStats());
                        System.out.println("1. Train Room");
                        System.out.println("2. Rest Room");
                        System.out.println("3. Fight Room (Ascend)");
                        System.out.print("Room choice: ");

                        int hubChoice = scanner.nextInt();

                        if (hubChoice == 1) {
                            System.out.println("  [1] +10 Max HP  |  [2] +3 Attack");
                            System.out.print("  Select focus: ");
                            int trainChoice = scanner.nextInt();

                            if (trainChoice == 1) {
                                player.trainHealth(10.0);
                                System.out.println("Max HP boosted!");
                                inRoomPhase = false;
                            } else if (trainChoice == 2) {
                                player.trainAttack(3);
                                System.out.println("Attack boosted!");
                                inRoomPhase = false;
                            }
                        } else if (hubChoice == 2) {
                            player.rest(15.0);
                            System.out.println("Rested. " + player.playerStats());
                            inRoomPhase = false;
                        } else if (hubChoice == 3) {
                            inRoomPhase = false;
                        }
                    }
                }
            }

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