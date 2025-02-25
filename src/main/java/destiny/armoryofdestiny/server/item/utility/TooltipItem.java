package destiny.armoryofdestiny.server.item.utility;

import destiny.armoryofdestiny.ArmoryOfDestiny;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static destiny.armoryofdestiny.server.misc.UtilityVariables.NONE;

public class TooltipItem extends Item {
    public TooltipItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        String modId = ArmoryOfDestiny.MODID;
        String itemName = getItemName(stack);
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

        //Abilities
        if (hasAbilities()) {
            if (!isShift(level)) {
                MutableComponent ability = Component.translatable("tooltip.line.collapsed")
                        .append(Component.translatable("tooltip.line.ability.collapsed")
                                .withStyle(ChatFormatting.DARK_GRAY));
                components.add(ability);
            } else {
                MutableComponent ability = Component.translatable("tooltip.line.expanded")
                        .append(Component.translatable("tooltip.line.ability.expanded")
                                .withStyle(ChatFormatting.DARK_GRAY));
                components.add(ability);

                int abilityCount = getAbilitiesTotal(itemName);
                //Print abilities
                for (int i = 1; i <= abilityCount; i++) {
                    //Ability title
                    MutableComponent ability_title = Component.translatable("tooltip.line.dropdown")
                            .append(Component.translatable("item." + modId + "." + itemName + ".ability." + i + ".title")
                                    .withStyle(ChatFormatting.GRAY));
                    components.add(ability_title);

                    //Ability description
                    int descriptionLineCount = getAbilityLines(itemName, i);
                    for (int ii = 1; ii <= descriptionLineCount; ii++) {
                        MutableComponent ability_description = Component.translatable("tooltip.line.dropdown")
                                .append(Component.translatable("item." + modId + "." + itemName + ".ability." + i + ".description." + ii)
                                        .withStyle(ChatFormatting.DARK_GRAY));
                        components.add(ability_description);
                    }

                    //Ability cooldown
                    String key = "item." + modId + "." + itemName + ".ability." + i + ".cooldown";
                    Component component = Component.translatable(key);
                    if (!component.getString().equals(key)) {
                        MutableComponent ability_cooldown = Component.translatable("tooltip.line.cooldown")
                                .append(Component.translatable("tooltip.line.ability.cooldown").withStyle(ChatFormatting.GRAY))
                                .append(Component.translatable(key)
                                        .withStyle(ChatFormatting.DARK_GRAY));
                        components.add(ability_cooldown);
                    }
                }
            }
        }
    }

    public int getAbilitiesTotal(String itemName) {
        int i = 1;
        String baseKey = "item." + ArmoryOfDestiny.MODID + "." + itemName + ".ability.";

        while (true) {
            String key = baseKey + i + ".title";
            Component component = Component.translatable(key);
            if (component.getString().equals(key)) {
                break;
            }
            i++;
        }
        return i - 1;
    }

    public int getAbilityLines(String itemName, int abilityIndex) {
        int i = 1;
        String baseKey = "item." + ArmoryOfDestiny.MODID + "." + itemName + ".ability." + abilityIndex + ".description.";

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

    public boolean isShift (Level level) {
        if (level instanceof ClientLevel) {
            return Screen.hasShiftDown();
        }
        return Screen.hasShiftDown();
    }

    public boolean isControl (Level level) {
        if (level instanceof ClientLevel) {
            return Screen.hasControlDown();
        }
        return Screen.hasControlDown();
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

    public boolean hasAbilities() {
        return true;
    }
}