package destiny.armoryofdestiny.server.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import destiny.armoryofdestiny.server.item.utility.TooltipItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeMod;

import static destiny.armoryofdestiny.server.misc.UtilityVariables.ARMORERS_WORKSHOP_PART;
import static destiny.armoryofdestiny.server.misc.UtilityVariables.SMITHING_HAMMER;

public class SmithingHammerItem extends TooltipItem {
    Item repairItem;

    private float attackDamage;

    public SmithingHammerItem(Properties properties, Item repairItem, int attackDamage) {
        super(properties);
        this.repairItem = repairItem;
        this.attackDamage = attackDamage;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        if (slot == EquipmentSlot.MAINHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", this.attackDamage, AttributeModifier.Operation.ADDITION));
            return builder.build();
        }
        return super.getAttributeModifiers(slot, stack);
    }

    @Override
    public String getTriviaType() {
        return SMITHING_HAMMER;
    }

    @Override
    public String getItemRarity(ItemStack stack) {
        return ARMORERS_WORKSHOP_PART;
    }

    @Override
    public boolean hasAbilities() {
        return false;
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack stack1) {
        return stack1.is(repairItem);
    }
}
