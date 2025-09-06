package destiny.armoryofdestiny.server.compatibility.jei;

import destiny.armoryofdestiny.ArmoryOfDestiny;
import mezz.jei.api.IModPlugin;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

public class ArmoryOfDestinyJEIPlugin implements IModPlugin {
    private static final ResourceLocation PLUGIN_LOCATION = new ResourceLocation(ArmoryOfDestiny.MODID, "jei_plugin");
    private static Minecraft minecraft = Minecraft.getInstance();

    @Override
    public ResourceLocation getPluginUid()
    {
        return PLUGIN_LOCATION;
    }
}
