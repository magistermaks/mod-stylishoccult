package net.darktree.stylishoccult.mixin;

import net.minecraft.client.resource.SplashTextResourceSupplier;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(SplashTextResourceSupplier.class)
abstract public class MessageProviderMixin {

    // Nothing to see here //

    @Shadow
    @Final
    private List<String> splashTexts;

    @Inject(at = @At("TAIL"), method = "apply(Ljava/util/List;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;)V")
    private void apply(List<String> list, ResourceManager resourceManager, Profiler profiler, CallbackInfo info) {
        splashTexts.add("D" + "own with JSON!");
        splashTexts.add("T" + "iny potato!");
        splashTexts.add("S" + "tylish nightmare!");
        splashTexts.add("T" + "he nightmare before breakfast!");
        splashTexts.add("T" + "he wheels of progress frozen motionless...");
    }

}

