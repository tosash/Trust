package com.kido.Trust.util;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private Integer id;
    private Integer pid = 0; //根节点的Pid=0;没有父节点
    private String name;
    private String description;
    //树的层级
    private int level;
    //当前 item的状态 是否展开
    private boolean isExpand = false;

    private int icon;

    private Node parent;
    private List<Node> children = new ArrayList<Node>();


    public Node() {
        super();
    }


    public Node(int id, int pid, String name, String description) {
        super();
        this.id = id;
        this.pid = pid;
        this.name = name;
        this.description = description;
    }


    /**
     * 判断当前是否是根节点
     *
     * @return
     */
    public boolean isRoot() {
        return parent == null;
    }


    /**
     * 判断父节点是否展开
     *
     * @return
     */
    public boolean isParentExpand() {
        if (parent == null) {
            return false;
        } else {
            return parent.isExpand();
        }
    }

    /**
     * 是否是叶子节点
     *
     * @return
     */
    public boolean isLeaf() {

        return children.size() == 0;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 计算当前节点的层级
     *
     * @return
     */
    public int getLevel() {

        return parent == null ? 0 : parent.getLevel() + 1;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean isExpand) {
        this.isExpand = isExpand;
        if (!isExpand) {
            for (Node node : children) {
                node.setExpand(false);
            }
        } else {

        }

    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public List<Node> getChildren() {
        return children;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
