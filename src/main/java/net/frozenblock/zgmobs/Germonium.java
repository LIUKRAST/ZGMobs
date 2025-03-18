package net.frozenblock.zgmobs;

import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import org.lwjgl.system.NonnullDefault;

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
}
