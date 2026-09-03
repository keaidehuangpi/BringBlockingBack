package com.felpslipe.bbb.mixin;

import com.felpslipe.bbb.misc.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {

    @Unique
    private boolean bringBlockingBack$eatingPunchStarted;

    @Inject(method = "handleKeybinds", at = @At("HEAD"))
    private void bringBlockingBack$animateAttackWhileEating(CallbackInfo ci) {
        Minecraft minecraft = (Minecraft) (Object) this;
        LocalPlayer player = minecraft.player;
        if (player == null || !Utils.shouldPunchWhileConsuming(player)) {
            this.bringBlockingBack$eatingPunchStarted = false;
            return;
        }

        boolean attackDown = minecraft.options.keyAttack.isDown();
        if (!attackDown || !minecraft.options.keyUse.isDown()) {
            if (!attackDown) {
                this.bringBlockingBack$eatingPunchStarted = false;
            }
            return;
        }

        // Repeated calls while targeting a block retain the old mining-style swing loop.
        if (!this.bringBlockingBack$eatingPunchStarted || minecraft.hitResult instanceof BlockHitResult) {
            player.swing(InteractionHand.MAIN_HAND);
            this.bringBlockingBack$eatingPunchStarted = true;
        }
    }
}
