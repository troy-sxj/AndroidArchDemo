package com.mika.arch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.mika.lib.net.repository.impl.ApiRepositoryImpl;
import com.mika.lib.util.android.ArchLog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextView tvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvContent = findViewById(R.id.tv);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request();
            }
        });
    }


    private void request() {
        ApiRepositoryImpl apiTodayRepository = new ApiRepositoryImpl();
        apiTodayRepository.getToday().enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                tvContent.setText(response.body().getBytes().toString());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                ArchLog.e("aaa");
            }
        });
    }
}
