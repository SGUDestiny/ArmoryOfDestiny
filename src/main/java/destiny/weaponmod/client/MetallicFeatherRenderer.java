package destiny.weaponmod.client;

import destiny.weaponmod.WeaponMod;
import destiny.weaponmod.entity.MetallicFeatherEntity;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class MetallicFeatherRenderer extends ArrowRenderer<MetallicFeatherEntity> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(WeaponMod.MODID, "textures/entity/metallic_feather.png");

    public MetallicFeatherRenderer(EntityRendererProvider.Context manager) {
        super(manager);
    }

    public ResourceLocation getTextureLocation(MetallicFeatherEntity arrow) {
        return TEXTURE;
    }


}
