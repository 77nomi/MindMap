package core;

import java.util.*;

public class MindMap {
    private Node root;
    private Map<Integer, Node> nodeMap;
    private String title;
    private int mapId;
    private int ifChange;

    //新建思维导图，mapId为-1标记为新建的
    public MindMap() {
        this.title = "新建思维导图";
        this.root = new Node("中心节点");
        this.nodeMap = new HashMap<>();
        this.mapId = -1;
        this.ifChange = 0;
        nodeMap.put(0, root);
    }

    //从数据库中导入的思维导图，data为根节点data，mapId不为-1表明非新建
    public MindMap(String title, String data, int mapId){
        this.title = title;
        this.mapId=mapId;
        this.root = new Node(data,0);
        this.nodeMap = new HashMap<>();
        this.ifChange = 0;
        nodeMap.put(0, root);
    }

    public void addNode(int parentId, String newData) {
        this.ifChange = 1;
        Node parent = nodeMap.get(parentId);
        if (parent != null) {
            Node newNode = new Node(newData);
            parent.addChild(newNode);
            nodeMap.put(newNode.getNodeId(), newNode);
        } else {
            System.out.println("Parent node not found.");
            System.out.println("Current mind map:");
            this.display();
        }
    }

    public void addNode(int parentId,String newData, int nodeId){
        this.ifChange = 1;
        Node parent = nodeMap.get(parentId);
        Node newNode = new Node(newData,nodeId);
        parent.addChild(newNode);
        nodeMap.put(newNode.getNodeId(), newNode);
    }

    public void removeNode(int nodeId) {
        if(nodeId==0){
            System.out.println("根节点不可移除");
            return ;
        }
        Node nodeToRemove = nodeMap.get(nodeId);
        if (nodeToRemove != null) {
            this.ifChange = 1;
            Node parent = nodeToRemove.getParent();
            if (parent != null) {
                parent.removeChild(nodeToRemove);
            }
            removeChildNodes(nodeToRemove);
            nodeMap.remove(nodeId);
        } else {
            System.out.println("Node to remove not found.");
        }
    }

    private void removeChildNodes(Node node) {
        this.ifChange = 1;
        List<Node> children = new ArrayList<>(node.getChildren());
        for (Node child : children) {
            removeChildNodes(child);
            node.removeChild(child);
            nodeMap.remove(child.getNodeId());
        }
    }

    public void display() {
        if (root == null) {
            return;
        }

        Queue<Node> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                Node current = queue.poll();
                if (current != null) {
                    System.out.print(current.getData() + "\t");
                }
                if (current != null) {
                    for (Node child : current.getChildren()) {
                        queue.offer(child);
                    }
                }
            }
            System.out.println();
        }
    }

    public void changeNodeData(int nodeId,String newData){
        this.ifChange=1;
        Node node = nodeMap.get(nodeId);
        node.setData(newData);
    }

    private void clearMindMap() {
        this.ifChange = 1;
        root = null;
        nodeMap.clear();
    }
    public void setTitle(String newTitle){
        this.title = newTitle;
    }

    public String getTitle(){
        return this.title;
    }

    public Node getRoot(){return root;}

    public int getMapId(){return this.mapId;}

    public void setIfChange(int newNum){this.ifChange=newNum;}
    public int getIfChange(){return this.ifChange;}

}