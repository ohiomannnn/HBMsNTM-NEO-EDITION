package com.hbm.config;

import com.hbm.blocks.ModBlocks;
import com.hbm.blocks.generic.BlockSellafieldSlaked;
import com.hbm.inventory.RecipesCommon.StateBlock;
import com.hbm.util.Tuple.Triplet;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.*;

public class FalloutConfigJSON {

    public static final List<FalloutEntry> entries = new ArrayList<>();
    public static Random rand = new Random();

//    public static final Gson gson = new Gson();
//
//    public static void initialize() {
//        File folder = CommonEvents.configHbmDir;
//
//        File config = new File(folder.getAbsolutePath() + File.separatorChar + "hbmFallout.json");
//        File template = new File(folder.getAbsolutePath() + File.separatorChar + "_hbmFallout.json");
//
//        initDefault();
//
//        if(!config.exists()) {
//            writeDefault(template);
//        } else {
//            List<FalloutEntry> conf = readConfig(config);
//
//            if(conf != null) {
//                entries.clear();
//                entries.addAll(conf);
//            }
//        }
//    }
//
    public static void initDefault() {

        double woodEffectRange = 65D;

        entries.add(new FalloutEntry()	.mT(BlockTags.LOGS)					   .prim(new Triplet(ModBlocks.WASTE_LOG.get(), 0, 1))	    .max(woodEffectRange));
        entries.add(new FalloutEntry()	.mB(Blocks.SNOW)					   .prim(new Triplet(Blocks.AIR, 0, 1))			            .max(woodEffectRange));
        entries.add(new FalloutEntry()	.mT(BlockTags.PLANKS)				   .prim(new Triplet(ModBlocks.WASTE_PLANKS.get(), 0, 1))     .max(woodEffectRange));
        entries.add(new FalloutEntry()	.mT(BlockTags.LEAVES)		           .prim(new Triplet(Blocks.AIR, 0, 1))				        .max(woodEffectRange));
        entries.add(new FalloutEntry()	.mB(ModBlocks.WASTE_LEAVES.get())	   .prim(new Triplet(Blocks.AIR, 0, 1))				        .max(woodEffectRange));
        entries.add(new FalloutEntry()	.mT(BlockTags.CROPS)		           .prim(new Triplet(Blocks.AIR, 0, 1))	                    .max(woodEffectRange));
        entries.add(new FalloutEntry()	.mT(BlockTags.FLOWERS)		           .prim(new Triplet(Blocks.AIR, 0, 1))	                    .max(woodEffectRange));
        entries.add(new FalloutEntry()	.mT(BlockTags.SAPLINGS)		           .prim(new Triplet(Blocks.AIR, 0, 1))	                    .max(woodEffectRange));
        entries.add(new FalloutEntry()  .mB(Blocks.MOSSY_COBBLESTONE)          .prim(new Triplet(Blocks.COAL_ORE, 0, 1)));
        entries.add(new FalloutEntry()  .mB(ModBlocks.ORE_NETHER_URANIUM.get()).prim(new Triplet(ModBlocks.ORE_NETHER_SCHRABIDIUM.get(), 0, 1), new Triplet(ModBlocks.ORE_NETHER_URANIUM_SCORCHED.get(), 0, 99)));

        entries.add(new FalloutEntry()  .mB(Blocks.MYCELIUM)                   .prim(new Triplet(ModBlocks.WASTE_MYCELIUM, 0, 1)));
        entries.add(new FalloutEntry()  .mB(Blocks.SAND)                       .prim(new Triplet(ModBlocks.WASTE_TRINITITE, 0, 1)).c(0.05));
        entries.add(new FalloutEntry()  .mB(Blocks.RED_SAND)                   .prim(new Triplet(ModBlocks.WASTE_TRINITITE, 0, 1)).c(0.05));
        entries.add(new FalloutEntry()  .mB(Blocks.CLAY)                       .prim(new Triplet(Blocks.TERRACOTTA, 0, 1)));

        for (int i = 1; i <= 10; i++) {
            int m = (10 - i) % 4;
            entries.add(new FalloutEntry()							.prim(new Triplet(ModBlocks.SELLAFIELD_BEDROCK.get().defaultBlockState().setValue(BlockSellafieldSlaked.VARIANT, m), m, 1)).max(i * 5).sol(true).mB(Blocks.BEDROCK));
            entries.add(new FalloutEntry()							.prim(new Triplet(ModBlocks.SELLAFIELD_SLAKED.get().defaultBlockState().setValue(BlockSellafieldSlaked.VARIANT, m), m, 1)).max(i * 5).sol(true).mT(BlockTags.NEEDS_IRON_TOOL));//mMa(Material.iron));
            entries.add(new FalloutEntry()							.prim(new Triplet(ModBlocks.SELLAFIELD_SLAKED.get().defaultBlockState().setValue(BlockSellafieldSlaked.VARIANT, m), m, 1)).max(i * 5).sol(true).mT(BlockTags.STONE_ORE_REPLACEABLES));//mMa(Material.rock));
            entries.add(new FalloutEntry()							.prim(new Triplet(ModBlocks.SELLAFIELD_SLAKED.get().defaultBlockState().setValue(BlockSellafieldSlaked.VARIANT, m), m, 1)).max(i * 5).sol(true).mT(BlockTags.SAND));//mMa(Material.sand));
            entries.add(new FalloutEntry()							.prim(new Triplet(ModBlocks.SELLAFIELD_SLAKED.get().defaultBlockState().setValue(BlockSellafieldSlaked.VARIANT, m), m, 1)).max(i * 5).sol(true).mT(BlockTags.DIRT));//mMa(Material.ground));
            if(i <= 9) entries.add(new FalloutEntry()				.prim(new Triplet(ModBlocks.SELLAFIELD_SLAKED.get().defaultBlockState().setValue(BlockSellafieldSlaked.VARIANT, m), m, 1)).max(i * 5).sol(true).mT(BlockTags.DIRT));//mMa(Material.grass));
            entries.add(new FalloutEntry()	                        .prim(new Triplet(ModBlocks.SELLAFIELD_SLAKED.get().defaultBlockState().setValue(BlockSellafieldSlaked.VARIANT, m), m, 1)).max(i * 5).sol(true).mB(Blocks.DEEPSLATE));
            entries.add(new FalloutEntry()	                        .prim(new Triplet(ModBlocks.SELLAFIELD_SLAKED.get().defaultBlockState().setValue(BlockSellafieldSlaked.VARIANT, m), m, 1)).max(i * 5).sol(true).mB(Blocks.STONE));
        }
    }
//
//    private static void writeDefault(File file) {
//
//        try {
//            JsonWriter writer = new JsonWriter(new FileWriter(file));
//            writer.setIndent("  ");					//pretty formatting
//            writer.beginObject();					//initial '{'
//            writer.name("entries").beginArray();	//all recipes are stored in an array called "entries"
//
//            for(FalloutEntry entry : entries) {
//                writer.beginObject();				//begin object for a single recipe
//                entry.write(writer);				//serialize here
//                writer.endObject();					//end recipe object
//            }
//
//            writer.endArray();						//end recipe array
//            writer.endObject();						//final '}'
//            writer.close();
//        } catch(IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private static List<FalloutEntry> readConfig(File config) {
//
//        try {
//            JsonObject json = gson.fromJson(new FileReader(config), JsonObject.class);
//            JsonArray recipes = json.get("entries").getAsJsonArray();
//            List<FalloutEntry> conf = new ArrayList<>();
//
//            for(JsonElement recipe : recipes) {
//                conf.add(FalloutEntry.readEntry(recipe));
//            }
//            return conf;
//
//        } catch(Exception ex) {
//            ex.printStackTrace();
//        }
//
//        return null;
//    }


