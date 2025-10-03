package com.hbm.config;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.hbm.CommonEvents;
import com.hbm.blocks.ModBlocks;
import com.hbm.blocks.generic.SellafieldSlakedBlock;
import com.hbm.inventory.RecipesCommon.StateBlock;
import com.hbm.util.ModTags;
import com.hbm.util.Tuple.Triplet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class FalloutConfigJSON {

    public static final List<FalloutEntry> entries = new ArrayList<>();
    public static Random rand = new Random();

    public static final Gson gson = new Gson();

    public static void initialize() {
        File folder = CommonEvents.configHbmDir;

        File config = new File(folder.getAbsolutePath() + File.separatorChar + "hbmFallout.json");
        File template = new File(folder.getAbsolutePath() + File.separatorChar + "_hbmFallout.json");

        initDefault();

        if(!config.exists()) {
            writeDefault(template);
        } else {
            List<FalloutEntry> conf = readConfig(config);

            if(conf != null) {
                entries.clear();
                entries.addAll(conf);
            }
        }
    }

    public static void initDefault() {

        double woodEffectRange = 65D;

        entries.add(new FalloutEntry()	.mT(BlockTags.LOGS)					   .prim(new Triplet(ModBlocks.WASTE_LOG.get().defaultBlockState(), 0, 1))	    .max(woodEffectRange));
        entries.add(new FalloutEntry()	.mB(Blocks.SNOW)					   .prim(new Triplet(Blocks.AIR.defaultBlockState(), 0, 1))			            .max(woodEffectRange));

        entries.add(new FalloutEntry()	.mB(Blocks.MUSHROOM_STEM)		       .prim(new Triplet(ModBlocks.WASTE_LOG.get().defaultBlockState(), 0, 1))	    .max(woodEffectRange));
        entries.add(new FalloutEntry()	.mB(Blocks.BROWN_MUSHROOM_BLOCK)	   .prim(new Triplet(Blocks.AIR.defaultBlockState(), 0, 1))			            .max(woodEffectRange));
        entries.add(new FalloutEntry()	.mB(Blocks.RED_MUSHROOM_BLOCK)		   .prim(new Triplet(Blocks.AIR.defaultBlockState(), 0, 1))			            .max(woodEffectRange));

        entries.add(new FalloutEntry()	.mT(BlockTags.PLANKS)				   .prim(new Triplet(ModBlocks.WASTE_PLANKS.get().defaultBlockState(), 0, 1))     .max(woodEffectRange));
        entries.add(new FalloutEntry()	.mT(BlockTags.LEAVES)		           .prim(new Triplet(Blocks.AIR.defaultBlockState(), 0, 1))				        .max(woodEffectRange));
        entries.add(new FalloutEntry()	.mB(ModBlocks.WASTE_LEAVES.get())	   .prim(new Triplet(Blocks.AIR.defaultBlockState(), 0, 1))				        .max(woodEffectRange));
        entries.add(new FalloutEntry()	.mT(BlockTags.CROPS)		           .prim(new Triplet(Blocks.AIR.defaultBlockState(), 0, 1))	                    .max(woodEffectRange));
        entries.add(new FalloutEntry()	.mT(BlockTags.FLOWERS)		           .prim(new Triplet(Blocks.AIR.defaultBlockState(), 0, 1))	                    .max(woodEffectRange));
        entries.add(new FalloutEntry()	.mT(BlockTags.SAPLINGS)		           .prim(new Triplet(Blocks.AIR.defaultBlockState(), 0, 1))	                    .max(woodEffectRange));
        entries.add(new FalloutEntry()	.mT(BlockTags.LEAVES)		           .prim(new Triplet(ModBlocks.WASTE_PLANKS.get().defaultBlockState(), 0, 1))     .max(woodEffectRange - 5D));

        entries.add(new FalloutEntry()  .mB(Blocks.MOSSY_COBBLESTONE)          .prim(new Triplet(Blocks.COAL_ORE.defaultBlockState(), 0, 1)));
        entries.add(new FalloutEntry()  .mB(ModBlocks.ORE_NETHER_URANIUM.get()).prim(new Triplet(ModBlocks.ORE_NETHER_SCHRABIDIUM.get().defaultBlockState(), 0, 1), new Triplet(ModBlocks.ORE_NETHER_URANIUM_SCORCHED.get(), 0, 99)));

        for (int i = 1; i <= 10; i++) {
            int m = 10 - i;
            entries.add(new FalloutEntry().prim(new Triplet(ModBlocks.ORE_SELLAFIELD_DIAMOND.get().defaultBlockState().setValue(SellafieldSlakedBlock.COLOR_LEVEL, m), m, 3),		new Triplet(ModBlocks.ORE_SELLAFIELD_EMERALD.get().defaultBlockState().setValue(SellafieldSlakedBlock.COLOR_LEVEL, m), m, 2))			.c(0.5)		.max(i * 5).sol(true).mB(Blocks.COAL_ORE));
            entries.add(new FalloutEntry()							.prim(new Triplet(ModBlocks.SELLAFIELD_BEDROCK.get().defaultBlockState().setValue(SellafieldSlakedBlock.COLOR_LEVEL, m), m, 1)).max(i * 5).sol(true).mB(Blocks.BEDROCK));
            entries.add(new FalloutEntry()							.prim(new Triplet(ModBlocks.SELLAFIELD_BEDROCK.get().defaultBlockState().setValue(SellafieldSlakedBlock.COLOR_LEVEL, m), m, 1)).max(i * 5).sol(true).mB(ModBlocks.SELLAFIELD_BEDROCK.get()));
            entries.add(new FalloutEntry()	                        .prim(new Triplet(ModBlocks.SELLAFIELD_SLAKED.get().defaultBlockState().setValue(SellafieldSlakedBlock.COLOR_LEVEL, m), m, 1)).max(i * 5).sol(true).mT(ModTags.Blocks.ACTUALLY_STONE));
            entries.add(new FalloutEntry()	                        .prim(new Triplet(ModBlocks.SELLAFIELD_SLAKED.get().defaultBlockState().setValue(SellafieldSlakedBlock.COLOR_LEVEL, m), m, 1)).max(i * 5).sol(true).mT(ModTags.Blocks.GROUND));
            if(i <= 9) entries.add(new FalloutEntry()				.prim(new Triplet(ModBlocks.SELLAFIELD_SLAKED.get().defaultBlockState().setValue(SellafieldSlakedBlock.COLOR_LEVEL, m), m, 1)).max(i * 5).sol(true).mB(Blocks.GRASS_BLOCK));
        }

        entries.add(new FalloutEntry()  .mB(Blocks.MYCELIUM)                   .prim(new Triplet(ModBlocks.WASTE_MYCELIUM.get().defaultBlockState(), 0, 1)));
        entries.add(new FalloutEntry()  .mB(Blocks.SAND)                       .prim(new Triplet(ModBlocks.WASTE_TRINITITE.get().defaultBlockState(), 0, 1)).c(0.05));
        entries.add(new FalloutEntry()  .mB(Blocks.RED_SAND)                   .prim(new Triplet(ModBlocks.WASTE_TRINITITE_RED.get().defaultBlockState(), 0, 1)).c(0.05));
        entries.add(new FalloutEntry()  .mB(Blocks.CLAY)                       .prim(new Triplet(Blocks.TERRACOTTA.defaultBlockState(), 0, 1)));

    }

    private static void writeDefault(File file) {

        try {
            JsonWriter writer = new JsonWriter(new FileWriter(file));
            writer.setIndent("  ");					//pretty formatting
            writer.beginObject();					//initial '{'
            writer.name("entries").beginArray();	//all recipes are stored in an array called "entries"

            for (FalloutEntry entry : entries) {
                writer.beginObject();				//begin object for a single recipe
                entry.write(writer);				//serialize here
                writer.endObject();					//end recipe object
            }

            writer.endArray();						//end recipe array
            writer.endObject();						//final '}'
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<FalloutEntry> readConfig(File config) {

        try {
            JsonObject json = gson.fromJson(new FileReader(config), JsonObject.class);
            JsonArray recipes = json.get("entries").getAsJsonArray();
            List<FalloutEntry> conf = new ArrayList<>();

            for(JsonElement recipe : recipes) {
                conf.add(FalloutEntry.readEntry(recipe));
            }
            return conf;

        } catch(Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }


    public static class FalloutEntry {
        private Block matchesBlock = null;
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

        private static BlockState resolveState(Object obj) {
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

        public void write(JsonWriter writer) throws IOException {
            if (matchesBlock != null) writer.name("matchesBlock").value(BuiltInRegistries.BLOCK.getKey(matchesBlock).toString());
            if (matchesTag != null) writer.name("matchesTag").value(matchesTag.location().toString());
            if (matchesOpaque) writer.name("mustBeOpaque").value(true);
            if (isSolid) writer.name("restrictDepth").value(true);

            if (primaryBlocks != null) { writer.name("primarySubstitution"); writeArray(writer, primaryBlocks); }
            if (secondaryBlocks != null) { writer.name("secondarySubstitutions"); writeArray(writer, secondaryBlocks); }

            if (primaryChance != 1D) writer.name("chance").value(primaryChance);

            if (minDist != 0.0D) writer.name("minimumDistancePercent").value(minDist);
            if (maxDist != 100.0D) writer.name("maximumDistancePercent").value(maxDist);
            if (falloffStart != 0.9D) writer.name("falloffStartFactor").value(falloffStart);
        }

        private static void writeArray(JsonWriter writer, Triplet<BlockState, Integer, Integer>[] array) throws IOException {
            writer.beginArray();
            writer.setIndent("");

            for (Triplet<BlockState, Integer, Integer> meta : array) {
                writer.beginArray();

                BlockState state = resolveState(meta.getX());
                Block block = state.getBlock();
                ResourceLocation key = BuiltInRegistries.BLOCK.getKey(block);

                writer.value(key.toString());

                writer.beginObject();
                for (var entry : state.getValues().entrySet()) {
                    writer.name(entry.getKey().getName()).value(entry.getValue().toString());
                }
                writer.endObject();

                writer.value(meta.getY());
                writer.value(meta.getZ());

                writer.endArray();
            }

            writer.endArray();
            writer.setIndent("  ");
        }

        private static FalloutEntry readEntry(JsonElement recipe) {
            FalloutEntry entry = new FalloutEntry();
            if(!recipe.isJsonObject()) return null;

            JsonObject obj = recipe.getAsJsonObject();

            if (obj.has("matchesBlock") || obj.has("block")) {
                String keyName = obj.has("matchesBlock") ? "matchesBlock" : "block";
                String idStr = obj.get(keyName).getAsString();
                ResourceLocation id = ResourceLocation.tryParse(idStr);
                if (id != null) {
                    Block block = BuiltInRegistries.BLOCK.getOptional(id).orElse(null);
                    if (block != null) {
                        entry.mB(block);
                    } else {
                        throw new IllegalArgumentException("hbmFallout.json: Unknown block id: " + idStr);
                    }
                } else {
                    throw new IllegalArgumentException("hbmFallout.json: Invalid resource location: " + idStr);
                }
            }

            if (obj.has("matchesTag")) {
                ResourceLocation id = ResourceLocation.tryParse(obj.get("matchesTag").getAsString());
                if (id != null) {
                    TagKey<Block> tagKey = TagKey.create(Registries.BLOCK, id);
                    entry.mT(tagKey);
                }
            }
            if(obj.has("mustBeOpaque")) entry.mO(obj.get("mustBeOpaque").getAsBoolean());
            if(obj.has("restrictDepth")) entry.sol(obj.get("restrictDepth").getAsBoolean());

            if(obj.has("primarySubstitution")) entry.prim(readArray(obj.get("primarySubstitution")));
            if(obj.has("secondarySubstitutions")) entry.sec(readArray(obj.get("secondarySubstitutions")));

            if (obj.has("chance")) entry.c(obj.get("chance").getAsDouble());

            if (obj.has("minimumDistancePercent")) entry.min(obj.get("minimumDistancePercent").getAsDouble());
            if (obj.has("maximumDistancePercent")) entry.max(obj.get("maximumDistancePercent").getAsDouble());
            if (obj.has("falloffStartFactor")) entry.fo(obj.get("falloffStartFactor").getAsDouble());

            return entry;
        }

        private static Triplet<BlockState, Integer, Integer>[] readArray(JsonElement jsonElement) {
            if (!jsonElement.isJsonArray()) return null;

            JsonArray array = jsonElement.getAsJsonArray();
            List<Triplet<BlockState, Integer, Integer>> tmp = new ArrayList<>();

            for (int i = 0; i < array.size(); i++) {
                JsonElement metaBlock = array.get(i);
                if (!metaBlock.isJsonArray()) {
                    throw new IllegalArgumentException("hbmFallout.json: Expected array item to be array, got: " + metaBlock);
                }

                JsonArray mBArray = metaBlock.getAsJsonArray();
                if (mBArray.size() < 3) {
                    throw new IllegalArgumentException("hbmFallout.json: Meta entry too short: " + mBArray);
                }

                String blockId = mBArray.get(0).getAsString();
                ResourceLocation id = ResourceLocation.tryParse(blockId);
                Block block = null;
                if (id != null) {
                    block = BuiltInRegistries.BLOCK.getOptional(id).orElse(null);
                }

                if (block == null) {
                    throw new IllegalArgumentException("hbmFallout.json: Unknown block id in substitution: " + blockId);
                }

                BlockState state = block.defaultBlockState();

                if (mBArray.size() > 3 && mBArray.get(1).isJsonObject()) {
                    JsonObject props = mBArray.get(1).getAsJsonObject();
                    for (Map.Entry<String, JsonElement> e : props.entrySet()) {
                        String propName = e.getKey();
                        String propValue = e.getValue().getAsString();

                        Property<?> property = block.getStateDefinition().getProperty(propName);
                        if (property != null) {
                            state = applyProperty(state, property, propValue);
                        } else {
                            throw new IllegalArgumentException("hbmFallout.json: Unknown property '" + propName + "' for block " + blockId);
                        }
                    }
                }

                int y = mBArray.get(mBArray.size() - 2).getAsInt();
                int z = mBArray.get(mBArray.size() - 1).getAsInt();

                tmp.add(new Triplet<>(state, y, z));
            }

            Triplet<BlockState, Integer, Integer>[] metaArray = new Triplet[tmp.size()];
            return tmp.toArray(metaArray);
        }

        private static <T extends Comparable<T>> BlockState applyProperty(BlockState state, Property<T> property, String valueStr) {
            Optional<T> value = property.getValue(valueStr);
            if (value.isPresent()) {
                return state.setValue(property, value.get());
            }
            return state;
        }
    }
}
