package net.frozenblock.zgmobs.mixin;

import net.frozenblock.zgmobs.Config;
import net.frozenblock.zgmobs.Germonium;
import net.frozenblock.zgmobs.GermoniumUtils;
import net.frozenblock.zgmobs.ZGMobs;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Shadow @Nullable protected Player lastHurtByPlayer;

    @Inject(method = "getExperienceReward", at = @At("RETURN"), cancellable = true)
    private void getExperienceReward(CallbackInfoReturnable<Integer> cir) {
        if(this instanceof Enemy) {
            cir.setReturnValue(cir.getReturnValueI() * switch(GermoniumUtils.getVariant(this)) {
                case NORMAL -> 1;
                case INFERNIUM -> 15;
                case CELESTIUM -> 30;
            });
        }
    }

    @Inject(method = "die", at = @At("HEAD"))
    private void die(DamageSource damageSource, CallbackInfo ci) {
        var that = (LivingEntity) (Object) this;
        if (this instanceof Enemy && GermoniumUtils.getVariant(this) == Germonium.CELESTIUM && !that.level().isClientSide()) {
            var entities = ZGMobs.getEntitiesFromTag(ZGMobs.id("celestium_spawns"));
            for (int i = 0; i < Math.random() * Config.CELESTIUM_DEATH_ROLL.get(); i++) {
                var entityType = entities.get((int) (Math.random() * entities.size()));
                ZGMobs.IGNORE_NEXT_SETUP = true;
                var entity = entityType.spawn((ServerLevel) that.level(), that.getOnPos().above(), MobSpawnType.REINFORCEMENT);
                assert entity != null;
                if (entity instanceof Mob mob) GermoniumUtils.setupInfernium(mob);
            }
        }
    }

    @Inject(method = "dropFromLootTable", at = @At("TAIL"))
    private void dropFromLootTable(DamageSource damageSource, boolean bl, CallbackInfo ci) {
        if(!(this instanceof Enemy)) return;
        var that = (LivingEntity)(Object)this;
        ResourceLocation resourcelocation = ResourceLocation.fromNamespaceAndPath(ZGMobs.MOD_ID, "entities/" + GermoniumUtils.getVariant(this).getSerializedName());
        var key = ResourceKey.create(Registries.LOOT_TABLE, resourcelocation);
        @SuppressWarnings("DataFlowIssue")
        LootTable loottable = that.level().getServer().reloadableRegistries().getLootTable(key);
        LootParams.Builder lootparams$builder = (new LootParams.Builder((ServerLevel)that.level())).withParameter(LootContextParams.THIS_ENTITY, that).withParameter(LootContextParams.ORIGIN, that.position()).withParameter(LootContextParams.DAMAGE_SOURCE, damageSource).withOptionalParameter(LootContextParams.ATTACKING_ENTITY, damageSource.getEntity()).withOptionalParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, damageSource.getDirectEntity());
        if (bl && lastHurtByPlayer != null) {
            lootparams$builder = lootparams$builder.withParameter(LootContextParams.LAST_DAMAGE_PLAYER, this.lastHurtByPlayer).withLuck(this.lastHurtByPlayer.getLuck());
        }

        LootParams lootparams = lootparams$builder.create(LootContextParamSets.ENTITY);
        loottable.getRandomItems(lootparams, that.getLootTableSeed(), that::spawnAtLocation);
    }
}
