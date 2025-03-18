package net.frozenblock.zgmobs.mixin;

import net.frozenblock.zgmobs.*;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public class MobMixin {
    @Inject(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/syncher/SynchedEntityData;defineId(Ljava/lang/Class;Lnet/minecraft/network/syncher/EntityDataSerializer;)Lnet/minecraft/network/syncher/EntityDataAccessor;"))
    private static void clinit(CallbackInfo ci) {
        ZGMobs.DATA_GERMONIUM = SynchedEntityData.defineId(Mob.class, EntityDataSerializers.INT);
    }

    @Inject(method = "finalizeSpawn", at = @At("TAIL"))
    private void finalizeSpawn(ServerLevelAccessor p_21434_, DifficultyInstance p_21435_, MobSpawnType p_21436_, SpawnGroupData p_21437_, CallbackInfoReturnable<SpawnGroupData> cir) {
        if(this instanceof Enemy) {
            if(ZGMobs.IGNORE_NEXT_SETUP) {
                ZGMobs.IGNORE_NEXT_SETUP = false;
                return;
            }
            Mob that = (Mob)(Object)this;
            if(!Config.DISABLE_GERMONIUM.get() && Math.random()*100 > Config.GERMONIUM_BASE_CHANCE.get()) return;
            if(Math.random()*100 > Config.CELESTIUM_VARIANT.get()) GermoniumUtils.setupInfernium(that);
            else GermoniumUtils.setupCelestium(that);
        }
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void defineSynchedData(CallbackInfo ci) {
        if(this instanceof Enemy) {
            ((Mob)(Object)this).getEntityData().set(ZGMobs.DATA_GERMONIUM, 0);
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

    @Inject(method = "aiStep", at = @At("HEAD"))
    private void aiStep(CallbackInfo ci) {
        var that = ((Mob)(Object)this);
        if (that.level().isClientSide && that instanceof Enemy && GermoniumUtils.getVariant(that) != Germonium.NORMAL) {
            for(int i = 0; i < 3; ++i) {
                that.level().addAlwaysVisibleParticle(
                        ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, (int) (Math.random()*16777216)),
                        that.getRandomX(that.getBoundingBox().getXsize()/2),
                        that.getRandomY(),
                        that.getRandomZ(that.getBoundingBox().getZsize()/2),
                        Math.random(), Math.random(), Math.random());

            }
        }
    }

    @Unique
    private int zGMobs$attackTime = 0;

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo ci) {
        final var that = (Mob)(Object)this;
        if (!(that instanceof Shulker) && that.level().getDifficulty() != Difficulty.PEACEFUL && that instanceof Enemy && GermoniumUtils.getVariant(that) != Germonium.NORMAL) {
            --this.zGMobs$attackTime;
            LivingEntity livingentity = that.getTarget();
            if (livingentity != null && Config.ENABLE_SHULKER_BULLETS.get()) {
                that.getLookControl().setLookAt(livingentity, 180.0F, 180.0F);
                double d0 = that.distanceToSqr(livingentity);
                if (d0 < 400.0D) {
                    if (this.zGMobs$attackTime <= 0) {
                        this.zGMobs$attackTime = 400 + that.getRandom().nextInt(600);
                        that.level().addFreshEntity(new ShulkerExplosiveBullet(that.level(), that, livingentity, Direction.Axis.Y));
                        if(GermoniumUtils.getVariant(that) == Germonium.CELESTIUM) {
                            that.level().addFreshEntity(new ShulkerExplosiveBullet(that.level(), that, livingentity, Direction.Axis.Y));
                        }
                        that.playSound(SoundEvents.SHULKER_SHOOT, 2.0F, (that.getRandom().nextFloat() - that.getRandom().nextFloat()) * 0.2F + 1.0F);
                    }
                } else {
                    that.setTarget(null);
                }
            }
        }
    }
}
