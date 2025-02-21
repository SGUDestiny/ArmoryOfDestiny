package destiny.armoryofdestiny.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import destiny.armoryofdestiny.blockentity.AssemblyTableBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class AssemblyTableRenderer<T extends AssemblyTableBlockEntity> implements BlockEntityRenderer<T> {
    public AssemblyTableRenderer(BlockEntityRendererProvider.Context rendererDispatcherIn) {

    }

    @Override
    public void render(T table, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        ItemStack wantItem = table.getWantItem();
        int craftingProgress = table.getCraftingProgress();

        //Check if blueprint is present
        if (table.getItem(1) != ItemStack.EMPTY) {
            //If result item is present, render result item
            if (table.getItem(11) != ItemStack.EMPTY) {
                renderResultItem();
            } else if (table.getItem(craftingProgress) == wantItem) {
                //Else if current stack is wanted item, render lying ingredient
                renderLyingItem();
            } else if (wantItem != ItemStack.EMPTY) {
                //Else if wanted item is present, render wanted item
                renderWantItem();
            }
        }
    }

    private void renderWantItem() {

    }

    private void renderLyingItem() {

    }

    private void renderResultItem() {

    }
}
