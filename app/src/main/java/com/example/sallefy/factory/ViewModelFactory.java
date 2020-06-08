package com.example.sallefy.factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.sallefy.network.SallefyRepository;
import com.example.sallefy.viewmodel.AddSongToPlaylistViewModel;
import com.example.sallefy.viewmodel.AddTrackToPlaylistViewModel;
import com.example.sallefy.viewmodel.CreatePlaylistViewModel;
import com.example.sallefy.viewmodel.CreateTrackViewModel;
import com.example.sallefy.viewmodel.HomeViewModel;
import com.example.sallefy.viewmodel.LoginViewModel;
import com.example.sallefy.viewmodel.OptionsViewModel;
import com.example.sallefy.viewmodel.PlayingSongViewModel;
import com.example.sallefy.viewmodel.PlaylistViewModel;
import com.example.sallefy.viewmodel.ProfileTopTracksViewModel;
import com.example.sallefy.viewmodel.ProfileTracksViewModel;
import com.example.sallefy.viewmodel.ProfileViewModel;
import com.example.sallefy.viewmodel.RegisterViewModel;
import com.example.sallefy.viewmodel.SearchViewModel;
import com.example.sallefy.viewmodel.StatisticsViewModel;
import com.example.sallefy.viewmodel.TrackOptionsViewModel;
import com.example.sallefy.viewmodel.ProfilePlaylistsViewModel;
import com.example.sallefy.viewmodel.TrackStatisticsViewModel;
import com.example.sallefy.viewmodel.UploadProfileImageViewModel;
import com.example.sallefy.viewmodel.YourLibraryFollowersViewModel;
import com.example.sallefy.viewmodel.YourLibraryFollowingsViewModel;
import com.example.sallefy.viewmodel.YourLibraryPlaylistsViewModel;
import com.example.sallefy.viewmodel.YourLibraryTracksViewModel;
import com.example.sallefy.viewmodel.YourLibraryViewModel;

import javax.inject.Inject;

import dagger.Module;

@Module
public class ViewModelFactory implements ViewModelProvider.Factory {

    private SallefyRepository sallefyRepository;

    @Inject
    public ViewModelFactory(SallefyRepository sallefyRepository) {
        this.sallefyRepository = sallefyRepository;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AddSongToPlaylistViewModel.class)) {
            return (T) new AddSongToPlaylistViewModel(sallefyRepository);
        }
        if (modelClass.isAssignableFrom(AddTrackToPlaylistViewModel.class)) {
            return (T) new AddTrackToPlaylistViewModel(sallefyRepository);
        }
        if (modelClass.isAssignableFrom(CreateTrackViewModel.class)) {
            return (T) new CreateTrackViewModel(sallefyRepository);
        }
        if (modelClass.isAssignableFrom(HomeViewModel.class)) {
            return (T) new HomeViewModel(sallefyRepository);
        }
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel(sallefyRepository);
        }
        if (modelClass.isAssignableFrom(OptionsViewModel.class)) {
            return (T) new OptionsViewModel(sallefyRepository);
        }
        if (modelClass.isAssignableFrom(PlayingSongViewModel.class)) {
            return (T) new PlayingSongViewModel(sallefyRepository);
        }
        if (modelClass.isAssignableFrom(PlaylistViewModel.class)) {
            return (T) new PlaylistViewModel(sallefyRepository);
        }
        if (modelClass.isAssignableFrom(RegisterViewModel.class)) {
            return (T) new RegisterViewModel(sallefyRepository);
        }
        if (modelClass.isAssignableFrom(SearchViewModel.class)) {
            return (T) new SearchViewModel(sallefyRepository);
        }
        if (modelClass.isAssignableFrom(TrackOptionsViewModel.class)) {
            return (T) new TrackOptionsViewModel(sallefyRepository);
        }
        if (modelClass.isAssignableFrom(ProfilePlaylistsViewModel.class)) {
            return (T) new ProfilePlaylistsViewModel(sallefyRepository);
        }
        if (modelClass.isAssignableFrom(ProfileTracksViewModel.class)) {
            return (T) new ProfileTracksViewModel(sallefyRepository);
        }
        if (modelClass.isAssignableFrom(ProfileViewModel.class)) {
            return (T) new ProfileViewModel(sallefyRepository);
        }
        if (modelClass.isAssignableFrom(YourLibraryFollowersViewModel.class)) {
            return (T) new YourLibraryFollowersViewModel(sallefyRepository);
        }
        if (modelClass.isAssignableFrom(YourLibraryFollowingsViewModel.class)) {
            return (T) new YourLibraryFollowingsViewModel(sallefyRepository);
        }
        if (modelClass.isAssignableFrom(YourLibraryPlaylistsViewModel.class)) {
            return (T) new YourLibraryPlaylistsViewModel(sallefyRepository);
        }
        if (modelClass.isAssignableFrom(YourLibraryTracksViewModel.class)) {
            return (T) new YourLibraryTracksViewModel(sallefyRepository);
        }
        if (modelClass.isAssignableFrom(YourLibraryViewModel.class)) {
            return (T) new YourLibraryViewModel(sallefyRepository);
        }
        if (modelClass.isAssignableFrom(CreatePlaylistViewModel.class)) {
            return (T) new CreatePlaylistViewModel(sallefyRepository);
        }
        if (modelClass.isAssignableFrom(UploadProfileImageViewModel.class)) {
            return (T) new UploadProfileImageViewModel(sallefyRepository);
        }
        if (modelClass.isAssignableFrom(StatisticsViewModel.class)) {
            return (T) new StatisticsViewModel(sallefyRepository);
        }
        if (modelClass.isAssignableFrom(ProfileTopTracksViewModel.class)) {
            return (T) new ProfileTopTracksViewModel(sallefyRepository);
        }
        if (modelClass.isAssignableFrom(TrackStatisticsViewModel.class)) {
            return (T) new TrackStatisticsViewModel(sallefyRepository);
        }


        throw new IllegalArgumentException("Unknown class!");
    }
}
