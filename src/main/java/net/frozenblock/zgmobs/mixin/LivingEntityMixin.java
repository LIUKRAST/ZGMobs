package net.frozenblock.zgmobs.mixin;

import net.frozenblock.zgmobs.Germonium;
import net.frozenblock.zgmobs.IGermonium;
import net.frozenblock.zgmobs.ZGMobs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Shadow @Nullable protected Player lastHurtByPlayer;

    @Inject(method = "getExperienceReward", at = @At("RETURN"), cancellable = true)
    private void getExperienceReward(CallbackInfoReturnable<Integer> cir) {
        if(this instanceof IGermonium holder) {
            cir.setReturnValue(cir.getReturnValueI() * switch(holder.getVariant()) {
                case NORMAL -> 1;
                case INFERNIUM -> 15;
                case CELESTIUM -> 30;
            });
        }
    }

    @Inject(method = "die", at = @At("TAIL"))
    private void die(DamageSource p_21014_, CallbackInfo ci) {
        var entities = ZGMobs.getEntitiesFromTag(ResourceLocation.fromNamespaceAndPath(ZGMobs.MOD_ID, "celestium_spawns"));
        for(int i = 0; i < Math.random() * 4; i++) {
            var entityType = entities.get((int) (Math.random() * entities.size()));
            var that = (LivingEntity) (Object) this;
            var entity = entityType.spawn((ServerLevel) that.level(), that.getOnPos(), MobSpawnType.REINFORCEMENT);
            if(entity instanceof IGermonium holder) holder.setVariant(Germonium.INFERNIUM);
        }
    }

    @Inject(method = "dropFromLootTable", at = @At("TAIL"))
    private void dropFromLootTable(DamageSource p_21021_, boolean p_21022_, CallbackInfo ci) {
        if(!(this instanceof IGermonium holder)) return;
        var that = (LivingEntity)(Object)this;
        ResourceLocation resourcelocation = ResourceLocation.fromNamespaceAndPath(ZGMobs.MOD_ID, "entities/" + holder.getVariant().getSerializedName());
        @SuppressWarnings("DataFlowIssue")
        LootTable loottable = that.level().getServer().getLootData().getLootTable(resourcelocation);
        LootParams.Builder lootparams$builder = (new LootParams.Builder((ServerLevel)that.level())).withParameter(LootContextParams.THIS_ENTITY, that).withParameter(LootContextParams.ORIGIN, that.position()).withParameter(LootContextParams.DAMAGE_SOURCE, p_21021_).withOptionalParameter(LootContextParams.KILLER_ENTITY, p_21021_.getEntity()).withOptionalParameter(LootContextParams.DIRECT_KILLER_ENTITY, p_21021_.getDirectEntity());
        if (p_21022_ && lastHurtByPlayer != null) {
            lootparams$builder = lootparams$builder.withParameter(LootContextParams.LAST_DAMAGE_PLAYER, this.lastHurtByPlayer).withLuck(this.lastHurtByPlayer.getLuck());
        }

        LootParams lootparams = lootparams$builder.create(LootContextParamSets.ENTITY);
        loottable.getRandomItems(lootparams, that.getLootTableSeed(), that::spawnAtLocation);
    }

}
