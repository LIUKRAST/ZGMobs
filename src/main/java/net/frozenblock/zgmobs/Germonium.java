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

    Germonium(int p_196658_, String p_196659_) {
        this.id = p_196658_;
        this.name = p_196659_;
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
}
