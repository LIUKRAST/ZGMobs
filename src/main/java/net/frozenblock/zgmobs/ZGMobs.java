package net.frozenblock.zgmobs;

import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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

    public ZGMobs(IEventBus modEventBus, ModContainer container) {
        container.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    public static List<EntityType<?>> getEntitiesFromTag(ResourceLocation id) {
        TagKey<EntityType<?>> entityTag = TagKey.create(BuiltInRegistries.ENTITY_TYPE.key(), id);
        List<EntityType<?>> list = new ArrayList<>();
        BuiltInRegistries.ENTITY_TYPE.getTagOrEmpty(entityTag).forEach(e -> list.add(e.value()));
        return list;
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
