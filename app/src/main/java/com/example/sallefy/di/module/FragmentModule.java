package com.example.sallefy.di.module;

import com.example.sallefy.fragment.AddSongToPlaylistFragment;
import com.example.sallefy.fragment.AddTrackToPlaylistFragment;
import com.example.sallefy.fragment.CreateTrackFragment;
import com.example.sallefy.fragment.HomeFragment;
import com.example.sallefy.fragment.LoginFragment;
import com.example.sallefy.fragment.OptionsFragment;
import com.example.sallefy.fragment.PlayingSongFragment;
import com.example.sallefy.fragment.PlaylistFragment;
import com.example.sallefy.fragment.ProfileFragment;
import com.example.sallefy.fragment.RegisterFragment;
import com.example.sallefy.fragment.SearchFragment;
import com.example.sallefy.fragment.TrackOptionsFragment;
import com.example.sallefy.fragment.UserPlaylistsFragment;
import com.example.sallefy.fragment.UserSettingsFragment;
import com.example.sallefy.fragment.UserTracksFragment;
import com.example.sallefy.fragment.YourLibraryFollowersFragment;
import com.example.sallefy.fragment.YourLibraryFollowingsFragment;
import com.example.sallefy.fragment.YourLibraryFragment;
import com.example.sallefy.fragment.YourLibraryPlaylistsFragment;
import com.example.sallefy.fragment.YourLibraryTracksFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract LoginFragment loginFragment();

    @ContributesAndroidInjector
    abstract RegisterFragment registerFragment();

    @ContributesAndroidInjector
    abstract HomeFragment homeFragment();

    @ContributesAndroidInjector
    abstract PlayingSongFragment playingSongFragment();

    @ContributesAndroidInjector
    abstract AddSongToPlaylistFragment addSongToPlaylistFragment();

    @ContributesAndroidInjector
    abstract AddTrackToPlaylistFragment addTrackToPlaylistFragment();

    @ContributesAndroidInjector
    abstract CreateTrackFragment createTrackFragment();

    @ContributesAndroidInjector
    abstract OptionsFragment optionsFragment();

    @ContributesAndroidInjector
    abstract PlaylistFragment playlistFragment();

    @ContributesAndroidInjector
    abstract SearchFragment searchFragment();

    @ContributesAndroidInjector
    abstract TrackOptionsFragment trackOptionsFragment();

    @ContributesAndroidInjector
    abstract ProfileFragment profileFragment();

    @ContributesAndroidInjector
    abstract UserPlaylistsFragment userPlaylistsFragment();

    @ContributesAndroidInjector
    abstract UserSettingsFragment userSettingsFragment();

    @ContributesAndroidInjector
    abstract UserTracksFragment userTracksFragment();

    @ContributesAndroidInjector
    abstract YourLibraryFragment yourLibraryFragment();

    @ContributesAndroidInjector
    abstract YourLibraryFollowersFragment yourLibraryFollowersFragment();

    @ContributesAndroidInjector
    abstract YourLibraryFollowingsFragment yourLibraryFollowingsFragment();

    @ContributesAndroidInjector
    abstract YourLibraryPlaylistsFragment yourLibraryPlaylistsFragment();

    @ContributesAndroidInjector
    abstract YourLibraryTracksFragment yourLibraryTracksFragment();

}
