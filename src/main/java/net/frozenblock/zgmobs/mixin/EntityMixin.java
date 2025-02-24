package net.frozenblock.zgmobs.mixin;

import net.frozenblock.zgmobs.Germonium;
import net.frozenblock.zgmobs.GermoniumUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Enemy;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(method = "displayFireAnimation", at = @At("RETURN"), cancellable = true)
    private void displayFireAnimation(CallbackInfoReturnable<Boolean> cir) {
        if(this instanceof Enemy) {
            if(GermoniumUtils.getVariant(this) == Germonium.NORMAL) return;
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "fireImmune", at = @At("RETURN"), cancellable = true)
    private void fireImmune(CallbackInfoReturnable<Boolean> cir) {
        if(this instanceof Enemy) {
            if(GermoniumUtils.getVariant(this) == Germonium.NORMAL) return;
            cir.setReturnValue(true);
        }
    }
}
