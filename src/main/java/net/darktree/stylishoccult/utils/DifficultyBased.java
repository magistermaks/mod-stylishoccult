package net.darktree.stylishoccult.utils;

import net.darktree.stylishoccult.config.Config;
import net.minecraft.world.Difficulty;
import net.minecraft.world.WorldAccess;

public class DifficultyBased implements Config.ConfigProperty {

    @Config.Entry()
    public float peaceful;

    @Config.Entry()
    public float easy;

    @Config.Entry()
    public float normal;

    @Config.Entry()
    public float hard;

    public DifficultyBased(float hard, float normal, float easy, float peaceful) {
        this.hard = hard;
        this.normal = normal;
        this.easy = easy;
        this.peaceful = peaceful;
    }

    public float get(Difficulty difficulty) {
        return switch (difficulty) {
            case EASY -> easy;
            case NORMAL -> normal;
            case HARD -> hard;
            case PEACEFUL -> peaceful;
        };
    }

    public float get(WorldAccess world) {
        return this.get(world.getDifficulty());
    }

}
