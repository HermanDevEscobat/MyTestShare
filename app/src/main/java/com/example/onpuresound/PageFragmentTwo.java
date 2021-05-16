package com.example.onpuresound;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PageFragmentTwo extends Fragment {
    RecyclerView recyclerView;
    ArrayList<Song> tempSongList = new ArrayList<>();
    PageFragment fragment;


    String pathSound = null;
    SoundAdapter.OnSoundClickListener soundClickListener = new SoundAdapter.OnSoundClickListener() {
        @Override
        public void onSoundClick(@NonNull Song song, int position) {
            pathSound = tempSongList.get(position).getPath();
            Toast.makeText(PageFragmentTwo.this.getContext(), "Была выбрана песня: " + song.getTitle(), Toast.LENGTH_SHORT).show();
            ((MainActivity)getActivity()).getPosList(tempSongList,position); // отправляет лист реков в маин
            ((MainActivity)getActivity()).playSong(position); // запускает трек из маина

        }
    };
    SoundAdapter soundAdapter = new SoundAdapter(getActivity(), tempSongList, soundClickListener);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page_two, container, false);
        recyclerView = view.findViewById(R.id.rec_view_sound);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext()); // збс, уже не вылетает эта чухня (Гавнокод работает)
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(soundAdapter);
        GetListSongsMStore();
        return view;
    }

    //Здесь я попробую начать получение аудиофайлов! И да оно работает, как то easy
    public void GetListSongsMStore() {
        ContentResolver contentResolver = getActivity().getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        @SuppressLint("Recycle") Cursor musicCursor = contentResolver.query(musicUri, null, null, null);
        if (musicCursor != null && musicCursor.moveToFirst()) {
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            int albumId = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.ALBUM_ID);
            int data = musicCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            int album = musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_KEY);
            //add songs to list
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                long thisalbumId = musicCursor.getLong(albumId);
                String thisdata = musicCursor.getString(data);
                String AlbumKey = musicCursor.getString(album);
                tempSongList.add(new Song(thisId, thisTitle, thisArtist, thisalbumId, thisdata, AlbumKey));
            }
            while (musicCursor.moveToNext());
        }
    }
}
