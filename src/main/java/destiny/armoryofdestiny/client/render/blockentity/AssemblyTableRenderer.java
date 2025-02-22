package destiny.armoryofdestiny.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import destiny.armoryofdestiny.block.AssemblyTableBlock;
import destiny.armoryofdestiny.blockentity.AssemblyTableBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class AssemblyTableRenderer implements BlockEntityRenderer<AssemblyTableBlockEntity> {
    public AssemblyTableRenderer(BlockEntityRendererProvider.Context rendererDispatcherIn) {

    }

    @Override
    public void render(AssemblyTableBlockEntity table, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        poseStack.pushPose();
        Direction direction = table.getBlockState().getValue(HORIZONTAL_FACING);
        ItemStack wantItem = table.getWantItem();
        ItemStack resultItem = table.getItem(10);
        Level level = table.getLevel();

        int craftingProgress = table.getCraftingProgress();

        //Check if blueprint is present
        if (table.getItem(0) != ItemStack.EMPTY) {
            //If result item is present, render result item
            if (craftingProgress > 0 && !table.getItem(table.getCraftingProgress()).isEmpty()) {
                ItemStack currentItem = table.getItem(table.getCraftingProgress());
                //Else if current stack is wanted item, render lying ingredient
                renderLyingItem(level, poseStack, currentItem, bufferIn, combinedLightIn, direction);
            } else if (wantItem != ItemStack.EMPTY) {
                //Else if wanted item is present, render wanted item
                renderSpinningItem(level, wantItem, poseStack, partialTicks, bufferIn, combinedLightIn);
            }
        } else if (resultItem != ItemStack.EMPTY) {
            renderSpinningItem(level, resultItem, poseStack, partialTicks, bufferIn, combinedLightIn);
        }
        poseStack.popPose();
    }

    private void renderSpinningItem(Level level, ItemStack stack, PoseStack poseStack, float partialTicks, MultiBufferSource bufferIn, int combinedLightIn) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        float time = (level.getGameTime() + partialTicks) * 0.8f;
        float yOffset = Mth.sin(time * 0.05f) * 0.1f + 0.25f;

        poseStack.translate(0.5, 1 + yOffset, 0.5);
        poseStack.mulPose(Axis.YP.rotation(time * 0.1f));
        poseStack.scale(1, 1, 1);

        itemRenderer.renderStatic(stack, ItemDisplayContext.GROUND, combinedLightIn, OverlayTexture.NO_OVERLAY, poseStack, bufferIn, level, 0);
    }

    private void renderLyingItem(Level level, PoseStack poseStack, ItemStack stack, MultiBufferSource bufferIn, int combinedLightIn, Direction direction) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        poseStack.translate(0.625, 1, 0.5);

        float f = -direction.toYRot();
        poseStack.mulPose(Axis.YP.rotationDegrees(f));

        poseStack.mulPose(Axis.XP.rotationDegrees(-90));

        poseStack.scale(1, 1, 1);

        itemRenderer.renderStatic(stack, ItemDisplayContext.GROUND, combinedLightIn, OverlayTexture.NO_OVERLAY, poseStack, bufferIn, level, 0);
    }

    private void renderResultItem() {

    }
}
