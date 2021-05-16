package com.example.onpuresound;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

// Тут представлен фрагмент страниы который будет проходить через адаптер, макет данного фрагмента fragment_page.xml
public class PageFragment extends Fragment {
    ImageView imageView;
    View view;
    Bitmap bitmap;
    final String TAG = "StatusMyLifeC";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Происходит создание");

    }

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(
                R.layout.fragment_page, container, false);
        imageView = view.findViewById(R.id.artSong);
        Log.d(TAG, "Происходит надувание View");
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "Перешёл в ViewCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "Перешёл в Start");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "Перешёл в Resume");
//        imageView.setImageBitmap(((MainActivity) getActivity()).getImgSong());
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "Перешёл в Pause");
//        imageView.setImageResource(R.drawable.cube);

    }


}