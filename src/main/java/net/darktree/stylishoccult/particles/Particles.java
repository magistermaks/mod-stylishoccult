package net.darktree.stylishoccult.particles;

import net.darktree.stylishoccult.utils.ModIdentifier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.client.particle.WaterSplashParticle;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.registry.Registry;

public class Particles {

    public static DefaultParticleType ORBITING_SPARK = FabricParticleTypes.simple();
    public static DefaultParticleType SPORE = FabricParticleTypes.simple();
    public static DefaultParticleType BLOOD_SPLASH = FabricParticleTypes.simple();
    public static DefaultParticleType BLOOD_DRIPPING = FabricParticleTypes.simple();
    public static DefaultParticleType BLOOD_FALLING = FabricParticleTypes.simple();
    public static DefaultParticleType ATTACK = FabricParticleTypes.simple(true);
    public static DefaultParticleType UNDER_BLOOD = FabricParticleTypes.simple();
    public static DefaultParticleType BOILING_BLOOD = FabricParticleTypes.simple();

    public static void init(){
        Registry.register(Registry.PARTICLE_TYPE, ModIdentifier.of("orbiting_spark"), ORBITING_SPARK);
        Registry.register(Registry.PARTICLE_TYPE, ModIdentifier.of("spore"), SPORE);
        Registry.register(Registry.PARTICLE_TYPE, ModIdentifier.of("blood_splash"), BLOOD_SPLASH);
        Registry.register(Registry.PARTICLE_TYPE, ModIdentifier.of("blood_dripping"), BLOOD_DRIPPING);
        Registry.register(Registry.PARTICLE_TYPE, ModIdentifier.of("blood_falling"), BLOOD_FALLING);
        Registry.register(Registry.PARTICLE_TYPE, ModIdentifier.of("attack"), ATTACK);
        Registry.register(Registry.PARTICLE_TYPE, ModIdentifier.of("under_blood"), UNDER_BLOOD);
        Registry.register(Registry.PARTICLE_TYPE, ModIdentifier.of("boiling_blood"), BOILING_BLOOD);
    }

    @Environment(EnvType.CLIENT)
    public static void clientInit() {
        registerFactory(SPORE, FlameParticle.Factory::new);
        registerFactory(ORBITING_SPARK, OrbitingFlameParticle.Factory::new);
        registerFactory(BLOOD_DRIPPING, BloodParticle.DrippingFactory::new);
        registerFactory(BLOOD_FALLING, BloodParticle.FallingFactory::new);
        registerFactory(BLOOD_SPLASH, WaterSplashParticle.SplashFactory::new);
        registerFactory(ATTACK, AttackParticle.Factory::new);
        registerFactory(UNDER_BLOOD, BloodParticle.SuspendFactory::new);
        registerFactory(BOILING_BLOOD, BoilingBloodParticle.Factory::new);
    }

    @Environment(EnvType.CLIENT)
    private static void registerFactory(DefaultParticleType type, ParticleFactoryRegistry.PendingParticleFactory<DefaultParticleType> factory) {
        ParticleFactoryRegistry.getInstance().register(type, factory);
    }

}
