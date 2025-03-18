package net.frozenblock.zgmobs;

import com.google.common.base.MoreObjects;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;

public class ShulkerExplosiveBullet extends ShulkerBullet {
    public ShulkerExplosiveBullet(Level level, LivingEntity shooter, Entity finalTarget, Direction.Axis axis) {
        super(level, shooter, finalTarget, axis);
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult hitResult) {
        Entity entity = hitResult.getEntity();
        Entity entity1 = this.getOwner();
        LivingEntity livingentity = entity1 instanceof LivingEntity ? (LivingEntity)entity1 : null;
        DamageSource damagesource = this.damageSources().mobProjectile(this, livingentity);
        boolean flag = entity.hurt(this.damageSources().mobProjectile(this, livingentity), 4.0F);
        if (flag) {
            assert livingentity != null;
            EnchantmentHelper.doPostAttackEffects((ServerLevel) this.level(), entity, damagesource);
            //this.doEnchantDamageEffects(livingentity, entity);
            if (entity instanceof LivingEntity livingEntity1) {
                if(Math.random() > 0.5) {
                    Holder<MobEffect>[] effects = new Holder[]{
                            MobEffects.MOVEMENT_SLOWDOWN, MobEffects.BLINDNESS, MobEffects.WEAKNESS, MobEffects.DIG_SLOWDOWN
                    };
                    livingEntity1.addEffect(new MobEffectInstance(effects[(int) (Math.random()*effects.length)], 100, 4), MoreObjects.firstNonNull(entity1, this));
                } else {
                    this.level().explode(this, this.getX(), this.getY(), this.getZ(), 4.5f, Level.ExplosionInteraction.NONE);
                }
            }
        }
    }
}
