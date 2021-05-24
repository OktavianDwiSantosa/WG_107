package main;

import main.item.Material;
import main.item.Weapon;
import required.builders.equipment.EquipmentBuilders;
import required.enums.ArmorType;
import required.enums.Direction;
import required.enums.RarityType;
import required.enums.WeaponType;
import view.WhiteGateGUI;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/*
    https://mkyong.com/java/how-to-read-and-write-java-object-to-a-file/
    https://www.geeksforgeeks.org/clone-method-in-java-2/
    https://refactoring.guru/design-patterns/memento
    https://refactoring.guru/design-patterns/prototype
    https://refactoring.guru/design-patterns/observer
    https://wiki.sei.cmu.edu/confluence/pages/viewpage.action?pageId=88487725
*/

// class GameEngine tidak dibuat final karena nanti bisa loadGame
public class GameEngine implements java.io.Serializable {
    private final BattleSystem battleSystem = BattleSystem.getBattleSystem();

    public void loopGame() {
        while (!GameInfo.isGameOver) {
            WhiteGateGUI.adventureMenu(this);
        }
        GameInfo.isGameOver = false;
    }

    public void startNewGame() {
        System.out.println("Starting a new game!");

        // ini harus diperbaiki lagi dengan objek asli dari game ini
        GameInfo.userInventory = Inventory.getInventory();
        GameInfo.theMerchant = Merchant.getMerchant();
        Chapter initChapter = new Chapter("The Broken World", 1);
        Room room1 = new Room("Basement", "The basement of destroyed building", 1);
        Room room2 = new Room("Armory", "Soldier Armory", 2);
        room1.addNextRoom(Direction.D, room2);
        room2.addNextRoom(Direction.A, room1);
        initChapter.addThing(room1);
        GameInfo.activeChapter = initChapter;
        GameInfo.activeRoom = (Room) initChapter.getOneClass("Room").get(0);
        GameInfo.knownChapters = new ArrayList<>();
        GameInfo.knownHeroes = new ArrayList<>();
        GameInfo.activeParty = new ArrayList<>();
        GameInfo.knownEnemies = new ArrayList<>();

        loopGame();
    }

