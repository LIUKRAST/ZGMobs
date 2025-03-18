package net.frozenblock.zgmobs;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue DISABLE_GERMONIUM = BUILDER
            .comment("Deactivate Germonium Mobs")
            .define("disableGermonium", false);

    public static final ModConfigSpec.IntValue GERMONIUM_BASE_CHANCE = BUILDER
            .comment("It determines the chance when a mob becomes a Germonium")
            .defineInRange("germoniumBaseChance", 5, 0, 100);

    public static final ModConfigSpec.IntValue CELESTIUM_VARIANT = BUILDER
            .comment("It determines the chance when a mob becomes a Germonium Celestium")
            .defineInRange("germoniumEqualizer", 10, 0, 100);

    public static final ModConfigSpec.IntValue CELESTIUM_DEATH_ROLL = BUILDER
            .comment("Max amount of mobs that will be spawned when a celestium dies. Will choose randomly from 0 to this value")
            .defineInRange("celestium_death_roll", 4, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec.BooleanValue ENABLE_SHULKER_BULLETS = BUILDER
            .comment("Wether the germonium mobs should or should not shoot shulker bullets")
            .define("enable_shulker_bullets", true);

    static final ModConfigSpec SPEC = BUILDER.build();
}
