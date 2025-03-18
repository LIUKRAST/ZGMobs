package net.frozenblock.zgmobs.mixin;

import net.frozenblock.zgmobs.Germonium;
import net.frozenblock.zgmobs.GermoniumUtils;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Enemy;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin<T extends Entity> {

    @Inject(method = "getBlockLightLevel", at = @At("RETURN"), cancellable = true)
    private void getBlockLightLevel(T entity, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        if(entity instanceof Enemy && GermoniumUtils.getVariant(entity) != Germonium.NORMAL) cir.setReturnValue(15);
    }
}
