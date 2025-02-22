package destiny.armoryofdestiny.item.utility;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;

public enum ItemTiers implements Tier {
    MURASAMA(4, 2031, 9.0F, 4.0F, 15, Items.ECHO_SHARD),
    ORIGINIUM_KATANA(4, 2031, 9.0F, 4.0F, 15, Items.AMETHYST_SHARD),
    SHARP_IRONY(4, 2031, 9.0F, 4.0F, 15, Items.NETHERITE_SCRAP),
    PUNISHER(4, 2031, 9.0F, 4.0F, 15, Items.DIAMOND),
    DRAGON_SLAYER(4, 2031, 9.0F, 4.0F, 15, Items.NETHERITE_INGOT);

    private float attackDamage, efficiency;
    private int durability, harvestLevel, enchantability;
    private Item repairMaterial;

    private ItemTiers(int harvestLevel, int durability, float efficiency, float attackDamage, int enchantability, Item repairMaterial) {
        this.harvestLevel = harvestLevel;
        this.durability = durability;
        this.efficiency = efficiency;
        this.attackDamage = attackDamage;
        this.enchantability = enchantability;
        this.repairMaterial = repairMaterial;
    }

    @Override
    public int getUses()
    {
        return this.durability;
    }

    @Override
    public float getSpeed()
    {
        return this.efficiency;
    }

    @Override
    public float getAttackDamageBonus()
    {
        return this.attackDamage;
    }

    @Override
    public int getLevel()
    {
        return this.harvestLevel;
    }

    @Override
    public int getEnchantmentValue()
    {
        return this.enchantability;
    }

    @Override
    public Ingredient getRepairIngredient()
    {
        return Ingredient.of(repairMaterial);
    }
}
