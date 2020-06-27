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
//    private static final String BASE_URL= "http://www.youcode.ca/";
//    private static final String USERNAME = "eron";
//    private static final String PASSWORD = "miranda";
    private String mUsername;
    private String mPassword;
    private String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_archive);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent() != null && getIntent().hasExtra("username")&& getIntent().hasExtra("password")) {
            mUsername = getIntent().getStringExtra("username");
            mPassword = getIntent().getStringExtra("password");
            mUrl = getIntent().getStringExtra("url");
        }

        ListView archiveListView = findViewById(R.id.view_archive_listview);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        YoucodeTodoListService service = retrofit.create(YoucodeTodoListService.class);

        Call<String> getCall = service.listLab02ByUsernamePassword(mUsername,mPassword);
        getCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String responseBody = response.body();
                ArchiveListItemAdapter archiveListItemAdapter = new ArchiveListItemAdapter(getApplicationContext());
                String[] listItemArray = responseBody.split("\n");

                if(!response.body().equals("")){
                    for (int index = 0; index < listItemArray.length; index+=4) {
                        ListItem currentItem  = new ListItem();
                        currentItem.setDate(listItemArray[index]);
                        currentItem.setListTitle(listItemArray[index + 1]);
                        currentItem.setListItemName(listItemArray[index + 2]);
                        currentItem.setIsComplete(listItemArray[index + 3]);
                        archiveListItemAdapter.addItem(currentItem);
                    }
                    archiveListView.setAdapter(archiveListItemAdapter);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.getCause();
                Toast.makeText(ViewArchiveActivity.this,"Fetch data was not successful", Toast.LENGTH_SHORT).show();
            }
        });
    }

}