package net.darktree.stylishoccult.utils;

import net.minecraft.nbt.CompoundTag;

@Deprecated
public class Tag {

    private Tag context;
    private String name;
    private CompoundTag tag;

    public Tag() {
        this( new CompoundTag() );
    }

    public Tag(CompoundTag tag) {
        this.tag = tag;
    }

    public Tag getTag( String key ) {
        Tag tag = new Tag( this.tag.getCompound( key ) );
        tag.context = this;
        tag.name = key;
        return tag;
    }

    public Tag newTag( String key ) {
        Tag tag = new Tag();
        tag.context = this;
        tag.name = key;
        return tag;
    }

    public Tag putTag( String name, CompoundTag tag ) {
        this.tag.put( name, tag );
        return this;
    }

    public Tag pop() {
        if( context != null && name != null ) {
            context.getSimpleTag().put( name, tag );
            return context;
        }else{
            throw new RuntimeException( "Unable to pop from top level tag!" );
        }
    }

    public Tag putString( String key, String value ) {
        tag.putString(key, value);
        return this;
    }

    public String getString( String key ) {
        return tag.getString(key);
    }

    public Tag putLong( String key, long value ) {
        tag.putLong(key, value);
        return this;
    }

    public long getLong( String key ) {
        return tag.getLong(key);
    }

    public Tag putInt( String key, int value ) {
        tag.putInt(key, value);
        return this;
    }

    public int getInt( String key ) {
        return tag.getInt(key);
    }

    public Tag putShort( String key, short value ) {
        tag.putShort(key, value);
        return this;
    }

    public short getShort( String key ) {
        return tag.getShort(key);
    }

    public Tag putByte( String key, byte value ) {
        tag.putByte(key, value);
        return this;
    }

    public byte getByte( String key ) {
        return tag.getByte(key);
    }

    public boolean isEmpty() {
        return tag.isEmpty();
    }

    public Tag remove( String key ) {
        tag.remove( key );
        return this;
    }

    public CompoundTag getSimpleTag() {
        return this.tag;
    }

}
