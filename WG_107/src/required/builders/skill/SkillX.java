package required.builders.skill;

import main.Hero;
import required.Figure;
import required.Thing;
import required.enums.ArmorType;
import required.enums.SkillType;
import required.enums.WeaponType;
import required.multipliers.Health;
import required.multipliers.Multiplier;
import required.multipliers.Shield;
import required.multipliers.Strength;

import java.util.ArrayList;

public class SkillX extends Thing {
    private Figure owner; // Pemilik skill --> ketika cukup level --> unlocked skill
    private int unlockedLevel; // terbuka ketika unlockedLevel == owner.level
    private SkillType skillType; // Light, Normal, & Heavy
    private boolean targetFoe; // apakah Skill ini dilancarkan untuk pihak lawan?
    private int timestamp; // waktu yang dibutuhkan untuk melancarkan Skill
    private ArrayList<Multiplier> multipliers;
    private Figure tempTarget; // target dari Skill --> dipilih di battleSystem

    @Override
    public void describe() {
        String vTarget = isTargetFoe() ? "Foe" : "Ally"; // ternary operator --> true == "Foe"

        System.out.println(getName());
        System.out.printf("%-10s%-10s%-10s", "Target", "Type", "Timestamp");
        multipliers.forEach(Multiplier::printText);
        System.out.printf("%n%-10s%-10s%-10s", vTarget, skillType, timestamp);
        multipliers.forEach(multiplier -> multiplier.printSkillValue(owner));
    }

    public void damage() { //  hanya bisa merusak lawan
        for (Multiplier multiplier : multipliers)
            if (multiplier instanceof Strength)
                multiplier.applySkValue(owner, tempTarget);
    }

    public void repair() { // hanya bisa memperbaiki kawan
        for (Multiplier multiplier : multipliers)
            if (multiplier instanceof Health)
                multiplier.applySkValue(owner, tempTarget);

        for (Multiplier multiplier : multipliers)
            if (multiplier instanceof Shield)
                multiplier.applySkValue(owner, tempTarget);
    }

    public void execute() {
        // rusak shield lawan dulu --> baru kurangi health lawan
        damage(); // jika terdapat strength multiplier

        // tambah shield & health kawan
        repair(); // jika terdapat health dan shield multiplier
    }

    public int getDamage() {
        for (Multiplier multiplier : multipliers)
            if (multiplier instanceof Strength)
                return multiplier.getValue(owner);
        return 0;
    }

    public int getHeal() {
        for (Multiplier multiplier : multipliers)
            if (multiplier instanceof Health)
                return multiplier.getValue(owner);
        return 0;
    }

    public int getDefense() {
        for (Multiplier multiplier : multipliers)
            if (multiplier instanceof Shield)
                return multiplier.getValue(owner);
        return 0;
    }

    // Getter & Setter
    public boolean isTargetFoe() { return targetFoe; }

    public int getTimestamp() { return timestamp; }

    public Figure getTempTarget() { return tempTarget; }

    public void setTempTarget(Figure vTempTarget) { tempTarget = vTempTarget; }

    public Figure getOwner() { return owner; }

    /**
     * SkillX Builder
     */
    public static class Builder {
        private String name;
        private String description;
        private Figure owner;
        private int unlockedLevel;
        private SkillType skillType;
        private boolean targetFoe;
        private int timestamp;
        private final ArrayList<Multiplier> multipliers = new ArrayList<>();

        public Builder name(String vName) {
            if (vName == null || vName.isEmpty())
                throw new IllegalArgumentException("Name cannot be empty!");

            name = vName;
            return this;
        }

        public Builder description(String vDescription) {
            if (vDescription == null || vDescription.isEmpty())
                throw new IllegalArgumentException("Description cannot be empty!");

            description = vDescription;
            return this;
        }


        public Builder owner(Figure vOwner) {
            if (vOwner == null)
                throw new IllegalArgumentException("Owner cannot be empty!");

            owner = vOwner;
            return this;
        }

        public Builder unlockedLevel(int vUnlockedLevel) {
            if (vUnlockedLevel < 0)
                throw new IllegalArgumentException("Unlocked Level cannot be smaller than 0!");

            unlockedLevel = vUnlockedLevel;
            return this;
        }

        public Builder skillType(SkillType vSkillType) {
            if (vSkillType == null)
                throw new IllegalArgumentException("Skill Type cannot be empty!");

            skillType = vSkillType;
            return this;
        }

        public Builder targetFoe(boolean vTargetFoe) {
            targetFoe = vTargetFoe;
            return this;
        }

        public Builder timestamp(int vTimestamp) {
            if (vTimestamp < 1)
                throw new IllegalArgumentException("Timestamp cannot be smaller than 1!");

            timestamp = vTimestamp;
            return this;
        }

        public Builder strengthMultiplier(double vStrengthMultiplier) {
            if (vStrengthMultiplier < 0)
                throw new IllegalArgumentException("Strength Multiplier cannot be smaller than 0!");

            multipliers.add(new Strength(vStrengthMultiplier));
            return this;
        }

        public Builder healthMultiplier(double vHealthMultiplier) {
            if (vHealthMultiplier < 0)
                throw new IllegalArgumentException("Health Multiplier cannot be smaller than 0!");

            multipliers.add(new Health(vHealthMultiplier));
            return this;
        }

        public Builder shieldMultiplier(double vShieldMultiplier) {
            if (vShieldMultiplier < 0)
                throw new IllegalArgumentException("Shield Multiplier cannot be smaller than 0!");

            multipliers.add(new Shield(vShieldMultiplier));
            return this;
        }

        public SkillX build() {
            SkillX newSkillX = new SkillX(name, description);
            newSkillX.owner = this.owner;
            newSkillX.unlockedLevel = this.unlockedLevel;
            newSkillX.skillType = this.skillType;
            newSkillX.targetFoe = this.targetFoe;
            newSkillX.timestamp = this.timestamp;
            newSkillX.multipliers = this.multipliers;

            return newSkillX;
        }

    }

    /**
     * Set SkillX constructor private
     */
    private SkillX(String vName, String vDescription) {
        super(vName, vDescription);
    }

    public static void main(String[] args) {
        Hero hero = new Hero("Elon", "", 1, 0,
                50, 15, 20, WeaponType.SWORD, ArmorType.HEAVY);

        SkillX skill1 = new SkillX.Builder()
                .name("Heavy Slash")
                .description("-")
                .owner(hero)
                .unlockedLevel(15)
                .skillType(SkillType.HEAVY)
                .targetFoe(true)
                .timestamp(45)
                .strengthMultiplier(0.875)
                .build();

        skill1.describe();
    }
}
