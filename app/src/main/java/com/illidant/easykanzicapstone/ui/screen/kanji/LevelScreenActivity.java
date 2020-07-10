package com.illidant.easykanzicapstone.ui.screen.kanji;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.illidant.easykanzicapstone.R;
import com.illidant.easykanzicapstone.domain.model.Kanji;

import java.util.ArrayList;
import java.util.List;

public class LevelScreenActivity extends AppCompatActivity {

    List<Kanji> lstKanji;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_screen);

        lstKanji = new ArrayList<>();
        lstKanji.add(new Kanji("bản", "bản", "bản" ,"本"));
        lstKanji.add(new Kanji("bản", "bản", "bản" ,"本"));
        lstKanji.add(new Kanji("bản", "bản", "bản" ,"本"));
        lstKanji.add(new Kanji("bản", "bản", "bản" ,"本"));
        lstKanji.add(new Kanji("bản", "bản", "bản" ,"本"));
        lstKanji.add(new Kanji("bản", "bản", "bản" ,"本"));
        lstKanji.add(new Kanji("bản", "bản", "bản" ,"本"));
        lstKanji.add(new Kanji("bản", "bản", "bản" ,"本"));
        lstKanji.add(new Kanji("bản", "bản", "bản" ,"本"));
        lstKanji.add(new Kanji("bản", "bản", "bản" ,"本"));
        lstKanji.add(new Kanji("bản", "bản", "bản" ,"本"));
        lstKanji.add(new Kanji("bản", "bản", "bản" ,"本"));
        lstKanji.add(new Kanji("bản", "bản", "bản" ,"本"));
        lstKanji.add(new Kanji("bản", "bản", "bản" ,"本"));
        lstKanji.add(new Kanji("bản", "bản", "bản" ,"本"));
        lstKanji.add(new Kanji("bản", "bản", "bản" ,"本"));
        lstKanji.add(new Kanji("bản", "bản", "bản" ,"本"));
        lstKanji.add(new Kanji("bản", "bản", "bản" ,"本"));
        lstKanji.add(new Kanji("bản", "bản", "bản" ,"本"));






        RecyclerView myrv = (RecyclerView) findViewById(R.id.recyclerview_id);
        RecyclerViewAdapter myAdapter = new RecyclerViewAdapter(this, lstKanji);
        myrv.setLayoutManager(new GridLayoutManager(this, 3));
        myrv.setAdapter(myAdapter);


    }
}