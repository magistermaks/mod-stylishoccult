package net.darktree.stylishoccult.config;

import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.utils.DifficultyBased;
import net.darktree.stylishoccult.utils.StructureConfig;

import java.util.Date;

public class Settings {

    // Rarity:  bigger => more rare
    // Chance:  bigger => more common (0% - 100%)
    // Base:    given value is only a base value of some math formula

    private final SimpleConfig CONFIG = SimpleConfig.of("stylish_occult").provider(Settings::provider).request();

    private static String provider(String name) {
        return "# Configuration file for mod Stylish Occult, intended for modpack makers and advanced users!\n# Generated on " + new Date() + "\n";
    }

    // lava demon
    public final int lavaDemonEmitterAirRarity = CONFIG.getOrDefault("demon.emitter.exposed.rarity", 8);
    public final int lavaDemonEmitterDefaultRarity = CONFIG.getOrDefault("demon.emitter.buried.rarity", 15);
    public final float lavaDemonRandomDisguise = CONFIG.getOrDefault("demon.disguise.chance", 12.0f);
    public final float lavaDemonCalmChance1 = CONFIG.getOrDefault("demon.calm.chance.1", 40.0f);
    public final float lavaDemonCalmChance2 = CONFIG.getOrDefault("demon.calm.chance.2", 50.0f);
    public final int lavaDemonCalmRadius = CONFIG.getOrDefault("demon.calm.radius", 2);
    public final float lavaDemonFireBallSpeed = CONFIG.getOrDefault("demon.fireball.speed", 0.821f);
    public final int lavaDemonFireBallAmountBase = CONFIG.getOrDefault("demon.fireball.amount", 4);
    public final int lavaDemonFireBallTimeoutBase = CONFIG.getOrDefault("demon.fireball.timeout", 180);

    // don't touch those unless you really know what are you doing, or you can cause infinite growth
    public final int lavaDemonSpreadLockDefaultRarity = CONFIG.getOrDefault("demon.spread.lock.buried.rarity", 6);
    public final int lavaDemonSpreadLockAirRarity = CONFIG.getOrDefault("demon.spread.lock.exposed.rarity", 10);
    public final int lavaDemonMaxSearchRadius = CONFIG.getOrDefault("demon.spread.seek.radius", 5);

    // other
    public final float entityHealth = CONFIG.getOrDefault("entity.health", 1.0f);
    public final float entityDamage = CONFIG.getOrDefault("entity.damage", 1.0f);
    public final float sporeEntityDamage = CONFIG.getOrDefault("entity.spore.harm", 0.4f); // damage dealt to itself on attack
    public final float fleshBloodChance = CONFIG.getOrDefault("flesh.bloody.chance", 12.5f);
    public final int fernPoisonTimeMinBase = CONFIG.getOrDefault("fern.poison.time.min", 40);
    public final boolean fleshInfiniteSpread = CONFIG.getOrDefault("flesh.spread.infinite", false);

    // features
    public final float featureBoulderChance = CONFIG.getOrDefault("feature.boulder.chance", 7.0f);
    public final float featureBoulderFireChance = CONFIG.getOrDefault("feature.boulder.fire.chance", 45.0f);
    public final float featureFleshVainChance = CONFIG.getOrDefault("feature.flesh.vain.chance", 79.0f);
    public final int featureFleshVainSize = CONFIG.getOrDefault("feature.flesh.vain.size", 30);
    public final int featureFleshStoneVainSize = CONFIG.getOrDefault("feature.flesh.stone.vain.size", 12);
    public final float featureGrassChance = CONFIG.getOrDefault("feature.grass.chance", 98.0f);
    public final float featureFernChance = CONFIG.getOrDefault("feature.fern.chance", 5.0f);
    public final float featureWallChance = CONFIG.getOrDefault("feature.wall.chance", 1.2f);
    public final float featureWallRuneChance = CONFIG.getOrDefault("feature.wall.rune.chance", 29.0f);
    public final float featureDemonChance = CONFIG.getOrDefault("feature.demon.chance", 25.0f);
    public final float featureSparkVentChance = CONFIG.getOrDefault("feature.spark.vent.chance", 18.0f);

    // difficult based stuff
    public final DifficultyBased<Integer> lavaDemonSparkSpawnRarity = difficultyInt("demon.spark.spawn.rarity", 180, 190, 200, 500);
    public final DifficultyBased<Integer> lavaDemonSpreadAngerRarity = difficultyInt("demon.anger.spread.rarity", 2, 3, 4, 5);
    public final DifficultyBased<Integer> sparkEntityBaseLiveTime = difficultyInt("spark.live.time.min", 13, 10, 5, 4);
    public final DifficultyBased<Integer> sparkVentSleepTimeBase = difficultyInt("spark.vent.sleep.min", 150, 250, 350, 400);
    public final DifficultyBased<Integer> fernPoisonTimeDelta = difficultyInt("fern.poison.time.delta", 80, 60, 40, 30);
    public final DifficultyBased<Float> runicErrorExplosionSize = new DifficultyBased<>(2.5f, 2.0f, 1.5f, 1.0f);

    // structure configs
    public final StructureConfig sanctum = new StructureConfig(CONFIG, "structure.sanctum", 10, 7, 48151, 5);
    public final StructureConfig stonehenge = new StructureConfig(CONFIG, "structure.stonehenge", 30, 14, 62342, 4);

    public Settings() {
        if( CONFIG.isBroken() ) {
            StylishOccult.LOGGER.error("Error: Invalid config data! reverting to default...");
            CONFIG.delete();
        }
    }

    private DifficultyBased<Integer> difficultyInt(String config, int hard, int normal, int easy, int peaceful) {
        return new DifficultyBased<>(
                CONFIG.getOrDefault(config + ".hard", hard),
                CONFIG.getOrDefault(config + ".normal", normal),
                CONFIG.getOrDefault(config + ".easy", easy),
                CONFIG.getOrDefault(config + ".peaceful", peaceful)
        );
    }
}
