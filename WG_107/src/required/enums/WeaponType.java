package required.enums;

public enum WeaponType {
    SWORD, PROJECTILE, GAUNTLET, SABER, CYBERWARE

    /* Multiplier
     * Sword --> strength (higher) & shield
     * --> contoh : strengthMultiplier(0.2) & shieldMultiplier(0.08)
     * Projectile --> strength (higher than saber)
     * --> contoh : strengthMultiplier(0.47)
     * Gauntlet --> strength & shield (higher)
     * --> contoh : strengthMultiplier(0.11) & shieldMultiplier(0.13)
     * Saber --> strength (higher than sword)
     * --> contoh : strengthMultiplier(0.3125)
     * Cyberware --> strength, shield & health
     * --> contoh : strengthMultiplier(0.18), shieldMultiplier(0.15) & healthMultiplier(0.06)
     */
}
