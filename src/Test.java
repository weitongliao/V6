import java.util.concurrent.ConcurrentHashMap;

public class Test {
    public static void main(String[] args) {
        ConcurrentHashMap<String, String> nodes = new ConcurrentHashMap<>();
        nodes.put("100100100000012", "1.1.1.1");
        nodes.put("100100100000011", "1.1.1.1");
        nodes.put("001001001000012", "1.1.1.1");

        String currentNodeID = "010010010000012";
//        String currentNodeID = "111111000000005";


        System.out.println("Left Outside Leaf: " + ServerTest.getLeftOutsideLeaf(nodes, currentNodeID));
        System.out.println("Right Outside Leaf: " + ServerTest.getRightOutsideLeaf(nodes, currentNodeID));
        System.out.println("Left Cyclic Neighbor: " + ServerTest.getLeftCyclicNeighbor(nodes, currentNodeID));
        System.out.println("Right Cyclic Neighbor: " + ServerTest.getRightCyclicNeighbor(nodes, currentNodeID));
        System.out.println("Cubical Neighbor: " + ServerTest.getCubicalNeighbor(nodes, currentNodeID));
        System.out.println(ServerTest.getInnerLeaf(nodes, currentNodeID));
    }
}
