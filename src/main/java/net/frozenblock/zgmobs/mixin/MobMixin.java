package net.frozenblock.zgmobs.mixin;

import net.frozenblock.zgmobs.*;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Shulker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.frozenblock.zgmobs.GermoniumUtils.setVariant;

@Mixin(Mob.class)
public class MobMixin {
    @Unique
    private int zGMobs$attackTime = 0;

    @Unique
    private Germonium zgmobs$variant = Germonium.NORMAL;


    @Inject(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/syncher/SynchedEntityData;defineId(Ljava/lang/Class;Lnet/minecraft/network/syncher/EntityDataSerializer;)Lnet/minecraft/network/syncher/EntityDataAccessor;"))
    private static void clinit(CallbackInfo ci) {
        ZGMobs.DATA_GERMONIUM = SynchedEntityData.defineId(Mob.class, EntityDataSerializers.INT);
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
            setVariant(this, Germonium.byName(nbt.getString("Germonium")));
        }
    }

    @Inject(method = "aiStep", at = @At("HEAD"))
    private void aiStep(CallbackInfo ci) {
        var that = ((Mob)(Object)this);
        if(!(that instanceof Enemy)) return;
        if(GermoniumUtils.getVariant(that) == Germonium.NORMAL) return;
        if(that.level().isClientSide()) {
            for(int i = 0; i < 3; ++i) {
                that.level().addAlwaysVisibleParticle(
                        ParticleTypes.ENTITY_EFFECT,
                        that.getRandomX(that.getBoundingBox().getXsize()/2),
                        that.getRandomY(),
                        that.getRandomZ(that.getBoundingBox().getZsize()/2),
                        (Math.random()+1)/2, (Math.random()+1)/2, (Math.random()+1)/2);

            }
        }
        if(that instanceof Shulker) return;
        if(that.level().getDifficulty() == Difficulty.PEACEFUL) return;
        --this.zGMobs$attackTime;
        LivingEntity livingentity = that.getTarget();
        if (livingentity != null && Config.ENABLE_SHULKER_BULLETS.get()) {
            that.getLookControl().setLookAt(livingentity, 180.0F, 180.0F);
            double d0 = that.distanceToSqr(livingentity);
            if (d0 < 400.0D) {
                if (this.zGMobs$attackTime <= 0) {
                    int min = Config.SHULKER_MIN_COOLDOWN.get();
                    this.zGMobs$attackTime = min + that.getRandom().nextInt(Config.SHULKER_MAX_COOLDOWN.get() - min);
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

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo ci) {
        final var that = (Mob)(Object)this;
        if(!(that instanceof Enemy)) return;
        if(that.level().isClientSide()) return;
        var variant = GermoniumUtils.getVariant(that);
        if(variant != zgmobs$variant) {
            zgmobs$variant = variant;
            variant.setAttributes(that.getAttributes());
        }
    }
}
