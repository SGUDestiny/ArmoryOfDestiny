package destiny.weaponmod.item;

import destiny.weaponmod.WeaponMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static destiny.weaponmod.item.DoubleTroubleItem.LEFT_BARREL;
import static destiny.weaponmod.item.DoubleTroubleItem.RIGHT_BARREL;
import static destiny.weaponmod.item.SharpIronyItem.*;

public class CreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, WeaponMod.MODID);

    public static final List<Supplier<? extends ItemLike>> CREATIVE_TAB_ITEMS = new ArrayList<>();
    public static final RegistryObject<CreativeModeTab> CREATIVE_TAB = CREATIVE_MODE_TABS.register("weaponmod", () -> CreativeModeTab.builder()
            .icon(() -> ItemRegistry.MURASAMA.get().getDefaultInstance())
            .title(Component.translatable("itemGroup.weaponmod_tab"))
            .displayItems((parameters, output) -> {
                ItemStack sharpIrony = new ItemStack(ItemRegistry.SHARP_IRONY.get());
                ItemStack doubleTrouble = new ItemStack(ItemRegistry.DOUBLE_TROUBLE.get());
                if(sharpIrony.getItem() instanceof SharpIronyItem) {
                    sharpIrony.getOrCreateTag().putBoolean(IS_OPEN, true);
                    sharpIrony.getOrCreateTag().putInt(AMMO_COUNT, 5);
                    sharpIrony.getOrCreateTag().putBoolean(VALUES_SET, true);
                    output.accept(sharpIrony);
                }
                if (doubleTrouble.getItem() instanceof DoubleTroubleItem){
                    doubleTrouble.getOrCreateTag().putBoolean(IS_OPEN, false);
                    doubleTrouble.getOrCreateTag().putInt(LEFT_BARREL, 2);
                    doubleTrouble.getOrCreateTag().putInt(RIGHT_BARREL, 2);
                    doubleTrouble.getOrCreateTag().putBoolean(VALUES_SET, true);
                    output.accept(doubleTrouble);
                }
                CREATIVE_TAB_ITEMS.forEach(ItemLike -> output.accept(ItemLike.get())); // Add the example item to the tab. For your own tabs, this method is preferred over the event
            })
            .build()
    );

    public static <T extends Item> RegistryObject<T> addToTab(RegistryObject<T> ItemLike) {
        CREATIVE_TAB_ITEMS.add(ItemLike);
        return ItemLike;
    }
}