    public static class FalloutEntry {
        private Block matchesBlock = null;
        private BlockState matchesState = null;
        private TagKey<Block> matchesTag = null;
        private boolean matchesOpaque = false;

        private Triplet<BlockState, Integer, Integer>[] primaryBlocks = null;
        private Triplet<BlockState, Integer, Integer>[] secondaryBlocks = null;
        private double primaryChance = 1.0D;
        private double minDist = 0.0D;
        private double maxDist = 100.0D;
        private double falloffStart = 0.9D;

        private boolean isSolid = false;

        public FalloutEntry clone() {
            FalloutEntry entry = new FalloutEntry();
            entry.mB(matchesBlock);
            entry.mT(matchesTag);
            entry.mO(matchesOpaque);
            entry.prim(primaryBlocks);
            entry.sec(secondaryBlocks);
            entry.min(minDist);
            entry.max(maxDist);
            entry.fo(falloffStart);
            entry.sol(isSolid);

            return entry;
        }

        public FalloutEntry mB(Block block) { this.matchesBlock = block; return this; }
        public FalloutEntry mT(TagKey<Block> tag) { this.matchesTag = tag; return this; }
        public FalloutEntry mO(boolean opaque) { this.matchesOpaque = opaque; return this; }

        public FalloutEntry prim(Triplet<BlockState, Integer, Integer>... blocks) { this.primaryBlocks = blocks; return this; }
        public FalloutEntry sec(Triplet<BlockState, Integer, Integer>... blocks) { this.secondaryBlocks = blocks; return this; }
        public FalloutEntry c(double chance) { this.primaryChance = chance; return this; }
        public FalloutEntry min(double min) { this.minDist = min; return this; }
        public FalloutEntry max(double max) { this.maxDist = max; return this; }
        public FalloutEntry fo(double falloffStart) { this.falloffStart = falloffStart; return this; }
        public FalloutEntry sol(boolean solid) { this.isSolid = solid; return this; }

