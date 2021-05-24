package main;

import required.enums.Direction;

import java.util.ArrayList;

// Class GameInfo berisi objek yang perlu dibagikan pada kelas lain (variabel global)

/*
    Variabel static itu "sedikit" melanggar prinsip OOP, namun,
    variabel yang berada di dalam GameInfo ini dibuat static,
    karena dalam cakupan game ini, hanya akan dibuat satu objek saja,
    tidak ada pembuatan objek kedua dst.

    Artinya, di dalam game ini:
    1. Hanya terdapat satu objek userInventory.
    2. Hanya terdapat satu objek theMerchant.
    3. Hanya terdapat satu objek ArrayList activeParty
    4. dst.

    Alasan lainnya, program ini masih single-thread,
    (hanya satu user yang melakukan eksekusi terhadap method public static void main).
*/

/*
 * https://stackoverflow.com/questions/11000975/are-static-variables-serialized-in-serialization-process
 */

public class GameInfo implements java.io.Serializable {
    public static Inventory userInventory; // inventory milik user
    public static Merchant theMerchant; // merchant selama permainan berlangsung
    public static ArrayList<Hero> activeParty; // hero yang dipakai untuk battle
    public static ArrayList<Hero> knownHeroes; // hero yang sudah dimiliki user
    public static ArrayList<Enemy> knownEnemies; // enemy yang sudah pernah dikalahkan
    public static ArrayList<Chapter> knownChapters; // chapter yang dapat dipilih user
    public static Chapter activeChapter; // chapter yang sedang dipilih
    public static Room activeRoom; // tempat hero berada saat bermain
    public static boolean isGameOver = false;

    // Ini untuk object serialization
    public Inventory objUserInventory;
    public Merchant objTheMerchant;
    public ArrayList<Hero> objActiveParty;
    public ArrayList<Hero> objKnownHeroes;
    public ArrayList<Enemy> objKnownEnemies;
    public ArrayList<Chapter> objKnownChapters;
    public Chapter objActiveChapter;
    public Room objActiveRoom;

    public void setStaticFields() {
        GameInfo.userInventory = objUserInventory;
        GameInfo.theMerchant = objTheMerchant;
        GameInfo.activeParty = objActiveParty;
        GameInfo.knownHeroes = objKnownHeroes;
        GameInfo.knownEnemies = objKnownEnemies;
        GameInfo.knownChapters = objKnownChapters;
        GameInfo.activeChapter = objActiveChapter;
        GameInfo.activeRoom = objActiveRoom;
    }

    public void setNonStaticFields() {
        objUserInventory = GameInfo.userInventory;
        objTheMerchant = GameInfo.theMerchant;
        objActiveParty = GameInfo.activeParty;
        objKnownHeroes = GameInfo.knownHeroes;
        objKnownEnemies = GameInfo.knownEnemies;
        objKnownChapters = GameInfo.knownChapters;
        objActiveChapter = GameInfo.activeChapter;
        objActiveRoom = GameInfo.activeRoom;
    }

    public static void moveToNextRoom(Direction userChoice) {
        if (activeRoom.getNextRoom().get(userChoice) != null) {
            activeRoom = activeRoom.getNextRoom().get(userChoice);
            System.out.println("\nYou are moving into the " + userChoice.toString() + "!");
        } else System.out.println("\nYou can't go in that direction!");
    }

}
