package com.example.matrimonyapp.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.matrimonyapp.R;
import com.example.matrimonyapp.modal.ExpandedMenuModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    //private List<ExpandedMenuModel> expandedMenuModelList; // header titles

    // child data in format of header title, child title
    //private HashMap<ExpandedMenuModel, List<String>> expandedMenuModelListHashMap;
    ExpandableListView expandableListView;

    private ArrayList<ExpandedMenuModel> arrayList_expandedMenuModel;

    public ExpandableListAdapter(Context context, ArrayList<ExpandedMenuModel> arrayList_expandedMenuModel, ExpandableListView expandableListView) {
        this.context = context;
        this.arrayList_expandedMenuModel = arrayList_expandedMenuModel;
        this.expandableListView = expandableListView;
    }

    @Override
    public int getGroupCount() {
/*        int i = expandedMenuModelList.size();
        Log.d("GROUPCOUNT", String.valueOf(i));
        return this.expandedMenuModelList.size();*/

        return arrayList_expandedMenuModel.size();

    }

    @Override
    public int getChildrenCount(int groupPosition) {
        int childCount = 0;
        /*if (groupPosition != 2) {
            childCount = this.expandedMenuModelListHashMap.get(this.expandedMenuModelList.get(groupPosition))
                    .size();
        }*/

        ArrayList<String> arrayList_submenu = arrayList_expandedMenuModel.get(groupPosition).getArrayList_subMenu();

        if(arrayList_submenu==null)
        {
            return 0;
        }
        else
        {
            return arrayList_expandedMenuModel.get(groupPosition).getArrayList_subMenu().size();
        }

    }

    @Override
    public Object getGroup(int groupPosition) {


        return this.arrayList_expandedMenuModel.get(groupPosition);
        //return this.expandedMenuModelList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
/*        Log.d("CHILD", expandedMenuModelListHashMap.get(this.expandedMenuModelList.get(groupPosition))
                .get(childPosition).toString());


        return this.expandedMenuModelListHashMap.get(this.expandedMenuModelList.get(groupPosition))
                .get(childPosition);*/

        return  arrayList_expandedMenuModel.get(groupPosition).getArrayList_subMenu().get(childPosition);

    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        ExpandedMenuModel expandedMenuModel = (ExpandedMenuModel) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_header, null);
        }
        TextView textView_menu = convertView.findViewById(R.id.textView_menu);
        ImageView imageView_menu = convertView.findViewById(R.id.imageView_menu);
        ImageView imageView_hasSubMenu = convertView.findViewById(R.id.imageView_hasSubMenu);
        //textView_menu.setTypeface(null, Typeface.BOLD);
        textView_menu.setText(expandedMenuModel.getMenuName());
        imageView_menu.setImageResource(expandedMenuModel.getMenuIconId());

        if(getChildrenCount(groupPosition)==0)
        {
            expandedMenuModel.setHasSubMenu(false);
            imageView_hasSubMenu.setVisibility(View.GONE);
        }
        else
        {
            expandedMenuModel.setHasSubMenu(true);
            imageView_hasSubMenu.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String submenu = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_submenu, null);
        }

        TextView textView_subMenu = (TextView) convertView
                .findViewById(R.id.textView_subMenu);

        textView_subMenu.setText(submenu);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

