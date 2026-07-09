package com.felpslipe.bbb.mixin;

import com.felpslipe.bbb.config.ConfigHelper;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.felpslipe.bbb.BringBlockingBack.client;

@Mixin(PlayerModel.class)
public abstract class PlayerModelMixin extends HumanoidModel<AvatarRenderState> {

    public PlayerModelMixin(ModelPart modelPart) {
        super(modelPart);
    }

    @Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;)V", at = @At("TAIL"))
    private void setupAnim(AvatarRenderState avatarRenderState, CallbackInfo ci) {
        LivingEntity player = client.player;
        if (player != null && avatarRenderState.id == client.player.getId()) {
            ItemStack mainHandItem = player.getMainHandItem();
            if (player.getOffhandItem().isEmpty() && ConfigHelper.canBlock(mainHandItem) && client.options.keyUse.isDown()) {
                this.rightArm.xRot = -0.94F;
                //this.rightArm.yRot = -0.52F;
            }
        }
    }


}
