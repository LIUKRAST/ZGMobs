package net.frozenblock.zgmobs;

import net.frozenblock.zgmobs.mixin.CreeperMixin;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.monster.Creeper;

public class GermoniumUtils {

    private GermoniumUtils() {}

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

    public static void setupInfernium(Mob that) {
        if(that instanceof Creeper creeper) creeper.getEntityData().set(CreeperMixin.accessor$DATA_IS_POWERED(), true);
        setVariant(that, Germonium.INFERNIUM);
    }

    public static void setupCelestium(Mob that) {
        if(that instanceof Creeper creeper) creeper.getEntityData().set(CreeperMixin.accessor$DATA_IS_POWERED(), true);
        setVariant(that, Germonium.CELESTIUM);
    }

    @Deprecated
    public static void attributeSetup(Mob that, Holder<Attribute> attribute, double value, AttributeModifier.Operation operation) {
        if(that.getAttribute(attribute) == null) return;
        //noinspection DataFlowIssue
        that.getAttribute(attribute)
                .addPermanentModifier(new AttributeModifier(ZGMobs.id("germonium_modifier"),value, operation));
    }
}
