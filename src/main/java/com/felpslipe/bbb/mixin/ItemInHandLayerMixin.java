package com.felpslipe.bbb.mixin;

import com.felpslipe.bbb.config.ConfigHelper;
import com.felpslipe.bbb.misc.Utils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.felpslipe.bbb.BringBlockingBack.client;

@Mixin(ItemInHandLayer.class)
public abstract class ItemInHandLayerMixin<S extends ArmedEntityRenderState, M extends EntityModel<S> & ArmedModel> {

    @Inject(method = "submitArmWithItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/item/ItemStackRenderState;submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;III)V", shift = At.Shift.BEFORE))
    private void submitArmWithItem(S armedEntityRenderState, ItemStackRenderState itemStackRenderState, ItemStack itemStack, HumanoidArm humanoidArm, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i, CallbackInfo ci) {
        LivingEntity player = client.player;
        if(player != null) {
            ItemStack mainHandItem = player.getMainHandItem();
            if (!(armedEntityRenderState instanceof AvatarRenderState avatarRenderState)) return;
            if(avatarRenderState.id == player.getId()) {
                 if (player.getOffhandItem().isEmpty() && ConfigHelper.canBlock(mainHandItem) && humanoidArm == HumanoidArm.RIGHT && client.options.keyUse.isDown()) {
                    Utils.swordBlockThirdPerson(poseStack);
                }
            }
        }
    }
}

