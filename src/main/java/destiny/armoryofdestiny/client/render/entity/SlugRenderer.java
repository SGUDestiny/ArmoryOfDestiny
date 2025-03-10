package destiny.armoryofdestiny.client.render.entity;

import destiny.armoryofdestiny.ArmoryOfDestiny;
import destiny.armoryofdestiny.server.entity.SlugEntity;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class SlugRenderer extends ArrowRenderer<SlugEntity> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(ArmoryOfDestiny.MODID, "textures/entity/slug.png");

    public SlugRenderer(EntityRendererProvider.Context manager) {
        super(manager);
    }

    public ResourceLocation getTextureLocation(SlugEntity arrow) {
        return TEXTURE;
    }
}