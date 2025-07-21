package net.frozenblock.zgmobs;

import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
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

    public static EntityDataAccessor<Integer> DATA_GERMONIUM;

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public ZGMobs(FMLJavaModLoadingContext ctx) {
        ctx.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static List<EntityType<?>> getEntitiesFromTag(ResourceLocation tagId) {
        TagKey<EntityType<?>> entityTag = TagKey.create(Registries.ENTITY_TYPE, tagId);
        var nullable = ForgeRegistries.ENTITY_TYPES.tags();
        return nullable == null ? List.of() : nullable.getTag(entityTag)
                .stream()
                .toList();
    }

    @SubscribeEvent
    public void livingDeath(LivingDeathEvent event) {
        var that = event.getEntity();
        if (that instanceof Enemy && GermoniumUtils.getVariant(that) == Germonium.CELESTIUM && !that.level().isClientSide()) {
            var entities = ZGMobs.getEntitiesFromTag(ZGMobs.id("celestium_spawns"));
            for (int i = 0; i < Math.random() * Config.CELESTIUM_DEATH_ROLL.get(); i++) {
                var entityType = entities.get((int) (Math.random() * entities.size()));
                var tag = new CompoundTag();
                tag.putString("Germonium", "infernium");
                entityType.spawn(
                        (ServerLevel) that.level(),
                        tag,
                        null,
                        that.getOnPos().above(),
                        MobSpawnType.REINFORCEMENT,
                        false,
                        false
                );
            }
        }
    }
}
