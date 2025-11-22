package com.hbm.config;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.hbm.CommonEvents;
import com.hbm.HBMsNTM;
import com.hbm.blocks.ModBlocks;
import com.hbm.blocks.generic.SellafieldSlakedBlock;
import com.hbm.inventory.RecipesCommon.StateBlock;
import com.hbm.util.ModTags;
import com.hbm.util.Tuple.Pair;
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

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FalloutConfigJSON {

    public static final List<FalloutEntry> entries = new ArrayList<>();
    public static Random rand = new Random();

    public static final Gson gson = new Gson();

    public static void initialize() {
        File folder = CommonEvents.configHbmDir;

        File config = new File(folder.getAbsolutePath() + File.separatorChar + "hbmFallout.json");
        File template = new File(folder.getAbsolutePath() + File.separatorChar + "_hbmFallout.json");

        initDefault();

        if (!config.exists()) {
            writeDefault(template);
        } else {
            List<FalloutEntry> conf = readConfig(config);

            if (conf != null) {
                entries.clear();
                entries.addAll(conf);
            }
        }
    }

    public static void initDefault() {

        double woodEffectRange = 65D;

        entries.add(new FalloutEntry()
                .mT(BlockTags.LOGS)
                .prim(new Pair(ModBlocks.WASTE_LOG.get().defaultBlockState(), 1))
                .max(woodEffectRange));
        entries.add(new FalloutEntry()
                .mBS(Blocks.MUSHROOM_STEM.defaultBlockState())
                .prim(new Pair(ModBlocks.WASTE_LOG.get().defaultBlockState(), 1))
                .max(woodEffectRange));
        entries.add(new FalloutEntry()
                .mBS(Blocks.BROWN_MUSHROOM_BLOCK.defaultBlockState())
                .prim(new Pair(Blocks.AIR.defaultBlockState(), 1))
                .max(woodEffectRange));
        entries.add(new FalloutEntry()
                .mBS(Blocks.RED_MUSHROOM_BLOCK.defaultBlockState())
                .prim(new Pair(Blocks.AIR.defaultBlockState(), 1))
                .max(woodEffectRange));

        entries.add(new FalloutEntry()
                .mBS(Blocks.SNOW.defaultBlockState())
                .prim(new Pair(Blocks.AIR.defaultBlockState(), 1))
                .max(woodEffectRange));

        entries.add(new FalloutEntry()
                .mT(BlockTags.PLANKS)
                .prim(new Pair(ModBlocks.WASTE_PLANKS.get().defaultBlockState(), 1))
                .max(woodEffectRange));
        entries.add(new FalloutEntry()
                .mT(ModTags.Blocks.LEAVES)
                .prim(new Pair(Blocks.AIR.defaultBlockState(), 1))
                .max(woodEffectRange));
        entries.add(new FalloutEntry()
                .mBS(ModBlocks.WASTE_LEAVES.get().defaultBlockState())
                .prim(new Pair(Blocks.AIR.defaultBlockState(), 1))
                .max(woodEffectRange));
        entries.add(new FalloutEntry()
                .mT(BlockTags.CROPS)
                .prim(new Pair(Blocks.AIR.defaultBlockState(), 1))
                .max(woodEffectRange));
        entries.add(new FalloutEntry()
                .mT(BlockTags.FLOWERS)
                .prim(new Pair(Blocks.AIR.defaultBlockState(), 1))
                .max(woodEffectRange));
        entries.add(new FalloutEntry()
                .mT(BlockTags.SAPLINGS)
                .prim(new Pair(Blocks.AIR.defaultBlockState(), 1))
                .max(woodEffectRange));
        entries.add(new FalloutEntry()
                .mT(ModTags.Blocks.LEAVES)
                .prim(new Pair(ModBlocks.WASTE_LEAVES.get().defaultBlockState(), 1))
                .max(woodEffectRange + 100D));

        entries.add(new FalloutEntry()
                .mBS(Blocks.MOSSY_COBBLESTONE.defaultBlockState())
                .prim(new Pair(Blocks.COAL_ORE.defaultBlockState(), 1)));
        entries.add(new FalloutEntry()
                .mBS(ModBlocks.ORE_NETHER_URANIUM.get().defaultBlockState())
                .prim(
                        new Pair(ModBlocks.ORE_NETHER_SCHRABIDIUM.get().defaultBlockState(), 1),
                        new Pair(ModBlocks.ORE_NETHER_URANIUM_SCORCHED.get().defaultBlockState(), 99)
                ));

        for (int i = 1; i <= 10; i++) {
            int m = 10 - i;
            entries.add(new FalloutEntry()
                    .prim(
                            new Pair(ModBlocks.ORE_SELLAFIELD_DIAMOND.get().defaultBlockState().setValue(SellafieldSlakedBlock.COLOR_LEVEL, m), 3),
                            new Pair(ModBlocks.ORE_SELLAFIELD_EMERALD.get().defaultBlockState().setValue(SellafieldSlakedBlock.COLOR_LEVEL, m), 2)
                    )
                    .c(0.5)
                    .max(i * 5)
                    .sol(true)
                    .mBS(Blocks.COAL_ORE.defaultBlockState()));
            entries.add(new FalloutEntry()
                    .prim(new Pair(ModBlocks.SELLAFIELD_BEDROCK.get().defaultBlockState().setValue(SellafieldSlakedBlock.COLOR_LEVEL, m), 1))
                    .max(i * 5)
                    .sol(true)
                    .mBS(Blocks.BEDROCK.defaultBlockState()));
            entries.add(new FalloutEntry()
                    .prim(new Pair(ModBlocks.SELLAFIELD_BEDROCK.get().defaultBlockState().setValue(SellafieldSlakedBlock.COLOR_LEVEL, m), 1))
                    .max(i * 5)
                    .sol(true)
                    .mBS(ModBlocks.SELLAFIELD_BEDROCK.get().defaultBlockState()));
            entries.add(new FalloutEntry()
                    .prim(new Pair(ModBlocks.SELLAFIELD_SLAKED.get().defaultBlockState().setValue(SellafieldSlakedBlock.COLOR_LEVEL, m), 1))
                    .max(i * 5)
                    .sol(true)
                    .mT(ModTags.Blocks.ACTUALLY_STONE));
            entries.add(new FalloutEntry()
                    .prim(new Pair(ModBlocks.SELLAFIELD_SLAKED.get().defaultBlockState().setValue(SellafieldSlakedBlock.COLOR_LEVEL, m), 1))
                    .max(i * 5)
                    .sol(true)
                    .mT(ModTags.Blocks.GROUND));
            if (i <= 9) entries.add(new FalloutEntry()
                    .prim(new Pair(ModBlocks.SELLAFIELD_SLAKED.get().defaultBlockState().setValue(SellafieldSlakedBlock.COLOR_LEVEL, m), 1))
                    .max(i * 5).sol(true)
                    .mBS(Blocks.GRASS_BLOCK.defaultBlockState()));
        }

        entries.add(new FalloutEntry()
                .mBS(Blocks.MYCELIUM.defaultBlockState())
                .prim(new Pair(ModBlocks.WASTE_MYCELIUM.get().defaultBlockState(), 1)));
        entries.add(new FalloutEntry()
                .mBS(Blocks.SAND.defaultBlockState())
                .prim(new Pair(ModBlocks.WASTE_TRINITITE.get().defaultBlockState(), 1))
                .c(0.05));
        entries.add(new FalloutEntry()
                .mBS(Blocks.RED_SAND.defaultBlockState())
                .prim(new Pair(ModBlocks.WASTE_TRINITITE_RED.get().defaultBlockState(), 1))
                .c(0.05));
        entries.add(new FalloutEntry()
                .mBS(Blocks.CLAY.defaultBlockState())
                .prim(new Pair(Blocks.TERRACOTTA.defaultBlockState(), 1)));

    }

    private static void writeDefault(File file) {

        HBMsNTM.LOGGER.info("No fallout config file found, registering defaults for {}", file.getName());

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
        private BlockState matchesBlockState = null;
        private TagKey<Block> matchesTag = null;
        private boolean matchesOpaque = false;

        //           BlockState / Weight
        private Pair<BlockState, Integer>[] primaryBlocks = null;
        private Pair<BlockState, Integer>[] secondaryBlocks = null;
        private double primaryChance = 1.0D;
        private double minDist = 0.0D;
        private double maxDist = 100.0D;
        private double falloffStart = 0.9D;

        /** Whether the depth value should be decremented when this block is converted */
        private boolean isSolid = false;

        public FalloutEntry clone() {
            FalloutEntry entry = new FalloutEntry();
            entry.mBS(matchesBlockState);
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

        public FalloutEntry mBS(BlockState state) { this.matchesBlockState = state; return this; }
        public FalloutEntry mT(TagKey<Block> tag) { this.matchesTag = tag; return this; }
        public FalloutEntry mO(boolean opaque) { this.matchesOpaque = opaque; return this; }

        public FalloutEntry prim(Pair<BlockState, Integer>... blocks) { this.primaryBlocks = blocks; return this; }
        public FalloutEntry sec(Pair<BlockState, Integer>... blocks) { this.secondaryBlocks = blocks; return this; }
        public FalloutEntry c(double chance) { this.primaryChance = chance; return this; }
        public FalloutEntry min(double min) { this.minDist = min; return this; }
        public FalloutEntry max(double max) { this.maxDist = max; return this; }
        public FalloutEntry fo(double falloffStart) { this.falloffStart = falloffStart; return this; }
        public FalloutEntry sol(boolean solid) { this.isSolid = solid; return this; }

        public boolean eval(Level level, BlockPos pos, BlockState state, double dist) {

            if (dist > maxDist || dist < minDist) return false;
            if (matchesBlockState != null && state != matchesBlockState) return false;
            if (matchesTag != null && !state.is(matchesTag)) return false;
            if (matchesOpaque && !state.isSolidRender(level, pos)) return false;
            if (dist > maxDist * falloffStart && Math.abs(level.random.nextGaussian()) < Math.pow((dist - maxDist * falloffStart) / (maxDist - maxDist * falloffStart), 2D) * 3D) return false;

            StateBlock conversion = chooseRandomOutcome((primaryChance == 1D || rand.nextDouble() < primaryChance) ? primaryBlocks : secondaryBlocks);

            if (conversion != null) {
                if (conversion.state == ModBlocks.SELLAFIELD_SLAKED.get().defaultBlockState() && state == ModBlocks.SELLAFIELD_SLAKED.get().defaultBlockState()) return false;
                if (conversion.state == ModBlocks.SELLAFIELD_BEDROCK.get().defaultBlockState() && state == ModBlocks.SELLAFIELD_BEDROCK.get().defaultBlockState()) return false;
                if (state == ModBlocks.SELLAFIELD_BEDROCK.get().defaultBlockState() && conversion.state != ModBlocks.SELLAFIELD_BEDROCK.get().defaultBlockState()) return false;
                if (pos.getY() == 0 && conversion.state != ModBlocks.SELLAFIELD_BEDROCK.get().defaultBlockState()) return false;
                level.setBlock(pos, conversion.state, 3);
                return true;
            }

            return false;
        }

        private StateBlock chooseRandomOutcome(Pair<BlockState, Integer>[] blocks) {
            if (blocks == null) return null;

            int weight = 0;

            for (Pair<BlockState, Integer> choice : blocks) {
                weight += choice.getValue();
            }

            int r = rand.nextInt(weight);

            for (Pair<BlockState, Integer> choice : blocks) {
                r -= choice.getValue();

                if (r <= 0) {
                    return new StateBlock(choice.getKey());
                }
            }
            return new StateBlock(blocks[0].getKey());
        }

        public boolean isSolid() {
            return this.isSolid;
        }

        public void write(JsonWriter writer) throws IOException {
            if (matchesBlockState != null) writer.name("matchesBlockState").value(BuiltInRegistries.BLOCK.getKey(matchesBlockState.getBlock()).toString());
            if (matchesTag != null) writer.name("matchesTag").value(matchesTag.location().toString());
            if (matchesOpaque) writer.name("mustBeOpaque").value(true);
            if (isSolid) writer.name("restrictDepth").value(true);

            if (primaryBlocks != null) { writer.name("primarySubstitution"); writeBlockStateArray(writer, primaryBlocks); }
            if (secondaryBlocks != null) { writer.name("secondarySubstitutions"); writeBlockStateArray(writer, secondaryBlocks); }

            if (primaryChance != 1D) writer.name("chance").value(primaryChance);

            if (minDist != 0.0D) writer.name("minimumDistancePercent").value(minDist);
            if (maxDist != 100.0D) writer.name("maximumDistancePercent").value(maxDist);
            if (falloffStart != 0.9D) writer.name("falloffStartFactor").value(falloffStart);
        }

        private static FalloutEntry readEntry(JsonElement recipe) {
            FalloutEntry entry = new FalloutEntry();
            if (!recipe.isJsonObject()) return null;

            JsonObject obj = recipe.getAsJsonObject();

            if (obj.has("matchesBlock")) entry.mBS(BuiltInRegistries.BLOCK.getOptional(ResourceLocation.parse(obj.get("matchesBlock").getAsString())).get().defaultBlockState());
            if (obj.has("matchesTag")) entry.mT(TagKey.create(Registries.BLOCK, ResourceLocation.parse(obj.get("matchesTag").getAsString())));
            if (obj.has("mustBeOpaque")) entry.mO(obj.get("mustBeOpaque").getAsBoolean());
            if (obj.has("restrictDepth")) entry.sol(obj.get("restrictDepth").getAsBoolean());

            if (obj.has("primarySubstitution")) entry.prim(readBlockStateArray(obj.get("primarySubstitution")));
            if (obj.has("secondarySubstitutions")) entry.sec(readBlockStateArray(obj.get("secondarySubstitutions")));

            if (obj.has("chance")) entry.c(obj.get("chance").getAsDouble());

            if (obj.has("minimumDistancePercent")) entry.min(obj.get("minimumDistancePercent").getAsDouble());
            if (obj.has("maximumDistancePercent")) entry.max(obj.get("maximumDistancePercent").getAsDouble());
            if (obj.has("falloffStartFactor")) entry.fo(obj.get("falloffStartFactor").getAsDouble());

            return entry;
        }

        private static void writeBlockStateArray(JsonWriter writer, Pair<BlockState, Integer>[] array) throws IOException {
            writer.beginArray();
            writer.setIndent("");

            for (Pair<BlockState, Integer> blockState : array) {
                writer.beginArray();
                writer.value(String.valueOf(BuiltInRegistries.BLOCK.getKey(blockState.getKey().getBlock())));
                writer.value(blockState.getValue());
                writer.endArray();
            }

            writer.endArray();
            writer.setIndent("  ");
        }

        private static Pair<BlockState, Integer>[] readBlockStateArray(JsonElement jsonElement) {

            if (!jsonElement.isJsonArray()) return null;

            JsonArray array = jsonElement.getAsJsonArray();
            Pair<BlockState, Integer>[] blockStateArray = new Pair[array.size()];

            for (int i = 0; i < blockStateArray.length; i++) {
                JsonElement stateBlock = array.get(i);

                if (!stateBlock.isJsonArray()) {
                    throw new IllegalStateException("Could not read block " + stateBlock);
                }

                JsonArray mBArray = stateBlock.getAsJsonArray();

                blockStateArray[i] = new Pair(BuiltInRegistries.BLOCK.get(ResourceLocation.parse(mBArray.get(0).getAsString())), mBArray.get(1).getAsInt());
            }

            return blockStateArray;
        }
    }
}
