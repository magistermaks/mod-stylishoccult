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

    public static VoxelShapeBuilder box(float x1, float y1, float z1, float x2, float y2, float z2) {
        return empty().box( x1, y1, z1, x2, y2, z2 );
    }

    public static VoxelShapeBuilder box(Box box) {
        return empty().box(box);
    }

    public static class VoxelShapeBuilder {

        private final ArrayList<VoxelShape> shapes;

        private VoxelShapeBuilder() {
            shapes = new ArrayList<>();
        }

        public VoxelShapeBuilder add(VoxelShape shape) {
            shapes.add(shape);
            return this;
        }

        public VoxelShapeBuilder box(float x1, float y1, float z1, float x2, float y2, float z2) {
            return add( Utils.shape(x1, y1, z1, x2, y2, z2) );
        }

        public VoxelShapeBuilder box(Box box) {
            return box((float) box.minX, (float) box.minY, (float) box.minZ, (float) box.maxX, (float) box.maxY, (float) box.maxZ);
        }

        public VoxelShape build( BooleanBiFunction function ) {
            int size = shapes.size();

            if( size < 1 ) {
                throw new RuntimeException("Can't join an empty collection!");
            }

            VoxelShape shape = shapes.get(0);

            for (int i = 1; i < size; i++) {
                shape = VoxelShapes.combine(shape, shapes.get(i), function);
            }

            return shape;
        }

        public VoxelShape build() {
            return build(BooleanBiFunction.OR);
        }

    }

}
