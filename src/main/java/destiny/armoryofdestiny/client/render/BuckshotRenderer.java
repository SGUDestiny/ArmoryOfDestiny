package destiny.armoryofdestiny.client.render;

import destiny.armoryofdestiny.ArmoryOfDestiny;
import destiny.armoryofdestiny.entity.BuckshotEntity;
import destiny.armoryofdestiny.entity.PelletEntity;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class BuckshotRenderer extends ArrowRenderer<BuckshotEntity> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(ArmoryOfDestiny.MODID, "textures/entity/buckshot.png");

    public BuckshotRenderer(EntityRendererProvider.Context manager) {
        super(manager);
    }

    public ResourceLocation getTextureLocation(BuckshotEntity arrow) {
        return TEXTURE;
    }
}
