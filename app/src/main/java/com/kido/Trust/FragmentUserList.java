package com.kido.Trust;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
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
import com.kido.Trust.bean.FileBean;
import com.kido.Trust.util.Node;
import com.kido.Trust.util.adapter.TreeListViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class FragmentUserList extends Fragment {

    private ListView mTree;
    private SimpleTreeListViewAdapter<FileBean> mAdapter;
    private List<FileBean> mDatas;

    private boolean mSearchCheck;
    private static final String TEXT_FRAGMENT = "TEXT_FRAGMENT";
    Context context;

    public FragmentUserList newInstance(String text) {
        FragmentUserList mFragment = new FragmentUserList();
        Bundle mBundle = new Bundle();
        mBundle.putString(TEXT_FRAGMENT, text);
        mFragment.setArguments(mBundle);
        return mFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        context = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View rootView = inflater.inflate(R.layout.fragment_userlist, container, false);

//        TextView mTxtTitle = (TextView) rootView.findViewById(R.id.txtTitle);
//        mTxtTitle.setText(getArguments().getString(TEXT_FRAGMENT));
        mTree = (ListView) rootView.findViewById(R.id.id_listview);

        initDatas();
        try {
            mAdapter = new SimpleTreeListViewAdapter<FileBean>(mTree, context, mDatas, 0);
            mTree.setAdapter(mAdapter);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        initEvent();
        rootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return rootView;
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
        mAdapter.setOnTreeNodeClickLister(new TreeListViewAdapter.OnTreeNodeClickLister() {

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
                        mAdapter.addExtraNode(position, editText.getText().toString(), editText.getText().toString());
                    }
                }).setNegativeButton("No", null).show();
                return true;
            }
        });
    }

    private void initDatas() {
        mDatas = new ArrayList<FileBean>();
        FileBean bean = new FileBean(1, 0, "Работа");
        mDatas.add(bean);
        bean = new FileBean(2, 0, "Друзья");
        mDatas.add(bean);
        bean = new FileBean(3, 0, "Стройка");
        mDatas.add(bean);
        bean = new FileBean(4, 1, "Секретарь Лена");
        mDatas.add(bean);
        bean = new FileBean(5, 1, "ТОВ Кидо");
        mDatas.add(bean);
        bean = new FileBean(6, 5, "Бухгалтерия");
        mDatas.add(bean);
        bean = new FileBean(7, 2, "Коля сосед");
        mDatas.add(bean);
        bean = new FileBean(8, 3, "Витя прораб");
        mDatas.add(bean);
        bean = new FileBean(9, 3, "Входные двери мастер");
        mDatas.add(bean);
        bean = new FileBean(10, 6, "Новикова Валентина Петровна");
        mDatas.add(bean);
    }

}
