package destiny.weaponmod.client;

import destiny.weaponmod.WeaponMod;
import destiny.weaponmod.entity.PelletEntity;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class PelletRenderer extends ArrowRenderer<PelletEntity> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(WeaponMod.MODID, "textures/entity/pellet.png");

    public PelletRenderer(EntityRendererProvider.Context manager) {
        super(manager);
    }

    public ResourceLocation getTextureLocation(PelletEntity arrow) {
        return TEXTURE;
    }
}
