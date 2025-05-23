package net.frozenblock.zgmobs;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.BooleanValue DISABLE_GERMONIUM = BUILDER
            .comment("Deactivate Germonium Mobs")
            .define("disableGermonium", false);

    public static final ForgeConfigSpec.IntValue GERMONIUM_BASE_CHANCE = BUILDER
            .comment("It determines the chance when a mob becomes a Germonium")
            .defineInRange("germoniumBaseChance", 5, 0, 100);

    public static final ForgeConfigSpec.IntValue CELESTIUM_VARIANT = BUILDER
            .comment("It determines the chance when a mob becomes a Germonium Celestium")
            .defineInRange("germoniumEqualizer", 10, 0, 100);

    public static final ForgeConfigSpec.IntValue CELESTIUM_DEATH_ROLL = BUILDER
            .comment("Max amount of mobs that will be spawned when a celestium dies. Will choose randomly from 0 to this value")
            .defineInRange("celestium_death_roll", 4, 0, Integer.MAX_VALUE);

    public static final ForgeConfigSpec.BooleanValue ENABLE_SHULKER_BULLETS = BUILDER
            .comment("Wether the germonium mobs should or should not shoot shulker bullets")
            .define("enable_shulker_bullets", true);

    public static final ForgeConfigSpec.IntValue SHULKER_MIN_COOLDOWN = BUILDER
            .comment("Minimum shulker cooldown time (in ticks)")
            .defineInRange("shulker_min_cooldown", 400, 0, Integer.MAX_VALUE);
    public static final ForgeConfigSpec.IntValue SHULKER_MAX_COOLDOWN = BUILDER
            .comment("Maximum shulker cooldown time (in ticks)")
            .defineInRange("shulker_max_cooldown", 600, 0, Integer.MAX_VALUE);

    static final ForgeConfigSpec SPEC = BUILDER.build();
}
