package net.darktree.stylishoccult.utils;

import net.minecraft.world.Difficulty;
import net.minecraft.world.WorldAccess;

public class DifficultyBased<T> {

    private final T peaceful;
    private final T easy;
    private final T normal;
    private final T hard;

    public DifficultyBased(T hard, T normal, T easy, T peaceful) {
        this.hard = hard;
        this.normal = normal;
        this.easy = easy;
        this.peaceful = peaceful;
    }

    public T get(Difficulty difficulty) {
        return switch (difficulty) {
            case EASY -> easy;
            case NORMAL -> normal;
            case HARD -> hard;
            case PEACEFUL -> peaceful;
        };
    }

    public T get(WorldAccess world) {
        return this.get(world.getDifficulty());
    }

}
