package com.example.proyectoseguridadmujer.ui.informative;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoseguridadmujer.InstitutionListAdapter;
import com.example.proyectoseguridadmujer.R;

public class InstitutionListFragment extends Fragment {

    RecyclerView mRecyclerView;

    String Titles[], Descriptions[];
    int Images[] = {R.drawable.building, R.drawable.icon_test, R.drawable.instagram_profile_image, R.drawable.logo_app,
    R.drawable.logo_transparent, R.drawable.selfdefense_icon1, R.drawable.selfdefense_icon2};

    public InstitutionListFragment() {
        // Required empty public constructor
    }

    /*
    // TODO: Rename and change types and number of parameters
    public static InstitutionListFragment newInstance(String param1, String param2) {
        InstitutionListFragment fragment = new InstitutionListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*Titles = getResources().getStringArray(R.array.Test_values_Recycler_view);*/
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_institution_list, container, false);
        mRecyclerView = view.findViewById(R.id.InstitutionList);
        //Descriptions = getResources().getStringArray(R.array.Test_values_Descriptions_Recycler_view);
        //InstitutionListAdapter adapter = new InstitutionListAdapter(view.getContext(), Titles, Descriptions, Images);
        //mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        return view;
    }

}