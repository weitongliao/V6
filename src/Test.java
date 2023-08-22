import java.util.concurrent.ConcurrentHashMap;

public class Test {
    public static void main(String[] args) {
        System.out.println(RequestHandler.padString("001", 3));
        ConcurrentHashMap<String, String> nodes = new ConcurrentHashMap<>();
        nodes.put("100100100000012", "1.1.1.1");
        nodes.put("100100100000011", "1.1.1.1");
        nodes.put("001001001000012", "1.1.1.1");

        String currentNodeID = "010010010000012";
//        String currentNodeID = "111111000000005";


        System.out.println("Left Outside Leaf: " + Server.getLeftOutsideLeaf(nodes, currentNodeID));
        System.out.println("Right Outside Leaf: " + Server.getRightOutsideLeaf(nodes, currentNodeID));
        System.out.println("Left Cyclic Neighbor: " + Server.getLeftCyclicNeighbor(nodes, currentNodeID));
        System.out.println("Right Cyclic Neighbor: " + Server.getRightCyclicNeighbor(nodes, currentNodeID));
        System.out.println("Cubical Neighbor: " + Server.getCubicalNeighbor(nodes, currentNodeID));
        System.out.println(Server.getInnerLeaf(nodes, currentNodeID));
    }
}
