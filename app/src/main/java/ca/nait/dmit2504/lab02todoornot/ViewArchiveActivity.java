package ca.nait.dmit2504.lab02todoornot;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ViewArchiveActivity extends AppCompatActivity {
    private static final String BASE_URL= "http://www.youcode.ca/";
    private static final String USERNAME = "eron";
    private static final String PASSWORD = "miranda";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_archive);

        ListView archiveListView = findViewById(R.id.view_archive_listview);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        YoucodeTodoListService service = retrofit.create(YoucodeTodoListService.class);

        Call<String> getCall = service.listLab02ByUsernamePassword(USERNAME,PASSWORD);
        getCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String responseBody = response.body();
                ListItemAdapter listItemAdapter = new ListItemAdapter(getApplicationContext());
                String[] listItemArray = responseBody.split("\n");

                for (int index = 0; index < listItemArray.length; index+=4) {
                    ListItem currentItem  = new ListItem();
                    currentItem.setDate(listItemArray[index]);
                    currentItem.setListTitle(listItemArray[index + 1]);
                    currentItem.setListItemName(listItemArray[index + 2]);
                    currentItem.setIsComplete(listItemArray[index + 3]);
                    listItemAdapter.addItem(currentItem);
                }
                archiveListView.setAdapter(listItemAdapter);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.getCause();
                Toast.makeText(ViewArchiveActivity.this,"Fetch data was not successful", Toast.LENGTH_SHORT).show();
            }
        });
    }

}