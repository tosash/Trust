package com.kido.Trust.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Node {
    private String ownerID = "";
    private String id;
    private String pid = "0"; //根节点的Pid=0;没有父节点
    private String name = "";
    private String description = "";
    private Date deadLine;
    private Date arhiveDate;
    private String userIdLastEdit = "";
    private Date lastEdit;
    private Boolean publicNode = false;
    private Boolean arhived = false;
    //树的层级
    private int level = 0;
    //当前 item的状态 是否展开
    private boolean Expand = false;
    private int icon = 0;
    private Node parent;
    private List<Node> children = new ArrayList<Node>();
    private List<String> usersID = new ArrayList<String>();

    public Node() {
        super();
    }


    public Node(String id, String pid, String name) {
        super();
        this.id = id;
        this.pid = pid;
        this.name = name;
    }

    public Node(String id, String pid, String name, List<String> users) {
        super();
        this.id = id;
        this.pid = pid;
        this.name = name;
        this.usersID = users;
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


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
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

    public boolean isExpand() {
        return Expand;
    }

    public void setExpand(boolean Expand) {
        this.Expand = Expand;
        if ((!Expand) && (children != null)) {
            for (Node node : children) {
                node.setExpand(false);
            }
        } else {

        }

    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Date getLastEdit() {
        return lastEdit;
    }

    public void setLastEdit(Date lastEdit) {
        this.lastEdit = lastEdit;
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

    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public Date getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(Date deadLine) {
        this.deadLine = deadLine;
    }

    public Date getArhiveDate() {
        return arhiveDate;
    }

    public void setArhiveDate(Date arhiveDate) {
        this.arhiveDate = arhiveDate;
    }

    public String getUserIdLastEdit() {
        return userIdLastEdit;
    }

    public void setUserIdLastEdit(String userIdLastEdit) {
        this.userIdLastEdit = userIdLastEdit;
    }

    public Boolean getPublicNode() {
        return publicNode;
    }

    public void setPublicNode(Boolean publicNode) {
        this.publicNode = publicNode;
    }

    public Boolean getArhived() {
        return arhived;
    }

    public void setArhived(Boolean arhived) {
        this.arhived = arhived;
    }


    public List<String> getUsersID() {
        return usersID;
    }

    public void setUsersID(List<String> usersID) {
        this.usersID = usersID;
    }

}
