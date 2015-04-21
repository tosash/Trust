package com.kido.Trust.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ListView;

import com.kido.Trust.R;
import com.kido.Trust.adapter.TreeListViewAdapter;
import com.kido.Trust.util.Node;

import java.util.List;

import yalantis.com.sidemenu.interfaces.ScreenShotable;

public class FragmentUserList extends Fragment implements ScreenShotable {

    private ListView mTree;
    //    private SimpleTreeListViewAdapter<Node> mAdapter;
    private TreeListViewAdapter<Node> mAdapter;
    private List<Node> mDatas;

    private boolean mSearchCheck;
    private static final String TEXT_FRAGMENT = "TEXT_FRAGMENT";
    Context context;
    private View containerView;
    private Bitmap bitmap;

    public static FragmentUserList newInstance(String text) {
        FragmentUserList mFragment = new FragmentUserList();
        Bundle mBundle = new Bundle();
        mBundle.putString(TEXT_FRAGMENT, text);
        mFragment.setArguments(mBundle);
        return mFragment;
    }

    @Override
    public void takeScreenShot() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                Bitmap bitmap = Bitmap.createBitmap(containerView.getWidth(),
                        containerView.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                containerView.draw(canvas);
                FragmentUserList.this.bitmap = bitmap;
            }
        };

        thread.start();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.containerView = view.findViewById(R.id.container);

    }

    @Override
    public Bitmap getBitmap() {
        return null;
    }
}

//    @Override
//    public void onAttach(Activity activity) {
//        // TODO Auto-generated method stub
//        super.onAttach(activity);
//        context = activity;
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // TODO Auto-generated method stub
//        View rootView = inflater.inflate(R.layout.fragment_userlist, container, false);
//
////        TextView mTxtTitle = (TextView) rootView.findViewById(R.id.txtTitle);
////        mTxtTitle.setText(getArguments().getString(TEXT_FRAGMENT));
//        mTree = (ListView) rootView.findViewById(R.id.id_listview);
//
//        try {
//            mAdapter = new SimpleTreeListViewAdapter<Node>(mTree, context, mDatas, 0);
//            mTree.setAdapter(mAdapter);
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//        initEvent();
//        rootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        return rootView;
//    }
//
//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        // TODO Auto-generated method stub
//        super.onActivityCreated(savedInstanceState);
//        setHasOptionsMenu(true);
//    }
//
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        // TODO Auto-generated method stub
//        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.menu, menu);
//
//        //Select search item
//        final MenuItem menuItem = menu.findItem(R.id.menu_search);
//        menuItem.setVisible(true);
//
//        SearchView searchView = (SearchView) menuItem.getActionView();
//        searchView.setQueryHint(this.getString(R.string.search));
//
//        ((EditText) searchView.findViewById(R.id.search_src_text))
//                .setHintTextColor(getResources().getColor(R.color.nliveo_white));
//        searchView.setOnQueryTextListener(onQuerySearchView);
//
//        menu.findItem(R.id.menu_add).setVisible(true);
//
//        mSearchCheck = false;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // TODO Auto-generated method stub
//
//        switch (item.getItemId()) {
//
//            case R.id.menu_add:
//                Toast.makeText(getActivity(), R.string.add, Toast.LENGTH_SHORT).show();
//                break;
//
//            case R.id.menu_search:
//                mSearchCheck = true;
//                Toast.makeText(getActivity(), R.string.search, Toast.LENGTH_SHORT).show();
//                break;
//        }
//        return true;
//    }
//
//    private SearchView.OnQueryTextListener onQuerySearchView = new SearchView.OnQueryTextListener() {
//        @Override
//        public boolean onQueryTextSubmit(String s) {
//            return false;
//        }
//
//        @Override
//        public boolean onQueryTextChange(String s) {
//            if (mSearchCheck) {
//                // implement your search here
//            }
//            return false;
//        }
//    };
//
//
//    private void initEvent() {
//        mAdapter.setOnTreeNodeClickLister(new TreeListViewAdapter.OnTreeNodeClickLister() {
//
//            @Override
//            public void OnClick(Node node, int position) {
//                if (node.isLeaf()) {
//                    Toast.makeText(context, node.getName(), Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//
//        mTree.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view,
//                                           final int position, long id) {
//                final EditText editText = new EditText(context);
//
//                new AlertDialog.Builder(context).setTitle("Add node")
//                        .setView(editText).setPositiveButton("Add", new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        if (TextUtils.isEmpty(editText.getText().toString())) {
//                            return;
//                        }
//                        mAdapter.addExtraNode(position, editText.getText().toString(), editText.getText().toString());
//                    }
//                }).setNegativeButton("No", null).show();
//                return true;
//            }
//        });
//    }
//
