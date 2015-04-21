package com.kido.Trust.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.kido.Trust.R;
import com.kido.Trust.adapter.TreeListViewAdapter;
import com.kido.Trust.parse.ParseHelper;
import com.kido.Trust.util.Node;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.parse.ParseException;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;

import java.util.ArrayList;
import java.util.List;

import yalantis.com.sidemenu.interfaces.ScreenShotable;


public class FragmentTreeList extends Fragment implements ScreenShotable {
    private DynamicListView mTree;
    private TreeListViewAdapter<Node> nodeAdapter;
    private List<Node> nodeList = null;
    private DialogFragment mMenuDialogFragment;
    private boolean mSearchCheck;
    private static final String TEXT_FRAGMENT = "TEXT_FRAGMENT";
    private Context context;
    ProgressDialog mProgressDialog;
    ParseHelper parse;
    private View containerView;
    protected ImageView mImageView;
    protected int res;
    private Bitmap bitmap;

    public static FragmentTreeList newInstance(int resId) {
        FragmentTreeList mFragment = new FragmentTreeList();
        Bundle mBundle = new Bundle();
        mBundle.putInt(Integer.class.getName(), resId);
        mFragment.setArguments(mBundle);
        return mFragment;

    }

    public Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case ParseHelper.GET_ALL_NODES:
                    showNodes((List<Node>) msg.obj);
                    initEvent();
                    mProgressDialog.dismiss();
                    break;

                case ParseHelper.ERROR_PARSE:
                    Toast.makeText(context, ((ParseException) msg.obj).getMessage(), Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss();
                    break;

                default:
                    break;
            }

            return false;
        }
    }
    );

    public Handler handlerMenu = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            final EditText editText = new EditText(context);
            switch (msg.what) {
                case 1:


                    new AlertDialog.Builder(context).setTitle(R.string.new_title)
                            .setView(editText).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if (TextUtils.isEmpty(editText.getText().toString())) {
                                return;
                            }
                            nodeAdapter.addRootNode(editText.getText().toString());
                        }
                    }).setNegativeButton(R.string.no, null).show();
                    break;
                case 2:

                    new AlertDialog.Builder(context).setTitle(R.string.new_title)
                            .setView(editText).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if (TextUtils.isEmpty(editText.getText().toString())) {
                                return;
                            }
                            nodeAdapter.addExtraNode(nodeAdapter.getCurrentNode(), editText.getText().toString());
                        }
                    }).setNegativeButton(R.string.no, null).show();
                    break;

                case 3:
                    editText.setText(nodeAdapter.findNodeByPos(nodeAdapter.getCurrentNode()).getDescription());
                    new AlertDialog.Builder(context).setTitle(R.string.new_title)
                            .setView(editText).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            nodeAdapter.editNodeDesc(nodeAdapter.getCurrentNode(), editText.getText().toString());
                        }
                    }).setNegativeButton(R.string.no, null).show();
                    break;

                default:
                    break;
            }

            return false;
        }
    }
    );

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.containerView = view.findViewById(R.id.container);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        res = getArguments().getInt(Integer.class.getName());

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        registerForContextMenu(mTree);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
        parse = new ParseHelper(context, handler);
//        Toolbar mToolbar = (Toolbar) activity.findViewById(R.id.toolbar);
//        ((ActionBarActivity)getActivity()).setSupportActionBar(mToolbar);
//        ((ActionBarActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
//        ((ActionBarActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        ((ActionBarActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
//        mToolbar.setNavigationIcon(R.drawable.icn_close);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_treelist, container, false);
        mTree = (DynamicListView) rootView.findViewById(com.kido.Trust.R.id.id_listview);

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);

//        ((ActionBarActivity)getActivity()).setSupportActionBar(toolbar);

        OpenProgressDialog();
        parse.getAllNodes();
        rootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        mMenuDialogFragment = ContextMenuDialogFragment.newInstance((int) getResources().getDimension(R.dimen.tool_bar_height), getMenuObjects());
        setHasOptionsMenu(true);


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
        //            nodeAdapter = new SimpleTreeListViewAdapter<Node>(mTree, context, nodes, 0);
        nodeAdapter = new TreeListViewAdapter<Node>(mTree, context, nodes, 0);
        // Binds the Adapter to the ListView
        AlphaInAnimationAdapter animationAdapter = new AlphaInAnimationAdapter(nodeAdapter);
        animationAdapter.setAbsListView(mTree);
        mTree.setAdapter(animationAdapter);

