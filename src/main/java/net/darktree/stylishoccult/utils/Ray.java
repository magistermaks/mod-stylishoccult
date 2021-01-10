package net.darktree.stylishoccult.utils;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class Ray {

    public Vec3d origin, dir;
    public Vec3d invDir;
    public int[] sign = new int[3];

    public Ray( Vec3d origin, Vec3d dir ) {
        this.origin = origin;
        this.dir = dir;

        invDir = new Vec3d( 1 / dir.x, 1 / dir.y, 1 / dir.z );

        sign[0] = (invDir.x < 0) ? 1 : 0;
        sign[1] = (invDir.y < 0) ? 1 : 0;
        sign[2] = (invDir.z < 0) ? 1 : 0;
    }

    public void offset( BlockPos pos ) {
        this.origin = this.origin.subtract( new Vec3d( pos.getX(), pos.getY(), pos.getZ() ) );
    }

    public boolean intersects( Box box ) {
        Vec3d[] bounds = new Vec3d[2];
        bounds[0] = new Vec3d( box.minX, box.minY, box.minZ );
        bounds[1] = new Vec3d( box.maxX, box.maxY, box.maxZ );

        double txMin, txMax, tyMin, tyMax, tzMin, tzMax;

        txMin = (bounds[this.sign[0]].x - origin.x) * invDir.x;
        txMax = (bounds[1-this.sign[0]].x - origin.x) * invDir.x;
        tyMin = (bounds[this.sign[1]].y - origin.y) * invDir.y;
        tyMax = (bounds[1-this.sign[1]].y - origin.y) * invDir.y;

        if ((txMin > tyMax) || (tyMin > txMax)) return false;
        if (tyMin > txMin) txMin = tyMin;
        if (tyMax < txMax) txMax = tyMax;

        tzMin = (bounds[this.sign[2]].z - origin.z) * invDir.z;
        tzMax = (bounds[1-this.sign[2]].z - origin.z) * invDir.z;

        return (txMin <= tzMax) && (tzMin <= txMax);
    }

}
