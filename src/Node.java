import java.util.Objects;

public class Node {
    private String nodeId;
    private NodeID cubicalNeighbor;
    private NodeID leftCyclicNeighbor;
    private NodeID rightCyclicNeighbor;
    private NodeID leftInsideLeaf;
    private NodeID rightInsideLeaf;
    private NodeID leftOutsideLeaf;
    private NodeID rightOutsideLeaf;

    public synchronized String getNodeId() {
        return nodeId;
    }

    public synchronized void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public synchronized NodeID getCubicalNeighbor() {
        return cubicalNeighbor;
    }

    public synchronized void setCubicalNeighbor(NodeID cubicalNeighbor) {
        this.cubicalNeighbor = cubicalNeighbor;
    }

    public synchronized NodeID getLeftCyclicNeighbor() {
        return leftCyclicNeighbor;
    }

    public synchronized void setLeftCyclicNeighbor(NodeID leftCyclicNeighbor) {
        this.leftCyclicNeighbor = leftCyclicNeighbor;
    }

    public synchronized NodeID getRightCyclicNeighbor() {
        return rightCyclicNeighbor;
    }

    public synchronized void setRightCyclicNeighbor(NodeID rightCyclicNeighbor) {
        this.rightCyclicNeighbor = rightCyclicNeighbor;
    }

    public synchronized NodeID getLeftInsideLeaf() {
        return leftInsideLeaf;
    }

    public synchronized void setLeftInsideLeaf(NodeID leftInsideLeaf) {
        this.leftInsideLeaf = leftInsideLeaf;
    }

    public synchronized NodeID getRightInsideLeaf() {
        return rightInsideLeaf;
    }

    public synchronized void setRightInsideLeaf(NodeID rightInsideLeaf) {
        this.rightInsideLeaf = rightInsideLeaf;
    }

    public synchronized NodeID getLeftOutsideLeaf() {
        return leftOutsideLeaf;
    }

    public synchronized void setLeftOutsideLeaf(NodeID leftOutsideLeaf) {
        this.leftOutsideLeaf = leftOutsideLeaf;
    }

    public synchronized NodeID getRightOutsideLeaf() {
        return rightOutsideLeaf;
    }

    public synchronized void setRightOutsideLeaf(NodeID rightOutsideLeaf) {
        this.rightOutsideLeaf = rightOutsideLeaf;
    }


    public Node(){
        this.nodeId = null;
        this.cubicalNeighbor = null;
        this.leftCyclicNeighbor = null;
        this.rightCyclicNeighbor = null;
        this.leftInsideLeaf = null;
        this.rightInsideLeaf = null;
        this.leftOutsideLeaf = null;
        this.rightOutsideLeaf = null;
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
        // current node is destination, return
        if(Objects.equals(destination, this.nodeId)){
            return;
        }

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

    // k < MSDB, send request to outside leaf
    private void ascending(String destination){
        if(this.leftOutsideLeaf != null){
            sendRequest(this.leftOutsideLeaf, "");
        }else if (this.rightOutsideLeaf != null){
            sendRequest(this.rightOutsideLeaf, "");
        }else{
            // TODO: 2023/8/10
            //routing();
            //返回结果给source
        }

    }

    // k = MSDB, send request to cubical neighbor
    private void descending(String destination){
        if(this.cubicalNeighbor != null){
            sendRequest(this.cubicalNeighbor, "");
        }else{
            // TODO: 2023/8/10 return to source 
        }
        
    }

    private void traverseCycle(String destination){
        if(this.leftInsideLeaf == null && this.rightInsideLeaf == null){
            // TODO: 2023/8/10 return to source
        } else if (this.leftInsideLeaf == null){
            sendRequest(this.rightInsideLeaf, "");
        } else if (this.rightInsideLeaf == null){
            sendRequest(this.leftInsideLeaf, "");
        } else {
            int targetValue = Integer.parseInt(destination);
            int value1 = Integer.parseInt(this.leftInsideLeaf.getId());
            int value2 = Integer.parseInt(this.rightInsideLeaf.getId());

            int diff1 = Math.abs(targetValue - value1);
            int diff2 = Math.abs(targetValue - value2);

            if (diff1 < diff2) {
                sendRequest(this.leftInsideLeaf, "");
            } else {
                sendRequest(this.rightInsideLeaf, "");
            }
        }
    }

    private void sendRequest(NodeID destination, String content){
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
