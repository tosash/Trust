package com.kido.Trust.parse;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.kido.Trust.R;
import com.kido.Trust.util.Node;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONObject;

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

    private Handler handlerParse = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case GET_ONE_NODE:

                    break;
                case ERROR_PARSE:

                    break;
                default:
                    break;
            }
            return false;
        }
    }
    );

    public ParseHelper(Context context, Handler handler) {
        super();
        this.context = context;
        this.handler = handler;
    }

    public void initParse(Intent intent) {
        Parse.initialize(context, context.getString(R.string.app_id), context.getString(R.string.client_key));
        ParseACL.setDefaultACL(new ParseACL(), true);
        ParseAnalytics.trackAppOpened(intent);
    }

    public static boolean isUserLoggedIn() {
        return ParseUser.getCurrentUser() != null;
    }

    public Node parseToNode(ParseObject obj) {
        Node map = new Node();
        map.setOwnerID(obj.getString("ownerID"));
        map.setId(obj.getObjectId());
        map.setPid(obj.getString("pid"));
        map.setName(obj.getString("name"));
        map.setDescription(obj.getString("description"));
//        map.setParent(null);
//        map.setChildren(null);
//        List<String> tempos = (List<String>) obj.get("usersID");
//        map.setUsersID(tempos == null ? new ArrayList<String>() : tempos);
        map.setUserIdLastEdit(obj.getString("userIdLastEdit"));
        map.setLastEdit(obj.getUpdatedAt());
        map.setArhived(obj.getBoolean("arhived"));
        map.setArhiveDate(obj.getDate("arhiveDate"));
        map.setDeadLine(obj.getDate("deadLine"));
        map.setPublicNode(obj.getBoolean("publicNode"));
        map.setLevel(obj.getInt("level"));
        map.setExpand(obj.getBoolean("expand"));
        return map;
    }

    public List<Node> parseToNodeS(List<ParseObject> ob) {
        List<Node> nodeList = new ArrayList<Node>();
        for (ParseObject node : ob) {
            nodeList.add(parseToNode(node));
        }
        return nodeList;
    }


    public void getNodeById(String nID) {
        setIsRunning(true);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Node");
        query.getInBackground(nID, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject ob, ParseException e) {
                if (e == null && getIsRunning()) {
                    Log.d("getNodeById", "Retrieved " + ob.getObjectId());
                    Message msg = new Message();
                    msg.what = GET_ALL_NODES;
                    msg.obj = parseToNode(ob);
                    handler.sendMessage(msg);
                } else {
                    Message msg = new Message();
                    msg.what = ERROR_PARSE;
                    msg.obj = e;
                    handler.sendMessage(msg);
                    Log.d("getNodeById", "Error: " + (e != null ? e.getMessage() : null));
                    if (e != null) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    public void getAllNodes() {
        setIsRunning(true);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Node");
        query.whereEqualTo("usersId", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> ob, ParseException e) {
                if (e == null && getIsRunning()) {
                    Log.d("getAllNodes", "Retrieved " + ob.size() + " nodes");
                    Message msg = new Message();
                    msg.what = GET_ALL_NODES;
                    msg.obj = parseToNodeS(ob);//
                    handler.sendMessage(msg);
                } else {
                    Message msg = new Message();
                    msg.what = ERROR_PARSE;
                    msg.obj = e;
                    handler.sendMessage(msg);
                    Log.d("getAllNode", "Error: " + (e != null ? e.getMessage() : null));
                    if (e != null) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    public void addNewNode(Node newNode, final int position) {
        final ParseObject node = new ParseObject("Node");

        ParseUser user = ParseUser.getCurrentUser();
        ParseRelation relation = node.getRelation("usersId");
        relation.add(user);

        node.put("ownerID", user.getObjectId());
        node.put("pid", newNode.getPid());
        node.put("name", newNode.getName());
        node.put("description", newNode.getDescription());
        node.put("userIdLastEdit", user.getObjectId());
        node.put("arhived", newNode.getArhived());
        node.put("arhiveDate", (newNode.getArhiveDate()==null? JSONObject.NULL:newNode.getArhiveDate()));
        node.put("deadLine", (newNode.getDeadLine()==null? JSONObject.NULL:newNode.getDeadLine()));
        node.put("publicNode", newNode.getPublicNode());
        node.put("level", newNode.getLevel());
        node.put("expand", newNode.isExpand());
        node.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("AddNewNode", "added node " + node.getObjectId());
                    Message msg = new Message();
                    msg.what = ADD_NEW_NODE;
                    msg.arg1=position;
                    msg.obj = parseToNode(node);
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

    public  void updateNode(final Node newNode, final int position) {
        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Node");
        query.getInBackground(newNode.getId(), new GetCallback<ParseObject>() {
            public void done(final ParseObject node, ParseException e) {
                if (e == null) {
                    node.put("ownerID", newNode.getOwnerID());
                    node.put("pid", newNode.getPid());
                    node.put("name", newNode.getName());
                    node.put("description", newNode.getDescription());
                    node.put("userIdLastEdit", ParseUser.getCurrentUser().getObjectId());
                    node.put("arhived", newNode.getArhived());
                    node.put("arhiveDate", (newNode.getArhiveDate()==null? JSONObject.NULL:newNode.getArhiveDate()));
                    node.put("deadLine", (newNode.getDeadLine()==null? JSONObject.NULL:newNode.getDeadLine()));
                    node.put("publicNode", newNode.getPublicNode());
                    node.put("level", newNode.getLevel());
                    node.put("expand", newNode.isExpand());
                    node.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Log.d("EditNode", "edit node " + newNode.getId());
                                Message msg = new Message();
                                msg.what = EDIT_NODE;
                                msg.arg1=position;
                                msg.obj = parseToNode(node);
                                handler.sendMessage(msg);
                            } else {
                                Message msg = new Message();
                                msg.what = ERROR_PARSE;
                                msg.obj = e;
                                handler.sendMessage(msg);
                                Log.d("EditNode", "Error: " + e.getMessage());
                                e.printStackTrace();
                            }

                        }
                    });
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
