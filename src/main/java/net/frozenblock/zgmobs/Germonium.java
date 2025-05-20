package net.frozenblock.zgmobs;

import net.minecraft.core.Holder;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.monster.Enemy;
import org.lwjgl.system.NonnullDefault;

import java.util.HashMap;
import java.util.Map;
import java.util.function.IntFunction;

import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_MULTIPLIED_BASE;
import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_VALUE;

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

    @Override
    public String getSerializedName() {
        return this.name;
    }

    public int getId() {
        return id;
    }

    public static Germonium byName(String name) {
        return CODEC.byName(name, NORMAL);
    }

    public static Germonium byId(int id) {
        return BY_ID.apply(id);
    }

    public void setAttributes(AttributeMap attributes) {
        final Map<Holder<Attribute>, MobEffect.AttributeTemplate> map = new HashMap<>();
        map.put(Attributes.MAX_HEALTH, create(forVariant(0,40, 80), ADD_VALUE));
        map.put(Attributes.ATTACK_DAMAGE, create(forVariant(0,8, 12), ADD_VALUE));
        map.put(Attributes.MOVEMENT_SPEED, create(forVariant(0d,0.5, 0.65), ADD_MULTIPLIED_BASE));
        map.put(Attributes.KNOCKBACK_RESISTANCE, create(forVariant(0d,0.4, 1d), ADD_VALUE));
        map.put(Attributes.ARMOR, create(forVariant(0,20, 25), ADD_VALUE));
        map.put(Attributes.ARMOR_TOUGHNESS, create(forVariant(0,15, 20), ADD_VALUE));
        for(var entry : map.entrySet()) {
            AttributeInstance instance = attributes.getInstance(entry.getKey());
            if(instance == null) continue;
            instance.removeModifier(entry.getValue().id());
            instance.addPermanentModifier(entry.getValue().create(0));
        }
    }

    private <T> T forVariant(T normal, T infernium, T celestium) {
        return switch (this) {
            case NORMAL -> normal;
            case INFERNIUM -> infernium;
            case CELESTIUM -> celestium;
        };
    }

    private static MobEffect.AttributeTemplate create(double amount, AttributeModifier.Operation operation) {
        return new MobEffect.AttributeTemplate(ZGMobs.id("zgmobs.variant.germonium"), amount, operation);
    }

    public static void finalizeSpawn(Mob that) {
        if(that instanceof Enemy) {
            if(!Config.DISABLE_GERMONIUM.get() && Math.random()*100 > Config.GERMONIUM_BASE_CHANCE.get()) return;
            boolean infernium = Math.random()*100 > Config.CELESTIUM_VARIANT.get();
            if(infernium) GermoniumUtils.setupInfernium(that);
            else GermoniumUtils.setupCelestium(that);
            (infernium ? Germonium.INFERNIUM : Germonium.CELESTIUM).setAttributes(that.getAttributes());
            that.setHealth(that.getMaxHealth());
        }
    }
}
