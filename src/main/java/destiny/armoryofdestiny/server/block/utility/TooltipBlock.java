package destiny.armoryofdestiny.server.block.utility;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static destiny.armoryofdestiny.server.misc.UtilityVariables.NONE;

public class TooltipBlock extends Block {
    public TooltipBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter getter, List<Component> components, TooltipFlag flag) {
        String itemRarity = getItemRarity(stack);
        String itemTrivia = getTriviaType();

        //Rarity
        if (hasRarity()) {
            MutableComponent rarity = Component.translatable("tooltip.line.rarity." + itemRarity);
            components.add(rarity);
        }

        //Trivia
        if (!itemTrivia.equals("none") && hasTrivia()) {
            MutableComponent trivia = Component.translatable("tooltip.line.trivia")
                    .withStyle(ChatFormatting.DARK_GRAY);
            components.add(trivia);
            MutableComponent trivia_title = Component.translatable("tooltip.line.dropdown")
                    .append(Component.translatable("tooltip.line.trivia." + itemTrivia + ".title")
                            .withStyle(ChatFormatting.GRAY));
            components.add(trivia_title);

            int triviaLines = getTriviaLines(itemTrivia);
            //Print trivia
            for (int i = 1; i <= triviaLines; i++) {
                MutableComponent trivia_description = Component.translatable("tooltip.line.dropdown")
                        .append(Component.translatable("tooltip.line.trivia." + itemTrivia + ".description." + i)
                                .withStyle(ChatFormatting.DARK_GRAY));
                components.add(trivia_description);
            }
        }
    }

    public int getTriviaLines(String trivia) {
        int i = 1;
        String baseKey = "tooltip.line.trivia." + trivia + ".description.";

        while (true) {
            String key = baseKey + i;
            Component component = Component.translatable(key);
            if (component.getString().equals(key)) {
                break;
            }
            i++;
        }
        return i - 1;
    }

    public String getItemName(ItemStack stack) {
        return stack.getItem().toString();
    }

    public String getItemRarity(ItemStack stack) {
        return NONE;
    }

    public String getTriviaType() {
        return NONE;
    }

    public boolean hasRarity() {
        return true;
    }

    public boolean hasTrivia() {
        return true;
    }
}
