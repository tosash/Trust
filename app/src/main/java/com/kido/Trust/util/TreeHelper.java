package com.kido.Trust.util;

import android.util.Log;

import com.kido.Trust.R;
import com.kido.Trust.utilannotation.TreeNodeDescription;
import com.kido.Trust.utilannotation.TreeNodeLabel;
import com.kido.Trust.utilannotation.TreeNodePid;
import com.kido.Trust.utilannotation.TreeNodeid;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class TreeHelper {


    /**
     * 将用户的数据转化为树形数据
     *
     * @param data
     * @return
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */

    public static <T> List<Node> convertDat2Nodes(List<T> data) throws IllegalArgumentException, IllegalAccessException {
        List<Node> nodes = new ArrayList<Node>();
        Node node = null;
        for (T t : data) {
            int id = -1;
            int pid = -1;
            String label = null;
            String desc = null;
            node = new Node();
            Class clazz = t.getClass();
            Field[] filds = clazz.getDeclaredFields();
            for (Field field : filds) {
                //判断注解
                if (field.getAnnotation(TreeNodeid.class) != null) {
                    field.setAccessible(true);
                    id = field.getInt(t);
                }
                if (field.getAnnotation(TreeNodePid.class) != null) {
                    field.setAccessible(true);
                    pid = field.getInt(t);
                }
                if (field.getAnnotation(TreeNodeLabel.class) != null) {
                    field.setAccessible(true);
                    label = (String) field.get(t);
                }
                if (field.getAnnotation(TreeNodeDescription.class) != null) {
                    field.setAccessible(true);
                    desc = (String) field.get(t);
                }
            }
            node = new Node(id, pid, label);
            nodes.add(node);
        }
        /**
         * 设置node间的节点关系
         */
        for (int i = 0; i < nodes.size(); i++) {
            Node n = nodes.get(i);
            for (int j = i + 1; j < nodes.size(); j++) {
                Node m = nodes.get(j);
                if (m.getPid() == n.getId()) {
                    n.getChildren().add(m);
                    m.setParent(n);
                } else if (m.getId() == n.getPid()) {
                    m.getChildren().add(n);
                    n.setParent(m);
                }
            }
        }

        for (Node n : nodes) {
            setNodeIcon(n);
        }
        Log.e("TAG", nodes.size() + "");
        return nodes;
    }

    /**
     * @param datas
     * @return
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    public static <T> List<Node> getSortedNodes(List<T> datas, int defaultExpandLevel) throws IllegalArgumentException, IllegalAccessException {
        List<Node> result = new ArrayList<Node>();
        List<Node> nodes = convertDat2Nodes(datas);
        //获取树的根节点
        List<Node> rootNodes = getRootNodes(nodes);
        for (Node node : rootNodes) {
            addNode(result, node, defaultExpandLevel, 1);
        }
        Log.e("TAG", result.size() + "");
        return result;
    }


    /**
     * 把一个节点的所有节点放入result
     *
     * @param result
     * @param node
     * @param defaultExpandLevel 设置默认展开的层级数
     *                           // * @param iLevel
     */
    private static void addNode(List<Node> result, Node node,
                                int defaultExpandLevel, int currentLevel) {
        result.add(node);
        if (defaultExpandLevel >= currentLevel) {
            node.setExpand(true);
        }

        if (node.isLeaf()) {
            return;
        }

        for (int i = 0; i < node.getChildren().size(); i++) {
            addNode(result, node.getChildren().get(i), defaultExpandLevel, currentLevel + 1);
        }


    }


    /**
     * 过滤可见的节点
     *
     * @param nodes
     * @return
     */
    public static List<Node> filterVisibleNode(List<Node> nodes) {
        List<Node> resutl = new ArrayList<Node>();

        for (Node node : nodes) {
            if (node.isRoot() || node.isParentExpand()) {
                setNodeIcon(node);
                resutl.add(node);
            }

        }

        return resutl;
    }


    /**
     * 从所有节点中取出根节点
     *
     * @param nodes
     * @return
     */
    private static List<Node> getRootNodes(List<Node> nodes) {
        List<Node> root = new ArrayList<Node>();
        for (Node node : nodes) {
            if (node.isRoot()) {
                root.add(node);
            }
        }
        return root;
    }

    /**
     * 为node设置图标
     *
     * @param n
     */
    private static void setNodeIcon(Node n) {
        if (n.getChildren().size() > 0 && n.isExpand()) {
            n.setIcon(R.drawable.down);
        } else if (n.getChildren().size() > 0 && !n.isExpand()) {
            n.setIcon(R.drawable.right);
        } else {
            n.setIcon(-1);
        }

    }
}	
