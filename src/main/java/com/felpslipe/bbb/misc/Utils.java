package com.felpslipe.bbb.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;

public class Utils {
    public static void firstPersonSwordBlock(PoseStack stack) {
        stack.translate(-0.15f, 0.16f, 0.15f);
        stack.mulPose(Axis.YP.rotationDegrees(-18.0f));
        stack.mulPose(Axis.ZP.rotationDegrees(82.0f));
        stack.mulPose(Axis.YP.rotationDegrees(112.0f));
    }

    public static void swordBlockThirdPerson(PoseStack stack) {
        stack.mulPose(Axis.YP.rotationDegrees(-90.0f));
        stack.mulPose(Axis.ZP.rotationDegrees(-40.0f));
        stack.mulPose(Axis.XP.rotationDegrees(51.0f));
        stack.mulPose(Axis.ZP.rotationDegrees(180.0f));
        stack.mulPose(Axis.XP.rotationDegrees(197.2f));
        stack.translate(-0.22f, 0.13f, -0.22f);
    }

    public static boolean shouldPunchWhileConsuming(LivingEntity entity) {
        return entity.isUsingItem() && isEatingOrDrinking(entity.getUseItem());
    }

    public static boolean shouldPunchWhileConsuming(LivingEntity entity, ItemStack renderedStack) {
        return shouldPunchWhileConsuming(entity)
                && renderedStack.getUseAnimation() == entity.getUseItem().getUseAnimation();
    }

    private static boolean isEatingOrDrinking(ItemStack stack) {
        ItemUseAnimation animation = stack.getUseAnimation();
        return animation == ItemUseAnimation.EAT || animation == ItemUseAnimation.DRINK;
    }

    public static void eatingPunch(PoseStack stack, float swingProgress) {
        float progress = Mth.clamp(swingProgress, 0.0F, 1.0F);
        float arc = Mth.sin(Mth.sqrt(progress) * Mth.PI);
        stack.translate(arc * -0.4F, 0.0F, 0.0F);
        stack.mulPose(Axis.YP.rotationDegrees(arc * -20.0F));
        stack.mulPose(Axis.XP.rotationDegrees(arc * -80.0F));
    }
}
