package com.kido.Trust;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.kido.Trust.adapter.SimpleTreeListViewAdapter;
import com.kido.Trust.parse.ParseHelper;
import com.kido.Trust.util.Node;
import com.kido.Trust.util.adapter.TreeListViewAdapter;

import java.util.List;


public class FragmentTreeList extends Fragment {

    private ListView mTree;
    private SimpleTreeListViewAdapter<Node> nodeAdapter;
    private List<Node> nodeList = null;

    private boolean mSearchCheck;
    private static final String TEXT_FRAGMENT = "TEXT_FRAGMENT";
    private Context context;
    ProgressDialog mProgressDialog;
    ParseHelper parse;

    public FragmentTreeList newInstance(String text) {
        FragmentTreeList mFragment = new FragmentTreeList();
        Bundle mBundle = new Bundle();
        mBundle.putString(TEXT_FRAGMENT, text);
        mFragment.setArguments(mBundle);
        return mFragment;

    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case ParseHelper.GET_ALL_NODES:
                    showNodes((List<Node>) msg.obj);
                    mProgressDialog.dismiss();
                    break;
                case ParseHelper.ERROR_PARSE:

                    break;
                default:
                    break;
            }

            return false;
        }
    }
    );

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
        parse = new ParseHelper(context,handler);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_treelist, container, false);
        mTree = (ListView) rootView.findViewById(com.kido.Trust.R.id.id_listview);
        OpenProgressDialog();
        parse.getAllNodes();
        rootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return rootView;
    }

    public void OpenProgressDialog() {
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setTitle("Подождите...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(true);
        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                parse.setIsRunning(false);
            }
        });
        mProgressDialog.show();
    }

    public void showNodes(List<Node> nodes) {
        // Pass the results into ListViewAdapter.java
        try {
            nodeAdapter = new SimpleTreeListViewAdapter<Node>(mTree, context, nodes, 0);
            // Binds the Adapter to the ListView
            mTree.setAdapter(nodeAdapter);
            initEvent();
        } catch (IllegalAccessException e) {
            Log.d("Adapter", "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);

        //Select search item
        final MenuItem menuItem = menu.findItem(R.id.menu_search);
        menuItem.setVisible(true);

        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint(this.getString(R.string.search));

        ((EditText) searchView.findViewById(R.id.search_src_text))
                .setHintTextColor(getResources().getColor(R.color.nliveo_white));
        searchView.setOnQueryTextListener(onQuerySearchView);

        menu.findItem(R.id.menu_add).setVisible(true);

        mSearchCheck = false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub

        switch (item.getItemId()) {

            case R.id.menu_add:
                Toast.makeText(getActivity(), R.string.add, Toast.LENGTH_SHORT).show();
                break;

            case R.id.menu_search:
                mSearchCheck = true;
                Toast.makeText(getActivity(), R.string.search, Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    private SearchView.OnQueryTextListener onQuerySearchView = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String s) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            if (mSearchCheck) {
                // implement your search here
            }
            return false;
        }
    };


    private void initEvent() {
        nodeAdapter.setOnTreeNodeClickLister(new TreeListViewAdapter.OnTreeNodeClickLister() {

            @Override
            public void OnClick(Node node, int position) {
                if (node.isLeaf()) {
                    Toast.makeText(context, node.getName(), Toast.LENGTH_SHORT).show();
                }
            }
        });


        mTree.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           final int position, long id) {
                final EditText editText = new EditText(context);

                new AlertDialog.Builder(context).setTitle("Add node")
                        .setView(editText).setPositiveButton("Add", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (TextUtils.isEmpty(editText.getText().toString())) {
                            return;
                        }
                        nodeAdapter.addExtraNode(position, editText.getText().toString());
                    }
                }).setNegativeButton("No", null).show();
                return true;
            }
        });
    }

    public void addNewNode(int position,String name,String description){

        nodeAdapter.addExtraNode(position, name);
    }


}
