package net.frozenblock.zgmobs.mixin;

import net.frozenblock.zgmobs.GermoniumUtils;
import net.frozenblock.zgmobs.ZGMobs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Shadow @Nullable protected Player lastHurtByPlayer;

    @ModifyVariable(method = "dropExperience", at = @At("STORE"))
    private int dropExperience(int value) {
        return value * (this instanceof Enemy ? switch (GermoniumUtils.getVariant(this)) {
            case NORMAL -> 1;
            case INFERNIUM -> 15;
            case CELESTIUM -> 30;
        } : 1);
    }

    @Inject(method = "dropFromLootTable", at = @At("TAIL"))
    private void dropFromLootTable(DamageSource damageSource, boolean bl, CallbackInfo ci) {
        if(!(this instanceof Enemy)) return;
        var that = (LivingEntity)(Object)this;
        ResourceLocation resourcelocation = ResourceLocation.fromNamespaceAndPath(ZGMobs.MOD_ID, "entities/" + GermoniumUtils.getVariant(this   ).getSerializedName());
        @SuppressWarnings("DataFlowIssue")
        LootTable loottable = that.level().getServer().getLootData().getLootTable(resourcelocation);
        LootParams.Builder lootparams$builder = (new LootParams.Builder((ServerLevel)that.level())).withParameter(LootContextParams.THIS_ENTITY, that).withParameter(LootContextParams.ORIGIN, that.position()).withParameter(LootContextParams.DAMAGE_SOURCE, damageSource).withOptionalParameter(LootContextParams.KILLER_ENTITY, damageSource.getEntity()).withOptionalParameter(LootContextParams.DIRECT_KILLER_ENTITY, damageSource.getDirectEntity());
        if (bl && lastHurtByPlayer != null) {
            lootparams$builder = lootparams$builder.withParameter(LootContextParams.LAST_DAMAGE_PLAYER, this.lastHurtByPlayer).withLuck(this.lastHurtByPlayer.getLuck());
        }

        LootParams lootparams = lootparams$builder.create(LootContextParamSets.ENTITY);
        loottable.getRandomItems(lootparams, that.getLootTableSeed(), that::spawnAtLocation);
    }

}
