import java.util.concurrent.ConcurrentHashMap;

public class RT4 {
    static ConcurrentHashMap<String, String> nodes = new ConcurrentHashMap<>();
    static ConcurrentHashMap<String, Integer> counts = new ConcurrentHashMap<>();
    public static void main(String[] args) throws InterruptedException {
        nodes.put("000000000000012", "1.1.1.1");
        nodes.put("111111111011112", "1.1.1.1");
        nodes.put("101101101010112", "1.1.1.1");
        nodes.put("101101101010111", "1.1.1.1");
        nodes.put("101101101010110", "1.1.1.1");
        nodes.put("101101101010109", "1.1.1.1");
        nodes.put("001001001000112", "1.1.1.1");

        // cpu 000 gpu 000 ram 000 cluster000 index 12
//        String currentNodeID = " 000000000000012";
        // cpu 111 gpu 111 ram 111 cluster 111 index 12
//        String currentNodeID = " 111111111011112";
//        // cpu 101 gpu 101 ram 101 cluster 101 index 12
//        String currentNodeID = " 101101101010112";
//        // cpu 101 gpu 101 ram 101 cluster 101 index 11
        String currentNodeID = "101101101010111";
//        // cpu 101 gpu 101 ram 101 cluster 101 index 10
//        String currentNodeID = " 101101101010110";
//        // cpu 101 gpu 101 ram 101 cluster 101 index 10
//        String currentNodeID = " 101101101010109";
//        // cpu 001 gpu 001 ram 001 cluster 001 index 12
//        String currentNodeID = " 001001001000112";

        TestNode node = new TestNode();
        node.setNodeId(currentNodeID);
        node.setLeftOutsideLeaf(new NodeID(ServerTest.getLeftOutsideLeaf(nodes, currentNodeID), ""));
        node.setRightOutsideLeaf(new NodeID(ServerTest.getRightOutsideLeaf(nodes, currentNodeID), ""));
//        node.setLeftCyclicNeighbor(new NodeID(ServerTest.getLeftCyclicNeighbor(nodes, currentNodeID), ""));
//        node.setRightCyclicNeighbor(new NodeID(ServerTest.getRightCyclicNeighbor(nodes, currentNodeID), ""));
//        node.setCubicalNeighbor(new NodeID(ServerTest.getCubicalNeighbor(nodes, currentNodeID), ""));
        node.setLeftInsideLeaf(new NodeID(ServerTest.getInnerLeaf(nodes, currentNodeID).get(0), ""));
        node.setRightInsideLeaf(new NodeID(ServerTest.getInnerLeaf(nodes, currentNodeID).get(1), ""));

//        System.out.println(node.getCubicalNeighbor().getId());

        System.out.println("Left Outside Leaf: " + node.getLeftOutsideLeaf().getId());
        System.out.println("Right Outside Leaf: " + node.getRightOutsideLeaf().getId());
//        System.out.println("Left Cyclic Neighbor: " + node.getLeftCyclicNeighbor().getId());
//        System.out.println("Right Cyclic Neighbor: " + node.getRightCyclicNeighbor().getId());
//        System.out.println("Cubical Neighbor: " + node.getCubicalNeighbor().getId());
        System.out.println("Left inside leaf: "+ node.getLeftInsideLeaf().getId());
        System.out.println("Right inside leaf "+ node.getRightInsideLeaf().getId());

        node.routing("f", "101101101010111", "000000000000012", "");
    }
}