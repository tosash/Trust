package com.kido.Trust.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.kido.Trust.R;
import com.kido.Trust.util.Node;
import com.kido.Trust.util.TreeHelper;
import com.kido.Trust.util.adapter.TreeListViewAdapter;

import java.util.List;

public class SimpleTreeListViewAdapter<T> extends TreeListViewAdapter<T> {

    public SimpleTreeListViewAdapter(ListView tree, Context context,
                                     List<T> datas, int defaultExpandLevel)
            throws IllegalArgumentException, IllegalAccessException {
        super(tree, context, datas, defaultExpandLevel);
    }

    @Override
    public View getConverView(Node node, int position, View convertView,
                              ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item, parent, false);
            holder = new ViewHolder();
            holder.mIcon = (ImageView) convertView.findViewById(R.id.id_item_icon);
            holder.mText = (TextView) convertView.findViewById(R.id.id_item_text);
            holder.mDesc = (TextView) convertView.findViewById(R.id.id_item_decs);

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

    private class ViewHolder {
        ImageView mIcon;
        TextView mText;
        TextView mDesc;
    }

    /**
     * 动态插入节点
     *
     * @param position
     * @param text
     */
    public void addExtraNode(int position, String text, String desc) {
        Node node = mVisibleNodes.get(position);
        int indexOf = mAllnodes.indexOf(node);
        Log.e("TAG", "--" + indexOf);
        //Node extraNode = new Node(-1, node.getId(), text);
        Node extraNode = new Node(mAllnodes.size() + 1, node.getId(), text, desc);
        extraNode.setParent(node);
        node.getChildren().add(extraNode);
        mAllnodes.add(indexOf + 1, extraNode);
        mVisibleNodes = TreeHelper.filterVisibleNode(mAllnodes);
        notifyDataSetChanged();
    }

}
