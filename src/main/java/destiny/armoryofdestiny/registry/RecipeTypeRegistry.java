package destiny.armoryofdestiny.registry;

import destiny.armoryofdestiny.ArmoryOfDestiny;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RecipeTypeRegistry {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, ArmoryOfDestiny.MODID);

    //public static final RegistryObject<RecipeSerializer<AssemblyRecipe>> ASSEMBLY_SERIALIZERS = SERIALIZERS.register("assembly", () -> AssemblyRecipe.SERIALIZER);

    public static void register(IEventBus eventBus)
    {
        SERIALIZERS.register(eventBus);
    }
}
