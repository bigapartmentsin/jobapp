package com.abln.futur.common.models;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abln.futur.R;

import java.util.Arrays;
import java.util.List;

public class model extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.model);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        BookAdapter bookAdapter = new BookAdapter(getBooks(), this);
        recyclerView.setAdapter(bookAdapter);
    }

    public List<BookModel> getBooks() {
        return Arrays.asList(new BookModel("Harry Porter"), new BookModel("Never Let Me Go"), new BookModel("Insurgent"), new BookModel("Babel"), new BookModel("History of World War"), new BookModel("Insufficient Boy"), new BookModel("If I Stay"), new BookModel("Here I'm"), new BookModel("World Partition"), new BookModel("Stylish Amy"));
    }
}
