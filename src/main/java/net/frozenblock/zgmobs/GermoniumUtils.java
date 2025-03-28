package net.frozenblock.zgmobs;

import net.frozenblock.zgmobs.mixin.CreeperMixin;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
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

    public static void setupInfernium(Mob that) {
        if(that instanceof Creeper creeper) creeper.getEntityData().set(CreeperMixin.accessor$DATA_IS_POWERED(), true);
        setVariant(that,Germonium.INFERNIUM);
        attributeSetup(that, Attributes.MAX_HEALTH, 40, AttributeModifier.Operation.ADDITION);
        that.setHealth(that.getMaxHealth());
        attributeSetup(that, Attributes.ATTACK_DAMAGE, 0.4, AttributeModifier.Operation.MULTIPLY_BASE);
        attributeSetup(that, Attributes.MOVEMENT_SPEED, 0.4, AttributeModifier.Operation.MULTIPLY_BASE);
        attributeSetup(that, Attributes.KNOCKBACK_RESISTANCE, 0.4, AttributeModifier.Operation.ADDITION);
        attributeSetup(that, Attributes.ARMOR, 12, AttributeModifier.Operation.ADDITION);
        attributeSetup(that, Attributes.ARMOR_TOUGHNESS, 10, AttributeModifier.Operation.ADDITION);
    }

    public static void setupCelestium(Mob that) {
        if(that instanceof Creeper creeper) creeper.getEntityData().set(CreeperMixin.accessor$DATA_IS_POWERED(), true);
        setVariant(that, Germonium.CELESTIUM);
        attributeSetup(that, Attributes.MAX_HEALTH, 80, AttributeModifier.Operation.ADDITION);
        that.setHealth(that.getMaxHealth());
        attributeSetup(that, Attributes.ATTACK_DAMAGE, 0.7, AttributeModifier.Operation.MULTIPLY_BASE);
        attributeSetup(that, Attributes.MOVEMENT_SPEED, 0.4, AttributeModifier.Operation.MULTIPLY_BASE);
        attributeSetup(that, Attributes.KNOCKBACK_RESISTANCE, 1, AttributeModifier.Operation.ADDITION);
        attributeSetup(that, Attributes.ARMOR, 16, AttributeModifier.Operation.ADDITION);
        attributeSetup(that, Attributes.ARMOR_TOUGHNESS, 14, AttributeModifier.Operation.ADDITION);
    }

    public static void attributeSetup(Mob that, Attribute attribute, double value, AttributeModifier.Operation operation) {
        if(that.getAttribute(attribute) == null) return;
        //noinspection DataFlowIssue
        that.getAttribute(attribute)
                .addPermanentModifier(new AttributeModifier("GermoniumModifier",value, operation));
    }
}
