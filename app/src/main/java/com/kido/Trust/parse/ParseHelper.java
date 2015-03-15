package com.kido.Trust.parse;

import android.content.Context;
import android.util.Log;

import com.kido.Trust.FragmentTreeList;
import com.kido.Trust.util.Node;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by A on 15.03.2015.
 */
public abstract class ParseHelper {

    public ParseHelper() {
        super();
    }

    public static void initParse(Context context) {
        // Add your initialization code here
        Parse.initialize(context, "0HX9UqA5qO2EJb3sDMhaBEpq1zqjb0FYq7iC5FnS", "0DN694vFCXSpD0LGjFugycNTZjj9cYjzoIRbNCLQ");

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();

        // If you would like all objects to be private by default, remove this
        // line.
        defaultACL.setPublicReadAccess(true);

        ParseACL.setDefaultACL(defaultACL, true);
    }

    public static void getAllNodes(final FragmentTreeList context) {
        // Create the array
        final List<Node> nodeList = new ArrayList<Node>();
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Node");
        // Locate the column named "ranknum" in Parse.com and order list
        // by ascending
//                query.orderByAscending("ranknum");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> ob, ParseException e) {
                if (e == null) {
                    Log.d("Node", "Retrieved " + ob.size() + " nodes");
                    for (ParseObject node : ob) {
                        // Locate images in flag column
                        Node map = new Node();
                        map.setObjectID(node.getObjectId());
                        map.setOwnerID(node.getString("ownerID"));
                        map.setId(node.getInt("nid"));
                        map.setPid(node.getInt("pid"));
                        map.setName(node.getString("name"));
                        map.setDescription(node.getString("description"));
                        map.setParent((Node) node.get("parent"));
                        List<Node> temples = (List<Node>) node.get("children");
                        map.setChildren(temples==null?new ArrayList<Node>():temples );
                        List<String> tempos =(List<String>) node.get("usersID");
                        map.setUsersID(tempos ==null?new ArrayList<String>(): tempos);
                        map.setUserIdLastEdit(node.getString("userIdLastEdit"));
                        map.setLastEdit(node.getUpdatedAt());
                        map.setArhived(node.getBoolean("arhived"));
                        map.setArhiveDate(node.getDate("arhiveDate"));
                        map.setDeadLine(node.getDate("deadLine"));
                        map.setPublicNode(node.getBoolean("publicNode"));
                        map.setLevel(node.getInt("level"));
                        map.setExpand(node.getBoolean ("expand"));
                        nodeList.add(map);
                        context.showNodes(nodeList);
                    }
                } else {
                    Log.d("Node", "Error: " + e.getMessage());
                    e.printStackTrace();
                }
            }

        });
    }



    public static void addNewNode(Node newNode) {
        ParseObject node = new ParseObject("Node");
        node.put("ownerID",newNode.getOwnerID());
        node.put("nid",newNode.getId());
        node.put("pid",newNode.getPid());
        node.put("name",newNode.getName());
        node.put("description",newNode.getDescription());
        node.put("parent",(Node) newNode.getParent());
        node.put("children",newNode.getChildren());
        node.put("usersID", newNode.getUsersID());
        node.put("userIdLastEdit",newNode.getUserIdLastEdit());
        node.put("arhived",newNode.getArhived());
        node.put("arhiveDate",newNode.getArhiveDate());
        node.put("deadLine", newNode.getDeadLine());
        node.put("publicNode",newNode.getPublicNode());
        node.put("level",newNode.getLevel());
        node.put("expand", newNode.isExpand());
        node.saveInBackground();
    }

    public static void updateNode(final Node newNode){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Node");
// Retrieve the object by id
        query.getInBackground(newNode.getObjectID(), new GetCallback<ParseObject>() {
            public void done(ParseObject node, ParseException e) {
                if (e == null) {
                    node.put("nid",newNode.getId());
                    node.put("pid",newNode.getPid());
                    node.put("parent",newNode.getParent());
                    node.put("children",newNode.getChildren());
                    node.put("userIdLastEdit",newNode.getUserIdLastEdit());
                    node.put("level",newNode.getLevel());
                    node.put("expand", newNode.isExpand());
                    node.saveInBackground();
                }
            }
        });
    }
}
