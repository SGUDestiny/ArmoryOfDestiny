package destiny.armoryofdestiny.server.item.utility;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static destiny.armoryofdestiny.server.util.UtilityVariables.NONE;

public class TooltipSwordItem extends SwordItem {
    public TooltipSwordItem(Tier p_43269_, int p_43270_, float p_43271_, Properties p_43272_) {
        super(p_43269_, p_43270_, p_43271_, p_43272_);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        String itemName = getItemName(stack);
        String rarityTranslatable = getRarityTranslatable(stack);
        String triviaTranslatable = getTriviaTranslatable();

        //Rarity
        if (hasRarity()) {
            MutableComponent rarity = Component.translatable(rarityTranslatable);
            components.add(rarity);
        }

        //Trivia
        if (!triviaTranslatable.equals(NONE) && hasTrivia()) {
            //Print trivia base
            MutableComponent trivia = Component.translatable("tooltip.armoryofdestiny.trivia").withStyle(ChatFormatting.DARK_GRAY);
            components.add(trivia);

            MutableComponent trivia_title = Component.translatable("tooltip.armoryofdestiny.dropdown")
                    .append(Component.translatable(triviaTranslatable + ".title")
                            .withStyle(ChatFormatting.GRAY));
            components.add(trivia_title);

            //Print trivia
            int triviaLines = getLineAmountTrivia(triviaTranslatable);
            for (int i = 1; i <= triviaLines; i++) {
                MutableComponent trivia_description = Component.translatable("tooltip.armoryofdestiny.dropdown")
                        .append(Component.translatable(triviaTranslatable + ".description." + i)
                                .withStyle(ChatFormatting.DARK_GRAY));
                components.add(trivia_description);
            }
        }

        //Abilities
        if (hasAbilities()) {
            if (!isShift(level)) {
                MutableComponent ability = Component.translatable("tooltip.armoryofdestiny.collapsed")
                        .append(Component.translatable("tooltip.armoryofdestiny.ability.collapsed")
                                .withStyle(ChatFormatting.DARK_GRAY));
                components.add(ability);
            } else {
                MutableComponent ability = Component.translatable("tooltip.armoryofdestiny.expanded")
                        .append(Component.translatable("tooltip.armoryofdestiny.ability.expanded")
                                .withStyle(ChatFormatting.DARK_GRAY));
                components.add(ability);

                //Print abilities
                int abilityCount = getAbilityAmount(itemName);
                for (int i = 1; i <= abilityCount; i++) {
                    //Ability title
                    MutableComponent ability_title = Component.translatable("tooltip.armoryofdestiny.dropdown")
                            .append(Component.translatable("item.armoryofdestiny." + itemName + ".ability." + i + ".title")
                                    .withStyle(ChatFormatting.GRAY));
                    components.add(ability_title);

                    //Ability description
                    int descriptionLineCount = getLineAmountAbility(itemName, i);
                    for (int ii = 1; ii <= descriptionLineCount; ii++) {
                        MutableComponent ability_description = Component.translatable("tooltip.armoryofdestiny.dropdown")
                                .append(Component.translatable("item.armoryofdestiny." + itemName + ".ability." + i + ".description." + ii)
                                        .withStyle(ChatFormatting.DARK_GRAY));
                        components.add(ability_description);
                    }

                    //Ability cooldown
                    String key = "item.armoryofdestiny." + itemName + ".ability." + i + ".cooldown";
                    Component component = Component.translatable(key);
                    if (!component.getString().equals(key)) {
                        MutableComponent ability_cooldown = Component.translatable("tooltip.armoryofdestiny.cooldown")
                                .append(Component.translatable("tooltip.armoryofdestiny.ability.cooldown").withStyle(ChatFormatting.GRAY))
                                .append(Component.translatable(key)
                                        .withStyle(ChatFormatting.DARK_GRAY));
                        components.add(ability_cooldown);
                    }
                }
            }
        }
    }

    public int getAbilityAmount(String itemName) {
        String key = "item.armoryofdestiny." + itemName + ".ability.";
        return getLineAmount(key, ".title");
    }

    public int getLineAmountAbility(String itemName, int abilityIndex) {
        String key = "item.armoryofdestiny." + itemName + ".ability." + abilityIndex + ".description.";
        return getLineAmount(key, null);
    }

    public int getLineAmountTrivia(String trivia) {
        String key = trivia + ".description.";
        return getLineAmount(key, null);
    }

    public int getLineAmount(String translatable, @Nullable String tail) {
        int i = 1;
        if (tail == null) {
            tail = "";
        }

        while (true) {
            String key = translatable + i + tail;
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
        return false;
    }

    public boolean isShift (Player player) {
        return player.isShiftKeyDown();
    }

    public boolean isControl (Level level) {
        if (level instanceof ClientLevel) {
            return Screen.hasControlDown();
        }
        return false;
    }

    public String getItemName(ItemStack stack) {
        return stack.getItem().toString();
    }

    public String getRarityTranslatable(ItemStack stack) {
        return NONE;
    }

    public String getTriviaTranslatable() {
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