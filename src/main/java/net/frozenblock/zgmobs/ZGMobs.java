package net.frozenblock.zgmobs;

import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

@SuppressWarnings("deprecation")
@Mod(ZGMobs.MOD_ID)
public class ZGMobs {
    public static final String MOD_ID = "zgmobs";

    public static final Material INFERNIUM_FIRE_0 = new Material(TextureAtlas.LOCATION_BLOCKS, ResourceLocation.fromNamespaceAndPath(MOD_ID, "block/infernium_fire_0"));
    public static final Material INFERNIUM_FIRE_1 = new Material(TextureAtlas.LOCATION_BLOCKS, ResourceLocation.fromNamespaceAndPath(MOD_ID, "block/infernium_fire_1"));

    public static final Material CELESTIUM_FIRE_0 = new Material(TextureAtlas.LOCATION_BLOCKS, ResourceLocation.fromNamespaceAndPath(MOD_ID, "block/celestium_fire_0"));
    public static final Material CELESTIUM_FIRE_1 = new Material(TextureAtlas.LOCATION_BLOCKS, ResourceLocation.fromNamespaceAndPath(MOD_ID, "block/celestium_fire_1"));


    public static final EntityDataAccessor<Integer> DATA_TYPE_ID = SynchedEntityData.defineId(Monster.class, EntityDataSerializers.INT);


    public ZGMobs(FMLJavaModLoadingContext ctx) {
        ctx.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    public static List<EntityType<?>> getEntitiesFromTag(ResourceLocation tagId) {
        TagKey<EntityType<?>> entityTag = TagKey.create(Registries.ENTITY_TYPE, tagId);
        return ForgeRegistries.ENTITY_TYPES.tags()
                .getTag(entityTag)
                .stream()
                .toList();
    }
}
