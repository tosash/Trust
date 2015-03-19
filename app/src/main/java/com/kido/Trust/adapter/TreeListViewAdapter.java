package com.kido.Trust.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kido.Trust.R;
import com.kido.Trust.parse.ParseHelper;
import com.kido.Trust.util.Node;
import com.kido.Trust.util.TreeHelper;
import com.parse.ParseException;

import java.util.List;

public class TreeListViewAdapter<T> extends BaseAdapter {
    protected Context mContext;
    protected List<Node> mAllnodes;
    protected List<Node> mVisibleNodes;
    protected LayoutInflater mInflater;
    protected ListView mTree;
    protected int currentNode;

    /**
     * @author Administrator
     *         设置node的点击回调
     */
    public interface OnTreeNodeClickLister {
        void OnClick(Node node, int position);
    }

    protected OnTreeNodeClickLister mLister;


    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case ParseHelper.ADD_NEW_NODE:
                    Node node = (Node) msg.obj;
                    if (msg.arg1 >= 0) {
                        Node nodeParent = findNodeByPid(mAllnodes, node.getPid());
                        node.setParent(nodeParent);
                        nodeParent.getChildren().add(node);
                        mAllnodes.add(msg.arg1 + 1, node);
                    } else {
                        mAllnodes.add(0, node);
                    }
                    mVisibleNodes = TreeHelper.filterVisibleNode(mAllnodes);
                    notifyDataSetChanged();

                    break;
                case ParseHelper.ERROR_PARSE:
                    Toast.makeText(mContext, ((ParseException) msg.obj).getMessage(), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            return false;
        }
    }
    );


    public void setOnTreeNodeClickLister(OnTreeNodeClickLister mLister) {
        this.mLister = mLister;
    }

    public TreeListViewAdapter(ListView tree, Context context, List<Node> datas, int defaultExpandLevel) {
        mContext = context;
        mAllnodes = TreeHelper.getSortedNodes(datas, defaultExpandLevel);
        mVisibleNodes = TreeHelper.filterVisibleNode(mAllnodes);
        mInflater = LayoutInflater.from(mContext);
        mTree = tree;
        mTree.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                currentNode = position;
                expandOrCollapse(position);
                if (mLister != null) {
                    mLister.OnClick(mVisibleNodes.get(position), position);
                }
            }
        });

    }

    /**
     * 点击收缩或展开
     *
     * @param position
     */
    private void expandOrCollapse(int position) {
        Node n = mVisibleNodes.get(position);
        if (n != null) {
            if (n.isLeaf()) {
                notifyDataSetChanged();
//                return;
            } else {
                n.setExpand(!n.isExpand());
                mVisibleNodes = TreeHelper.filterVisibleNode(mAllnodes);
                notifyDataSetChanged();
            }
        }
    }


    @Override
    public int getCount() {
        return mVisibleNodes.size();
    }

    @Override
    public Object getItem(int position) {
        return mVisibleNodes.get(position);
    }

    @Override
    public long getItemId(int position) {
        //return mVisibleNodes.get(position).getId();
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Node node = mVisibleNodes.get(position);
        convertView = getConverView(node, position, convertView, parent);

        //设置缩进的宽度
        convertView.setPadding(node.getLevel() * 30, 3, 3, 3);
        return convertView;
    }

    public View getConverView(Node node, int position, View convertView,
                              ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.treelist_item, parent, false);
            holder = new ViewHolder();
            holder.mIcon = (ImageView) convertView.findViewById(R.id.id_item_icon);
            holder.mText = (TextView) convertView.findViewById(R.id.id_item_text);
            holder.mDesc = (TextView) convertView.findViewById(R.id.id_item_decs);
            holder.mLayout = (RelativeLayout) convertView.findViewById(R.id.id_layout);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (node.getIcon() == -1) {
            holder.mIcon.setVisibility(View.INVISIBLE);
        } else {
            holder.mIcon.setVisibility(View.VISIBLE);
            holder.mIcon.setImageResource(node.getIcon());
        }

        if (position == currentNode) {

            holder.mLayout.setBackgroundColor(mContext.getResources().getColor(R.color.current_item_color));
        } else {
            holder.mLayout.setBackgroundColor(mContext.getResources().getColor(R.color.default_item_color));
        }

        holder.mText.setText(node.getName());
        holder.mText.setTextSize(18 - node.getLevel() * 2);
        if (node.getChildren().size() > 0) {
            holder.mText.setTypeface(Typeface.DEFAULT_BOLD);

        } else {
            holder.mText.setTypeface(Typeface.DEFAULT);
        }

        if (node.getDescription() != null) {
            holder.mDesc.setVisibility(View.VISIBLE);
            holder.mDesc.setText(node.getDescription());
        } else {
            holder.mDesc.setVisibility(View.GONE);
        }

        return convertView;
    }

    public void addExtraNode(int position, String text) {
        ParseHelper parse = new ParseHelper(mContext, handler);
        Node node = mVisibleNodes.get(position);
        int indexOf = mAllnodes.indexOf(node);
        Log.e("TAG", "--" + indexOf);
        Node extraNode = new Node("", node.getId(), text);
        parse.addNewNode(extraNode, indexOf);
    }

    public void addRootNode(String text) {
        ParseHelper parse = new ParseHelper(mContext, handler);
        Node extraNode = new Node("", "0", text);
        parse.addNewNode(extraNode, -1);
    }

    public Node findNodeByPid(List<Node> list, String pid) {
        for (Node node : list) {
            if (node.getId().equals(pid)) {
                return node;
            }
        }
        return null;
    }

    private class ViewHolder {
        ImageView mIcon;
        TextView mText;
        TextView mDesc;
        RelativeLayout mLayout;
    }


}
