package net.darktree.stylishoccult;

import net.darktree.stylishoccult.utils.DifficultyBased;

public class Settings {

    // Rarity:  bigger => more rare
    // Chance:  bigger => more common (%)
    // Base:    given value is only a base value of some math formula

    public final boolean debug = false;
    public final short candleLayerBurnoutRarity = 30;
    public final short candleHolderLayerBurnoutRarity = 35;
    public final short lavaDemonEmitterAirRarity = 8;
    public final float lavaDemonRandomDisguise = 12.0f;
    public final short lavaDemonEmitterDefaultRarity = 15;
    public final short lavaDemonSpreadLockDefaultRarity = 6;
    public final short lavaDemonSpreadLockAirRarity = 10;
    public final short lavaDemonMaxSearchRadius = 5;
    public final short lavaDemonCalmChance1 = 40;
    public final short lavaDemonCalmChance2 = 50;
    public final short lavaDemonCalmRadius = 2;
    public final float lavaDemonFireBallSpeed = 0.821f;
    public final short lavaDemonFireBallAmountBase = 4;
    public final short lavaDemonFireBallTimeoutBase = 180;
    public final float sparkEntityHealth = 1.0f;
    public final float sparkEntityDamage = 3.0f;
    public final float featureFleshBloodChance = 10.5f;
    public final short fernPoisonTimeMinBase = 40;

    public final DifficultyBased<Integer> lavaDemonSparkSpawnRarity = new DifficultyBased<>(180, 190, 200);
    public final DifficultyBased<Integer> lavaDemonSpreadAngerRarity = new DifficultyBased<>(3, 4, 5, 6);
    public final DifficultyBased<Integer> sparkEntityBaseLiveTime = new DifficultyBased<>(13, 10, 5);
    public final DifficultyBased<Integer> sparkVentSleepTimeBase = new DifficultyBased<>(200, 300, 400);
    public final DifficultyBased<Integer> fernPoisonTimeDelta = new DifficultyBased<>(80, 60, 40);
    public final DifficultyBased<Float> runicErrorExplosionSize = new DifficultyBased<>(2.5f, 2.0f, 1.5f);

    public Settings() {

    }

}
