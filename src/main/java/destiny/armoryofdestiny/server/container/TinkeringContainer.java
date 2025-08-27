package destiny.armoryofdestiny.server.container;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class TinkeringContainer implements Container
{
    public List<ItemStack> inputs;

    public TinkeringContainer(List<ItemStack> inputs)
    {
        this.inputs = inputs;
    }

    @Override
    public int getContainerSize()
    {
        return 32;
    }

    @Override
    public boolean isEmpty()
    {
        return inputs.isEmpty();
    }

    @Override
    public ItemStack getItem(int slot)
    {
        return inputs.get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount)
    {
        ItemStack stack = inputs.get(slot);
        stack.shrink(amount);
        if(!stack.isEmpty())
            this.setChanged();

        return stack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot)
    {
        return inputs.remove(slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack)
    {
        this.inputs.set(slot, stack);
    }

    @Override
    public void setChanged()
    {

    }

    @Override
    public boolean stillValid(Player player)
    {
        return true;
    }

    @Override
    public void clearContent()
    {
        this.inputs = new ArrayList<>();
    }
}
