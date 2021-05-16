package com.example.onpuresound;

import android.os.Parcel;
import android.os.Parcelable;

public class Song implements Parcelable {
    private long id;
    private String title;
    private String artist, data, album;
    private long alid;

    public Song(long songID, String songTitle, String songArtist, long albumID, String thisdata, String AlbumKey) {
        id = songID;
        title = songTitle;
        artist = songArtist;
        alid = albumID;
        data = thisdata;
        album = AlbumKey;

    }

    public Song() {

    }

    public long getID() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public long getAlbumID() {
        return alid;
    }

    public String getPath() {
        return data;
    }

    public String getAlbumKey() {
        return album;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(artist);
        dest.writeLong(alid);
        dest.writeLong(id);
        dest.writeString(data);
        dest.writeString(album);
    }

    public static final Parcelable.Creator<Song> CREATOR = new Parcelable.Creator<Song>() {
        public Song createFromParcel(Parcel in) {
            Song song = new Song();
            song.title = in.readString();
            song.artist = in.readString();
            song.alid = in.readLong();
            song.id = in.readLong();
            song.data = in.readString();
            song.album = in.readString();
            return song;
        }

        public Song[] newArray(int size) {
            return new Song[size];
        }
    };
}
