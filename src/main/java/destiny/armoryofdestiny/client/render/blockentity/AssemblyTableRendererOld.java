package destiny.armoryofdestiny.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import destiny.armoryofdestiny.server.block.blockentity.AssemblyTableBlockEntityOld;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

public class AssemblyTableRendererOld implements BlockEntityRenderer<AssemblyTableBlockEntityOld> {
    private final ItemRenderer itemRenderer;

    public AssemblyTableRendererOld(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }


    @Override
    public void render(AssemblyTableBlockEntityOld be, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        Level level = be.getLevel();
        if (level == null) return;

        // Render current ingredient
        if (be.getCurrentIngredientIndex() != -1 && !be.getRemainingIngredients().isEmpty()) {
            Ingredient ingredient = be.getRemainingIngredients().get(be.getCurrentIngredientIndex());
            if (ingredient != null && !ingredient.isEmpty()) {
                renderWantItem(poseStack, bufferSource, packedLight, partialTick,
                        ingredient.getItems()[0], 0.8f, level);
            }
        }

        // Render result item
        ItemStack result = be.getResultItem();
        if (!result.isEmpty()) {
            renderWantItem(poseStack, bufferSource, packedLight, partialTick,
                    result, 1.0f, level);
        }
    }

    private void renderWantItem(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, float partialTick, ItemStack stack, float scale, Level level) {
        poseStack.pushPose();

        // Animation calculations
        float time = (level.getGameTime() + partialTick) * 0.8f;
        float yOffset = Mth.sin(time * 0.05f) * 0.1f + 0.25f;

        // Transformations
        poseStack.translate(0.5, 1.25 + yOffset, 0.5);
        poseStack.mulPose(Axis.YP.rotation(time * 0.3f));
        poseStack.scale(scale, scale, scale);

        // Render with transparency
        itemRenderer.renderStatic(stack, ItemDisplayContext.GROUND, packedLight,
                OverlayTexture.NO_OVERLAY, poseStack, bufferSource,
                level, 0);

        poseStack.popPose();
    }

    private void renderIngredientItem() {

    }

    private void renderResultItem() {

    }
}