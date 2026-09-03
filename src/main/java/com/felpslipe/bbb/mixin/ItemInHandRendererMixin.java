package com.felpslipe.bbb.mixin;

import com.felpslipe.bbb.config.ConfigHelper;
import com.felpslipe.bbb.misc.Utils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.felpslipe.bbb.BringBlockingBack.client;

@Mixin(ItemInHandRenderer.class)
public abstract class ItemInHandRendererMixin {

    @Shadow
    protected abstract void swingArm(float swingProgress, float equipProgress, PoseStack matrices, int armX, HumanoidArm arm);

    @Shadow
    protected abstract void applyItemArmTransform(PoseStack matrices, HumanoidArm arm, float equipProgress);

    @Shadow
    protected abstract void applyItemArmAttackTransform(PoseStack matrices, HumanoidArm arm, float swingProgress);

    @Inject(method = "renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext; Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector; I)V", at = @At("HEAD"))
    private void renderItem(LivingEntity livingEntity, ItemStack itemStack, ItemDisplayContext itemDisplayContext, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i, CallbackInfo ci) {
        ItemStack mainHandItem = livingEntity.getMainHandItem();
        if(!itemStack.isEmpty() && livingEntity.getOffhandItem().isEmpty() && ConfigHelper.canBlock(mainHandItem) && client.options.keyUse.isDown()) {
            Utils.firstPersonSwordBlock(poseStack);
        }

    }
    
    @Redirect(method = "renderArmWithItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;swingArm(FFLcom/mojang/blaze3d/vertex/PoseStack;ILnet/minecraft/world/entity/HumanoidArm;)V"))
    private void renderArmWithItem(ItemInHandRenderer instance, float f, float g, PoseStack poseStack, int i, HumanoidArm humanoidArm) {
        LivingEntity player = client.player;
        if(player != null) {
            ItemStack mainHandItem = player.getMainHandItem();
            if (player.getOffhandItem().isEmpty() && ConfigHelper.canBlock(mainHandItem) && client.options.keyUse.isDown()) {
                // Keep the blockhit swing, but do not let the equip progress lower and raise the sword.
                applyItemArmTransform(poseStack, humanoidArm, 0.0F);
                applyItemArmAttackTransform(poseStack, humanoidArm, f);
            } else {
                swingArm(f, g, poseStack, i, humanoidArm);
            }
        }
    }
}
