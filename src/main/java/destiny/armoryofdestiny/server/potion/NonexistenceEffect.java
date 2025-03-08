package destiny.armoryofdestiny.server.potion;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;

public class NonexistenceEffect extends MobEffect {
    public NonexistenceEffect(MobEffectCategory category, int color) {
        super(category, color);
    }
}
