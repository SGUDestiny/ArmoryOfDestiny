package destiny.armoryofdestiny.server.item.utility;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

import static destiny.armoryofdestiny.server.item.MurasamaItem.ABILITY_TICK;

public class MurasamaItemProperty implements ClampedItemPropertyFunction {
    @Override
    public float unclampedCall(@NotNull ItemStack stack, @Nullable ClientLevel pLevel, @Nullable LivingEntity pEntity, int pSeed) {
        if (stack.getTag() != null) {
            if (stack.getTag().getInt(ABILITY_TICK) > 0) {
                return 1;
            }
            return 0;
        }
        return 0;
    }
}

