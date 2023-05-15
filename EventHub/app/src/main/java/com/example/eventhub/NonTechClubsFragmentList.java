package com.example.eventhub;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NonTechClubsFragmentList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NonTechClubsFragmentList extends Fragment implements RecyclerViewInterface{
    DatabaseReference reff;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    List<ClubsListModelClass> userList;
    Adapter adapter;
    View view;

    ArrayList<String> clubs;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NonTechClubsFragmentList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NonTechClubsFragmentList.
     */
    // TODO: Rename and change types and number of parameters
    public static NonTechClubsFragmentList newInstance(String param1, String param2) {
        NonTechClubsFragmentList fragment = new NonTechClubsFragmentList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return view = inflater.inflate(R.layout.fragment_non_tech_clubs_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        // Initializing the data for the recycler view.
        initializeData();
//        initializeRecyclerView();
    }

    private void initializeData() {
        userList = new ArrayList<>();
//        String[] clubs = {"Cyborg", "Byld" ,"Dark Code", "Foobar", "BioBytes", "ACM" ,"IEEE", "Evariste", "LeanIn", "Women In Tech", "OWASP", "Design Hub" ,"Electroholics"};
//        clubs = new ArrayList<String>(Arrays.asList("Cyborg", "Byld" ,"Dark Code", "Foobar", "BioBytes",
//                "ACM" ,"IEEE", "Evariste", "LeanIn", "Women In Tech", "OWASP", "Design Hub" ,"Electroholics"));
//        clubs = new ArrayList<>();
        // Fetching clubs list from real time database.
        reff = FirebaseDatabase.getInstance().getReference();
        reff.child("ClubsName").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                ArrayList<ClubsListModelClass> clubNames = new ArrayList<>(); // fetch club list
                for (DataSnapshot ds: snapshot.getChildren()) {
                    Boolean tech = ds.getValue(Boolean.class);
                    String c_name = ds.getKey();
//                    clubNames.add(c_name);
//                    clubs.add(c_name);
                    if(Boolean.FALSE.equals(tech))
                        clubNames.add(new ClubsListModelClass(c_name, "---------------------"));
//                  Toast.makeText(getActivity(), c_name, Toast.LENGTH_SHORT).show();
                }
                initializeRecyclerView(clubNames);
//                for(String club: clubNames){
//                    userList.add(new ClubsListModelClass(club,"--------------------"));
//                }
//                Toast.makeText(getActivity(), "SIZE: " + clubNames.size(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed");
            }
        });
//        Toast.makeText(getActivity(), "SIZE: "+ String.valueOf(clubs.size()), Toast.LENGTH_SHORT).show();
        // Initializing the data objects.
//        for(String club : clubs) {
//        userList.add(new ClubsListModelClass(c_name, "---------------------"));
//            Toast.makeText(getActivity(), club, Toast.LENGTH_SHORT).show();
//        }
    }

    private void initializeRecyclerView(ArrayList<ClubsListModelClass> clubNames) {
        if(!isAdded())
            return;
        @SuppressLint("DiscouragedApi") int startView = getResources().getIdentifier("recyclerView1", "id", requireContext().getPackageName());
        recyclerView = view.findViewById(startView);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(recyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new Adapter(clubNames, this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onClickForListItem(int position) {
        // position: index(o-based) of the view present in recycler view.
        reff.child("ClubsName").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                ArrayList<String> clubNames = new ArrayList<>(); // fetch club list
                for (DataSnapshot ds: snapshot.getChildren()) {
                    String c_name = ds.getKey();
                    Boolean tech = ds.getValue(Boolean.class);
//                    clubNames.add(c_name);
                    if(Boolean.FALSE.equals(tech))
                        clubNames.add(c_name);
//                    clubNames.add(new ClubsListModelClass(c_name, "---------------------"));
//                  Toast.makeText(getActivity(), c_name, Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(getActivity(), ClubPage.class);
                intent.putExtra("CLUB_NAME", clubNames.get(position));
                startActivity(intent);
//                initializeRecyclerView(clubNames);
//                for(String club: clubNames){
//                    userList.add(new ClubsListModelClass(club,"--------------------"));
//                }
//                Toast.makeText(getActivity(), "SIZE: " + clubNames.size(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed");
            }
        });
    }
}