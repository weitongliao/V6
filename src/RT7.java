import java.util.concurrent.ConcurrentHashMap;

public class RT7 {
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
//        System.out.println(Integer.toBinaryString(ServerTest.getCubicalIndex("111111111011112")));
//        System.out.println(Integer.toBinaryString(ServerTest.getCubicalIndex("111111111011112")));

        String currentNodeID = "001001001000112";

        TestNode node = new TestNode();
        node.setIp("11");
        node.setNodeId(currentNodeID);
        System.out.println(currentNodeID);
        node.setLeftOutsideLeaf(new NodeID(Server.getLeftOutsideLeaf(nodes, currentNodeID), ""));
        node.setRightOutsideLeaf(new NodeID(Server.getRightOutsideLeaf(nodes, currentNodeID), ""));
//        node.setLeftCyclicNeighbor(new NodeID(ServerTest.getLeftCyclicNeighbor(nodes, currentNodeID), ""));
//        node.setRightCyclicNeighbor(new NodeID(ServerTest.getRightCyclicNeighbor(nodes, currentNodeID), ""));
        node.setCubicalNeighbor(new NodeID(Server.getCubicalNeighbor(nodes, currentNodeID), ""));
//        node.setLeftInsideLeaf(new NodeID(ServerTest.getInnerLeaf(nodes, currentNodeID).get(0), ""));
//        node.setRightInsideLeaf(new NodeID(ServerTest.getInnerLeaf(nodes, currentNodeID).get(1), ""));

//        System.out.println(node.getCubicalNeighbor().getId());

        System.out.println("Left Outside Leaf: " + node.getLeftOutsideLeaf().getId());
        System.out.println("Right Outside Leaf: " + node.getRightOutsideLeaf().getId());
//        System.out.println("Left Cyclic Neighbor: " + node.getLeftCyclicNeighbor().getId());
//        System.out.println("Right Cyclic Neighbor: " + node.getRightCyclicNeighbor().getId());
        System.out.println("Cubical Neighbor: " + node.getCubicalNeighbor().getId());
//        System.out.println("Left inside leaf: "+ node.getLeftInsideLeaf().getId());
//        System.out.println("Right inside leaf "+ node.getRightInsideLeaf().getId());

        node.routing("f", "101101101010111", "000000000000012", "");
    }
}