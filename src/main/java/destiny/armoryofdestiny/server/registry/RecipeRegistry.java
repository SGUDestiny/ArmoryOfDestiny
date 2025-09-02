package destiny.armoryofdestiny.server.registry;

import destiny.armoryofdestiny.ArmoryOfDestiny;
import destiny.armoryofdestiny.server.recipe.*;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RecipeRegistry
{
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, ArmoryOfDestiny.MODID);

    public static final RegistryObject<RecipeSerializer<TinkeringRecipe>> TINKERING = SERIALIZERS.register("tinkering",
            () -> TinkeringRecipe.Serializer.INSTANCE);
    public static final RegistryObject<RecipeSerializer<SmithingRecipe>> SMITHING = SERIALIZERS.register("smithing",
            () -> SmithingRecipe.Serializer.INSTANCE);
    public static final RegistryObject<RecipeSerializer<BloomingRecipe>> BLOOMING = SERIALIZERS.register("blooming",
            () -> BloomingRecipe.Serializer.INSTANCE);
    public static final RegistryObject<RecipeSerializer<SuperheatedBloomingRecipe>> SUPERHEATED_BLOOMING = SERIALIZERS.register("superheated_blooming",
            () -> SuperheatedBloomingRecipe.Serializer.INSTANCE);
    public static final RegistryObject<RecipeSerializer<TemperingRecipe>> TEMPERING = SERIALIZERS.register("tempering",
            () -> TemperingRecipe.Serializer.INSTANCE);

    public static void register(IEventBus bus)
    {
        SERIALIZERS.register(bus);
    }
}