//        mTree.setAdapter(nodeAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.context_menu:
                if (getFragmentManager().findFragmentByTag(ContextMenuDialogFragment.TAG) == null) {
                    mMenuDialogFragment.show(getFragmentManager(), ContextMenuDialogFragment.TAG);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }



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
//                final EditText editText = new EditText(getActivity());
//                new AlertDialog.Builder(getActivity()).setTitle(R.string.new_title)
//                        .setView(editText).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        if (TextUtils.isEmpty(editText.getText().toString())) {
//                            return;
//                        }
//
//                        nodeAdapter.addRootNode(editText.getText().toString());
//                    }
//                }).setNegativeButton(R.string.no, null).show();
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


    private void initEvent() {
//        nodeAdapter.setOnTreeNodeClickLister(new TreeListViewAdapter.OnTreeNodeClickLister() {
//
//            @Override
//            public void OnClick(Node node, int position) {
//                if (node.isLeaf()) {
//                    Toast.makeText(context, node.getName(), Toast.LENGTH_SHORT).show();
//                }
//            }
//        });


//        mTree.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view,
//                                           final int position, long id) {
//                final EditText editText = new EditText(context);
//
//                new AlertDialog.Builder(context).setTitle(R.string.new_title)
//                        .setView(editText).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        if (TextUtils.isEmpty(editText.getText().toString())) {
//                            return;
//                        }
//                        nodeAdapter.addExtraNode(position, editText.getText().toString());
//                    }
//                }).setNegativeButton(R.string.no, null).show();
//                return true;
//            }
//        });
    }

    public void addNewNode(int position, String name) {

        nodeAdapter.addExtraNode(position, name);
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
                FragmentTreeList.this.bitmap = bitmap;
            }
        };

        thread.start();
    }

    @Override
    public Bitmap getBitmap() {
        return null;
    }




    private List<MenuObject> getMenuObjects() {
        // You can use any [resource, bitmap, drawable, color] as image:
        // item.setResource(...)
        // item.setBitmap(...)
        // item.setDrawable(...)
        // item.setColor(...)
        // You can set image ScaleType:
        // item.setScaleType(ScaleType.FIT_XY)
        // You can use any [resource, drawable, color] as background:
        // item.setBgResource(...)
        // item.setBgDrawable(...)
        // item.setBgColor(...)
        // You can use any [color] as text color:
        // item.setTextColor(...)
        // You can set any [color] as divider color:
        // item.setDividerColor(...)

        List<MenuObject> menuObjects = new ArrayList<>();

        MenuObject close= new MenuObject();
        close.setResource(R.drawable.icn_quit);
        close.setBgColor(getResources().getColor(R.color.context_color));

        MenuObject newRoot = new MenuObject("Добавить категорию");
        newRoot.setResource(R.drawable.new_root);
        newRoot.setBgColor(getResources().getColor(R.color.context_color));

        MenuObject newTask = new MenuObject("Добавить задачу");
        newTask.setResource(R.drawable.new_task);
        newTask.setBgColor(getResources().getColor(R.color.context_color));

        MenuObject info = new MenuObject("Описание");
        info.setResource(R.drawable.info_task);
        info.setBgColor(getResources().getColor(R.color.context_color));

        MenuObject trustTask = new MenuObject("Поручить задачу");
        trustTask.setResource(R.drawable.trust_task);
        trustTask.setBgColor(getResources().getColor(R.color.context_color));

        MenuObject checkTask = new MenuObject("В архив");
        checkTask.setResource(R.drawable.check_task);
        checkTask.setBgColor(getResources().getColor(R.color.context_color));

        MenuObject deleteTask = new MenuObject("Удалить");
        deleteTask.setResource(R.drawable.delete_task);
        deleteTask.setBgColor(getResources().getColor(R.color.context_color));

        menuObjects.add(close);
        menuObjects.add(newRoot);
        menuObjects.add(newTask);
        menuObjects.add(info);
        menuObjects.add(trustTask);
        menuObjects.add(checkTask);
        menuObjects.add(deleteTask);
        return menuObjects;
    }

}
