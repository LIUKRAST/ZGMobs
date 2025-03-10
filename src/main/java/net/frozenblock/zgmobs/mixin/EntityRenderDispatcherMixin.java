package net.frozenblock.zgmobs.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.mojang.blaze3d.vertex.PoseStack;
import net.frozenblock.zgmobs.Germonium;
import net.frozenblock.zgmobs.GermoniumUtils;
import net.frozenblock.zgmobs.ZGMobs;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Enemy;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {
    @Inject(method = "renderFlame", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;pushPose()V", ordinal = 0))
    private void changeLocals(PoseStack p_114454_, MultiBufferSource p_114455_, Entity entity, CallbackInfo ci, @Local(ordinal = 0) LocalRef<TextureAtlasSprite> sprite0, @Local(ordinal = 1) LocalRef<TextureAtlasSprite> sprite1) {
        if(entity instanceof Enemy) {
            var variant = GermoniumUtils.getVariant(entity);
            if(variant == Germonium.NORMAL) return;
            sprite0.set(variant == Germonium.INFERNIUM ? ZGMobs.INFERNIUM_FIRE_0.sprite() : ZGMobs.CELESTIUM_FIRE_0.sprite());
            sprite1.set(variant == Germonium.INFERNIUM ? ZGMobs.INFERNIUM_FIRE_1.sprite() : ZGMobs.CELESTIUM_FIRE_1.sprite());
        }

    }

    @Redirect(method = "renderFlame", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;getYRot()F"))
    private float renderFlameRedirect(Camera instance) {
        return 0;
    }
}
