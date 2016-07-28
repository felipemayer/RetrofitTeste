package com.felipemayer.retrofitteste;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.felipemayer.retrofitteste.models.Course;
import com.felipemayer.retrofitteste.models.Instructor;
import com.felipemayer.retrofitteste.models.UdacityCatalog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView name = (TextView) findViewById(R.id.name);

        // criando um objeto retrofit com base na url
        // convertendo usando o Gson
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UdacityService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        // criando uma chamada para o objeto UdacityCatalogo usando a interface UdacityService
        UdacityService service = retrofit.create(UdacityService.class);
        Call<UdacityCatalog> requestCatalog = service.listCatalog();

        // fazendo a chamada assíncrona do objeto UdacityCatalog
        requestCatalog.enqueue(new Callback<UdacityCatalog>() {
            @Override
            public void onResponse(Call<UdacityCatalog> call, Response<UdacityCatalog> response) {
                if (!response.isSuccessful()){
                    Log.e(TAG,  "erro: " + response.code());
                } else {
                    UdacityCatalog catalog = response.body();

                    for (Course c : catalog.courses) {
                        Log.d(TAG, String.format("%s: %s", c.title, c.subtitle));
                        name.setText(c.title);

                        for (Instructor i : c.instructors){
                            Log.d(TAG, String.format("%s", i.name));
                        }
                        Log.d(TAG, "---------------");
                    }
                }
            }

            @Override
            public void onFailure(Call<UdacityCatalog> call, Throwable t) {
                Log.e(TAG,  "error: " + t.getMessage());
            }
        });

    }
}
