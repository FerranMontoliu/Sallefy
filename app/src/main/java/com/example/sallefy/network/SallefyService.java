package com.example.sallefy.network;

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

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SallefyService {

    // GENRES ENDPOINT
    @GET("genres")
    Call<List<Genre>> getAllGenres();


    // PLAYLISTS ENDPOINT
    @POST("playlists")
    Call<Playlist> createPlaylist(@Body Playlist playlist);

    @GET("playlists/{id}")
    Call<Playlist> getPlaylistById(@Path("id") Integer id);

    @GET("playlists?recent=true")
    Call<List<Playlist>> getAllPlaylistsByMostRecent();

    @GET("playlists?popular=true")
    Call<List<Playlist>> getAllPlaylistsByMostFollowed();

    @PUT("playlists")
    Call<Playlist> updatePlaylist(@Body Playlist playlist);

    @PUT("playlists/{id}/follow")
    Call<ResponseBody> followPlaylist(@Path("id") String id);

    @GET("playlists/{id}/follow")
    Call<Followed> isPlaylistFollowed(@Path("id") String id);


    // USERS ENDPOINT
    @GET("users/{login}")
    Call<User> getUserById(@Path("login") String login);

    @GET("users/{login}/follow")
    Call<Followed> isUserFollowed(@Path("login") String login);

    @PUT("users/{login}/follow")
    Call<ResponseBody> followUserToggle(@Path("login") String login);

    @GET("users/{login}/tracks")
    Call<List<Track>> getUserTracks(@Path("login") String login);

    @GET("users/{login}/playlists")
    Call<List<Playlist>> getUserPlaylists(@Path("login") String login);

    @DELETE("users/{login}")
    Call<ResponseBody> deleteUser(@Path("login") String login);


    // SEARCH ENDPOINT
    @GET("search")
    Call<Search> getSearchResults(@Query("keyword") String keyword);


    // TRACKS ENDPOINT
    @GET("tracks")
    Call<List<Track>> getAllTracks();

    @PUT("tracks/{id}/like")
    Call<ResponseBody> likeTrack(@Path("id") String id);

    @GET("tracks/{id}/like")
    Call<Liked> isTrackLiked(@Path("id") String id);

    @POST("tracks")
    Call<ResponseBody> createTrack(@Body Track track);


    // ACCOUNT ENDPOINT
    @POST("account/change-password")
    Call<ResponseBody> changePassword(@Body PasswordChange passwordChange);


    // REGISTER ENDPOINT
    @POST("register")
    Call<ResponseBody> registerUser(@Body UserRegister user);


    // AUTHENTICATE ENDPOINT
    @POST("authenticate")
    Call<UserToken> loginUser(@Body UserLogin login);


    // ME ENDPOINT
    @GET("me/playlists")
    Call<List<Playlist>> getOwnPlaylists();

    @GET("me/tracks")
    Call<List<Track>> getOwnTracks();

    @GET("me/followers")
    Call<List<User>> getFollowers();

    @GET("me/followings")
    Call<List<User>> getFollowings();
}
