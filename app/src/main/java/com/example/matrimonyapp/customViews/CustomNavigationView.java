package com.example.matrimonyapp.customViews;

import android.app.Activity;
import android.content.Context;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.Toast;

import androidx.drawerlayout.widget.DrawerLayout;

import com.example.matrimonyapp.R;
import com.example.matrimonyapp.activity.PaymentMethodsActivity;
import com.example.matrimonyapp.activity.SubscriptionActivity;
import com.example.matrimonyapp.adapter.ExpandableListAdapter;
import com.example.matrimonyapp.modal.ExpandedMenuModel;
import com.example.matrimonyapp.modal.UserModel;
import com.example.matrimonyapp.volley.CustomSharedPreference;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Locale;


public class CustomNavigationView
{
    private Context context;
    private DrawerLayout drawerLayout;
    private ExpandableListView expandableListView;
    private NavigationView navigationView;

    private ArrayList<ExpandedMenuModel> arrayList_expandedMenuModel;
    private ExpandableListAdapter expandableListAdapter;

    private Locale locale;

    public CustomNavigationView(Context context, DrawerLayout drawerLayout,
                                ExpandableListView expandableListView, NavigationView navigationView) {
        this.context = context;
        this.drawerLayout = drawerLayout;
        this.expandableListView = expandableListView;
        this.navigationView = navigationView;
        this.arrayList_expandedMenuModel = new ArrayList<>();

    }

    public void createNavigation()
    {
        prepareMenu();

        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
        expandableListAdapter = new ExpandableListAdapter(context, arrayList_expandedMenuModel, expandableListView);

        expandableListView.setAdapter(expandableListAdapter);

        onClickListener();

    }

    private void onClickListener()
    {
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long l)
            {
                //Toast.makeText(context, "Select : "+arrayList_expandedMenuModel.get(groupPosition).getMenuName(),Toast.LENGTH_SHORT).show();

                switch (groupPosition)
                {


                    case 0:
                        drawerLayout.closeDrawers();
                        break;


                    case 1:
                        drawerLayout.closeDrawers();
                        break;


                    case 2:
                        drawerLayout.closeDrawers();
                        break;


                    case 3:
                        drawerLayout.closeDrawers();

                        Intent intent = new Intent(context, SubscriptionActivity.class);
                        context.startActivity(intent);


                        break;


                    case 5:
                        drawerLayout.closeDrawers();
                        break;


                    case 7:
                        CustomSharedPreference.getInstance(context).logout();
                        break;

                }


                return false; // when returns true -> doesn't show submenu
            }
        });


        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition,int childPosition, long l)
            {

                switch (groupPosition)
                {

                    case 4:
                        switch (childPosition)
                        {
                            case 0:
                                setLocale("en");
                                break;

                            case 1:
                                setLocale("mr");
                                break;

                            case 2:
                                setLocale("hi");
                                break;
                        }

                        break;
                    default:

                }


                drawerLayout.closeDrawers();
                //Toast.makeText(context, "Select : "+arrayList_expandedMenuModel.get(groupPosition).getMenuName(),Toast.LENGTH_SHORT).show();

                return true;
            }
        });




    }

    private void setupDrawerContent(NavigationView navigationView) {
        //revision: this don't works, use setOnChildClickListener() and setOnGroupClickListener() above instead
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);

                        return true;
                    }
                });




    }


    public void setLocale(String localeName) {

            UserModel userModel = CustomSharedPreference.getInstance(context).getUser();
            locale = new Locale(localeName);
            Resources res = context.getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = locale;


            res.updateConfiguration(conf, dm);


            ((Activity)context).recreate();
            userModel.setLanguage(localeName);
            CustomSharedPreference.getInstance(context).saveUser(userModel);



        Intent intent = ((Activity)context).getIntent();
        ((Activity)context).finish();
        ((Activity)context).startActivity(intent);

    }

    private void prepareMenu() {

        //0
        ExpandedMenuModel expandedMenuModel = new ExpandedMenuModel();
        expandedMenuModel.setMenuName(context.getResources().getString(R.string.my_profile));
        expandedMenuModel.setMenuIconId(R.drawable.my_acc);

        arrayList_expandedMenuModel.add(expandedMenuModel);

        //1
        expandedMenuModel = new ExpandedMenuModel();
        expandedMenuModel.setMenuName(context.getResources().getString(R.string.favorite));
        expandedMenuModel.setMenuIconId(R.drawable.start1);
        arrayList_expandedMenuModel.add(expandedMenuModel);


        //2
        expandedMenuModel = new ExpandedMenuModel();
        expandedMenuModel.setMenuName(context.getResources().getString(R.string.notifications));
        expandedMenuModel.setMenuIconId(R.drawable.notification);
        arrayList_expandedMenuModel.add(expandedMenuModel);

        //3
        expandedMenuModel = new ExpandedMenuModel();
        expandedMenuModel.setMenuName(context.getResources().getString(R.string.subscription_plan));
        expandedMenuModel.setMenuIconId(R.drawable.offer);
        arrayList_expandedMenuModel.add(expandedMenuModel);


        //4
        expandedMenuModel = new ExpandedMenuModel();
        expandedMenuModel.setMenuName(context.getResources().getString(R.string.change_language));
        expandedMenuModel.setMenuIconId(R.drawable.language1);
        ArrayList<String> arrayList_submenu = new ArrayList<>();
        arrayList_submenu.add(context.getResources().getString(R.string.english));
        arrayList_submenu.add(context.getResources().getString(R.string.marathi));
        arrayList_submenu.add(context.getResources().getString(R.string.hindi));
        expandedMenuModel.setArrayList_subMenu(arrayList_submenu);
        arrayList_expandedMenuModel.add(expandedMenuModel);

        //5
        expandedMenuModel = new ExpandedMenuModel();
        expandedMenuModel.setMenuName(context.getResources().getString(R.string.help_and_feedback));
        expandedMenuModel.setMenuIconId(R.drawable.ic_action_info);
        arrayList_expandedMenuModel.add(expandedMenuModel);


        //6
        expandedMenuModel = new ExpandedMenuModel();
        expandedMenuModel.setMenuName(context.getResources().getString(R.string.rate_app));
        expandedMenuModel.setMenuIconId(R.drawable.start1);
        arrayList_expandedMenuModel.add(expandedMenuModel);



        //7
        expandedMenuModel = new ExpandedMenuModel();
        expandedMenuModel.setMenuName(context.getResources().getString(R.string.logout));
        expandedMenuModel.setMenuIconId(R.drawable.power);
        arrayList_expandedMenuModel.add(expandedMenuModel);



    }


}