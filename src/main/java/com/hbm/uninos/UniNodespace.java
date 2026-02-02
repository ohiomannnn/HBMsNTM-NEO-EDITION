package com.hbm.uninos;

import com.hbm.util.Tuple.Pair;
import com.hbm.util.fauxpointtwelve.DirPos;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Unified Nodespace, a Nodespace for all applications.
 * "Nodespace" is an invisible "dimension" where nodes exist, a node is basically the "soul" of a block entity with networking capabilities.
 * Instead of block entities having to find each other which is costly and assumes the BEs are loaded, BEs simply create nodes at their
 * respective position in nodespace, the nodespace itself handles stuff like connections which can also happen in unloaded chunks.
 * A node is so to say the "soul" of a block entity which can act independent of its "body".
 * @author hbm
 */
public class UniNodespace {

    // funny name
    public static HashMap<Level, UniNodeWorld> levels = new HashMap<>();
    public static Set<NodeNet> activeNodeNets = new HashSet<>();

    public static GenNode getNode(Level level, BlockPos pos, INetworkProvider type) {
        UniNodeWorld nodeWorld = levels.get(level);
        if (nodeWorld != null) return nodeWorld.nodes.get(new Pair(pos, type));
        return null;
    }

    public static void createNode(Level level, GenNode node) {
        UniNodeWorld nodeWorld = levels.get(level);
        if(nodeWorld == null) {
            nodeWorld = new UniNodeWorld();
            levels.put(level, nodeWorld);
        }
        nodeWorld.pushNode(node);
    }

    public static void destroyNode(Level level, BlockPos pos, INetworkProvider type) {
        GenNode node = getNode(level, pos, type);
        if (node != null) {
            levels.get(level).popNode(node);
        }
    }

    public static void destroyNode(Level level, GenNode node) {
        if (node != null) {
            levels.get(level).popNode(node);
        }
    }

    public static void updateNodespace(MinecraftServer server) {

        for (Level level : server.getAllLevels()) {
            UniNodeWorld nodeWorld = levels.get(level);

            if (nodeWorld == null) continue;

            for (Entry<Pair<BlockPos, INetworkProvider>, GenNode> entry : nodeWorld.nodes.entrySet()) {
                GenNode node = entry.getValue();
                INetworkProvider provider = entry.getKey().getValue();
                if (!node.hasValidNet() || node.recentlyChanged) {
                    checkNodeConnection(level, node, provider);
                    node.recentlyChanged = false;
                }
            }
        }

        updateNetworks();
    }

    private static void updateNetworks() {

        for (NodeNet net : activeNodeNets) net.resetTrackers(); //reset has to be done before everything else
        for (NodeNet net : activeNodeNets) net.update();
    }

    /** Goes over each connection point of the given node, tries to find neighbor nodes and to join networks with them */
    private static void checkNodeConnection(Level level, GenNode node, INetworkProvider provider) {

        for (DirPos con : node.connections) {
            GenNode conNode = getNode(level, new BlockPos(con.getX(), con.getY(), con.getZ()), provider); // get whatever neighbor node intersects with that connection
            if (conNode != null) { // if there is a node at that place
                if (conNode.hasValidNet() && conNode.net == node.net) continue; // if the net is valid and both nodes have the same net, skip
                if (checkConnection(conNode, con, false)) {
                    connectToNode(node, conNode);
                }
            }
        }

        if (node.net == null || !node.net.isValid()) provider.provideNetwork().joinLink(node);
    }


    /** Checks if the node can be connected to given the DirPos, skipSideCheck will ignore the DirPos' direction value */
    public static boolean checkConnection(GenNode connectsTo, DirPos connectFrom, boolean skipSideCheck) {
        for (DirPos revCon : connectsTo.connections) {
            if (revCon.getX() - revCon.getDir().getStepX() == connectFrom.getX() && revCon.getY() - revCon.getDir().getStepY() == connectFrom.getY() && revCon.getZ() - revCon.getDir().getStepZ() == connectFrom.getZ() && (revCon.getDir() == connectFrom.getDir().getOpposite() || skipSideCheck)) {
                return true;
            }
        }
        return false;
    }

    /** Links two nodes with different or potentially no networks */
    private static void connectToNode(GenNode origin, GenNode connection) {

        if(origin.hasValidNet() && connection.hasValidNet()) { // both nodes have nets, but the nets are different (previous assumption), join networks
            if(origin.net.links.size() > connection.net.links.size()) {
                origin.net.joinNetworks(connection.net);
            } else {
                connection.net.joinNetworks(origin.net);
            }
        } else if(!origin.hasValidNet() && connection.hasValidNet()) { // origin has no net, connection does, have origin join connection's net
            connection.net.joinLink(origin);
        } else if(origin.hasValidNet() && !connection.hasValidNet()) { // ...and vice versa
            origin.net.joinLink(connection);
        }
    }


    public static class UniNodeWorld {

        public HashMap<Pair<BlockPos, INetworkProvider>, GenNode> nodes = new LinkedHashMap<>();

        /** Adds a node at all its positions to the nodespace */
        public void pushNode(GenNode node) {
            for(BlockPos pos : node.positions) {
                nodes.put(new Pair(pos, node.networkProvider), node);
            }
        }

        /** Removes the specified node from all positions from nodespace */
        public void popNode(GenNode node) {
            if(node.net != null) node.net.destroy();
            for(BlockPos pos : node.positions) {
                nodes.remove(new Pair(pos, node.networkProvider));
            }
            node.expired = true;
        }
    }
}

