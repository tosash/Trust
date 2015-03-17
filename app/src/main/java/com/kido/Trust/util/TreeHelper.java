package com.kido.Trust.util;

import android.util.Log;

import com.kido.Trust.R;

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

    public static List<Node> convertDat2Nodes(List<Node> data) {
        for (int i = 0; i < data.size(); i++) {
            Node n = data.get(i);
            for (int j = i + 1; j < data.size(); j++) {
                Node m = data.get(j);

                if (m.getPid().equals(n.getId())) {
                    n.getChildren().add(m);
                    m.setParent(n);
                } else if (m.getId().equals(n.getPid())) {
                    m.getChildren().add(n);
                    n.setParent(m);
                }
            }
        }

        for (Node n : data) {
            setNodeIcon(n);
        }
        Log.e("TAG", data.size() + "");
        return data;
    }

    /**
     * @param datas
     * @return
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    public static <T> List<Node> getSortedNodes(List<Node> datas, int defaultExpandLevel) {
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