    public void saveGame() {
        try {
            GameInfo gi = new GameInfo();
            gi.setNonStaticFields();
            FileOutputStream fos = new FileOutputStream("WG.txt");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this);
            oos.writeObject(gi);
            oos.flush();
            oos.close();
            System.out.println("Game Saved\n");
        } catch (Exception e) {
            System.out.println("ERROR! Can't save the data!");
            System.out.println(e.getClass() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void loadGame() {
        try {
            GameInfo gi;
            FileInputStream fis = new FileInputStream("WG.txt");
            ObjectInputStream ois = new ObjectInputStream(fis);
            ois.readObject();
            gi = (GameInfo) ois.readObject();
            ois.close();
            System.out.println("Game Loaded\n");
            gi.setStaticFields();
            loopGame();
        } catch (Exception e) {
            // coba hapus file WG.txt, nanti akan muncul ini
            System.out.println("\nSave game not found!\nPlease start a new game!");
            /*System.out.println("ERROR! Can't load the data!");
            System.out.println(e.getClass() + ": " + e.getMessage());
            e.printStackTrace();*/
        }
    }

    // Getter
    public BattleSystem getBattleSystem() { return battleSystem; }

    public static void main(String[] args) {
        // Contoh pembuatan hero
        Hero elon = new Hero("Elon",
                "An older brother who wants to save his younger brother.",
                1, 0, 40, 10, 13,
                WeaponType.SWORD, ArmorType.HEAVY);

        Hero farlan = new Hero("Farlan",
                "A soldier loyal to his comrade.",
                1, 0, 30, 20, 7,
                WeaponType.PROJECTILE, ArmorType.ROBE);

        // Contoh pembuatan enemy
        Enemy enemy1 = new Enemy("Robot A",
                "A little robot that is strong enough", 1,
                10, 50, 10, 15, 6);

        Enemy enemy2 = new Enemy("Robot B",
                "Big dangerous robot", 3,
                23, 95, 17, 21, 18);

        Enemy enemy3 = new Enemy("Robot C",
                "A very vicious robot", 8,
                50, 170, 31, 38, 50);


        // Contoh pembuatan material
        Material material1 = new Material("Iron Ore",
                "An ordinary iron ore",
                enemy1, 100, RarityType.COMMON);

        Material material2 = new Material("Gold",
                "Precious metal which is quite valuable",
                enemy1, 150, RarityType.RARE);

        Material material3 = new Material("Diamond",
                "This is a very precious diamond",
                enemy2, 200, RarityType.EPIC);

        Material material4 = new Material("Platinum",
                "Stunning platinum",
                enemy3, 200, RarityType.EPIC);

        // Contoh pembuatan weapon
        Weapon weapon1 = EquipmentBuilders.newWeapon()
                .name("Thalassa")
                .description("An ordinary sword.")
                .owner(elon)
                .price(300)
                .rarity(RarityType.COMMON)
                .level(1)

                // lihat class WeaponType untuk menentukan jenis multiplier yang diperlukan
                .strengthMultiplier(0.2)
                .shieldMultiplier(0.08)
                .weaponType(WeaponType.SWORD) // --> butuh strength & shield multiplier

                // sesuaikan material dengan kebutuhan
                .requiredMaterials("Iron Ore", 10)
                .requiredMaterials("Gold", 3)
                .build();

        Weapon weapon2 = EquipmentBuilders.newWeapon()
                .name("Soul Spear")
                .description("An automatic spear.")
                .owner(farlan)
                .price(300)
                .rarity(RarityType.COMMON)
                .level(1)

                // lihat class WeaponType untuk menentukan jenis multiplier yang diperlukan
                .strengthMultiplier(0.49)
                .weaponType(WeaponType.PROJECTILE) // --> butuh strength saja

                // sesuaikan material dengan kebutuhan
                .requiredMaterials("Iron Ore", 7)
                .requiredMaterials("Gold", 2)
                .build();

        // start game
        GameEngine ge = new GameEngine();
        WhiteGateGUI.startMenu(ge);

        // Contoh objek-objek ketika di battleSystem
        /*Hero hero1 = new Hero("Elon", "A Dedicated Engineer", 1,
                0, 50, 12, 10,
                WeaponType.SWORD, ArmorType.HEAVY);

        Skill skill1 = new SkillBuilders()
                .name("Elon Skill A")
                .description("-")
                .owner(hero1)
                .unlockedLevel(1)
                .skillType(SkillType.LIGHT)
                .targetFoe(true)
                .timestamp(15)
                .strengthMultiplier(0.25)
                .build();

        Skill skill2 = new SkillBuilders()
                .name("Elon Skill B")
                .description("-")
                .owner(hero1)
                .unlockedLevel(1)
                .skillType(SkillType.NORMAL)
                .targetFoe(true)
                .timestamp(30)
                .strengthMultiplier(0.5)
                .build();

        Skill skill3 = new SkillBuilders()
                .name("Elon Skill C")
                .description("-")
                .owner(hero1)
                .unlockedLevel(1)
                .skillType(SkillType.HEAVY)
                .targetFoe(true)
                .timestamp(45)
                .strengthMultiplier(0.75)
                .build();

        hero1.addThing(skill1);
        hero1.addThing(skill2);
        hero1.addThing(skill3);

        Hero hero2 = new Hero("Tony", "Tech Pro", 1,
                0, 40, 15, 10,
                WeaponType.CYBERWARE, ArmorType.ROBE);

        Skill skill4 = new SkillBuilders()
                .name("Tony Skill A")
                .description("-")
                .owner(hero2)
                .unlockedLevel(1)
                .skillType(SkillType.LIGHT)
                .targetFoe(true)
                .timestamp(15)
                .strengthMultiplier(0.25)
                .build();

        Skill skill5 = new SkillBuilders()
                .name("Tony Skill B")
                .description("-")
                .owner(hero2)
                .unlockedLevel(1)
                .skillType(SkillType.NORMAL)
                .targetFoe(true)
                .timestamp(30)
                .strengthMultiplier(0.5)
                .build();

        Skill skill6 = new SkillBuilders()
                .name("Tony Skill C")
                .description("-")
                .owner(hero2)
                .unlockedLevel(1)
                .skillType(SkillType.HEAVY)
                .targetFoe(true)
                .timestamp(45)
                .strengthMultiplier(0.75)
                .build();

        hero2.addThing(skill4);
        hero2.addThing(skill5);
        hero2.addThing(skill6);

        Enemy enemy1 = new Enemy("Robot A", "", 1,
                10, 50, 10, 15, 6);

        Skill skill7 = new SkillBuilders()
                .name("Enemy1 Skill A")
                .description("-")
                .owner(enemy1)
                .unlockedLevel(1)
                .skillType(SkillType.LIGHT)
                .targetFoe(true)
                .timestamp(15)
                .strengthMultiplier(0.25)
                .build();

        Skill skill8 = new SkillBuilders()
                .name("Enemy1 Skill B")
                .description("-")
                .owner(enemy1)
                .unlockedLevel(1)
                .skillType(SkillType.NORMAL)
                .targetFoe(true)
                .timestamp(30)
                .strengthMultiplier(0.5)
                .build();

        Skill skill9 = new SkillBuilders()
                .name("Enemy1 Skill C")
                .description("-")
                .owner(enemy1)
                .unlockedLevel(1)
                .skillType(SkillType.HEAVY)
                .targetFoe(true)
                .timestamp(45)
                .strengthMultiplier(0.75)
                .build();

        enemy1.addThing(skill7);
        enemy1.addThing(skill8);
        enemy1.addThing(skill9);

        Enemy enemy2 = new Enemy("Robot B", "", 2,
                12, 40, 12, 10, 10);

        Skill skill10 = new SkillBuilders()
                .name("Enemy2 Skill A")
                .description("-")
                .owner(enemy2)
                .unlockedLevel(1)
                .skillType(SkillType.LIGHT)
                .targetFoe(true)
                .timestamp(15)
                .strengthMultiplier(0.25)
                .build();

        Skill skill11 = new SkillBuilders()
                .name("Enemy2 Skill B")
                .description("-")
                .owner(enemy2)
                .unlockedLevel(1)
                .skillType(SkillType.NORMAL)
                .targetFoe(true)
                .timestamp(30)
                .strengthMultiplier(0.5)
                .build();

        Skill skill12 = new SkillBuilders()
                .name("Enemy2 Skill C")
                .description("-")
                .owner(enemy2)
                .unlockedLevel(1)
                .skillType(SkillType.HEAVY)
                .targetFoe(true)
                .timestamp(45)
                .strengthMultiplier(0.75)
                .build();

        enemy2.addThing(skill10);
        enemy2.addThing(skill11);
        enemy2.addThing(skill12);

        GameInfo.activeParty.add(hero1);
        GameInfo.activeParty.add(hero2);
        GameInfo.knownHeroes.addAll(GameInfo.activeParty);

        Box box1 = new Box("Steel Box", "Unlocked box", 10);
        Weapon sword1 = EquipmentBuilders.newWeapon()
                .name("Long Sword")
                .description("A solid steel sword")
                .owner(hero1)
                .price(500)
                .rarity(RarityType.RARE)
                .level(1)
                .strengthMultiplier(0.5)
                .shieldMultiplier(0.2)
                .weaponType(WeaponType.SWORD)
                .build();
        box1.addThing(sword1);

        Room room2 = new Room("Soldier Armory", "This room is full of enemy", 2);
        HashMap<Direction, Room> nextRoom1 = new HashMap<>();
        nextRoom1.put(Direction.D, room2);

        room2.addThing(enemy1);
        room2.addThing(enemy2);
        room2.addThing(box1);

        HashMap<Direction, Room> nextRoom2 = new HashMap<>();
        nextRoom2.put(Direction.A, GameInfo.activeRoom);

        GameInfo.activeRoom.setNextRoom(nextRoom1);
        room2.setNextRoom(nextRoom2);

        WhiteGateGUI.startMenu(ge);*/
    }
}
