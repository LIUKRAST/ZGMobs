package net.frozenblock.zgmobs;

import com.google.common.base.MoreObjects;
import net.minecraft.core.Direction;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ShulkerExplosiveBullet extends ShulkerBullet {

    public ShulkerExplosiveBullet(Level p_37330_, LivingEntity p_37331_, Entity p_37332_, Direction.Axis p_37333_) {
        super(p_37330_, p_37331_, p_37332_, p_37333_);
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult hitResult) {
        Entity entity = hitResult.getEntity();
        Entity entity1 = this.getOwner();
        LivingEntity livingentity = entity1 instanceof LivingEntity ? (LivingEntity)entity1 : null;
        boolean flag = entity.hurt(this.damageSources().mobProjectile(this, livingentity), 4.0F);
        if (flag) {
            assert livingentity != null;
            this.doEnchantDamageEffects(livingentity, entity);
            if (entity instanceof LivingEntity livingEntity1) {
                if(Math.random() > 0.5) {
                    List<MobEffectInstance> instances = List.of(
                            new MobEffectInstance(MobEffects.WITHER, 100, 2),
                            new MobEffectInstance(MobEffects.DARKNESS, 100, 0),
                            new MobEffectInstance(MobEffects.HARM, 100, 2),
                            new MobEffectInstance(MobEffects.CONFUSION, 100, 0),
                            new MobEffectInstance(MobEffects.BAD_OMEN, 100, 9),
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
        if(entity instanceof Enemy) return GermoniumUtils.getVariant(entity) == Germonium.NORMAL;
        return super.canHitEntity(entity);
    }
}
