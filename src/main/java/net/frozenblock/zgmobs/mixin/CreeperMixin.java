package net.frozenblock.zgmobs.mixin;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.monster.Creeper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Creeper.class)
public interface CreeperMixin {
    @Accessor("DATA_IS_POWERED")
    static EntityDataAccessor<Boolean> accessor$DATA_IS_POWERED() {
        throw new AssertionError();
    }

}
