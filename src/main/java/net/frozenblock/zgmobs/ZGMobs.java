package net.frozenblock.zgmobs;

import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

import java.util.ArrayList;
import java.util.List;

@Mod(ZGMobs.MOD_ID)
public class ZGMobs {
    public static final String MOD_ID = "zgmobs";

    @SuppressWarnings("deprecation")
    public static final Material INFERNIUM_FIRE_0 = new Material(TextureAtlas.LOCATION_BLOCKS, id("block/infernium_fire_0"));
    @SuppressWarnings("deprecation")
    public static final Material INFERNIUM_FIRE_1 = new Material(TextureAtlas.LOCATION_BLOCKS, id("block/infernium_fire_1"));
    @SuppressWarnings("deprecation")
    public static final Material CELESTIUM_FIRE_0 = new Material(TextureAtlas.LOCATION_BLOCKS, id("block/celestium_fire_0"));
    @SuppressWarnings("deprecation")
    public static final Material CELESTIUM_FIRE_1 = new Material(TextureAtlas.LOCATION_BLOCKS, id("block/celestium_fire_1"));

    public static boolean IGNORE_NEXT_SETUP = false;
    public static EntityDataAccessor<Integer> DATA_GERMONIUM;

    public ZGMobs(@SuppressWarnings("unused") IEventBus modEventBus, ModContainer container) {
        //modEventBus.register(this);
        container.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    public static List<EntityType<?>> getEntitiesFromTag(ResourceLocation tag) {
        TagKey<EntityType<?>> entityTag = TagKey.create(BuiltInRegistries.ENTITY_TYPE.key(), tag);
        List<EntityType<?>> list = new ArrayList<>();
        BuiltInRegistries.ENTITY_TYPE.getTagOrEmpty(entityTag).forEach(e -> list.add(e.value()));
        return list;
    }

    public static ResourceLocation id(String germoniumModifier) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, germoniumModifier);
    }
}
