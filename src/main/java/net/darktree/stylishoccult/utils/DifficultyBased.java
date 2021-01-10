package net.darktree.stylishoccult.utils;

import net.minecraft.world.Difficulty;

public class DifficultyBased<T> {

    private final T peaceful;
    private final T easy;
    private final T normal;
    private final T hard;

    public DifficultyBased( T peaceful, T easy, T normal, T hard ) {
        this.peaceful = peaceful;
        this.easy = easy;
        this.normal = normal;
        this.hard = hard;
    }

    public DifficultyBased( T easy, T normal, T hard ) {
        this( null, easy, normal, hard );
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

    public T getPeaceful() {
        return peaceful;
    }

    public T getEasy() {
        return easy;
    }

    public T getNormal() {
        return normal;
    }

    public T getHard() {
        return hard;
    }
}
