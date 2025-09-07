package destiny.armoryofdestiny.server.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.stream.Stream;

public class ModUtil {
    public static VoxelShape buildShape(VoxelShape... from) {
        return Stream.of(from).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    }

    public static ResourceLocation stitchResourceLocationFromItem(String itemKey, String insert) {
        int colonIndex = itemKey.indexOf(':');
        String firstPart = itemKey.substring(0, colonIndex + 1);
        String secondPart = itemKey.substring(colonIndex + 1);
        return ResourceLocation.tryParse(firstPart + insert + secondPart);
    }
}
