package destiny.armoryofdestiny.server.block;

import destiny.armoryofdestiny.server.block.utility.TooltipBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import static destiny.armoryofdestiny.server.util.UtilityVariables.HOT_ITEM;

public class HotBlock extends TooltipBlock {
    int damage = 2;
    int delay = 20;

    public HotBlock(Properties properties) {
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
    public String getTriviaTranslatable() {
        return HOT_ITEM;
    }
}
