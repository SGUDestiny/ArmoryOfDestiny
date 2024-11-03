package destiny.armoryofdestiny.client.render;

import destiny.armoryofdestiny.ArmoryOfDestiny;
import destiny.armoryofdestiny.entity.ExplosiveSlugEntity;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class ExplosiveSlugRenderer extends ArrowRenderer<ExplosiveSlugEntity> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(ArmoryOfDestiny.MODID, "textures/entity/explosive_slug.png");

    public ExplosiveSlugRenderer(EntityRendererProvider.Context manager) {
        super(manager);
    }

    public ResourceLocation getTextureLocation(ExplosiveSlugEntity arrow) {
        return TEXTURE;
    }
}
