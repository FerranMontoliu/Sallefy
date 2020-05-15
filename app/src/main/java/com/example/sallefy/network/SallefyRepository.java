package com.example.sallefy.network;

import android.content.DialogInterface;

import com.example.sallefy.auth.TokenManager;
import com.example.sallefy.model.Followed;
import com.example.sallefy.model.Genre;
import com.example.sallefy.model.Liked;
import com.example.sallefy.model.PasswordChange;
import com.example.sallefy.model.Playlist;
import com.example.sallefy.model.Search;
import com.example.sallefy.model.Track;
import com.example.sallefy.model.User;
import com.example.sallefy.model.UserLogin;
import com.example.sallefy.model.UserRegister;
import com.example.sallefy.model.UserToken;
import com.example.sallefy.network.callback.CreatePlaylistCallback;
import com.example.sallefy.network.callback.CreateTrackCallback;
import com.example.sallefy.network.callback.FollowCheckCallback;
import com.example.sallefy.network.callback.FollowToggleCallback;
import com.example.sallefy.network.callback.GenreCallback;
import com.example.sallefy.network.callback.GetPlaylistsCallback;
import com.example.sallefy.network.callback.GetTracksCallback;
import com.example.sallefy.network.callback.GetUserCallback;
import com.example.sallefy.network.callback.GetUsersCallback;
import com.example.sallefy.network.callback.LikeTrackCallback;
import com.example.sallefy.network.callback.LoginCallback;
import com.example.sallefy.network.callback.PasswordChangeCallback;
import com.example.sallefy.network.callback.PlaylistCallback;
import com.example.sallefy.network.callback.RegisterCallback;
import com.example.sallefy.network.callback.SearchCallback;
import com.example.sallefy.network.callback.TrackCallback;
import com.example.sallefy.network.callback.DeleteUserCallback;
import com.example.sallefy.auth.Session;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


@Singleton
public class SallefyRepository {
    private static final String TAG = SallefyRepository.class.getName();

    private SallefyService service;
    private TokenManager tokenManager;

    @Inject
    public SallefyRepository(SallefyService service) {
        this.service = service;
        this.tokenManager = TokenManager.getInstance();
    }


    // GENRES ENDPOINT
    public synchronized void getAllGenres(final GenreCallback callback) {
        service.getAllGenres().enqueue(new Callback<List<Genre>>() {
            @Override
            public void onResponse(Call<List<Genre>> call, Response<List<Genre>> response) {
                if (response.isSuccessful()) {
                    callback.onGenresReceived(response.body());
                } else {
                    callback.onNoGenres(new Throwable(String.valueOf(response.errorBody())));
                }
            }

            @Override
            public void onFailure(Call<List<Genre>> call, Throwable t) {
                callback.onNoGenres(t);
            }
        });
    }


    // PLAYLISTS ENDPOINT
    public synchronized void createPlaylist(Playlist playlist, final CreatePlaylistCallback callback) {
        service.createPlaylist(playlist).enqueue(new Callback<Playlist>() {
            @Override
            public void onResponse(Call<Playlist> call, Response<Playlist> response) {
                if (response.isSuccessful()) {
                    callback.onPlaylistCreated();
                } else {
                    callback.onFailure(new Throwable(String.valueOf(response.errorBody())));
                }
            }

            @Override
            public void onFailure(Call<Playlist> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    public synchronized void getPlaylistById(Integer idPlaylist, final PlaylistCallback callback) {
        service.getPlaylistById(idPlaylist).enqueue(new Callback<Playlist>() {
            @Override
            public void onResponse(Call<Playlist> call, Response<Playlist> response) {
                if (response.isSuccessful()) {
                    callback.onPlaylistReceived(response.body());
                } else {
                    callback.onPlaylistNotReceived(new Throwable(String.valueOf(response.errorBody())));
                }
            }

            @Override
            public void onFailure(Call<Playlist> call, Throwable t) {
                callback.onPlaylistNotReceived(t);
            }
        });
    }

    public synchronized void getAllPlaylistsByMostRecent(final GetPlaylistsCallback callback) {
        service.getAllPlaylistsByMostRecent().enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                if (response.isSuccessful()) {
                    callback.onPlaylistsReceived(response.body());
                } else {
                    callback.onFailure(new Throwable(String.valueOf(response.errorBody())));
                }
            }

            @Override
            public void onFailure(Call<List<Playlist>> call, Throwable t) {
                callback.onFailure(t);

            }
        });
    }

    public synchronized void getAllPlaylistsByMostFollowed(final GetPlaylistsCallback callback) {
        service.getAllPlaylistsByMostFollowed().enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                if (response.isSuccessful()) {
                    callback.onPlaylistsReceived(response.body());
                } else {
                    callback.onFailure(new Throwable(String.valueOf(response.errorBody())));
                }
            }

            @Override
            public void onFailure(Call<List<Playlist>> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    public synchronized void updatePlaylist(Playlist playlist, final PlaylistCallback callback) {
        service.updatePlaylist(playlist).enqueue(new Callback<Playlist>() {
            @Override
            public void onResponse(Call<Playlist> call, Response<Playlist> response) {
                if (response.isSuccessful()) {
                    callback.onPlaylistUpdated(response.body());
                } else {
                    callback.onPlaylistNotUpdated(new Throwable(String.valueOf(response.errorBody())));
                }
            }

            @Override
            public void onFailure(Call<Playlist> call, Throwable t) {
                callback.onPlaylistNotUpdated(t);
            }
        });
    }

    public synchronized void followPlaylistToggle(Playlist playlist, final FollowToggleCallback callback) {
        service.followPlaylist(playlist.getId().toString()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    callback.onObjectFollowChanged();
                } else {
                    callback.onFailure(new Throwable(String.valueOf(response.errorBody())));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onFailure(t);

            }
        });
    }


