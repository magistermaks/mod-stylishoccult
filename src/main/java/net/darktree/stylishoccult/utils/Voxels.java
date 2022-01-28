package net.darktree.stylishoccult.utils;

import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

import java.util.ArrayList;

public class Voxels {

    public static VoxelShapeBuilder empty() {
        return new VoxelShapeBuilder();
    }

    public static VoxelShapeBuilder from( Box box ) {
        VoxelShapeBuilder builder = empty();
        return builder.add( IntBox.from(box) );
    }

    public static VoxelShapeBuilder from( IntBox box ) {
        VoxelShapeBuilder builder = empty();
        return builder.add(box);
    }

    public static VoxelShapeBuilder box( int x1, int y1, int z1, int x2, int y2, int z2 ) {
        VoxelShapeBuilder builder = empty();
        return builder.box( x1, y1, z1, x2, y2, z2 );
    }

    public static class IntBox {

        public int x1, y1, z1, x2, y2, z2;

        IntBox( int x1, int y1, int z1, int x2, int y2, int z2 ) {
            this.x1 = x1;
            this.x2 = x2;
            this.y1 = y1;
            this.y2 = y2;
            this.z1 = z1;
            this.z2 = z2;
        }

        IntBox( IntBox box ) {
            this.x1 = box.x1;
            this.x2 = box.x2;
            this.y1 = box.y1;
            this.y2 = box.y2;
            this.z1 = box.z1;
            this.z2 = box.z2;
        }

        public static IntBox from( Box box ) {
            return new IntBox(
                    (int) box.minX,
                    (int) box.minY,
                    (int) box.maxZ,
                    (int) box.maxX,
                    (int) box.maxY,
                    (int) box.maxZ
            );
        }

        public IntBox copy() {
            return new IntBox(this);
        }

        public VoxelShape asVoxelShape() {
            return Utils.box(x1, y1, z1, x2, y2, z2);
        }

    }

    public static class VoxelShapeBuilder {

        private final ArrayList<IntBox> boxes;

        private VoxelShapeBuilder() {
            boxes = new ArrayList<>();
        }

        private VoxelShapeBuilder(VoxelShapeBuilder builder) {
            boxes = copyBoxes( builder.boxes );
        }

        private ArrayList<IntBox> copyBoxes( ArrayList<IntBox> boxes ) {
            ArrayList<IntBox> copied = new ArrayList<>();
            copied.ensureCapacity( boxes.size() );

            for( IntBox box : boxes ) {
                copied.add( box.copy() );
            }

            return copied;
        }

        public VoxelShapeBuilder add( IntBox box ) {
            boxes.add(box);
            return this;
        }

        public VoxelShapeBuilder box( int x1, int y1, int z1, int x2, int y2, int z2 ) {
            return add( new IntBox( x1, y1, z1, x2, y2, z2 ) );
        }

        public VoxelShapeBuilder copy() {
            return new VoxelShapeBuilder(this);
        }

        public VoxelShape build( BooleanBiFunction function ) {
            int size = boxes.size();

            if( size < 1 ) {
                throw new RuntimeException("Can't join an empty collection!");
            }

            VoxelShape shape = boxes.get(0).asVoxelShape();

            for (int i = 1; i < size; i++) {
                shape = VoxelShapes.combine(shape, boxes.get(i).asVoxelShape(), function);
            }

            return shape;
        }

        public VoxelShape build() {
            return build( BooleanBiFunction.OR );
        }

    }

}
