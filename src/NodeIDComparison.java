import java.util.HashMap;
import java.util.Map;

public class NodeIDComparison {
    public static void main(String[] args) {
        HashMap<String, String> nodes = new HashMap<>();
        nodes.put("10000011", "value1");
        nodes.put("10000101", "value2");
        nodes.put("10000003", "value3");
        nodes.put("10000110", "value4");
        nodes.put("10000113", "value4");
        nodes.put("10000115", "value4");

        String currentNodeID = "10000010";

        NodeIDComparison comparer = new NodeIDComparison();
        String rightOutsideLeaf = comparer.getRightOutsideLeaf(nodes, currentNodeID);
        System.out.println("Right Outside Leaf: " + rightOutsideLeaf);
    }

    public String getRightOutsideLeaf(HashMap<String, String> nodes, String currentNodeID) {
        String rightOutsideLeaf = null;

        for (Map.Entry<String, String> entry : nodes.entrySet()) {
            String key = entry.getKey();
            if (key.substring(0, 7).compareTo(currentNodeID.substring(0, 7)) > 0) {
                if (rightOutsideLeaf == null || key.charAt(7) > rightOutsideLeaf.charAt(7)) {
                    rightOutsideLeaf = key;
                }
            }
        }

        return rightOutsideLeaf;
    }
}
