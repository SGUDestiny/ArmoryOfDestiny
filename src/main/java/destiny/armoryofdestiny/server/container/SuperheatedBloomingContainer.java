package destiny.armoryofdestiny.server.container;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class SuperheatedBloomingContainer implements Container {
    public ItemStack input;
    public int meltTime;

    public SuperheatedBloomingContainer(ItemStack input, int meltTime)
    {
        this.input = input;
        this.meltTime = meltTime;
    }

    public int getMeltTime()
    {
        return meltTime;
    }

    public void setMeltTime(int meltTime)
    {
        this.meltTime = meltTime;
    }

    @Override
    public int getContainerSize()
    {
        return 32;
    }

    @Override
    public boolean isEmpty()
    {
        return input.isEmpty();
    }

    @Override
    public ItemStack getItem(int slot)
    {
        return input;
    }

    @Override
    public ItemStack removeItem(int slot, int amount)
    {
        input.shrink(amount);
        if(!input.isEmpty())
            this.setChanged();

        return input;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot)
    {
        if(input.isEmpty())
            return ItemStack.EMPTY;
        else input.setCount(0);
        return input;
    }

    @Override
    public void setItem(int slot, ItemStack stack)
    {
        this.input = stack;
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
        this.input = ItemStack.EMPTY;
    }
}
