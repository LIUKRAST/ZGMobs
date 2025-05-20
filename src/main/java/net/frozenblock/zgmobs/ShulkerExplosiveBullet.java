package net.frozenblock.zgmobs;

import com.google.common.base.MoreObjects;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.List;

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
                    List<MobEffectInstance> instances = List.of(
                            new MobEffectInstance(MobEffects.WITHER, 100, 2),
                            new MobEffectInstance(MobEffects.DARKNESS, 100),
                            new MobEffectInstance(MobEffects.HARM, 100, 3),
                            new MobEffectInstance(MobEffects.CONFUSION, 100),
                            new MobEffectInstance(MobEffects.BAD_OMEN, 100, 10),
                            new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 2),
                            new MobEffectInstance(MobEffects.BLINDNESS, 100, 0),
                            new MobEffectInstance(MobEffects.WEAKNESS, 100, 2),
                            new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 100, 2)
                    );
                    livingEntity1.addEffect(instances.get((int) (Math.random() * instances.size())), MoreObjects.firstNonNull(entity1, this));
                } else {
                    this.level().explode(this, this.getX(), this.getY(), this.getZ(), 4.5f, Level.ExplosionInteraction.NONE);
                }
            }
        }
    }

    @Override
    protected boolean canHitEntity(@NotNull Entity entity) {
        return GermoniumUtils.getVariant(entity) == Germonium.NORMAL && super.canHitEntity(entity);
    }
}