        public boolean eval(Level level, BlockPos pos, BlockState state, double dist) {
            if (dist > maxDist || dist < minDist) return false;
            if (matchesBlock != null && !state.is(matchesBlock)) return false;
            if (matchesTag != null && !state.is(matchesTag)) return false;
            if (matchesState != null && !state.is(matchesState.getBlock())) return false;
            if (matchesOpaque && !state.isSolidRender(level, pos)) return false;

            if (dist > maxDist * falloffStart && Math.abs(level.random.nextGaussian()) < Math.pow((dist - maxDist * falloffStart) / (maxDist - maxDist * falloffStart), 2D) * 3D) return false;

            StateBlock conversion = chooseRandomOutcome((primaryChance == 1D || level.random.nextDouble() < primaryChance) ? primaryBlocks : secondaryBlocks);
            if (conversion != null) {
                level.setBlock(pos, conversion.state, 3);
                return true;
            }
            return false;
        }

        private <T> StateBlock chooseRandomOutcome(Triplet<T, Integer, Integer>[] blocks) {
            if (blocks == null) return null;

            int weight = 0;
            for (Triplet<T, Integer, Integer> choice : blocks) {
                weight += choice.getZ();
            }

            int r = rand.nextInt(weight);
            for (Triplet<T, Integer, Integer> choice : blocks) {
                r -= choice.getZ();
                if (r <= 0) {
                    return new StateBlock(resolveState(choice.getX()));
                }
            }
            return new StateBlock(resolveState(blocks[0].getX()));
        }

        private BlockState resolveState(Object obj) {
            if (obj instanceof BlockState state) {
                return state;
            } else if (obj instanceof Block block) {
                return block.defaultBlockState();
            } else if (obj instanceof DeferredBlock<?> def) {
                return def.get().defaultBlockState();
            }
            throw new IllegalArgumentException("Unsupported type: " + obj.getClass());
        }

