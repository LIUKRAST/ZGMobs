package net.frozenblock.zgmobs;

import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.monster.Enemy;
import org.lwjgl.system.NonnullDefault;

import java.util.HashMap;
import java.util.Map;
import java.util.function.IntFunction;

@NonnullDefault
public enum Germonium implements StringRepresentable {
    NORMAL(0, "normal"),
    INFERNIUM(1, "infernium"),
    CELESTIUM(2, "celestium");

    @SuppressWarnings("deprecation")
    public static final StringRepresentable.EnumCodec<Germonium> CODEC = StringRepresentable.fromEnum(Germonium::values);
    private static final IntFunction<Germonium> BY_ID = ByIdMap.continuous(Germonium::getId, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    private final int id;
    private final String name;

    Germonium(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getSerializedName() {
        return this.name;
    }

    public int getId() {
        return this.id;
    }

    public static Germonium byName(String p_28817_) {
        return CODEC.byName(p_28817_, NORMAL);
    }

    public static Germonium byId(int p_28813_) {
        return BY_ID.apply(p_28813_);
    }

    public void setAttributes(AttributeMap attributes) {
        final Map<Attribute, AttributeModifier> map = new HashMap<>();
        map.put(Attributes.MAX_HEALTH, create(forVariant(0,40, 80), AttributeModifier.Operation.ADDITION));
        map.put(Attributes.ATTACK_DAMAGE, create(forVariant(0,8, 12), AttributeModifier.Operation.ADDITION));
        map.put(Attributes.MOVEMENT_SPEED, create(forVariant(0d,0.5, 0.65), AttributeModifier.Operation.MULTIPLY_BASE));
        map.put(Attributes.KNOCKBACK_RESISTANCE, create(forVariant(0d,0.4, 1d), AttributeModifier.Operation.ADDITION));
        map.put(Attributes.ARMOR, create(forVariant(0,20, 25), AttributeModifier.Operation.ADDITION));
        map.put(Attributes.ARMOR_TOUGHNESS, create(forVariant(0,15, 20), AttributeModifier.Operation.ADDITION));
        for(var entry : map.entrySet()) {
            AttributeInstance instance = attributes.getInstance(entry.getKey());
            if(instance == null) continue;
            instance.removeModifier(entry.getValue().getId());
            instance.addPermanentModifier(entry.getValue());
        }
    }

    private <T> T forVariant(T normal, T infernium, T celestium) {
        return switch (this) {
            case NORMAL -> normal;
            case INFERNIUM -> infernium;
            case CELESTIUM -> celestium;
        };
    }

    private static AttributeModifier create(double amount, AttributeModifier.Operation operation) {
        return new AttributeModifier("zgmobs.variant.germonium", amount, operation);
    }

    public static void finalizeSpawn(Mob mob) {
        if(mob instanceof Enemy) {
            if(!Config.DISABLE_GERMONIUM.get() && Math.random()*100 > Config.GERMONIUM_BASE_CHANCE.get()) return;
            boolean infernium = Math.random()*100 > Config.CELESTIUM_VARIANT.get();
            if(infernium) GermoniumUtils.setupInfernium(mob);
            else GermoniumUtils.setupCelestium(mob);
            (infernium ? Germonium.INFERNIUM : Germonium.CELESTIUM).setAttributes(mob.getAttributes());
            mob.setHealth(mob.getMaxHealth());
        }
    }
}
