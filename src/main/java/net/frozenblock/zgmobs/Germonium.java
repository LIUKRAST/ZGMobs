package net.frozenblock.zgmobs;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.core.Holder;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.*;
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
        map.put(Attributes.ATTACK_DAMAGE, create(forVariant(0d,0.4, 0.7), ADD_MULTIPLIED_BASE));
        map.put(Attributes.MOVEMENT_SPEED, create(forVariant(0d,0.4, 0.4), ADD_MULTIPLIED_BASE));
        map.put(Attributes.KNOCKBACK_RESISTANCE, create(forVariant(0d,0.4, 1d), ADD_VALUE));
        map.put(Attributes.ARMOR, create(forVariant(0,12, 16), ADD_VALUE));
        map.put(Attributes.ARMOR_TOUGHNESS, create(forVariant(0,10, 14), ADD_VALUE));
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
}
