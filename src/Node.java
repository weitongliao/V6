public class Node {
    private String nodeId;
    private NodeID cubicalNeighbor;
    private NodeID leftCyclicNeighbor;
    private NodeID rightCyclicNeighbor;
    private NodeID leftInsideLeaf;
    private NodeID rightInsideLeaf;
    private NodeID leftOutsideLeaf;
    private NodeID rightOutsideLeaf;

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public NodeID getCubicalNeighbor() {
        return cubicalNeighbor;
    }

    public void setCubicalNeighbor(NodeID cubicalNeighbor) {
        this.cubicalNeighbor = cubicalNeighbor;
    }

    public NodeID getLeftCyclicNeighbor() {
        return leftCyclicNeighbor;
    }

    public void setLeftCyclicNeighbor(NodeID leftCyclicNeighbor) {
        this.leftCyclicNeighbor = leftCyclicNeighbor;
    }

    public NodeID getRightCyclicNeighbor() {
        return rightCyclicNeighbor;
    }

    public void setRightCyclicNeighbor(NodeID rightCyclicNeighbor) {
        this.rightCyclicNeighbor = rightCyclicNeighbor;
    }

    public NodeID getLeftInsideLeaf() {
        return leftInsideLeaf;
    }

    public void setLeftInsideLeaf(NodeID leftInsideLeaf) {
        this.leftInsideLeaf = leftInsideLeaf;
    }

    public NodeID getRightInsideLeaf() {
        return rightInsideLeaf;
    }

    public void setRightInsideLeaf(NodeID rightInsideLeaf) {
        this.rightInsideLeaf = rightInsideLeaf;
    }

    public NodeID getLeftOutsideLeaf() {
        return leftOutsideLeaf;
    }

    public void setLeftOutsideLeaf(NodeID leftOutsideLeaf) {
        this.leftOutsideLeaf = leftOutsideLeaf;
    }

    public NodeID getRightOutsideLeaf() {
        return rightOutsideLeaf;
    }

    public void setRightOutsideLeaf(NodeID rightOutsideLeaf) {
        this.rightOutsideLeaf = rightOutsideLeaf;
    }




    public Node(String nodeId, NodeID cubicalNeighbor, NodeID leftCyclicNeighbor, NodeID rightCyclicNeighbor, NodeID leftInsideLeaf, NodeID rightInsideLeaf, NodeID leftOutsideLeaf, NodeID rightOutsideLeaf) {
        this.nodeId = nodeId;
        this.cubicalNeighbor = cubicalNeighbor;
        this.leftCyclicNeighbor = leftCyclicNeighbor;
        this.rightCyclicNeighbor = rightCyclicNeighbor;
        this.leftInsideLeaf = leftInsideLeaf;
        this.rightInsideLeaf = rightInsideLeaf;
        this.leftOutsideLeaf = leftOutsideLeaf;
        this.rightOutsideLeaf = rightOutsideLeaf;
    }

    public void routing(String destination){
        System.out.println(destination);

        int k = Integer.parseInt(String.valueOf(this.nodeId.charAt(nodeId.length() - 1)));
        int MSDB = findHighestDifferentBit(this.nodeId.substring(0, nodeId.length() - 1), destination.substring(0, nodeId.length() - 1));
        if(k < MSDB){
            ascending(destination);
        }else if(k > MSDB){
            descending(destination);
        }else {
            traverseCycle(destination);
        }
    }

    private void ascending(String destination){
        sendRequest(destination, "");
    }

    private void descending(String destination){
        sendRequest(destination, "");
    }

    private void traverseCycle(String destination){
        sendRequest(destination, "");
    }

    private void sendRequest(String destination, String content){
        System.out.println(destination);
    }

    public static int findHighestDifferentBit(String str1, String str2) {
        int length = Math.min(str1.length(), str2.length());

        for (int i = 0; i < length; i++) {
            if (str1.charAt(i) != str2.charAt(i)) {
                return length - i - 1;
            }
        }

        return -1; // No difference found
    }

}
