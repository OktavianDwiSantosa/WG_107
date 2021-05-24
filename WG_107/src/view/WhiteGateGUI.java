package view;

import main.Box;
import main.Chapter;
import main.Enemy;
import main.GameEngine;
import main.GameInfo;
import main.Hero;
import main.Room;
import required.Item;
import required.Thing;
import required.enums.Direction;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class WhiteGateGUI extends BasicView {
    /*
        https://refactoring.guru/design-patterns/decorator
        https://refactoring.guru/design-patterns/decorator/java/example
        https://refactoring.guru/duplicate-observed-data
        https://refactoring.guru/design-patterns/mediator
    */

//    private static String centeredText(String text, int width) {
//        int textLength = text.length();
//        return width > textLength
//                ? /*true*/ (" ".repeat((width - textLength) / 2) + text)
//                : /*false*/ text;
//    }
//
//    private static void headerMenu(String menuName, int width) {
//        System.out.println("\n" + "=".repeat(width));
//        System.out.println(centeredText(menuName, width));
//        System.out.println("=".repeat(width) + "\n");
//    }
//
//    private static void footerMenu(String inputCommand, int width) {
//        System.out.println("\n" + "=".repeat(width));
//        System.out.print(inputCommand);
//    }

    public static void startMenu(GameEngine ge) {
        Scanner sc = new Scanner(System.in);
        int actionChoice = -1;

        while (actionChoice != 0) {
            headerMenu("Welcome to White Gate", 40);
            System.out.println("""
                    [1] Start a new game
                    [2] Load a save game
                    [0] Exit""");
            footerMenu("Your choice : ", 40);

            try {
                actionChoice = sc.nextInt();
                switch (actionChoice) {
                    case 0 -> System.out.println("You're leaving the game!\nSee you again!");
                    case 1 -> ge.startNewGame();
                    case 2 -> ge.loadGame();
                    default -> System.out.println("\nYour choice doesn't available!");
                }
            } catch (InputMismatchException e) {
                System.out.println("\nYour input doesn't valid!");
                sc = new Scanner(System.in);
            }
        }
    }

    public static void mainMenu(GameEngine ge) {
        Scanner sc = new Scanner(System.in);
        int actionChoice = -1;

        while (actionChoice != 0) {
            headerMenu("Main Menu", 40);
            // text blocks
            System.out.println("""
                    [1] Inventory
                    [2] Equipment
                    [3] Hero Status
                    [4] Active Party
                    [5] Enemies Record
                    [6] Chapter Selection
                    [7] Continue
                    [0] Exit Game""");
            footerMenu("Your choice : ", 40);

            try {
                actionChoice = sc.nextInt();
                switch (actionChoice) {
                    case 0 -> {
                        ge.saveGame();
                        System.out.println("You're exit the game!");
                        GameInfo.isGameOver = true;
                    }
                    case 1 -> GameInfo.userInventory.showAllClass();
                    case 2 -> equipmentMenu(sc);
                    case 3 -> GameInfo.knownHeroes.forEach(Hero::describe);
                    case 4 -> activePartyMenu(sc);
                    case 5 -> GameInfo.knownEnemies.forEach(Enemy::describe);
                    case 6 -> selectChapterMenu(sc);
                    case 7 -> adventureMenu(ge);
                    default -> System.out.println("Your choice doesn't available!");
                }
            } catch (InputMismatchException e) {
                System.out.println("\nYour input doesn't valid!");
                sc = new Scanner(System.in);
            }
        }
    }

    private static void printHeroes(ArrayList<Hero> arrHero) {
        for (int i = 0; i < arrHero.size(); i++)
            System.out.println("[" + (i + 1) + "] " + arrHero.get(i).getName());
    }

    private static Hero selectHero(Scanner sc, ArrayList<Hero> arrHero) {
        headerMenu("Select Hero", 40);
        printHeroes(arrHero);
        footerMenu("Choose Hero : ", 40);
        int selectHero = sc.nextInt();
        return arrHero.get(selectHero - 1);
    }

    private static void equipmentMenu(Scanner sc) {
        Hero tempHero = selectHero(sc, GameInfo.knownHeroes);

        for (Thing objItem : GameInfo.userInventory.getArrThing())
            if (((Item) objItem).getOwner() == tempHero) objItem.describe();
    }

    private static void addHeroToParty(Scanner sc) {
        if (GameInfo.activeParty.size() < 3) {
            Hero tempHero = selectHero(sc, GameInfo.knownHeroes);

            // Hero selection logic
            if (tempHero.addToParty()) System.out.println(tempHero.getName() +
                    " has been successfully added to active party");
            else System.out.println("Please select another hero!\n"
                    + tempHero.getName() + " has already been in party before!");
        }
    }

    private static void removeHeroFromParty(Scanner sc) {
        if (GameInfo.activeParty.size() > 0) {
            Hero tempHero = selectHero(sc, GameInfo.activeParty);

            // Hero selection logic
            if (tempHero.removeFromParty()) System.out.println(tempHero.getName() +
                    " has been successfully removed from active party");
        }
    }

    private static void activePartyMenu(Scanner sc) {
        String editChoice = "-";
        while (!editChoice.equals("N")) {
            headerMenu("Active Party", 40);
            printHeroes(GameInfo.activeParty);
            footerMenu("Edit active party? (Y/N) ", 40);
            editChoice = sc.next().toUpperCase();

            if ("Y".equals(editChoice)) {
                int editMenu = -1;
                while (editMenu != 0) {
                    headerMenu("Edit Active Party", 40);
                    System.out.println("""
                            [1] Add Hero to Party
                            [2] Remove Hero from Party
                            [0] Cancel""");
                    footerMenu("Your choice : ", 40);
                    editMenu = sc.nextInt();

                    switch (editMenu) {
                        case 0 -> System.out.println("""
                                You're cancelled editing!
                                Active party didn't changes!""");
                        case 1 -> addHeroToParty(sc);
                        case 2 -> removeHeroFromParty(sc);
                        default -> System.out.println("\nYour choice doesn't available!");
                    }
                }
            } else if ("N".equals(editChoice))
                System.out.println("\nOkay! Active party didn't changes!");
            else System.out.println("\nYour input doesn't valid!");
        }
    }

    private static void selectChapterMenu(Scanner sc) {
        ArrayList<Chapter> knownChapters = GameInfo.knownChapters;
        headerMenu("Chapter Selection", 40);
        for (int i = 0; i < knownChapters.size(); i++) {
            Chapter vChapter = knownChapters.get(i);
            System.out.println("[" + (i + 1) + "] " + vChapter.getName());
        }
        footerMenu("Select Chapter : ", 40);
        int chapterChoice = sc.nextInt();

        GameInfo.activeChapter = knownChapters.get(chapterChoice - 1);
        for (Thing room : GameInfo.activeChapter.getArrThing()) {
            if (((Room) room).getRoomID() == 1) {
                // langsung ambil room ke-1 dari chapter pilihan
                GameInfo.activeRoom = (Room) room;
                break;
            }
        }
    }

    public static void adventureMenu(GameEngine ge) {
        Scanner sc = new Scanner(System.in);
        int actionChoice = -1;

        while (actionChoice != 0) {
            System.out.println("\n" + "=".repeat(40));
            System.out.print("\nNow you're in ");
            GameInfo.activeRoom.describe();

            headerMenu("Your Action", 40);
            System.out.println("""
                    [1] Move to the next room
                    [2] Attack the enemy
                    [3] Open available box""");
            if (GameInfo.activeRoom.getArrThing().contains(GameInfo.theMerchant))
                System.out.println("[4] Visit the merchant");
            System.out.println("[0] Main menu");
            footerMenu("Choose action : ", 40);

            try {
                actionChoice = sc.nextInt();
                switch (actionChoice) {
                    case 0 -> mainMenu(ge);
                    case 1 -> moveToNextRoom(sc, ge);
                    case 2 -> attackTheEnemy(ge);
                    case 3 -> openAvailableBox(sc);
                    case 4 -> visitTheMerchant();
                    default -> System.out.println("\nYour choice doesn't available!");
                }
            } catch (InputMismatchException e) {
                System.out.println("\nYour input doesn't valid!");
                sc = new Scanner(System.in);
            }
        }
    }

    private static void moveToNextRoom(Scanner sc, GameEngine ge) {
        System.out.print("\nChoose direction (W,A,S,D): ");
        String roomDirection = sc.next().toUpperCase();

        try {
            GameInfo.moveToNextRoom(Direction.valueOf(roomDirection));
            ge.saveGame();
        } catch (IllegalArgumentException e) { // jika roomDirection bukan (W,A,S,D)
            System.out.println("\nYour input doesn't valid!");
        }
    }

    private static void attackTheEnemy(GameEngine ge) {
        ArrayList<Thing> tempEnemy = GameInfo.activeRoom.getOneClass("Enemy");
        if (tempEnemy.isEmpty())
            System.out.println("\nThere's no enemy to be attacked in this room");
        else ge.getBattleSystem().startBattle();
    }

    private static void openAvailableBox(Scanner sc) {
        ArrayList<Thing> tempBox = GameInfo.activeRoom.getOneClass("Box");
        if (tempBox.isEmpty()) {
            System.out.println("\nThere's no box to be opened in this room");
        } else {
            headerMenu("Available Boxes", 40);
            GameInfo.activeRoom.showOneClass("Box");
            footerMenu("Choose the box to open : ", 40);

            try {
                int boxChoice = sc.nextInt();
                if (boxChoice < 1 || boxChoice > tempBox.size()) {
                    System.out.println("\nYour choice doesn't available!");
                } else {
                    ((Box) tempBox.get(boxChoice - 1)).opened();
                }
            } catch (InputMismatchException e) { // jika input bukan integer
                System.out.println("\nYour input doesn't valid!");
            }
        }
    }

    private static void visitTheMerchant() {
        if (GameInfo.activeRoom.getArrThing().contains(GameInfo.theMerchant))
            GameInfo.theMerchant.merchantMenu();
        else System.out.println("\nYour choice doesn't available!");
    }

}
