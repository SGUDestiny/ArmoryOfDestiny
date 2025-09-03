package destiny.armoryofdestiny.server.block;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static destiny.armoryofdestiny.server.util.UtilityVariables.HOT_ITEM;
import static destiny.armoryofdestiny.server.util.UtilityVariables.NONE;

public class HotIronBarsBlock extends IronBarsBlock {
    int damage = 2;
    int delay = 20;

    public HotIronBarsBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource source) {
        BlockPos blockpos = pos.above();
        if (level.getBlockState(blockpos).isAir() && !level.getBlockState(blockpos).isSolidRender(level, blockpos)) {
            if (source.nextInt(100) == 0) {
                double d0 = pos.getX() + source.nextDouble();
                double d1 = pos.getY() + 1.0F;
                double d2 = pos.getZ() + source.nextDouble();
                level.addParticle(ParticleTypes.LAVA, d0, d1, d2, 0.0F, 0.0F, 0.0F);
                level.playLocalSound(d0, d1, d2, SoundEvents.LAVA_POP, SoundSource.BLOCKS, 0.2F + source.nextFloat() * 0.2F, 0.9F + source.nextFloat() * 0.15F, false);
            }
        }
    }

    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (entity instanceof LivingEntity livingEntity && !EnchantmentHelper.hasFrostWalker(livingEntity)) {
            if (level.getGameTime() % delay == 0) {
                entity.hurt(level.damageSources().hotFloor(), damage);
            }
        }
        super.stepOn(level, pos, state, entity);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter getter, List<Component> components, TooltipFlag flag) {
        String itemTrivia = getTriviaTranslatable();

        //Trivia
        if (!itemTrivia.equals("none") && hasTrivia()) {
            MutableComponent trivia = Component.translatable("tooltip.armoryofdestiny.trivia")
                    .withStyle(ChatFormatting.DARK_GRAY);
            components.add(trivia);
            MutableComponent trivia_title = Component.translatable("tooltip.armoryofdestiny.dropdown")
                    .append(Component.translatable("tooltip.armoryofdestiny.trivia." + itemTrivia + ".title")
                            .withStyle(ChatFormatting.GRAY));
            components.add(trivia_title);

            int triviaLines = getTriviaLines(itemTrivia);
            //Print trivia
            for (int i = 1; i <= triviaLines; i++) {
                MutableComponent trivia_description = Component.translatable("tooltip.armoryofdestiny.dropdown")
                        .append(Component.translatable("tooltip.armoryofdestiny.trivia." + itemTrivia + ".description." + i)
                                .withStyle(ChatFormatting.DARK_GRAY));
                components.add(trivia_description);
            }
        }
    }

    public int getTriviaLines(String trivia) {
        int i = 1;
        String baseKey = "tooltip.armoryofdestiny.trivia." + trivia + ".description.";

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

    public String getTriviaTranslatable() {
        return HOT_ITEM;
    }

    public boolean hasTrivia() {
        return true;
    }
}
