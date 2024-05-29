package core;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private String data;
    private Node parent;
    private List<Node> children;
    private int nodeId;
    private static int count = 0;

    public Node(String data) {
        this.data = data;
        this.nodeId = count++;
        this.children = new ArrayList<>();
    }

    public Node(String data, int nodeId){
        this.data = data;
        this.nodeId = nodeId;
        if(count<nodeId+1)
            count=nodeId+1;
        this.children = new ArrayList<>();
    }

    public void addChild(Node child) {
        child.setParent(this);
        children.add(child);
    }
    public void removeChild(Node child) {
        children.remove(child);
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public void setData(String newData){
        this.data = newData;
    }
    public String getData() {
        return data;
    }
    public int getNodeId(){ return nodeId; }
    public Node getParent() { return parent; }
    public List<Node> getChildren() { return children; }
    public static void setCount(int newCount){count = newCount;}
    public boolean getLeftChild() {
        if (children.size() > 0) {
            // 如果有子节点，返回第一个子节点
            return true;
        } else {
            // 没有左子节点
            return false;
        }
    }



}