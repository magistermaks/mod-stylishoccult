package net.darktree.stylishoccult.utils;

import net.minecraft.world.Difficulty;
import net.minecraft.world.WorldAccess;

public class DifficultyBased<T> {

    private final T peaceful;
    private final T easy;
    private final T normal;
    private final T hard;

    public DifficultyBased( T hard, T normal, T easy, T peaceful ) {
        this.hard = hard;
        this.normal = normal;
        this.easy = easy;
        this.peaceful = peaceful;
    }

    public DifficultyBased( T hard, T normal, T easy ) {
        this( hard, normal, easy, null );
    }

    public T get( Difficulty difficulty ) {
        switch ( difficulty ) {
            case EASY: return easy;
            case NORMAL: return normal;
            case HARD: return hard;
            case PEACEFUL: return peaceful;
        }

        throw new RuntimeException( "Unexpected difficulty!" );
    }

    public T get(WorldAccess world) {
        return this.get(world.getDifficulty());
    }

}
