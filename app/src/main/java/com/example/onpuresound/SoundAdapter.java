package com.example.onpuresound;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;


public class SoundAdapter extends RecyclerView.Adapter<SoundAdapter.ViewHolder> {
    private final ArrayList<Song> arrayList;
    Context context;

    //снизу реализация интерфейса
    public interface OnSoundClickListener {
        void onSoundClick(Song song, int position) throws IOException;
    }

    private final OnSoundClickListener onClickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        TextView textView2;
        CardView cardView;
        ImageView imageView;
        View viewList;

        public ViewHolder(View view) {
            super(view);
            cardView = view.findViewById(R.id.card_view);
            textView = view.findViewById(R.id.titleSong);
            imageView = view.findViewById(R.id.person_photo);
            textView2 = view.findViewById(R.id.person_age);
            viewList = view;
        }
    }

    // тут мы напишим получение картинки нашего трека
    public Bitmap getPicture(String pathSound) {
        Bitmap bitmap = null;
        MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();
        try {
            metadataRetriever.setDataSource(pathSound);
            byte[] embPic = metadataRetriever.getEmbeddedPicture();
            bitmap = BitmapFactory.decodeByteArray(embPic, 0, embPic.length);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                metadataRetriever.release();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return bitmap;
    }

    public SoundAdapter(Context context, ArrayList<Song> arrayList, OnSoundClickListener onClickListener) {
        this.context = context;
        this.arrayList = arrayList;
        this.onClickListener = onClickListener; // тут записывает
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Song song = arrayList.get(position);
        viewHolder.textView.setText(arrayList.get(position).getTitle());
        viewHolder.textView2.setText(arrayList.get(position).getArtist());
        viewHolder.imageView.setImageBitmap(getPicture(arrayList.get(position).getPath()));
        viewHolder.viewList.setOnClickListener(v -> {
            try {
                onClickListener.onSoundClick(song, position);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }); // надо будет разобраться как это работает
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}




