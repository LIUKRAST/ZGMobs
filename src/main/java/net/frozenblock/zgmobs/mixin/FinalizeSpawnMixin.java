package net.frozenblock.zgmobs.mixin;

import net.frozenblock.zgmobs.Germonium;
import net.frozenblock.zgmobs.GermoniumUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.CaveSpider;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = {Mob.class, CaveSpider.class})
public class FinalizeSpawnMixin {
    @Inject(method = "finalizeSpawn", at = @At("TAIL"))
    private void finalizeSpawn(ServerLevelAccessor p_21434_, DifficultyInstance p_21435_, MobSpawnType p_21436_, SpawnGroupData p_21437_, CompoundTag tag, CallbackInfoReturnable<SpawnGroupData> cir) {
        if(tag.contains("Germonium"));
        Germonium.finalizeSpawn(Mob.class.cast(this), tag.contains("Germonium") ? Germonium.byName(tag.getString("Germonium")) : null);
    }
}
