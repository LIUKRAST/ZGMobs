package net.frozenblock.zgmobs;

import net.frozenblock.zgmobs.mixin.CreeperMixin;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Creeper;

public class GermoniumUtils {

    public static void setVariant(Object entity, Germonium germonium) {
        setVariant((Entity) entity, germonium);
    }

    public static void setVariant(Entity entity, Germonium germonium) {
        entity.getEntityData().set(ZGMobs.DATA_GERMONIUM, germonium.getId());
    }

    public static Germonium getVariant(Object entity) {
        return getVariant((Entity) entity);
    }

    public static Germonium getVariant(Entity entity) {
        return Germonium.byId(entity.getEntityData().get(ZGMobs.DATA_GERMONIUM));
    }

    public static void setup(Mob that, boolean infernium) {
        if(that instanceof Creeper creeper) creeper.getEntityData().set(CreeperMixin.accessor$DATA_IS_POWERED(), true);
        setVariant(that, infernium ? Germonium.INFERNIUM : Germonium.CELESTIUM);
    }
}