    public synchronized void isPlaylistFollowed(Playlist playlist, final FollowCheckCallback callback) {
        service.isPlaylistFollowed(playlist.getId().toString()).enqueue(new Callback<Followed>() {
            @Override
            public void onResponse(Call<Followed> call, Response<Followed> response) {
                if (response.isSuccessful()) {
                    callback.onObjectFollowedReceived(response.body().getFollowed());
                } else {
                    callback.onFailure(new Throwable(String.valueOf(response.errorBody())));
                }
            }

            @Override
            public void onFailure(Call<Followed> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }


    // USERS ENDPOINT
    public synchronized void getUserById(String login, final GetUserCallback callback) {
        service.getUserById(login).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    callback.onUserReceived(response.body());
                } else {
                    callback.onFailure(new Throwable(String.valueOf(response.errorBody())));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    public synchronized void getActualUser(final GetUserCallback callback) {
        service.getUserById(Session.getUser().getLogin()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    callback.onUserReceived(response.body());
                } else {
                    callback.onFailure(new Throwable(String.valueOf(response.errorBody())));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }


    // SEARCH ENDPOINT
    public synchronized void getSearchResults(String search, final SearchCallback callback) {
        service.getSearchResults(search).enqueue(new Callback<Search>() {
            @Override
            public void onResponse(Call<Search> call, Response<Search> response) {
                if (response.isSuccessful()) {
                    callback.onSearchResultsReceived(response.body());
                } else {
                    callback.onFailure(new Throwable(String.valueOf(response.errorBody())));
                }

            }

            @Override
            public void onFailure(Call<Search> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }


    // TRACKS ENDPOINT
    public synchronized void getAllTracks(final TrackCallback callback) {
        service.getAllTracks().enqueue(new Callback<List<Track>>() {
            @Override
            public void onResponse(Call<List<Track>> call, Response<List<Track>> response) {
                if (response.isSuccessful()) {
                    callback.onTracksReceived(response.body());
                } else {
                    callback.onNoTracks(new Throwable(String.valueOf(response.errorBody())));
                }
            }

            @Override
            public void onFailure(Call<List<Track>> call, Throwable t) {
                callback.onNoTracks(t);
            }
        });
    }

    public synchronized void isUserFollowed(String username, final FollowCheckCallback callback) {
        service.isUserFollowed(username).enqueue(new Callback<Followed>() {
            @Override
            public void onResponse(Call<Followed> call, Response<Followed> response) {
                if (response.isSuccessful()) {
                    callback.onObjectFollowedReceived(response.body().getFollowed());
                } else {
                    callback.onFailure(new Throwable(String.valueOf(response.errorBody())));
                }
            }

            @Override
            public void onFailure(Call<Followed> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    public synchronized void followUserToggle(String username, final FollowToggleCallback callback) {
        service.followUserToggle(username).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                int code = response.code();
                if (response.isSuccessful()) {
                    callback.onObjectFollowChanged();
                } else {
                    callback.onFailure(new Throwable(String.valueOf(response.errorBody())));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    public synchronized void getUserTracks(String username, final GetTracksCallback callback) {
        service.getUserTracks(username).enqueue(new Callback<List<Track>>() {
            @Override
            public void onResponse(Call<List<Track>> call, Response<List<Track>> response) {
                if (response.isSuccessful()) {
                    callback.onTracksReceived(response.body());
                } else {
                    callback.onFailure(new Throwable(String.valueOf(response.errorBody())));
                }
            }

            @Override
            public void onFailure(Call<List<Track>> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    public synchronized void getUserPlaylists(String username, final GetPlaylistsCallback callback) {
        service.getUserPlaylists(username).enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                if (response.isSuccessful()) {
                    callback.onPlaylistsReceived(response.body());
                } else {
                    callback.onFailure(new Throwable(String.valueOf(response.errorBody())));
                }
            }

            @Override
            public void onFailure(Call<List<Playlist>> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    public synchronized void deleteUser(final DeleteUserCallback callback) {
        service.deleteUser().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    callback.onAccountDeleted();
                } else {
                    callback.onFailure(new Throwable(String.valueOf(response.errorBody())));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    public synchronized void likeTrack(Track track, final LikeTrackCallback callback) {
        service.likeTrack(track.getId().toString()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    callback.onTrackLiked();
                } else {
                    callback.onFailure(new Throwable(String.valueOf(response.errorBody())));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    public synchronized void isTrackLiked(Track track, final TrackCallback callback, final int position) {
        service.isTrackLiked(track.getId().toString()).enqueue(new Callback<Liked>() {
            @Override
            public void onResponse(Call<Liked> call, Response<Liked> response) {
                if (response.isSuccessful()) {
                    callback.onTrackLikedReceived(response.body(), position);
                } else {
                    callback.onFailure(new Throwable(String.valueOf(response.errorBody())));
                }
            }

            @Override
            public void onFailure(Call<Liked> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    public synchronized void createTrack(Track track, final CreateTrackCallback callback) {
        service.createTrack(track).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    callback.onTrackCreated();
                } else {
                    callback.onFailure(new Throwable(String.valueOf(response.errorBody())));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }


    // ACCOUNT ENDPOINT
    public synchronized void changePassword(final PasswordChange passwordChange, final DialogInterface dialog, final PasswordChangeCallback callback) {
        service.changePassword(passwordChange).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                int code = response.code();
                if (response.isSuccessful()) {
                    callback.onPasswordChanged(dialog);
                } else {
                    callback.onFailure(new Throwable(String.valueOf(response.errorBody())));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }


    // REGISTER ENDPOINT
    public synchronized void registerAttempt(String email, String username, String password, final RegisterCallback callback) {
        service.registerUser(new UserRegister(email, username, password)).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    callback.onRegisterSuccess();
                } else {
                    callback.onRegisterFailure(new Throwable(String.valueOf(response.errorBody())));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }


    // AUTHENTICATE ENDPOINT
    public synchronized void loginAttempt(String username, String password, final LoginCallback callback) {
        service.loginUser(new UserLogin(username, password, true)).enqueue(new Callback<UserToken>() {
            @Override
            public void onResponse(Call<UserToken> call, Response<UserToken> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    tokenManager.setToken(response.body().getIdToken());
                    callback.onLoginSuccess(response.body());
                } else {
                    callback.onLoginFailure(new Throwable(String.valueOf(response.errorBody())));
                }
            }

            @Override
            public void onFailure(Call<UserToken> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }


    // ME ENDPOINT
    public synchronized void getOwnPlaylists(final GetPlaylistsCallback callback) {
        service.getOwnPlaylists().enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                if (response.isSuccessful()) {
                    callback.onPlaylistsReceived(response.body());
                } else {
                    callback.onFailure(new Throwable(String.valueOf(response.errorBody())));
                }
            }

            @Override
            public void onFailure(Call<List<Playlist>> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    public synchronized void getOwnTracks(final GetTracksCallback callback) {
        service.getOwnTracks().enqueue(new Callback<List<Track>>() {
            @Override
            public void onResponse(Call<List<Track>> call, Response<List<Track>> response) {
                if (response.isSuccessful()) {
                    callback.onTracksReceived(response.body());
                } else {
                    callback.onFailure(new Throwable(String.valueOf(response.errorBody())));
                }
            }

            @Override
            public void onFailure(Call<List<Track>> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    public synchronized void getFollowers(final GetUsersCallback callback) {
        service.getFollowers().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    callback.onUsersReceived(response.body());
                } else {
                    callback.onFailure(new Throwable(String.valueOf(response.errorBody())));
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }


    public synchronized void getFollowings(final GetUsersCallback callback) {
        service.getFollowings().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    callback.onUsersReceived(response.body());
                } else {
                    callback.onFailure(new Throwable(String.valueOf(response.errorBody())));
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    public synchronized void getAllFollowedPlaylists(final GetPlaylistsCallback callback) {
        service.getAllFollowedPlaylists().enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                if (response.isSuccessful()) {
                    callback.onPlaylistsReceived(response.body());
                } else {
                    callback.onFailure(new Throwable(String.valueOf(response.errorBody())));
                }
            }

            @Override
            public void onFailure(Call<List<Playlist>> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }
}
