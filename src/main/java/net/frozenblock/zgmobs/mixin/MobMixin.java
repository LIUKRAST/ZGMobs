package net.frozenblock.zgmobs.mixin;

import net.frozenblock.zgmobs.Config;
import net.frozenblock.zgmobs.Germonium;
import net.frozenblock.zgmobs.GermoniumUtils;
import net.frozenblock.zgmobs.ZGMobs;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Debug(export = true)
@Mixin(Mob.class)
public class MobMixin {

    @Inject(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/syncher/SynchedEntityData;defineId(Ljava/lang/Class;Lnet/minecraft/network/syncher/EntityDataSerializer;)Lnet/minecraft/network/syncher/EntityDataAccessor;"))
    private static void clinit(CallbackInfo ci) {
        ZGMobs.DATA_GERMONIUM = SynchedEntityData.defineId(Mob.class, EntityDataSerializers.INT);
    }

    @Inject(method = "finalizeSpawn", at = @At("TAIL"))
    private void finalizeSpawn(ServerLevelAccessor p_21434_, DifficultyInstance p_21435_, MobSpawnType p_21436_, SpawnGroupData p_21437_, CompoundTag p_21438_, CallbackInfoReturnable<SpawnGroupData> cir) {
        if(this instanceof Enemy) {
            Mob that = (Mob)(Object)this;
            if(!Config.DISABLE_GERMONIUM.get() && Math.random()*100 > Config.GERMONIUM_PERCENTAGE.get()) return;
            if(that instanceof Creeper creeper) creeper.getEntityData().set(CreeperMixin.accessor$DATA_IS_POWERED(), true);
            if(Math.random()*100 < Config.CELESTIUM_PERCENTAGE.get()) {
                GermoniumUtils.setVariant(this,Germonium.CELESTIUM);
                zgmobs$attribute(that, Attributes.MAX_HEALTH, 40, AttributeModifier.Operation.ADDITION);
                that.setHealth(that.getMaxHealth());
                zgmobs$attribute(that, Attributes.ATTACK_DAMAGE, 1.4, AttributeModifier.Operation.MULTIPLY_BASE);
                //Projectile damage should go along with attack damage itself
                zgmobs$attribute(that, Attributes.MOVEMENT_SPEED, 1.4, AttributeModifier.Operation.MULTIPLY_BASE);
                zgmobs$attribute(that, Attributes.KNOCKBACK_RESISTANCE, 1.3, AttributeModifier.Operation.MULTIPLY_BASE);
            } else {
                GermoniumUtils.setVariant(this,Germonium.INFERNIUM);
                zgmobs$attribute(that, Attributes.MAX_HEALTH, 80, AttributeModifier.Operation.ADDITION);
                that.setHealth(that.getMaxHealth());
                zgmobs$attribute(that, Attributes.ATTACK_DAMAGE, 1.7, AttributeModifier.Operation.MULTIPLY_BASE);
                //Projectile damage should go along with attack damage itself
                zgmobs$attribute(that, Attributes.MOVEMENT_SPEED, 1.4, AttributeModifier.Operation.MULTIPLY_BASE);
                zgmobs$attribute(that, Attributes.KNOCKBACK_RESISTANCE, 2, AttributeModifier.Operation.MULTIPLY_BASE);
            }
        }
    }

    @Unique
    private static void zgmobs$attribute(Mob that, Attribute attribute, double value, AttributeModifier.Operation operation) {
        //noinspection DataFlowIssue
        that.getAttribute(attribute)
                .addPermanentModifier(new AttributeModifier("GermoniumModifier",value, operation));
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void defineSynchedData(CallbackInfo ci) {
        if(this instanceof Enemy) {
            ((Mob)(Object)this).getEntityData().define(ZGMobs.DATA_GERMONIUM, 0);
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void addAdditionalSaveData(CompoundTag nbt, CallbackInfo ci) {
        if(this instanceof Enemy) {
            nbt.putString("Germonium", GermoniumUtils.getVariant(this).getSerializedName());
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readAdditionalSaveData(CompoundTag nbt, CallbackInfo ci) {
        if(this instanceof Enemy) {
            GermoniumUtils.setVariant(this, Germonium.byName(nbt.getString("Germonium")));
        }
    }
}
