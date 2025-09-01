package destiny.armoryofdestiny.server.item.utility;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

import static destiny.armoryofdestiny.server.item.SmithingTongsItem.HELD_ITEM;

public class SmithingTongsItemProperty implements ClampedItemPropertyFunction {
    @Override
    public float unclampedCall(@NotNull ItemStack stack, @Nullable ClientLevel pLevel, @Nullable LivingEntity pEntity, int pSeed) {
        if (stack.getTag() != null) {
            ItemStack held_item = ItemStack.of(stack.getTag().getCompound(HELD_ITEM));

            if (held_item.getItem() != Items.AIR) {
                return 1;
            }
            return 0;
        }
        return 0;
    }
}
