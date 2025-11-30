package api.hbm.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public interface IDepthRockTool {
    /**
     * Whether our item can break depthrock, has a couple of params so we can restrict mining for certain blocks, dimensions or positions
     */
    boolean canBreakRock(Level level, Player player, ItemStack tool, Block block, BlockPos pos);
}
