package com.example.matrimonyapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.matrimonyapp.R;
import com.example.matrimonyapp.activity.EditProfileActivity;
import com.example.matrimonyapp.adapter.PhotosTabLayoutAdapter;
import com.example.matrimonyapp.adapter.ProfileTabLayoutAdapter;
import com.example.matrimonyapp.customViews.CustomViewPager;
import com.example.matrimonyapp.modal.UserModel;
import com.example.matrimonyapp.volley.CustomSharedPreference;
import com.google.android.material.tabs.TabLayout;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BlankFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BlankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlankFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TEXT = "text";

    // TODO: Rename and change types of parameters
    private String mText;

    private OnFragmentInteractionListener mListener;

    private EditText editTextFragment;
    private Button buttonFragment;
    ImageView back;

    TabLayout tabLayout_photoes;
    CustomViewPager tabPhotoesCustomViewPager;
    private UserModel userModel;


    public BlankFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static BlankFragment newInstance(String text) {
        BlankFragment fragment = new BlankFragment();
        Bundle args = new Bundle();
        args.putString(TEXT, text);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mText = getArguments().getString(TEXT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blank, container, false);

        back=view.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fragmentManager = getFragmentManager();

                if (fragmentManager.getBackStackEntryCount() > 0)
                {
                    fragmentManager.popBackStack();
                    removeCurrentFragment();
                }
                else
                {
                    getActivity().finish();
                }
            }
        });

        userModel = CustomSharedPreference.getInstance(getContext()).getUser();

        tabPhotoesCustomViewPager = view.findViewById(R.id.viewPager);
        tabLayout_photoes = view.findViewById(R.id.tabLayout);

        tabLayout_photoes.setTabGravity(TabLayout.GRAVITY_FILL);

        final PhotosTabLayoutAdapter adapter = new PhotosTabLayoutAdapter(getContext(),getChildFragmentManager(), tabLayout_photoes.getTabCount());
        tabPhotoesCustomViewPager.setAdapter(adapter);

        tabPhotoesCustomViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout_photoes));

        tabLayout_photoes.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabPhotoesCustomViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });




        return view;
    }



    public void removeCurrentFragment()
    {
        FragmentTransaction transaction =getActivity().getSupportFragmentManager().beginTransaction();

        Fragment currentFrag =  getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_container);


        String fragName = "NONE";

        if (currentFrag!=null)
            fragName = currentFrag.getClass().getSimpleName();


        if (currentFrag != null)
            transaction.remove(currentFrag);

        transaction.commit();

    }
    // TODO: Rename method, update argument and hook method into UI event
    public void sendBack(String sendBackText) {
        if (mListener != null) {
            mListener.onFragmentInteraction(sendBackText);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String sendBackText);
    }
}