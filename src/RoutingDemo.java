import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class RoutingDemo {
    static ConcurrentHashMap<String, String> nodes = new ConcurrentHashMap<>();

    public static void main(String[] args) throws InterruptedException {

        // Initialize a few nodes to simulate the network structure.
        // As in this demo there is no communication between node and server,
        // assume all the node IDs are already assigned by server.
        List<TestNode> allTestingNodes = new ArrayList<>();

        // cpu 000 gpu 000 ram 000 cluster 000
        TestNode node_000000000000012 = new TestNode();
        node_000000000000012.setNodeId("000000000000012");
        allTestingNodes.add(node_000000000000012);
        TestNode node_000000000000011 = new TestNode();
        node_000000000000011.setNodeId("000000000000011");
        allTestingNodes.add(node_000000000000011);
        TestNode node_000000000000010 = new TestNode();
        node_000000000000010.setNodeId("000000000000010");
        allTestingNodes.add(node_000000000000010);
        TestNode node_000000000000009 = new TestNode();
        node_000000000000009.setNodeId("000000000000009");
        allTestingNodes.add(node_000000000000009);
        TestNode node_000000000000008 = new TestNode();
        node_000000000000008.setNodeId("000000000000008");
        allTestingNodes.add(node_000000000000008);
        TestNode node_000000000000007 = new TestNode();
        node_000000000000007.setNodeId("000000000000007");
        allTestingNodes.add(node_000000000000007);
        TestNode node_000000000000006 = new TestNode();
        node_000000000000006.setNodeId("000000000000006");
        allTestingNodes.add(node_000000000000006);

        //cpu 100 gpu 100 ram 100 cluster 001
        TestNode node_100100100000112 = new TestNode();
        node_100100100000112.setNodeId("100100100000112");
        allTestingNodes.add(node_100100100000112);
        TestNode node_100100100000111 = new TestNode();
        node_100100100000111.setNodeId("100100100000111");
        allTestingNodes.add(node_100100100000111);
        TestNode node_100100100000110 = new TestNode();
        node_100100100000110.setNodeId("100100100000110");
        allTestingNodes.add(node_100100100000110);
        TestNode node_100100100000109 = new TestNode();
        node_100100100000109.setNodeId("100100100000109");
        allTestingNodes.add(node_100100100000109);
        TestNode node_100100100000108 = new TestNode();
        node_100100100000108.setNodeId("100100100000108");
        allTestingNodes.add(node_100100100000108);
        TestNode node_100100100000107 = new TestNode();
        node_100100100000107.setNodeId("100100100000107");
        allTestingNodes.add(node_100100100000107);

        //cpu 001 gpu 001 ram 001 cluster 000
        TestNode node_001001001000012 = new TestNode();
        node_001001001000012.setNodeId("001001001000012");
        allTestingNodes.add(node_001001001000012);
        TestNode node_001001001000011 = new TestNode();
        node_001001001000011.setNodeId("001001001000011");
        allTestingNodes.add(node_001001001000011);
        TestNode node_001001001000010 = new TestNode();
        node_001001001000010.setNodeId("001001001000010");
        allTestingNodes.add(node_001001001000010);
        TestNode node_001001001000009 = new TestNode();
        node_001001001000009.setNodeId("001001001000009");
        allTestingNodes.add(node_001001001000009);

        //cpu 011 gpu 111 ram 001 cluster 000
        TestNode node_011111001000012 = new TestNode();
        node_011111001000012.setNodeId("011111001000012");
        allTestingNodes.add(node_011111001000012);
        TestNode node_011111001000011 = new TestNode();
        node_011111001000011.setNodeId("011111001000011");
        allTestingNodes.add(node_011111001000011);
        TestNode node_011111001000010 = new TestNode();
        node_011111001000010.setNodeId("011111001000010");
        allTestingNodes.add(node_011111001000010);
        TestNode node_011111001000009 = new TestNode();
        node_011111001000009.setNodeId("011111001000009");
        allTestingNodes.add(node_011111001000009);

        //cpu 000 gpu 000 ram 000 cluster 001
        TestNode node_000000000000112 = new TestNode();
        node_000000000000112.setNodeId("000000000000112");
        allTestingNodes.add(node_000000000000112);
        TestNode node_000000000000111 = new TestNode();
        node_000000000000111.setNodeId("000000000000111");
        allTestingNodes.add(node_000000000000111);
        TestNode node_000000000000110 = new TestNode();
        node_000000000000110.setNodeId("000000000000110");
        allTestingNodes.add(node_000000000000110);
        TestNode node_000000000000109 = new TestNode();
        node_000000000000109.setNodeId("000000000000109");
        allTestingNodes.add(node_000000000000109);

        // Server code: put node ID into a concurrency hash map
        nodes.put("100100100000112", "1.1.1.1");
        nodes.put("100100100000111", "1.1.1.1");
        nodes.put("100100100000110", "1.1.1.1");
        nodes.put("100100100000109", "1.1.1.1");
        nodes.put("100100100000108", "1.1.1.1");
        nodes.put("100100100000107", "1.1.1.1");

        nodes.put("011111001000012", "1.1.1.1");
        nodes.put("011111001000011", "1.1.1.1");
        nodes.put("011111001000010", "1.1.1.1");
        nodes.put("011111001000009", "1.1.1.1");

        nodes.put("001001001000012", "1.1.1.1");
        nodes.put("001001001000011", "1.1.1.1");
        nodes.put("001001001000010", "1.1.1.1");
        nodes.put("001001001000009", "1.1.1.1");

        nodes.put("000000000000112", "1.1.1.1");
        nodes.put("000000000000111", "1.1.1.1");
        nodes.put("000000000000110", "1.1.1.1");
        nodes.put("000000000000109", "1.1.1.1");

        nodes.put("000000000000012", "1.1.1.1");
        nodes.put("000000000000011", "1.1.1.1");
        nodes.put("000000000000010", "1.1.1.1");
        nodes.put("000000000000009", "1.1.1.1");
        nodes.put("000000000000008", "1.1.1.1");
        nodes.put("000000000000007", "1.1.1.1");
        nodes.put("000000000000006", "1.1.1.1");

        // Server code: server uses functions like ServerTest.get... to return neighbor information to each node
        // to maintenance network structure.
        for (TestNode node: allTestingNodes) {
            if(ServerTest.getLeftOutsideLeaf(nodes, node.getNodeId()) !=null){
                node.setLeftOutsideLeaf(new NodeID(ServerTest.getLeftOutsideLeaf(nodes, node.getNodeId()), ""));
            }
            if(ServerTest.getRightOutsideLeaf(nodes, node.getNodeId()) !=null){
                node.setRightOutsideLeaf(new NodeID(ServerTest.getRightOutsideLeaf(nodes, node.getNodeId()), ""));
            }
            if(ServerTest.getLeftCyclicNeighbor(nodes, node.getNodeId()) !=null){
                node.setLeftCyclicNeighbor(new NodeID(ServerTest.getLeftCyclicNeighbor(nodes, node.getNodeId()), ""));
            }
            if(ServerTest.getRightCyclicNeighbor(nodes, node.getNodeId()) !=null){
                node.setRightCyclicNeighbor(new NodeID(ServerTest.getRightCyclicNeighbor(nodes, node.getNodeId()), ""));
            }
            if(ServerTest.getCubicalNeighbor(nodes, node.getNodeId()) !=null){
                node.setCubicalNeighbor(new NodeID(ServerTest.getCubicalNeighbor(nodes, node.getNodeId()), ""));
            }
            if(ServerTest.getInnerLeaf(nodes, node.getNodeId()).get(0) !=null){
                node.setLeftInsideLeaf(new NodeID(ServerTest.getInnerLeaf(nodes, node.getNodeId()).get(0), ""));
            }
            if(ServerTest.getInnerLeaf(nodes, node.getNodeId()).get(1) !=null){
                node.setRightInsideLeaf(new NodeID(ServerTest.getInnerLeaf(nodes, node.getNodeId()).get(1), ""));
            }
        }

        // The routing method is called here to show how the request is passed
        // Node 100100100000108 send request to find 000000000000010
        System.out.println("Node 100100100000108 send request to find 000000000000010");
        System.out.println("Node Info: " + node_100100100000108);
        node_100100100000108.routing("f", "100100100000108", "000000000000010", "");
        // Node 100100100000108 ascend to: 011111001000012

        System.out.println("-----------------------------------------------------------------");
        // Node 011111001000012 get the request
        System.out.println("Node 011111001000012 get the request");
        System.out.println("Node Info: " + node_011111001000012);
        node_011111001000012.routing("f", "100100100000108", "000000000000010", "");
        // Node 011111001000012 descend to :011111001000011

        System.out.println("-----------------------------------------------------------------");
        // Node 011111001000011 get the request
        System.out.println("Node 011111001000011 get the request");
        System.out.println("Node Info: " + node_011111001000011);
        node_011111001000011.routing("f", "100100100000108", "000000000000010", "");
        // Node 011111001000011 send to cubical neighbor: 001001001000010

        System.out.println("-----------------------------------------------------------------");
        // Node 001001001000010 get the request
        System.out.println("Node 001001001000010 get the request");
        System.out.println("Node Info: " + node_001001001000010);
        node_001001001000010.routing("f", "100100100000108", "000000000000010", "");
        // Node 001001001000010 send to cubical neighbor: 000000000000109

        System.out.println("-----------------------------------------------------------------");
        // Node 000000000000109 get the request
        System.out.println("Node 000000000000109 get the request");
        System.out.println("Node Info: " + node_000000000000109);
        node_000000000000109.routing("f", "100100100000108", "000000000000010", "");
        // Node 000000000000109 descend to :000000000000008

        System.out.println("-----------------------------------------------------------------");
        // Node 000000000000008 get the request
        System.out.println("Node 000000000000008 get the request");
        System.out.println("Node Info: " + node_000000000000008);
        node_000000000000008.routing("f", "000000000000008", "000000000000010", "");
        // Node 000000000000008 traverse to 000000000000009

        System.out.println("-----------------------------------------------------------------");
        // Node 000000000000009 get the request
        System.out.println("Node 000000000000009 get the request");
        System.out.println("Node Info: " + node_000000000000009);
        node_000000000000009.routing("f", "000000000000008", "000000000000010", "");
        // Node 000000000000009 traverse to 000000000000010

        System.out.println("-----------------------------------------------------------------");
        // Node 000000000000010 get the request
        System.out.println("Node 000000000000010 get the request");
        System.out.println("Node Info: " + node_000000000000010);
        node_000000000000010.routing("f", "000000000000008", "000000000000010", "");
        // Successful!!!: current node is destination, return echo

    }
}
