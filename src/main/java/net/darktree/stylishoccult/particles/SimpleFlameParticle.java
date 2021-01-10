package net.darktree.stylishoccult.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.FabricSpriteProvider;
import net.minecraft.client.particle.AnimatedParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

@Environment(EnvType.CLIENT)
public class SimpleFlameParticle extends AnimatedParticle {

    protected SimpleFlameParticle(ClientWorld world, double x, double y, double z, SpriteProvider sprites) {
        super(world, x, y, z, sprites, 1);

        this.maxAge = 39;
        setSprite( sprites.getSprite(world.random) );

        this.velocityZ = world.random.nextFloat() * 6.28;
        this.velocityY = world.random.nextInt( 100 ) - 50;
        this.scale = 0.1F * (this.random.nextFloat() * 0.5F + 0.55F) * 1.3F;
    }

    @Override
    public float getSize(float tickDelta) {
        float g = ((float)this.age + tickDelta);
        float s = ( (float)Math.sin( g/4 + this.velocityZ ) + 1 ) / (4 + (float) age/32);

        float f = (g / ((float)this.maxAge + (float) this.velocityZ)) + s;
        return this.scale * (1.0F - f * f * 0.3F);
    }

    @Override
    public void tick() {
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;

        if( this.age ++ > this.maxAge ) this.markDead();
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<DefaultParticleType> {
        private final FabricSpriteProvider sprites;

        public Factory(FabricSpriteProvider sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(DefaultParticleType type, ClientWorld world, double x, double y, double z, double vX, double vY, double vZ) {
            return new SimpleFlameParticle(world, x, y, z, sprites);
        }
    }
}
