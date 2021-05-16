
package com.example.onpuresound;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends FragmentActivity {
    private SeekBar SeekB;
    private final Handler myHandler = new Handler();
    private ImageButton myImgBut;// объявление кнопки
    MediaPlayer mediaPlayer; // создание объекта медиаплеера
    Intent data; // данные полученные интент фильтром
    Uri uriSongOne; // данные URI из интент фильтра
    private static final int NUM_PAGES = 2; // количество страниц для ViewPager2
    ViewPager2 viewPager; // инициализация объекта
    FragmentStateAdapter pagerAdapter; // инициализция адаптера для ViewPager2
    int currentPos;
    ArrayList<Song> myListSong = new ArrayList<>();
    Bitmap bitmap;
    protected void onCreate(Bundle savedInstanceState)  // создание обэектов на экране
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SeekB = findViewById(R.id.SB7); // объявление сик бара
        myImgBut = findViewById(R.id.imageButton); // кнопка Play
        SeekB.setClickable(false);
        SeekB.setOnSeekBarChangeListener(seekList);
        mediaPlayer = new MediaPlayer();
        // ниже это все то что связано с слайдингом
        viewPager = findViewById(R.id.pager);
        pagerAdapter = new ScreenSlidePagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);
        /////
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                1); // <--- тут приложение запрашивате разрешение к доступу локального хранилища фалов
    }

    public void getPosList(ArrayList<Song> arrayList, int position) // получение списка треков и выбранной позиции из PageFragmentTwo
    {
        myListSong = arrayList;
        currentPos = position;
    }


    public void playSong(int pos) // Этот метод будет играть трек
    {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(myListSong.get(pos).getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            myImgBut.setImageResource(R.drawable.ic_baseline_pause_24);
            // Здесь надо добавить смену картинки
            bitmap = getPicture(myListSong.get(pos).getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Bitmap getImgSong()
    {
        return bitmap;
    }
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
    } // этот класс помогает получить картинку трека

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    // ниже у нас реализован фрагментадаптер
    private static class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        public ScreenSlidePagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if (position == 0) {
                return new PageFragment();
            } else {
                return new PageFragmentTwo();
            }
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }

    protected void onStart() {
        super.onStart();
        data = getIntent();
//        data = getIntent();
//        music = data.getData();
//        if (music != null) {
//            try {
//                mediaPlayer.setDataSource(this, music);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            try {
//                mediaPlayer.prepare();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } else {
//            myImgBut.setEnabled(true); // юлокирует нажатие кнопки если нет трека
//        }
    }

    private final Runnable UpdateSongTime = new Runnable() // метод отвечает за текущее положение СикБара
    {

        @Override
        public void run() {
            int startTime = mediaPlayer.getCurrentPosition();
            int finalTime = mediaPlayer.getDuration();
            SeekB.setProgress(startTime);
            int progress = (int) ((((double) startTime) / finalTime) * 100);
            SeekB.setProgress(progress);
            myHandler.postDelayed(this, 100);
        }
    };
    public SeekBar.OnSeekBarChangeListener seekList = new SeekBar.OnSeekBarChangeListener() { // SeekBar работает стабильно
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            myHandler.removeCallbacks(UpdateSongTime);
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            myHandler.removeCallbacks(UpdateSongTime);
            int totDur = mediaPlayer.getDuration();
            int currPos = progressToTimer(SeekB.getProgress(), totDur);
            mediaPlayer.seekTo(currPos);
            myHandler.postDelayed(UpdateSongTime, 100);
        }
    };

    public int progressToTimer(int progress, int totDur) {
        int currDur;
        totDur = totDur / 1000;
        currDur = (int) ((((double) progress) / 100) * totDur);
        return currDur * 1000;
    }

    public void onPlay(View v) // Метод обработки кнопки Play
    {
        myHandler.postDelayed(UpdateSongTime, 100);
        if (mediaPlayer.isPlaying()) {
            if (mediaPlayer != null) // плеер принимает 1 или 0
            {
                mediaPlayer.pause();
                myImgBut.setImageResource(R.drawable.ic_baseline_play_arrow_24);
            }
        } else {
            if (mediaPlayer != null) {
                mediaPlayer.start();
                myImgBut.setImageResource(R.drawable.ic_baseline_pause_24);
            }
        }
    }

    public void stopPlayer() // Тут приложение отходит на задний план, когда нам кто то звонит или какоето уведомление
    {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onStop() // Тут приложение свернуто и находится в режиме ожидания
    {
        super.onStop();
        stopPlayer();
    }

    @Override
    protected void onDestroy() // Уничтожение приложения
    {
        super.onDestroy();
    }
}
