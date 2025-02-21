package net.frozenblock.zgmobs.mixin;

import net.frozenblock.zgmobs.Config;
import net.frozenblock.zgmobs.Germonium;
import net.frozenblock.zgmobs.IGermonium;
import net.frozenblock.zgmobs.ZGMobs;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public class MobMixin {

    @Inject(method = "finalizeSpawn", at = @At("TAIL"))
    private void finalizeSpawn(ServerLevelAccessor p_21434_, DifficultyInstance p_21435_, MobSpawnType p_21436_, SpawnGroupData p_21437_, CompoundTag p_21438_, CallbackInfoReturnable<SpawnGroupData> cir) {
        if(this instanceof IGermonium holder) {
            Mob that = (Mob)(Object)this;
            if(!Config.DISABLE_GERMONIUM.get() && Math.random()*100 > Config.GERMONIUM_PERCENTAGE.get()) return;
            if(that instanceof Creeper creeper) creeper.getEntityData().set(CreeperMixin.accessor$DATA_IS_POWERED(), true);
            if(Math.random()*100 < Config.CELESTIUM_PERCENTAGE.get()) {
                holder.setVariant(Germonium.CELESTIUM);
                zgmobs$attribute(that, Attributes.MAX_HEALTH, 40, AttributeModifier.Operation.ADDITION);
                zgmobs$attribute(that, Attributes.ATTACK_DAMAGE, 1.4, AttributeModifier.Operation.MULTIPLY_BASE);
                //Projectile damage should go along with attack damage itself
                zgmobs$attribute(that, Attributes.MOVEMENT_SPEED, 1.4, AttributeModifier.Operation.MULTIPLY_BASE);
                zgmobs$attribute(that, Attributes.KNOCKBACK_RESISTANCE, 1.3, AttributeModifier.Operation.MULTIPLY_BASE);
            } else {
                holder.setVariant(Germonium.INFERNIUM);
                zgmobs$attribute(that, Attributes.MAX_HEALTH, 80, AttributeModifier.Operation.ADDITION);
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
        if(((Mob)(Object)this) instanceof Monster monster) {
            monster.getEntityData().define(ZGMobs.DATA_TYPE_ID, 0);
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void addAdditionalSaveData(CompoundTag nbt, CallbackInfo ci) {
        if(this instanceof IGermonium holder) {
            nbt.putString("Germonium", holder.getVariant().getSerializedName());
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readAdditionalSaveData(CompoundTag nbt, CallbackInfo ci) {
        if(this instanceof IGermonium holder) {
            holder.setVariant(Germonium.byName(nbt.getString("Germonium")));
        }
    }
}
