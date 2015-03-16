//package com.kido.Trust.util.adapter;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.BaseAdapter;
//import android.widget.ListView;
//
//import com.kido.Trust.util.Node;
//import com.kido.Trust.util.TreeHelper;
//
//import java.util.List;
//
//public abstract class TreeListViewAdapterN<T> extends BaseAdapter {
//    protected Context mContext;
//    protected List<Node> mAllnodes;
//    protected List<Node> mVisibleNodes;
//    protected LayoutInflater mInflater;
//    protected ListView mTree;
//
//    /**
//     * @author Administrator
//     *         设置node的点击回调
//     */
//    public interface OnTreeNodeClickLister {
//        void OnClick(Node node, int position);
//    }
//
//    protected OnTreeNodeClickLister mLister;
//
//
//    public void setOnTreeNodeClickLister(OnTreeNodeClickLister mLister) {
//        this.mLister = mLister;
//    }
//
//    public TreeListViewAdapterN(ListView tree, Context context, List<T> datas, int defaultExpandLevel) throws IllegalArgumentException, IllegalAccessException {
//        mContext = context;
////        mAllnodes = TreeHelper.getSortedNodes(datas, defaultExpandLevel);
//        mAllnodes = (List<Node>) datas;
//        mVisibleNodes = TreeHelper.filterVisibleNode(mAllnodes);
//        mInflater = LayoutInflater.from(mContext);
//        mTree = tree;
//        mTree.setOnItemClickListener(new OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//
//                expandOrCollapse(position);
//                if (mLister != null) {
//                    mLister.OnClick(mVisibleNodes.get(position), position);
//                }
//            }
//        });
//
//    }
//
//    /**
//     * 点击收缩或展开
//     *
//     * @param position
//     */
//    private void expandOrCollapse(int position) {
//        Node n = mVisibleNodes.get(position);
//        if (n != null) {
//            if (n.isLeaf()) {
//                return;
//            } else {
//                n.setExpand(!n.isExpand());
//                mVisibleNodes = TreeHelper.filterVisibleNode(mAllnodes);
//                notifyDataSetChanged();
//            }
//        }
//    }
//
//
//    @Override
//    public int getCount() {
//        return mVisibleNodes.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return mVisibleNodes.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        //return mVisibleNodes.get(position).getId();
//        return position;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        Node node = mVisibleNodes.get(position);
//        convertView = getConverView(node, position, convertView, parent);
//
//        //设置缩进的宽度
//        convertView.setPadding(node.getLevel() * 30, 3, 3, 3);
//        return convertView;
//    }
//
//    public abstract View getConverView(Node node, int position, View convertView, ViewGroup parent);
//
//
//}
