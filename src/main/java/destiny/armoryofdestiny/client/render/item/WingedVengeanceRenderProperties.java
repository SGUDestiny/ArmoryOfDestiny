package destiny.armoryofdestiny.client.render.item;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public class WingedVengeanceRenderProperties implements IClientItemExtensions
{
    public static final WingedVengeanceRenderProperties INSTANCE = new WingedVengeanceRenderProperties();
    private WingedVengeanceRenderer renderer;

    public WingedVengeanceRenderProperties()
    {
    }

    @Override
    public HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
        if(this.renderer == null)
            this.renderer = new WingedVengeanceRenderer();

        this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);
        return this.renderer;
    }
}
