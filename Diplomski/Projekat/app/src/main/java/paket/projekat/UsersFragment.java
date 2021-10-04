package paket.projekat;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UsersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class  UsersFragment extends Fragment {
    TextView textViewNumberOfFriendRequest;

    RecyclerView recyclerViewListOfUsers;
    CustomAdapter2 customAdapter2;
    List<UserData> userList;
    List<FriendsData> friendsDataList;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;

    Integer counter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UsersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UsersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UsersFragment newInstance(String param1, String param2) {
        UsersFragment fragment = new UsersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_users, container, false);

        textViewNumberOfFriendRequest = view.findViewById(R.id.textViewNumberOfFriendRequest);

        recyclerViewListOfUsers = view.findViewById(R.id.recyclerViewListOfUsers2);
        recyclerViewListOfUsers.setHasFixedSize(true);
        recyclerViewListOfUsers.setLayoutManager(new LinearLayoutManager(getActivity()));

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Friends").child("user_"+firebaseUser.getUid());

        friendsDataList = new ArrayList<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                friendsDataList.clear();
                for(DataSnapshot ds : snapshot.getChildren()) {
                    FriendsData friendsData = ds.getValue(FriendsData.class);
                    friendsDataList.add(friendsData);
                }
                loadFriends();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        loadNumberOfFriendRequest();

        textViewNumberOfFriendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), FriendRequestActivity.class));
        //        getActivity().finish();
            }
        });

        return view;
    }

    private void loadFriends() {
        userList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for(DataSnapshot ds : snapshot.getChildren()) {
                    UserData user = ds.getValue(UserData.class);

                    for(FriendsData friendsData : friendsDataList) {
                        if(user.getUserId() != null && user.getUserId().equals(friendsData.getId())) {
                            userList.add(user);
                            break;
                        }
                    }

                    customAdapter2 = new CustomAdapter2(getContext(), userList);
                    customAdapter2.notifyDataSetChanged();
                    recyclerViewListOfUsers.setAdapter(customAdapter2);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void searchUsers(String query) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for(DataSnapshot ds : snapshot.getChildren()) {
                    UserData userData = ds.getValue(UserData.class);

                    for(FriendsData friendsData : friendsDataList) {
                        if(userData.getUserId() != null && userData.getUserId().equals(friendsData.getId())) {
                            if(userData.getUserName().toLowerCase().contains(query.toLowerCase()) ||
                                    userData.getFirstName().toLowerCase().contains(query.toLowerCase()) ||
                                    userData.getSecondName().toLowerCase().contains(query.toLowerCase())) {
                                userList.add(userData);
                            }
                            break;
                        }
                    }

                    customAdapter2 = new CustomAdapter2(getContext(), userList);
                    customAdapter2.notifyDataSetChanged();
                    recyclerViewListOfUsers.setAdapter(customAdapter2);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadNumberOfFriendRequest() {
        String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        counter = 0;

        databaseReference = FirebaseDatabase.getInstance().getReference("FriendRequest").child("user_"+myUid);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    counter++;
                }
                textViewNumberOfFriendRequest.setText(counter.toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                textViewNumberOfFriendRequest.setText(counter.toString());
            }
        });

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);    // za prikazivanje meni opcija u fragmentu

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

        //menu.findItem(R.id.action_add_post).setVisible(false);

        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!query.trim().isEmpty()) {
                    searchUsers(query);
                }
                else {
                    loadFriends();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(!newText.trim().isEmpty()) {
                    searchUsers(newText);
                }
                else {
                    loadFriends();
                }
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_logout) {
            offlineStatus();
            firebaseAuth.signOut();
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        }
        if(id == R.id.action_add_post) {
            startActivity(new Intent(getActivity(), AllUsersActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void offlineStatus() {
        String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(myUid);
        try {
            Map<String, Object> hashMap = new HashMap<>();
            hashMap.put("onlineStatus", "offline");

            mDatabaseReference.updateChildren(hashMap);
        }
        catch (Exception e) {
        }
    }
}