package required.builders.skill;

import main.Hero;
import main.Skill;
import required.Figure;
import required.multipliers.Multiplier;
import required.multipliers.*;
import required.enums.ArmorType;
import required.enums.SkillType;
import required.enums.WeaponType;

import java.util.ArrayList;

/* GoF Design Pattern - Builder
 * https://howtodoinjava.com/design-patterns/creational/builder-pattern-in-java/
 * https://blog.jayway.com/2012/02/07/builder-pattern-with-a-twist/
 * https://aidium.se/2015/01/09/builder-pattern-for-mandatory-values/
 */

public class SkillBuilders {

    private String name;
    private String description;
    private Figure owner;
    private int unlockedLevel;
    private SkillType skillType;
    private boolean targetFoe;
    private int timestamp;
    private final ArrayList<Multiplier> multipliers = new ArrayList<>();

    public SkillBuilders name(String vName) {
        if (vName == null || vName.isEmpty())
            throw new IllegalArgumentException("Name cannot be empty!");

        name = vName;
        return this;
    }

    public SkillBuilders description(String vDescription) {
        if (vDescription == null || vDescription.isEmpty())
            throw new IllegalArgumentException("Description cannot be empty!");

        description = vDescription;
        return this;
    }

    public SkillBuilders owner(Figure vOwner) {
        if (vOwner == null)
            throw new IllegalArgumentException("Owner cannot be empty!");

        owner = vOwner;
        return this;
    }

    public SkillBuilders unlockedLevel(int vUnlockedLevel) {
        if (vUnlockedLevel < 0)
            throw new IllegalArgumentException("Unlocked Level cannot be smaller than 0!");

        unlockedLevel = vUnlockedLevel;
        return this;
    }

    public SkillBuilders skillType(SkillType vSkillType) {
        if (vSkillType == null)
            throw new IllegalArgumentException("Skill Type cannot be empty!");

        skillType = vSkillType;
        return this;
    }

    public SkillBuilders targetFoe(boolean vTargetFoe) {
        targetFoe = vTargetFoe;
        return this;
    }

    public SkillBuilders timestamp(int vTimestamp) {
        if (vTimestamp < 1)
            throw new IllegalArgumentException("Timestamp cannot be smaller than 1!");

        timestamp = vTimestamp;
        return this;
    }

    public SkillBuilders strengthMultiplier(double vStrengthMultiplier) {
        if (vStrengthMultiplier < 0)
            throw new IllegalArgumentException("Strength Multiplier cannot be smaller than 0!");

        multipliers.add(new Strength(vStrengthMultiplier));
        return this;
    }

    public SkillBuilders healthMultiplier(double vHealthMultiplier) {
        if (vHealthMultiplier < 0)
            throw new IllegalArgumentException("Health Multiplier cannot be smaller than 0!");

        multipliers.add(new Health(vHealthMultiplier));
        return this;
    }

    public SkillBuilders shieldMultiplier(double vShieldMultiplier) {
        if (vShieldMultiplier < 0)
            throw new IllegalArgumentException("Shield Multiplier cannot be smaller than 0!");

        multipliers.add(new Shield(vShieldMultiplier));
        return this;
    }

    public Skill build() {
        return new Skill(name, description, owner, unlockedLevel,
                skillType, targetFoe, timestamp, multipliers);
    }

    public static void main(String[] args) {
        Hero hero = new Hero("Elon", "", 1, 0,
                50, 15, 20, WeaponType.SWORD, ArmorType.HEAVY);

        Skill skill = new SkillBuilders()
                .name("Slash")
                .description("-")
                .owner(hero)
                .unlockedLevel(1)
                .skillType(SkillType.LIGHT)
                .targetFoe(true) // target musuh
                .timestamp(15)
                .strengthMultiplier(0.2)
                .build();

        Skill skill1 = new SkillBuilders()
                .name("Heavy Heal")
                .description("-")
                .owner(hero)
                .unlockedLevel(1)
                .skillType(SkillType.HEAVY)
                .targetFoe(false) // target kawan
                .timestamp(45)
                .healthMultiplier(0.5)
                .shieldMultiplier(0.5)
                .build();

        skill.describe();
        skill1.describe();
    }
}
