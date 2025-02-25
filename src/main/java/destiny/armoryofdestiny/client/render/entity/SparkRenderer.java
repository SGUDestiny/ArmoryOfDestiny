package destiny.armoryofdestiny.client.render.entity;

import destiny.armoryofdestiny.ArmoryOfDestiny;
import destiny.armoryofdestiny.server.entity.SparkEntity;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class SparkRenderer extends ArrowRenderer<SparkEntity> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(ArmoryOfDestiny.MODID, "textures/entity/spark.png");

    public SparkRenderer(EntityRendererProvider.Context manager) {
        super(manager);
    }

    public ResourceLocation getTextureLocation(SparkEntity arrow) {
        return TEXTURE;
    }
}