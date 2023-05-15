package com.example.eventhub;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpcomingEventsListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpcomingEventsListFragment extends Fragment implements RecyclerViewInterface{

    String clubName;
    DatabaseReference reff;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    List<ClubsListModelClass> userList;
    Adapter adapter;
    View view;

    ArrayList<String> eventsList;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UpcomingEventsListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UpcomingEventsListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UpcomingEventsListFragment newInstance(String param1, String param2) {
        UpcomingEventsListFragment fragment = new UpcomingEventsListFragment();
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
        return view =  inflater.inflate(R.layout.fragment_upcoming_events_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        ClubPage clubPage = (ClubPage) getActivity();
        clubName = clubPage.getClubName();
        // Initializing the data for the recycler view.
        initializeData();
    }

    private void initializeData() {
//        userList = new ArrayList<>();
//        eventsList = new ArrayList<String>(Arrays.asList("Upcoming Event 1", "Upcoming Event 2", "Upcoming Event 3" ,"Upcoming Event 4",
//                "Upcoming Event 5", "Upcoming Event 6", "Upcoming Event 7", "Upcoming Event 8", "Upcoming Event 9", "Upcoming Event 10", "Upcoming Event 11", "Upcoming Event 12"));
//        for(String event : eventsList) {
//            userList.add(new ClubsListModelClass(event, "---------------------"));
//        }
        reff = FirebaseDatabase.getInstance().getReference();
        reff.child("Clubs").child(clubName).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                ArrayList<ClubsListModelClass> eventList = new ArrayList<>(); // fetch club list
                ArrayList<Event> data = new ArrayList<>();
                for (DataSnapshot ds: snapshot.getChildren()) {
                    data.add(ds.getValue(Event.class));
                }
                for (Event event: data) {
                    String eventName = event.getName();
//                    Date date = event.getTime();
                    Boolean hasHappened = true;
                    Calendar date = Calendar.getInstance();
                    try {
                        date.setTime(Objects.requireNonNull(new SimpleDateFormat("dd-M-yyyy hh:mm:ss").parse(event.getTime())));
                        if (date.compareTo(Calendar.getInstance())>0) {
                            hasHappened = false;
                        }
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    if(!hasHappened) {
                        eventList.add(new ClubsListModelClass(eventName, "---------------------"));
                    }
                }
                // Initializing the recycler view.
                initializeRecyclerView(eventList);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed");
            }
        });
    }

    private void initializeRecyclerView(ArrayList<ClubsListModelClass> eventsList) {
        if(!isAdded())
            return;
        @SuppressLint("DiscouragedApi") int startView = getResources().getIdentifier("recyclerView3", "id", requireContext().getPackageName());
        recyclerView = view.findViewById(startView);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(recyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new Adapter(eventsList, this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClickForListItem(int position) {
        // OnClick Listener for all upcoming events.
        reff = FirebaseDatabase.getInstance().getReference();
        reff.child("Clubs").child(clubName).addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                int index = -1;
//                Toast.makeText(getActivity(), String.valueOf(position),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), EventPage.class);
                ArrayList<Event> data = new ArrayList<>();
                ArrayList<String> keys = new ArrayList<>();
                for (DataSnapshot ds: snapshot.getChildren()) {
                    keys.add(ds.getKey());
                    data.add(ds.getValue(Event.class));

                }
                for(int i =0; i<data.size(); i++) {
                    Event event = data.get(i);
                    String key = keys.get(i);
                    String eventName = event.getName();
//                    Toast.makeText(getActivity(), event.getName(), Toast.LENGTH_SHORT).show();
//                    Date date = event.getTime();
                    Boolean hasHappened = true;
                    Calendar date = Calendar.getInstance();
                    try {
                        date.setTime(Objects.requireNonNull(new SimpleDateFormat("dd-M-yyyy hh:mm:ss").parse(event.getTime())));
                        if (date.compareTo(Calendar.getInstance())>0) {
                            hasHappened = false;
                        }
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
//                    Toast.makeText(getActivity(), String.valueOf(hasHappened), Toast.LENGTH_SHORT).show();
                    if(!hasHappened) {
//                        Toast.makeText(getActivity(), eventName, Toast.LENGTH_SHORT).show();
                        // Check if the current event was clicked up on.
                        index ++;
                        if(index == position){
//                            Toast.makeText(getActivity(), organizer, Toast.LENGTH_SHORT).show();
                            intent.putExtra("ORGANIZER", event.getOrganizer());
                            intent.putExtra("VENUE", event.getVenue());
//                            Toast.makeText(getActivity(), "VENUE: " + event.getVenue(), Toast.LENGTH_SHORT).show();
                            intent.putExtra("TIME", event.getTime());
                            intent.putExtra("EVENT",event.getName());
                            intent.putExtra("DESCRIPTION",event.getDesc());
                            intent.putExtra("CLUB_NAME", clubName);
                            intent.putExtra("KEY", key);
                            break;
                        }
                    }
                }
                startActivity(intent);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed");
            }
        });
    }
}