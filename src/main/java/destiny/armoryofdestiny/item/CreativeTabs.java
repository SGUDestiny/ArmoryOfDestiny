package destiny.armoryofdestiny.item;

import destiny.armoryofdestiny.ArmoryOfDestiny;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static destiny.armoryofdestiny.item.DoubleTroubleItem.LEFT_BARREL;
import static destiny.armoryofdestiny.item.DoubleTroubleItem.RIGHT_BARREL;
import static destiny.armoryofdestiny.item.SharpIronyItem.*;
import static destiny.armoryofdestiny.item.Spas12Item.*;

public class CreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ArmoryOfDestiny.MODID);

    public static final List<Supplier<? extends ItemLike>> CREATIVE_TAB_ITEMS = new ArrayList<>();
    public static final RegistryObject<CreativeModeTab> CREATIVE_TAB = CREATIVE_MODE_TABS.register("armoryofdestiny", () -> CreativeModeTab.builder()
            .icon(() -> ItemRegistry.MURASAMA.get().getDefaultInstance())
            .title(Component.translatable("itemGroup.armoryofdestiny.tab"))
            .displayItems((parameters, output) -> {
                ItemStack sharpIrony = new ItemStack(ItemRegistry.SHARP_IRONY.get());
                ItemStack doubleTrouble = new ItemStack(ItemRegistry.DOUBLE_TROUBLE.get());
                ItemStack spas12 = new ItemStack(ItemRegistry.SPAS12.get());

                if(sharpIrony.getItem() instanceof SharpIronyItem) {
                    sharpIrony.getOrCreateTag().putBoolean(IS_OPEN, true);
                    sharpIrony.getOrCreateTag().putInt(AMMO_COUNT, 5);
                    sharpIrony.getOrCreateTag().putBoolean(VALUES_SET, true);
                    output.accept(sharpIrony);
                }
                if (spas12.getItem() instanceof Spas12Item){
                    spas12.getOrCreateTag().putInt(CHAMBER, 0);
                    spas12.getOrCreateTag().putInt(SHELL_TUBE, 0);
                    spas12.getOrCreateTag().putString(STATE, "idle_pump");
                    spas12.getOrCreateTag().putString(SHELL_TYPE, "buckshot_shell");
                    output.accept(spas12);
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
