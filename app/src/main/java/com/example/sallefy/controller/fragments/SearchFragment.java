package com.example.sallefy.controller.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sallefy.R;
import com.example.sallefy.controller.activities.PlayingSongActivity;
import com.example.sallefy.controller.adapters.PlaylistListAdapter;
import com.example.sallefy.controller.adapters.SearchPlaylistListAdapter;
import com.example.sallefy.controller.adapters.SearchUserListAdapter;
import com.example.sallefy.controller.adapters.TrackListAdapter;
import com.example.sallefy.controller.callbacks.PlaylistAdapterCallback;
import com.example.sallefy.controller.callbacks.TrackListAdapterCallback;
import com.example.sallefy.controller.callbacks.UserListAdapterCallback;
import com.example.sallefy.controller.restapi.callback.PlaylistCallback;
import com.example.sallefy.controller.restapi.callback.SearchCallback;
import com.example.sallefy.controller.restapi.manager.PlaylistManager;
import com.example.sallefy.controller.restapi.manager.SearchManager;
import com.example.sallefy.model.Followed;
import com.example.sallefy.model.Playlist;
import com.example.sallefy.model.Search;
import com.example.sallefy.model.Track;
import com.example.sallefy.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SearchFragment extends Fragment implements SearchCallback, TrackListAdapterCallback, PlaylistAdapterCallback, UserListAdapterCallback, PlaylistCallback {
    public static final String TAG = SearchFragment.class.getName();

    private FragmentManager mFragmentManager;
    private FragmentTransaction mTransaction;

    private RecyclerView playlistsRV;
    private RecyclerView tracksRV;
    private RecyclerView usersRV;

    private BottomNavigationView mNav;

    private CheckBox backBtn;

    private EditText etSearch;

    private TextView tvTracks;
    private TextView tvPlaylists;
    private TextView tvUsers;

    private Playlist mPlaylist;

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

        tvPlaylists = v.findViewById(R.id.search_playlists_text);
        tvUsers = v.findViewById(R.id.search_users_text);
        tvTracks = v.findViewById(R.id.search_tracks_text);

        backBtn = v.findViewById(R.id.back_btn_search);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();
                }
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

        tvTracks.setVisibility(View.GONE);
        tvUsers.setVisibility(View.GONE);
        tvPlaylists.setVisibility(View.GONE);

        if(mTracks.size() != 0){
            tvTracks.setVisibility(View.VISIBLE);
        }

        if(mUsers.size() != 0){
            tvUsers.setVisibility(View.VISIBLE);
        }

        if(mPlaylists.size() != 0){
            tvPlaylists.setVisibility(View.VISIBLE);
        }

        if(mPlaylists.size() == 0 && mUsers.size() == 0 && mTracks.size() == 0){
            Toast.makeText(getContext(), R.string.search_empty_results, Toast.LENGTH_SHORT).show();
        }

        //Create Recycler View Adapters
        TrackListAdapter adapterTL = new TrackListAdapter(this.getContext(), mTracks, SearchFragment.this, R.layout.track_item);
        SearchPlaylistListAdapter adapterPL = new SearchPlaylistListAdapter(this.getContext(), mPlaylists, SearchFragment.this);
        SearchUserListAdapter adapterUL = new SearchUserListAdapter(this.getContext(), mUsers, SearchFragment.this);

        //Set adapters
        playlistsRV.setAdapter(adapterPL);
        tracksRV.setAdapter(adapterTL);
        usersRV.setAdapter(adapterUL);

    }

    @Override
    public void onNoResults(Throwable throwable) {
        Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFailure(Throwable throwable) {

    }

    @Override
    public void onTrackClick(Track track) {
        Intent intent = new Intent(getContext(), PlayingSongActivity.class);
        intent.putExtra("track", track);
        Playlist playlist = new Playlist();
        playlist.setName("Search " + track.getName());
        intent.putExtra("playlist", playlist);
        startActivity(intent);
    }

    @Override
    public void onPlaylistClick(Playlist playlist) {
        mPlaylist = playlist;
        PlaylistManager.getInstance(getContext()).chechFollowed(playlist, SearchFragment.this);
    }

    @Override
    public void onUserClick(User user) {
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, UserFragment.getInstance(user), UserFragment.TAG)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onPlaylistCreated(Playlist playlist) {

    }

    @Override
    public void onPlaylistFailure(Throwable throwable) {

    }

    @Override
    public void onPlaylistReceived(Playlist playlist) {

    }

    @Override
    public void onPlaylistNotReceived(Throwable throwable) {

    }

    @Override
    public void onPlaylistUpdated(Playlist playlist) {

    }

    @Override
    public void onPlaylistNotUpdated(Throwable throwable) {

    }

    @Override
    public void onPlaylistsReceived(List<Playlist> playlists) {

    }

    @Override
    public void onPlaylistsNotReceived(Throwable throwable) {

    }

    @Override
    public void onPlaylistFollowed() {

    }

    @Override
    public void onPlaylistFollowError(Throwable throwable) {

    }

    @Override
    public void onIsFollowedReceived(Followed followed) {
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, PlaylistFragment.getInstance(mPlaylist, followed.getFollowed()), PlaylistFragment.TAG)
                .addToBackStack(null)
                .commit();
    }
}
