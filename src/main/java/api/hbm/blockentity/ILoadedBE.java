package api.hbm.blockentity;

import com.hbm.util.Compat;
import com.hbm.util.Tuple.Quartet;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.HashMap;
import java.util.Map;

/** For anything that should be removed off networks when considered unloaded, only affects providers and receivers, not links. Must not necessarily be a tile. */
public interface ILoadedBE {
    boolean isLoaded();

    // should we gunk this into the API? no, but i don't care
    class BlockEntityAccessCache {

        public static Map<Quartet<Integer, Integer, Integer, ResourceKey<Level>>, BlockEntityAccessCache> cache = new HashMap<>();

        public static int NULL_CACHE = 20;
        public static int NONNULL_CACHE = 60;

        public BlockEntity be;
        public long expiresOn;

        public BlockEntityAccessCache(BlockEntity be, long expiresOn) {
            this.be = be;
            this.expiresOn = expiresOn;
        }

        public boolean hasExpired(long worldTime) {
            if(be != null && be.isRemoved()) return true;
            if(worldTime >= expiresOn) return true;
            if(be instanceof ILoadedBE && !((ILoadedBE) be).isLoaded()) return true;
            return false;
        }

        public static Quartet<Integer, Integer, Integer, ResourceKey<Level>> publicCumRag = new Quartet<>(0, 0, 0, Level.OVERWORLD);

        public static BlockEntity getBEOrCache(Level level, BlockPos pos) {
            publicCumRag.mangle(pos.getX(), pos.getY(), pos.getZ(), level.dimension());
            BlockEntityAccessCache cache = BlockEntityAccessCache.cache.get(publicCumRag);

            if(cache == null || cache.hasExpired(level.getGameTime())) {
                BlockEntity be = Compat.getBlockEntityStandard(level, pos);
                cache = new BlockEntityAccessCache(be, level.getGameTime() + (be == null ? NULL_CACHE : NONNULL_CACHE));
                BlockEntityAccessCache.cache.put(publicCumRag.clone(), cache);
                return be;
            } else {
                return cache.be;
            }
        }
    }
}
