package destiny.armoryofdestiny.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import destiny.armoryofdestiny.blockentity.AssemblyTableBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemStack;

//public class AssemblyTableRenderer implements BlockEntityRenderer<AssemblyTableBlockEntity> {
//    public AssemblyTableRenderer(BlockEntityRendererProvider.Context rendererDispatcherIn) {
//
//    }
//
//    @Override
//    public void render(AssemblyTableBlockEntity table, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
//        poseStack.pushPose();
//        ItemStack wantItem = table.getWantItem();
//        int craftingProgress = table.getCraftingProgress();
//
//        //Check if blueprint is present
//        if (table.getItem(1) != ItemStack.EMPTY) {
//            //If result item is present, render result item
//            if (table.getItem(11) != ItemStack.EMPTY) {
//                renderResultItem();
//            } else if (table.getItem(craftingProgress) == wantItem) {
//                //Else if current stack is wanted item, render lying ingredient
//                renderLyingItem();
//            } else if (wantItem != ItemStack.EMPTY) {
//                //Else if wanted item is present, render wanted item
//                renderWantItem();
//            }
//        }
//        poseStack.popPose();
//    }
//
//    private void renderWantItem() {
//
//    }
//
//    private void renderLyingItem() {
//
//    }
//
//    private void renderResultItem() {
//
//    }
//}
