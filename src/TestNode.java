import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TestNode {
    private int clientPort = 23456;
    private String nodeId;
    private NodeID cubicalNeighbor;
    private NodeID leftCyclicNeighbor;
    private NodeID rightCyclicNeighbor;
    private NodeID leftInsideLeaf;
    private NodeID rightInsideLeaf;
    private NodeID leftOutsideLeaf;
    private NodeID rightOutsideLeaf;
    private String ip;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }



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


    public TestNode(){
        this.nodeId = null;
        this.cubicalNeighbor = null;
        this.leftCyclicNeighbor = null;
        this.rightCyclicNeighbor = null;
        this.leftInsideLeaf = null;
        this.rightInsideLeaf = null;
        this.leftOutsideLeaf = null;
        this.rightOutsideLeaf = null;
        this.ip = null;
    }

    public TestNode(String nodeId, String ip, NodeID cubicalNeighbor, NodeID leftCyclicNeighbor, NodeID rightCyclicNeighbor, NodeID leftInsideLeaf, NodeID rightInsideLeaf, NodeID leftOutsideLeaf, NodeID rightOutsideLeaf) {
        this.nodeId = nodeId;
        this.ip = ip;
        this.cubicalNeighbor = cubicalNeighbor;
        this.leftCyclicNeighbor = leftCyclicNeighbor;
        this.rightCyclicNeighbor = rightCyclicNeighbor;
        this.leftInsideLeaf = leftInsideLeaf;
        this.rightInsideLeaf = rightInsideLeaf;
        this.leftOutsideLeaf = leftOutsideLeaf;
        this.rightOutsideLeaf = rightOutsideLeaf;
    }

    public void routing(String header, String source, String destination, String data){
//        System.out.println(header+" "+source+" "+destination);
        // current node is destination, return
//        if(Objects.equals(destination, this.nodeId)){
//            return;
//        }

        int k = Integer.parseInt(this.nodeId.substring(nodeId.length() - 2));
//        int k = Integer.parseInt(String.valueOf(this.nodeId.charAt(nodeId.length() - 1)));
        int MSDB = findHighestDifferentBit(this.nodeId.substring(0, nodeId.length() - 2), destination.substring(0, nodeId.length() - 2));
        System.out.println("route: "+MSDB);
        if(MSDB == -1){
            if(this.nodeId.substring(nodeId.length() - 2).equals(destination.substring(destination.length() - 2)) && Objects.equals(header, "f")){
                System.out.println("current node is destination, return echo");
                this.routing("e", this.nodeId, source, this.ip);
            }else if(this.nodeId.substring(nodeId.length() - 2).equals(destination.substring(destination.length() - 2)) && Objects.equals(header, "e")){
                // 因为路由算法的原因，echo这里的显示可能不准确
                System.out.println("get echo from (maybe)" + source+" ip:"+data);
            }else {
                traverseCycle(header, source, destination, data);
            }

//            ClientTest.sendMsg("e", this.nodeId, this.nodeId, source, this.leftOutsideLeaf.getIp(), clientPort);
        } else if(k < MSDB){
            ascending(header, source, destination, data);
        } else if(k > MSDB){
            descending(header, source, destination, data, k, MSDB);
        } else {
            sendToCubicNeighbor(header, source, destination, data);
        }
    }

    // k = MSDB
    private void sendToCubicNeighbor(String header, String source, String destinationID, String data){
        if(this.getLeftCyclicNeighbor() != null){
            System.out.println(this.getLeftCyclicNeighbor().getId());
//            ClientTest.sendMsg(header, data, source, this.nodeId, destinationID, this.leftCyclicNeighbor.getIp(), clientPort);
        } else if (this.getRightCyclicNeighbor() != null){
            System.out.println(this.getRightCyclicNeighbor().getId());
//            ClientTest.sendMsg(header, data, source, this.nodeId, destinationID, this.rightCyclicNeighbor.getIp(), clientPort);
        } else {
//            System.out.println("sendToCubicNeighbor return to source "+this.nodeId);
            traverseCycle(header, source, destinationID, data);
//            this.routing("e", this.nodeId, source, this.ip);
        }
    }

    // k < MSDB, send request to outside leaf
    private void ascending(String header, String source, String destinationID, String data){
        if(this.leftOutsideLeaf == null && this.rightOutsideLeaf == null){
            // TODO: 2023/8/10
            //routing();
            System.out.println("ascend return");
            this.routing("e", this.nodeId, source, this.ip);
            //返回结果给source
        }else if(this.leftOutsideLeaf == null){
            ClientTest.sendMsg(header, data, source, this.nodeId, destinationID, this.rightOutsideLeaf.getIp(), clientPort);
        }else if (this.rightOutsideLeaf == null){
            ClientTest.sendMsg(header, data, source, this.nodeId, destinationID, this.leftOutsideLeaf.getIp(), clientPort);
        }else {
            int targetValue = Integer.parseInt(destinationID.substring(0, destinationID.length()-2), 2);

            int value1 = Integer.parseInt(this.leftOutsideLeaf.getId().substring(0, this.leftOutsideLeaf.getId().length()-2), 2);
            int value2 = Integer.parseInt(this.rightOutsideLeaf.getId().substring(0, this.rightOutsideLeaf.getId().length()-2), 2);

            int diff1 = Math.abs(targetValue - value1);
            int diff2 = Math.abs(targetValue - value2);

            if (diff1 < diff2) {
                System.out.println(this.leftOutsideLeaf.getId());
//                ClientTest.sendMsg(header, data, source, this.nodeId, destinationID, this.leftOutsideLeaf.getIp(), clientPort);
//                sendRequest(this.leftInsideLeaf, "");
            } else {
                // TODO data中包含source节点需要的资源量
                System.out.println(this.rightOutsideLeaf.getId());
//                ClientTest.sendMsg(header, data, source, this.nodeId, destinationID, this.rightOutsideLeaf.getIp(), clientPort);
//                sendRequest(this.rightInsideLeaf, "");
            }
        }

    }

    // k = MSDB, send request to cubical neighbor
    private void descending(String header, String source, String destinationID, String data, int k, int MSDB){
        List<NodeID> candidate = new ArrayList<>();
        if (this.leftCyclicNeighbor == null && this.rightCyclicNeighbor == null && this.leftInsideLeaf == null && this.rightInsideLeaf == null){
            // TODO: 2023/8/11 return to source
            System.out.println("descend return");
            this.routing("e", this.nodeId, source, this.ip);
        }
        else {
            if(this.leftCyclicNeighbor != null ){
                candidate.add(this.leftCyclicNeighbor);
            }
            if(this.rightCyclicNeighbor != null){
                candidate.add(this.rightCyclicNeighbor);
            }
            if(this.leftInsideLeaf != null){
                candidate.add(this.leftInsideLeaf);
            }
            if(this.rightInsideLeaf != null){
                candidate.add(this.rightInsideLeaf);
            }
            int minDiff = Integer.MAX_VALUE;
            NodeID minNode = null;
            int destCubic = Integer.parseInt(destinationID.substring(0, destinationID.length()-2), 2);
            for (NodeID nodeID : candidate) {
//                System.out.println(nodeID.getId());
                int tempCubic = Integer.parseInt(nodeID.getId().substring(0, nodeID.getId().length()-2), 2);
                int diff = Math.abs(tempCubic - destCubic);
                if (diff < minDiff) {
                    minNode = nodeID;
                    minDiff = diff;
                }
            }
            System.out.println(minNode.getId());
//            ClientTest.sendMsg(header, data, source, this.nodeId, destinationID, minNode.getIp(), clientPort);
        }

    }

    private void traverseCycle(String header, String source, String destinationID, String data){
        if(this.leftInsideLeaf == null && this.rightInsideLeaf == null){
            // TODO: 2023/8/10 return to source
            System.out.println("traver return");
            this.routing("e", this.nodeId, source, this.ip);
        } else if (this.leftInsideLeaf == null){
            System.out.println(this.rightInsideLeaf.getId());
//            ClientTest.sendMsg(header, data, source, this.nodeId, destinationID, this.rightInsideLeaf.getIp(), clientPort);
//            sendRequest(this.rightInsideLeaf, "");
        } else if (this.rightInsideLeaf == null){
            System.out.println(this.leftInsideLeaf.getId());
//            ClientTest.sendMsg(header, data, source, this.nodeId, destinationID, this.leftInsideLeaf.getIp(), clientPort);
//            sendRequest(this.leftInsideLeaf, "");
        } else {
            // left inside leaf is destination
            if (Objects.equals(destinationID, this.leftInsideLeaf.getId())){
                System.out.println(this.leftInsideLeaf.getId());
//                ClientTest.sendMsg(header, data, source, this.nodeId, destinationID, this.leftInsideLeaf.getIp(), clientPort);
            }
            // left inside leaf is destination
            else if (Objects.equals(destinationID, this.rightInsideLeaf.getId())){
                System.out.println(this.rightInsideLeaf.getId());
//                ClientTest.sendMsg(header, data, source, this.nodeId, destinationID, this.rightInsideLeaf.getIp(), clientPort);
            }
            // closer node
            else {
                int targetValue = Integer.parseInt(destinationID.substring(destinationID.length()-2));

                int value1 = Integer.parseInt(this.leftInsideLeaf.getId().substring(destinationID.length()-2));
                int value2 = Integer.parseInt(this.rightInsideLeaf.getId().substring(destinationID.length()-2));

                int diff1 = Math.abs(targetValue - value1);
                int diff2 = Math.abs(targetValue - value2);

                if (diff1 < diff2) {
                    System.out.println(this.leftInsideLeaf.getId());
//                    ClientTest.sendMsg(header, data, source, this.nodeId, destinationID, this.leftInsideLeaf.getIp(), clientPort);
//                sendRequest(this.leftInsideLeaf, "");
                } else {
                    // TODO data中包含source节点需要的资源量
                    System.out.println(this.rightInsideLeaf.getId());
//                    ClientTest.sendMsg(header, data, source, this.nodeId, destinationID, this.rightInsideLeaf.getIp(), clientPort);
//                sendRequest(this.rightInsideLeaf, "");
                }
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
