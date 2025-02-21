package net.frozenblock.zgmobs;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.BooleanValue DISABLE_GERMONIUM = BUILDER
            .comment("Deactivate Germonium Mobs")
            .define("disableGermonium", false);

    public static final ForgeConfigSpec.IntValue GERMONIUM_PERCENTAGE = BUILDER
            .comment("Percentage for a mob to be a Germonium type")
            .defineInRange("germoniumPercentage", 5, 0, 100);

    public static final ForgeConfigSpec.IntValue CELESTIUM_PERCENTAGE = BUILDER
            .comment("Percentage for a Germonium to be a Celestium")
            .defineInRange("celestiumPercentage", 10, 0, 100);

    static final ForgeConfigSpec SPEC = BUILDER.build();
}
