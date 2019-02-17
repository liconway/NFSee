package com.example.nfctest;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class FavoriteActivity extends AppCompatActivity {

    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView = findViewById(R.id.list);

        ArrayList<FavoriteItem> favoriteItems = new ArrayList<>();
        ArrayList<String> favoriteList = new ArrayList<>();
        favoriteItems.add(new FavoriteItem("Test", "Other test"));
        favoriteItems.add(new FavoriteItem("more", "even more"));
        favoriteList.add(favoriteItems.get(0).toString());
        favoriteList.add(favoriteItems.get(0).toString());

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, favoriteList);
        listView.setAdapter(arrayAdapter);
    }

    class FavoriteItem{
        private String title;
        private String address;

        public FavoriteItem(String title, String address){
            this.title = title;
            this.address = address;
        }

        public String toString(){
           return title + "\n" + address;
        }
    }

}
