package net.darktree.stylishoccult.script.components;

import net.minecraft.nbt.CompoundTag;

import java.util.ArrayList;

public class StackManager {

    private ArrayList<Double> stack = new ArrayList<>();

    public CompoundTag stackToTag(CompoundTag tag ) {
        int size = stack.size();
        for( int i = 0; i < size; i ++ ) {
            tag.putDouble(String.valueOf(i), stack.get(i));
        }
        return tag;
    }

    public void stackFromTag( CompoundTag tag ) {
        try {
            int size = tag.getSize();
            for( int i = 0; i < size; i ++ ) {
                stack.add( tag.getDouble(String.valueOf(i)) );
            }
        } catch (Exception ignore) {}
    }

    public void copy( StackManager stackManager ) {
        stack = new ArrayList<>(stackManager.stack);
    }

    public void write( double value, int offset ) {
        stack.set( stack.size() - offset - 1, value );
    }

    public double get( int offset ) {
        return stack.get( stack.size() - offset - 1 );
    }

    public void put( double value ) {
        stack.add( value );
    }

    public void pop( int count ) {
        for( int i = 0; i < count; i ++ ) {
            stack.remove(stack.size() - 1);
        }
    }

    public double pull() {
        return stack.remove(stack.size() - 1);
    }

    public void duplicate() {
        put( get(0) );
    }

    public void exchange() {
        double v0 = get(0);
        double v1 = get(1);
        write( v0, 1 );
        write( v1, 0 );
    }

    public void replace( int count, double value ) {
        pop(count);
        put(value);
    }

    public void or() {
        replace( 2, (get(0) != 0 || get(1) != 0) ? 1 : 0 );
    }

    public void not() {
        replace( 1, (get(0) != 0) ? 0 : 1 );
    }

    public void invert() {
        replace( 1, -get(0) );
    }

    public void reciprocal() {
        replace( 1, 1.0d/get(0) );
    }

    public void and() {
        replace( 2, (get(0) != 0 && get(1) != 0) ? 1 : 0 );
    }

    public void equal( double value ) {
        replace( 1, value == get(0) ? 1 : 0 );
    }

    public void more( double value ) {
        replace( 1, get(0) > value ? 1 : 0 );
    }

    public void less( double value ) {
        replace( 1, get(0) < value ? 1 : 0 );
    }

    public void moreOrEqual( double value ) {
        replace( 1, get(0) >= value ? 1 : 0 );
    }

    public void lessOrEqual( double value ) {
        replace( 1, get(0) <= value ? 1 : 0 );
    }

    public void add( double value ) {
        replace( 1, get(0) + value );
    }

    public void multiply( double value ) {
        replace( 1, get(0) * value );
    }

    public void validate() {
        if( stack.size() > 32 ) {
            throw new RuneException("stack_too_big");
        }
    }

    public void reset() {
        stack = new ArrayList<>();
    }

    public int size() {
        return stack.size();
    }

    public String print() {
        StringBuilder str = new StringBuilder();

        for( double value : stack ) {
            str.append("  ").append(value).append("\n");
        }

        return str.toString();
    }

}
