package com.kido.Trust.parse;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.kido.Trust.util.Node;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by A on 15.03.2015.
 */
public class ParseHelper {

    public static final int ERROR_PARSE = 0;
    public static final int GET_ALL_NODES = 1;
    public static final int ADD_NEW_NODE = 2;
    public static final int GET_ONE_NODE = 3;
    public static final int EDIT_NODE = 4;
    public static final int DELETE_NODE = 5;


    private Boolean isRunning = false;
    private Context context;
    private Handler handler;

    public ParseHelper(Context context, Handler handler) {
        super();
        this.context = context;
        this.handler = handler;
    }

    public void initParse() {
        // Add your initialization code here
        Parse.initialize(context, "0HX9UqA5qO2EJb3sDMhaBEpq1zqjb0FYq7iC5FnS", "0DN694vFCXSpD0LGjFugycNTZjj9cYjzoIRbNCLQ");

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();

        // If you would like all objects to be private by default, remove this
        // line.
        defaultACL.setPublicReadAccess(true);

        ParseACL.setDefaultACL(defaultACL, true);
    }

    public void getAllNodes() {
        setIsRunning(true);
        final List<Node> nodeList = new ArrayList<Node>();
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Node");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> ob, ParseException e) {
                if (e == null && getIsRunning()) {
                    Log.d("getAllNodes", "Retrieved " + ob.size() + " nodes");
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
                        ArrayList<Node> temples = (ArrayList<Node>) node.get("children");
                        map.setChildren(temples == null ? new ArrayList<Node>() : temples);
                        ArrayList<String> tempos = (ArrayList<String>) node.get("usersID");
                        map.setUsersID(tempos == null ? new ArrayList<String>() : tempos);
                        map.setUserIdLastEdit(node.getString("userIdLastEdit"));
                        map.setLastEdit(node.getUpdatedAt());
                        map.setArhived(node.getBoolean("arhived"));
                        map.setArhiveDate(node.getDate("arhiveDate"));
                        map.setDeadLine(node.getDate("deadLine"));
                        map.setPublicNode(node.getBoolean("publicNode"));
                        map.setLevel(node.getInt("level"));
                        map.setExpand(node.getBoolean("expand"));
                        nodeList.add(map);
                    }
                    Message msg = new Message();
                    msg.what = GET_ALL_NODES;
                    msg.obj = nodeList;
                    handler.sendMessage(msg);
                } else {
                    Message msg = new Message();
                    msg.what = ERROR_PARSE;
                    msg.obj = e;
                    handler.sendMessage(msg);
                    Log.d("getAllNode", "Error: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }


    public void addNewNode(Node newNode) {
        setIsRunning(true);
        final ParseObject node = new ParseObject("Node");
        node.put("ownerID", newNode.getOwnerID());
        node.put("nid", newNode.getId());
        node.put("pid", newNode.getPid());
        node.put("name", newNode.getName());
        node.put("description", newNode.getDescription());

//        ParseObject object = new ParseObject();
//        node.put("parent",(Node) newNode.getParent()
        node.put("children", newNode.getChildren());
        node.put("usersID", newNode.getUsersID());
        node.put("userIdLastEdit", newNode.getUserIdLastEdit());
        node.put("arhived", newNode.getArhived());
//        node.put("arhiveDate",newNode.getArhiveDate());
//        node.put("deadLine", newNode.getDeadLine());
        node.put("publicNode", newNode.getPublicNode());
        node.put("level", newNode.getLevel());
        node.put("expand", newNode.isExpand());
        node.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null && getIsRunning()) {
                    Log.d("AddNewNode", "added node " + node.getObjectId());
                    Message msg = new Message();
                    msg.what = ADD_NEW_NODE;
                    msg.obj = node.getObjectId();
                    handler.sendMessage(msg);
                } else {
                    Message msg = new Message();
                    msg.what = ERROR_PARSE;
                    msg.obj = e;
                    handler.sendMessage(msg);
                    Log.d("addNewNode", "Error: " + e.getMessage());
                    e.printStackTrace();
                }

            }
        });
    }

    public static void updateNode(final Node newNode) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Node");
// Retrieve the object by id
        query.getInBackground(newNode.getObjectID(), new GetCallback<ParseObject>() {
            public void done(ParseObject node, ParseException e) {
                if (e == null) {
                    node.put("nid", newNode.getId());
                    node.put("pid", newNode.getPid());
//                    node.put("parent",newNode.getParent());
                    ArrayList<Node> childrens = new ArrayList<Node>();
                    childrens = (ArrayList<Node>) newNode.getChildren();
                    node.put("children", newNode.getChildren());
                    node.put("userIdLastEdit", newNode.getUserIdLastEdit());
                    node.put("level", newNode.getLevel());
                    node.put("expand", newNode.isExpand());
                    node.saveInBackground();
                }
            }
        });
    }

    public Boolean getIsRunning() {
        return isRunning;
    }

    public void setIsRunning(Boolean isRunning) {
        this.isRunning = isRunning;
    }

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }
}
