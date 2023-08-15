import java.util.Objects;

public class Node {
    private int clientPort = 23456;
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

    public void routing(String header, String source, String destination, String data){
        System.out.println(destination);
        // current node is destination, return
        if(Objects.equals(destination, this.nodeId)){
            return;
        }

        int k = Integer.parseInt(this.nodeId.substring(nodeId.length() - 2));
//        int k = Integer.parseInt(String.valueOf(this.nodeId.charAt(nodeId.length() - 1)));
        int MSDB = findHighestDifferentBit(this.nodeId.substring(0, nodeId.length() - 2), destination.substring(0, nodeId.length() - 2));
        if(k == -1){
            // TODO: 2023/8/13
            //routing();
            //返回结果给source
            this.routing("e", this.nodeId, source, this.nodeId);
//            ClientTest.sendMsg("e", this.nodeId, this.nodeId, source, this.leftOutsideLeaf.getIp(), clientPort);
        }
        else if(k < MSDB){
            ascending(header, source, destination, data);
        }else if(k > MSDB){
            descending(header, source, destination, data, k, MSDB);
        }else {
            traverseCycle(header, source, destination, data);
        }
    }

    // k < MSDB, send request to outside leaf
    private void ascending(String header, String source, String destination, String data){
        if(this.leftOutsideLeaf != null){
            ClientTest.sendMsg(header, data, source, this.nodeId, this.leftOutsideLeaf.getIp(), clientPort);
//            sendRequest(this.leftOutsideLeaf, "");
        }else if (this.rightOutsideLeaf != null){
            ClientTest.sendMsg(header, data, source, this.nodeId, this.rightOutsideLeaf.getIp(), clientPort);
//            sendRequest(this.rightOutsideLeaf, "");
        }else{
            // TODO: 2023/8/10
            //routing();
            this.routing(header, this.nodeId, source, this.nodeId);
            //返回结果给source
        }

    }

    // k = MSDB, send request to cubical neighbor
    private void descending(String header, String source, String destination, String data, int k, int MSDB){
        if(k==MSDB && this.cubicalNeighbor != null){
            ClientTest.sendMsg(header, data, source, this.nodeId, this.cubicalNeighbor.getIp(), clientPort);
//            sendRequest(this.cubicalNeighbor, "");
        }else if (this.leftCyclicNeighbor != null){
            ClientTest.sendMsg(header, data, source, this.nodeId, this.leftCyclicNeighbor.getIp(), clientPort);
        }else if(this.rightCyclicNeighbor != null){
            ClientTest.sendMsg(header, data, source, this.nodeId, this.rightCyclicNeighbor.getIp(), clientPort);
        }else if(this.leftInsideLeaf != null){
            ClientTest.sendMsg(header, data, source, this.nodeId, this.leftInsideLeaf.getIp(), clientPort);
        }else if(this.rightInsideLeaf != null){
            ClientTest.sendMsg(header, data, source, this.nodeId, this.rightInsideLeaf.getIp(), clientPort);
        }else {
            // TODO: 2023/8/11 return to source
            this.routing(header, this.nodeId, source, this.nodeId);
        }
        
    }

    private void traverseCycle(String header, String source, String destination, String data){
        if(this.leftInsideLeaf == null && this.rightInsideLeaf == null){
            // TODO: 2023/8/10 return to source
            this.routing(header, this.nodeId, source, this.nodeId);
        } else if (this.leftInsideLeaf == null){
            ClientTest.sendMsg(header, data, source, this.nodeId, this.rightInsideLeaf.getIp(), clientPort);
//            sendRequest(this.rightInsideLeaf, "");
        } else if (this.rightInsideLeaf == null){
            ClientTest.sendMsg(header, data, source, this.nodeId, this.leftInsideLeaf.getIp(), clientPort);
//            sendRequest(this.leftInsideLeaf, "");
        } else {
            int targetValue = Integer.parseInt(destination);
            int value1 = Integer.parseInt(this.leftInsideLeaf.getId());
            int value2 = Integer.parseInt(this.rightInsideLeaf.getId());

            int diff1 = Math.abs(targetValue - value1);
            int diff2 = Math.abs(targetValue - value2);

            if (diff1 < diff2) {
                ClientTest.sendMsg(header, data, source, this.nodeId, this.leftInsideLeaf.getIp(), clientPort);
//                sendRequest(this.leftInsideLeaf, "");
            } else {
                // TODO data中包含source节点需要的资源量
                ClientTest.sendMsg(header, data, source, this.nodeId, this.rightInsideLeaf.getIp(), clientPort);
//                sendRequest(this.rightInsideLeaf, "");
            }
        }
    }

//    private void sendRequest(NodeID destination, String content){
//        System.out.println(destination);
//    }

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
