package net.darktree.stylishoccult.worldgen;

import com.mojang.serialization.Codec;
import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.blocks.ModBlocks;
import net.darktree.stylishoccult.blocks.occult.PassiveFleshBlock;
import net.darktree.stylishoccult.utils.RandUtils;
import net.darktree.stylishoccult.utils.SimpleFeature;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.RangeDecoratorConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.OreFeature;
import net.minecraft.world.gen.feature.OreFeatureConfig;

import java.util.BitSet;
import java.util.Random;

public class FleshPatchFeature extends OreFeature implements SimpleFeature {

    public FleshPatchFeature(Codec<OreFeatureConfig> codec) {
        super(codec);
    }

    protected boolean generateVeinPart(WorldAccess world, Random random, OreFeatureConfig config, double startX, double endX, double startZ, double endZ, double startY, double endY, int x, int y, int z, int size, int i) {

        // Copied from OreFeature.java
        int j = 0;
        BitSet bitSet = new BitSet(size * i * size);
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        int k = config.size;
        double[] ds = new double[k * 4];

        int n;
        double p;
        double q;
        double r;
        double s;
        for(n = 0; n < k; ++n) {
            float f = (float)n / (float)k;
            p = MathHelper.lerp(f, startX, endX);
            q = MathHelper.lerp(f, startY, endY);
            r = MathHelper.lerp(f, startZ, endZ);
            s = random.nextDouble() * (double)k / 16.0D;
            double m = ((double)(MathHelper.sin(3.1415927F * f) + 1.0F) * s + 1.0D) / 2.0D;
            ds[n * 4] = p;
            ds[n * 4 + 1] = q;
            ds[n * 4 + 2] = r;
            ds[n * 4 + 3] = m;
        }

        for(n = 0; n < k - 1; ++n) {
            if (!(ds[n * 4 + 3] <= 0.0D)) {
                for(int o = n + 1; o < k; ++o) {
                    if (!(ds[o * 4 + 3] <= 0.0D)) {
                        p = ds[n * 4] - ds[o * 4];
                        q = ds[n * 4 + 1] - ds[o * 4 + 1];
                        r = ds[n * 4 + 2] - ds[o * 4 + 2];
                        s = ds[n * 4 + 3] - ds[o * 4 + 3];
                        if (s * s > p * p + q * q + r * r) {
                            if (s > 0.0D) {
                                ds[o * 4 + 3] = -1.0D;
                            } else {
                                ds[n * 4 + 3] = -1.0D;
                            }
                        }
                    }
                }
            }
        }

        for(n = 0; n < k; ++n) {
            double u = ds[n * 4 + 3];
            if (!(u < 0.0D)) {
                double v = ds[n * 4];
                double w = ds[n * 4 + 1];
                double aa = ds[n * 4 + 2];

                int ab = Math.max(MathHelper.floor(v - u), x);
                int ac = Math.max(MathHelper.floor(w - u), y);
                int ad = Math.max(MathHelper.floor(aa - u), z);
                int ae = Math.max(MathHelper.floor(v + u), ab);
                int af = Math.max(MathHelper.floor(w + u), ac);
                int ag = Math.max(MathHelper.floor(aa + u), ad);

                for(int ah = ab; ah <= ae; ++ah) {
                    double ai = ((double)ah + 0.5D - v) / u;
                    if (ai * ai < 1.0D) {
                        for(int aj = ac; aj <= af; ++aj) {
                            double ak = ((double)aj + 0.5D - w) / u;
                            if (ai * ai + ak * ak < 1.0D) {
                                for(int al = ad; al <= ag; ++al) {
                                    double am = ((double)al + 0.5D - aa) / u;
                                    if (ai * ai + ak * ak + am * am < 1.0D) {
                                        int an = ah - x + (aj - y) * size + (al - z) * size * i;
                                        if (!bitSet.get(an)) {
                                            bitSet.set(an);
                                            mutable.set(ah, aj, al);
                                            if (config.target.test(world.getBlockState(mutable), random)) {

                                                if( RandUtils.getBool(StylishOccult.SETTINGS.featureFleshBloodChance, random) ) {
                                                    world.setBlockState(mutable, config.state.with(PassiveFleshBlock.BLOODY, true), 2);
                                                }else{
                                                    world.setBlockState(mutable, config.state, 2);
                                                }

                                                ++ j;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return j > 0;
    }

    public ConfiguredFeature<?, ?> configure() {
        return configure( new OreFeatureConfig(
                        OreFeatureConfig.Rules.NETHERRACK,
                        ModBlocks.FLESH_PASSIVE.getDefaultState(),
                        16 ))   // vein size
                .decorate(Decorator.RANGE.configure( new RangeDecoratorConfig(
                        0,      // bottom offset
                        0,      // min y level
                        250)))  // max y level
                .spreadHorizontally()
                .repeat(3);    // number of veins per chunk
    }

}
