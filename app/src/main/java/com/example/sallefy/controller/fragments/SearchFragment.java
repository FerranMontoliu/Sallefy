package com.example.sallefy.controller.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sallefy.R;
import com.example.sallefy.controller.adapters.PlaylistListAdapter;
import com.example.sallefy.controller.adapters.SearchPlaylistListAdapter;
import com.example.sallefy.controller.adapters.TrackListAdapter;
import com.example.sallefy.controller.adapters.UserListAdapter;
import com.example.sallefy.controller.restapi.callback.SearchCallback;
import com.example.sallefy.controller.restapi.manager.SearchManager;
import com.example.sallefy.model.Playlist;
import com.example.sallefy.model.Search;
import com.example.sallefy.model.Track;
import com.example.sallefy.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.concurrent.RecursiveAction;

public class SearchFragment extends Fragment implements SearchCallback {
    public static final String TAG = SearchFragment.class.getName();

    private FragmentManager mFragmentManager;
    private FragmentTransaction mTransaction;

    private RecyclerView playlistsRV;
    private RecyclerView tracksRV;
    private RecyclerView usersRV;

    private BottomNavigationView mNav;

    private CheckBox backBtn;

    private EditText etSearch;

    private ArrayList<User> mUsers;
    private ArrayList<Track> mTracks;
    private ArrayList<Playlist> mPlaylists;

    public static SearchFragment getInstance() {
        return new SearchFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);

        etSearch = v.findViewById(R.id.search_edit_text);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SearchManager.getInstance(getContext()).getSearchResults(etSearch.getText().toString(), SearchFragment.this);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //Set Recycler Views
        playlistsRV = v.findViewById(R.id.search_playlists_rv);
        tracksRV = v.findViewById(R.id.search_tracks_rv);
        usersRV = v.findViewById(R.id.search_profiles_rv);

        backBtn = v.findViewById(R.id.back_btn_search);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assert getFragmentManager() != null;
                getFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, new HomeFragment())
                        .remove(SearchFragment.this)
                        .commit();
            }
        });

        SearchManager.getInstance(getContext()).getSearchResults(etSearch.getText().toString(), SearchFragment.this);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSearchResultsReceived(Search results) {
        //Recycler View LAYOUT MANAGERS
        playlistsRV.setLayoutManager(new LinearLayoutManager(this.getContext(), RecyclerView.VERTICAL, false));
        tracksRV.setLayoutManager(new LinearLayoutManager(this.getContext(), RecyclerView.VERTICAL, false));
        usersRV.setLayoutManager(new LinearLayoutManager(this.getContext(), RecyclerView.VERTICAL, false));

        //getSearchResults
        mTracks = (ArrayList<Track>)results.getTracks();
        mUsers = (ArrayList<User>)results.getUsers();
        mPlaylists = (ArrayList<Playlist>)results.getPlaylists();

        //Create Recycler View Adapters
        TrackListAdapter adapterTL = new TrackListAdapter(this.getContext(), mTracks);
        SearchPlaylistListAdapter adapterPL = new SearchPlaylistListAdapter(this.getContext(), mPlaylists);
        UserListAdapter adapterUL = new UserListAdapter(this.getContext(), mUsers);

        //Set adapters
        playlistsRV.setAdapter(adapterPL);
        tracksRV.setAdapter(adapterTL);
        usersRV.setAdapter(adapterUL);

    }

    @Override
    public void onNoResults(Throwable throwable) {

    }

    @Override
    public void onFailure(Throwable throwable) {

    }
}
