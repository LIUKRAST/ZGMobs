package net.frozenblock.zgmobs.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.frozenblock.zgmobs.Germonium;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.BaseSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BaseSpawner.class)
public class SpawnerMixin {
    @Inject(method = "serverTick", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/event/ForgeEventFactory;onFinalizeSpawnSpawner(Lnet/minecraft/world/entity/Mob;Lnet/minecraft/world/level/ServerLevelAccessor;Lnet/minecraft/world/DifficultyInstance;Lnet/minecraft/world/entity/SpawnGroupData;Lnet/minecraft/nbt/CompoundTag;Lnet/minecraft/world/level/BaseSpawner;)Lnet/minecraftforge/event/entity/living/MobSpawnEvent$FinalizeSpawn;"))
    private void serverTick(ServerLevel p_151312_, BlockPos p_151313_, CallbackInfo ci, @Local Mob mob) {
        Germonium.finalizeSpawn(mob);
    }
}
