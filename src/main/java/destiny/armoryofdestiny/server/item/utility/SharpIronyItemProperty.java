package destiny.armoryofdestiny.server.item.utility;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

import static destiny.armoryofdestiny.server.item.weapon.SharpIronyItem.IS_OPEN;

public class SharpIronyItemProperty implements ClampedItemPropertyFunction {
    @Override
    public float unclampedCall(@NotNull ItemStack stack, @Nullable ClientLevel pLevel, @Nullable LivingEntity pEntity, int pSeed) {
        if (stack.getTag() != null) {
            if (stack.getTag().getBoolean(IS_OPEN)) {
                return 1;
            } else {
                return 0;
            }
        }
        return 1;
    }
}