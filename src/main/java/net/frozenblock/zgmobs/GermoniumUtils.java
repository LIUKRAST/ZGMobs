package net.frozenblock.zgmobs;

import net.minecraft.world.entity.Entity;

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
}
