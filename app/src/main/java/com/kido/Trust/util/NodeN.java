//package com.kido.Trust.util;
//
//import java.util.ArrayList;
//import java.util.Date;
//
//public class NodeN {
//    private String objectID="";
//    private String ownerID="";
//    private Integer id;
//    private Integer pid = 0; //根节点的Pid=0;没有父节点
//    private String name="";
//    private String description="";
//    private Date deadLine;
//    private Date arhiveDate;
//    private String userIdLastEdit="";
//    private Date lastEdit;
//    private Boolean publicNode=false;
//    private Boolean arhived=false;
//    //树的层级
//    private int level=0;
//    //当前 item的状态 是否展开
//    private boolean Expand = false;
//    private int icon=0;
//    private NodeN parent;
//    private ArrayList<NodeN> children = new ArrayList<NodeN>();
//    private ArrayList<String> usersID = new ArrayList<String>();
//
//    public NodeN() {
//        super();
//    }
//
//
//    public NodeN(int id, int pid, String name) {
//        super();
//        this.id = id;
//        this.pid = pid;
//        this.name = name;
//    }
//
//    public NodeN(int id, int pid, String name, String description) {
//        super();
//        this.id = id;
//        this.pid = pid;
//        this.name = name;
//        this.description = description;
//    }
//
//
//    /**
//     * 判断当前是否是根节点
//     *
//     * @return
//     */
//    public boolean isRoot() {
//        return parent == null;
//    }
//
//
//    /**
//     * 判断父节点是否展开
//     *
//     * @return
//     */
//    public boolean isParentExpand() {
//        if (parent == null) {
//            return false;
//        } else {
//            return parent.isExpand();
//        }
//    }
//
//    /**
//     * 是否是叶子节点
//     *
//     * @return
//     */
//    public boolean isLeaf() {
//
//        return children.size() == 0;
//    }
//
//
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public int getPid() {
//        return pid;
//    }
//
//    public void setPid(int pid) {
//        this.pid = pid;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    /**
//     * 计算当前节点的层级
//     *
//     * @return
//     */
//    public int getLevel() {
//
//        return parent == null ? 0 : parent.getLevel() + 1;
//    }
//
////    public void setLevel(int level) {
////        this.level = level;
////    }
//
//    public boolean isExpand() {
//        return Expand;
//    }
//
//    public void setExpand(boolean Expand) {
//        this.Expand = Expand;
//        if ((!Expand)&&(children!=null)) {
//            for (NodeN node : children) {
//                node.setExpand(false);
//            }
//        } else {
//
//        }
//
//    }
//
//    public void setLevel(int level) {
//        this.level = level;
//    }
//
//    public Date getLastEdit() {
//        return lastEdit;
//    }
//
//    public void setLastEdit(Date lastEdit) {
//        this.lastEdit = lastEdit;
//    }
//
//    public int getIcon() {
//        return icon;
//    }
//
//    public void setIcon(int icon) {
//        this.icon = icon;
//    }
//
//    public NodeN getParent() {
//        return parent;
//    }
//
//    public void setParent(NodeN parent) {
//        this.parent = parent;
//    }
//
//    public ArrayList<NodeN> getChildren() {
//        return children;
//    }
//
//    public void setChildren(ArrayList<NodeN> children) {
//        this.children = children;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public String getOwnerID() {
//        return ownerID;
//    }
//
//    public void setOwnerID(String ownerID) {
//        this.ownerID = ownerID;
//    }
//
//    public void setId(Integer id) {
//        this.id = id;
//    }
//
//    public void setPid(Integer pid) {
//        this.pid = pid;
//    }
//
//    public Date getDeadLine() {
//        return deadLine;
//    }
//
//    public void setDeadLine(Date deadLine) {
//        this.deadLine = deadLine;
//    }
//
//    public Date getArhiveDate() {
//        return arhiveDate;
//    }
//
//    public void setArhiveDate(Date arhiveDate) {
//        this.arhiveDate = arhiveDate;
//    }
//
//    public String getUserIdLastEdit() {
//        return userIdLastEdit;
//    }
//
//    public void setUserIdLastEdit(String userIdLastEdit) {
//        this.userIdLastEdit = userIdLastEdit;
//    }
//
//    public Boolean getPublicNode() {
//        return publicNode;
//    }
//
//    public void setPublicNode(Boolean publicNode) {
//        this.publicNode = publicNode;
//    }
//
//    public Boolean getArhived() {
//        return arhived;
//    }
//
//    public void setArhived(Boolean arhived) {
//        this.arhived = arhived;
//    }
//
//    public String getObjectID() {
//        return objectID;
//    }
//
//    public void setObjectID(String objectID) {
//        this.objectID = objectID;
//    }
//
//    public ArrayList<String> getUsersID() {
//        return usersID;
//    }
//
//    public void setUsersID(ArrayList<String> usersID) {
//        this.usersID = usersID;
//    }
//
//}