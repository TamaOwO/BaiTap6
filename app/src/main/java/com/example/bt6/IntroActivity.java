package com.example.bt6;

import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    List<String> rowsArrayList = new ArrayList<>();
    boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_intro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerView);
        populateData();
        initAdapter();
        initScrollListener();


//        // Tự động chuyển activity sau 2 giây
//        new Thread(() -> {
//            try {
//                Thread.sleep(20000); // Đợi 20 giây
//                runOnUiThread(() -> {
//                    // Chuyển sang LoginActivity
//                    Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
//                    startActivity(intent);
//                    finish(); // Đóng MainActivity
//                });
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }).start();

    }
    private void populateData(){
        int i = 0;
        while (i<10){
            rowsArrayList.add("Item "+i);
            i++;
        }
    }
    private void initAdapter(){
        recyclerViewAdapter = new RecyclerViewAdapter(rowsArrayList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        recyclerView.setAdapter((recyclerViewAdapter));
    }

    private void initScrollListener(){
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if(!isLoading){
                    if(linearLayoutManager != null && linearLayoutManager.findFirstCompletelyVisibleItemPosition()== rowsArrayList.size() - 1);
                    //bottom of list!
                    loadMore();
                    isLoading = true;
                }
            }
        });
    }
    private void loadMore(){
        rowsArrayList.add(null);
        recyclerViewAdapter.notifyItemInserted(rowsArrayList.size()-1);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int scrollPosition = rowsArrayList.size();
                recyclerViewAdapter.notifyItemRemoved(scrollPosition);
                int currentSize = scrollPosition;
                int nextLimit = currentSize + 10;

                while (currentSize - 1 < nextLimit){
                    rowsArrayList.add("Item "+currentSize);
                    currentSize++;
                }
                recyclerViewAdapter.notifyDataSetChanged();
                isLoading = false;
            }
        },2000);
    }
}