        public boolean isSolid() {
            return this.isSolid;
        }

//        public void write(JsonWriter writer) throws IOException {
//            if(matchesBlock != null) writer.name("matchesBlock").value(BuiltInRegistries.BLOCK.getKey(matchesBlock).toString());
//            if(matchesTag != null) writer.name("matchesMeta").value(matchesTag);
//            if(matchesOpaque) writer.name("mustBeOpaque").value(true);
//            if(isSolid) writer.name("restrictDepth").value(true);
//
//            if(primaryBlocks != null) { writer.name("primarySubstitution"); writeMetaArray(writer, primaryBlocks); }
//            if(secondaryBlocks != null) { writer.name("secondarySubstitutions"); writeMetaArray(writer, secondaryBlocks); }
//
//            if(primaryChance != 1D) writer.name("chance").value(primaryChance);
//
//            if(minDist != 0.0D) writer.name("minimumDistancePercent").value(minDist);
//            if(maxDist != 100.0D) writer.name("maximumDistancePercent").value(maxDist);
//            if(falloffStart != 0.9D) writer.name("falloffStartFactor").value(falloffStart);
//        }
//
//        private static FalloutEntry readEntry(JsonElement recipe) {
//            FalloutEntry entry = new FalloutEntry();
//            if(!recipe.isJsonObject()) return null;
//
//            JsonObject obj = recipe.getAsJsonObject();
//
//            if(obj.has("matchesBlock")) entry.mB(BuiltInRegistries.BLOCK.get(ResourceLocation.read(obj.get("matchesBlock").getAsString()));
//            if(obj.has("mustBeOpaque")) entry.mO(obj.get("mustBeOpaque").getAsBoolean());
//            if(obj.has("matchesMaterial")) entry.mMa(matNames.get(obj.get("mustBeOpaque").getAsString()));
//            if(obj.has("restrictDepth")) entry.sol(obj.get("restrictDepth").getAsBoolean());
//
//            if(obj.has("primarySubstitution")) entry.prim(readMetaArray(obj.get("primarySubstitution")));
//            if(obj.has("secondarySubstitutions")) entry.sec(readMetaArray(obj.get("secondarySubstitutions")));
//
//            if(obj.has("chance")) entry.c(obj.get("chance").getAsDouble());
//
//            if(obj.has("minimumDistancePercent")) entry.min(obj.get("minimumDistancePercent").getAsDouble());
//            if(obj.has("maximumDistancePercent")) entry.max(obj.get("maximumDistancePercent").getAsDouble());
//            if(obj.has("falloffStartFactor")) entry.fo(obj.get("falloffStartFactor").getAsDouble());
//
//            return entry;
//        }
//
//        private static void writeMetaArray(JsonWriter writer, Triplet<BlockState, Integer, Integer>[] array) throws IOException {
//            writer.beginArray();
//            writer.setIndent("");
//
//            for(Triplet<BlockState, Integer, Integer> meta : array) {
//                writer.beginArray();
//                writer.value(Block.blockRegistry.getNameForObject(meta.getX()));
//                writer.value(meta.getY());
//                writer.value(meta.getZ());
//                writer.endArray();
//            }
//
//            writer.endArray();
//            writer.setIndent("  ");
//        }
//
//        private static Triplet<Block, Integer, Integer>[] readMetaArray(JsonElement jsonElement) {
//
//            if(!jsonElement.isJsonArray()) return null;
//
//            JsonArray array = jsonElement.getAsJsonArray();
//            Triplet<Block, Integer, Integer>[] metaArray = new Triplet[array.size()];
//
//            for(int i = 0; i < metaArray.length; i++) {
//                JsonElement metaBlock = array.get(i);
//
//                if(!metaBlock.isJsonArray()) {
//                    throw new IllegalStateException("Could not read meta block " + metaBlock.toString());
//                }
//
//                JsonArray mBArray = metaBlock.getAsJsonArray();
//
//                metaArray[i] = new Triplet(Block.blockRegistry.getObject(mBArray.get(0).getAsString()), mBArray.get(1).getAsInt(), mBArray.get(2).getAsInt());
//            }
//
//            return metaArray;
//        }
//    }
//
//        private static void writeMetaArray(JsonWriter writer, Triplet<Block, Integer, Integer>[] array) throws IOException {
//            writer.beginArray();
//            writer.setIndent("");
//
//            for (Triplet<Block, Integer, Integer> meta : array) {
//                writer.beginArray();
//                ResourceLocation id = BuiltInRegistries.BLOCK.getKey(meta.getX());
//                writer.value(id.toString());
//                writer.value(meta.getY());
//                writer.value(meta.getZ());
//                writer.endArray();
//            }
//
//            writer.endArray();
//            writer.setIndent("  ");
//        }
//
//        @SuppressWarnings("unchecked")
//        private static Triplet<Block, Integer, Integer>[] readMetaArray(JsonElement jsonElement) {
//            if (!jsonElement.isJsonArray()) return null;
//
//            JsonArray array = jsonElement.getAsJsonArray();
//            Triplet<Block, Integer, Integer>[] metaArray = new Triplet[array.size()];
//
//            for (int i = 0; i < metaArray.length; i++) {
//                JsonElement metaBlock = array.get(i);
//
//                if (!metaBlock.isJsonArray()) {
//                    throw new IllegalStateException("Could not read meta block " + metaBlock.toString());
//                }
//
//                JsonArray mBArray = metaBlock.getAsJsonArray();
//                ResourceLocation id = new ResourceLocation(mBArray.get(0).getAsString());
//                Block block = BuiltInRegistries.BLOCK.get(id);
//
//                metaArray[i] = new Triplet<>(block, mBArray.get(1).getAsInt(), mBArray.get(2).getAsInt());
//            }
//
//            return metaArray;
//        }
//
//
//        public static final Map<String, TagKey<Block>> tagNames = new HashMap<>();
//
//        static {
//            tagNames.put("grass", BlockTags.DIRT);
//            tagNames.put("ground", BlockTags.DIRT);
//            tagNames.put("wood", BlockTags.LOGS);
//            tagNames.put("stone", BlockTags.STONE_ORE_REPLACEABLES);
//            tagNames.put("leaves", BlockTags.LEAVES);
//        }
    }
}
