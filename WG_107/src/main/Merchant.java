package main;

import required.Item;
import required.Thing;
import required.enums.ModifiedType;
import view.BasicView;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public final class Merchant extends Thing {
    private static Merchant theMerchant;

    private Merchant() {
        super("Stranger Merchant", "He was just a strange stranger merchant");
    }

    // Singleton
    public static Merchant getMerchant() {
        if (theMerchant == null) {
            theMerchant = new Merchant();
        }
        return theMerchant;
    }

    public void merchantMenu() {
        Scanner sc = new Scanner(System.in);
        int selectMenu = -1;

        while (selectMenu != 0) {
            BasicView.headerMenu("Welcome Stranger!", 40);
            System.out.println("""
                    [1] Buy
                    [2] Sell
                    [3] Craft
                    [4] Upgrade
                    [0] Cancel""");
            BasicView.footerMenu("Choose menu : ", 40);
            selectMenu = sc.nextInt();

            switch (selectMenu) {
                case 0 -> System.out.println("See you later stranger!");
                case 1 -> transactionMenu(ModifiedType.BUY);
                case 2 -> transactionMenu(ModifiedType.SELL);
                case 3 -> transactionMenu(ModifiedType.CRAFT);
                case 4 -> transactionMenu(ModifiedType.UPGRADE);
                default -> System.out.println("Your choice doesn't available!");
            }
        }
    }

    private void itemsLooping(ArrayList<Thing> tempItem) {
        for (int i = 0; i < tempItem.size(); i++) {
            Thing objItem = tempItem.get(i);
            System.out.print("[" + (i + 1) + "] ");
            objItem.describe();
        }
        System.out.println("\n" + "=".repeat(30));
    }

    private ArrayList<Thing> itemTypeToModified(ModifiedType vModifiedType, Thing vOwner) {
        ArrayList<Thing> tempItem = null;
        Scanner sc = new Scanner(System.in);
        int selectItem = -1;

        while (selectItem != 0) {
            BasicView.headerMenu((vModifiedType.toString() + " Item"), 40);
            System.out.println("[1] Armor");
            if (vModifiedType == ModifiedType.BUY || vModifiedType == ModifiedType.SELL) {
                System.out.println("[2] Material");
                System.out.println("[3] MedKit");
                System.out.println("[4] Weapon");
            } else {
                System.out.println("[2] Weapon");
            }
            System.out.println("[0] Cancel");
            BasicView.footerMenu("Select item type : ", 40);

            try {
                selectItem = sc.nextInt();

                if (selectItem == 0) {
                    System.out.println("You canceled the transaction!");
                } else if (selectItem == 1) {
                    tempItem = vOwner.getOneClass("Armor");
                } else if (vModifiedType == ModifiedType.BUY || vModifiedType == ModifiedType.SELL) {
                    if (selectItem == 2) {
                        tempItem = vOwner.getOneClass("Material");
                    } else if (selectItem == 3) {
                        tempItem = vOwner.getOneClass("MedKit");
                    } else if (selectItem == 4) {
                        tempItem = vOwner.getOneClass("Weapon");
                    } else {
                        System.out.println("\nYour choice doesn't available!\n");
                    }
                } else if (selectItem == 2) {
                    tempItem = vOwner.getOneClass("Weapon");
                } else {
                    System.out.println("\nYour choice doesn't available!\n");
                }
            } catch (InputMismatchException e) { // jika input bukan integer
                System.out.println("Your input doesn't valid!");
                sc = new Scanner(System.in);
            }
        }

        return tempItem;
    }

    private void transactionMenu(ModifiedType vModifiedType) {
        ArrayList<Thing> tempItem;
        if (vModifiedType == ModifiedType.BUY) {
            tempItem = itemTypeToModified(vModifiedType, this);
        } else {
            tempItem = itemTypeToModified(vModifiedType, GameInfo.userInventory);
        }

        if (tempItem != null) {
            Scanner sc = new Scanner(System.in);

            BasicView.headerMenu((vModifiedType.toString() + " Item"), 40);
            itemsLooping(tempItem);
            BasicView.footerMenu("Select item to " + vModifiedType.toString(), 40);

            try {
                int selectItem = sc.nextInt();
                if (((Item) tempItem.get(selectItem - 1)).modified(vModifiedType)) {
                    System.out.println("Well Done stranger!\nItem has been successfully "
                            + vModifiedType + "!");
                } else {
                    System.out.println("Sorry stranger!\nItem cannot be "
                            + vModifiedType + "!");
                }
            } catch (InputMismatchException e) {
                System.out.println("\nYour input doesn't valid!");
            }
        } else {
            System.out.println("Sorry stranger!\nSeems you have no item to "
                    + vModifiedType + "!");
        }
    }

}
