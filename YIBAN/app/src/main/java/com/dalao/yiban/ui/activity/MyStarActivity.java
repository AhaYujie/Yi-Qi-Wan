package com.dalao.yiban.ui.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dalao.yiban.R;
import com.dalao.yiban.db.MyStar;
import com.dalao.yiban.ui.adapter.MyStarAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyStarActivity extends AppCompatActivity {

    List<MyStar> myStarList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_star);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.my_star_RecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        MyStarAdapter myStarAdapter = new MyStarAdapter(myStarList);
        recyclerView.setAdapter(myStarAdapter);

    }
}

