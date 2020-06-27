package ca.nait.dmit2504.lab02todoornot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.fragment.app.DialogFragment;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private EditText mListTitleEditText;
    private Button mAddTitleButton;
    private Spinner mListTitleSpinner;
    private EditText mListItemEditText;
    private EditText mDateEditText;
    private Button mAddItemButton;
    private Button mUpdateItemButton;
    private Button mDeleteItemButton;
    private Button mArchiveItemButton;
    private Button mCompleteItemButton;
    private ListView mListItemListView;

    private long mEditId = 0;
    private long mSelectedListTitleId;
    private boolean mEditMode = false;

    private static final String BASE_URL= "http://www.youcode.ca/";


    private TodoListDB mTodoDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListTitleEditText = findViewById(R.id.activity_main_list_title_edittext);
        mAddTitleButton = findViewById(R.id.activity_main_add_title_btn);
        mListTitleSpinner = findViewById(R.id.activity_main_list_title_spinner);
        mListItemEditText = findViewById(R.id.activity_main_list_item_name_edittext);
        mDateEditText = findViewById(R.id.activity_main_date_editTextDate);
        mListItemListView = findViewById(R.id.activity_main_list_item_listview);

        mAddItemButton = findViewById(R.id.activity_main_add_item_button);
        mUpdateItemButton = findViewById(R.id.activity_main_update_item_Button);
        mDeleteItemButton = findViewById(R.id.activity_main_delete_item_button);
        mArchiveItemButton =  findViewById(R.id.activity_main_archive_button);
        mCompleteItemButton = findViewById(R.id.activity_main_complete_button);

        mTodoDatabase = new TodoListDB(this);


        mUpdateItemButton.setVisibility(View.GONE);
        mDeleteItemButton.setVisibility(View.GONE);
        mArchiveItemButton.setVisibility(View.GONE);
        mCompleteItemButton.setVisibility(View.GONE);
        mDateEditText.setVisibility(View.GONE);



        mAddTitleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titleName = mListTitleEditText.getText().toString();
                mTodoDatabase.createListTitle(titleName);
                mListTitleEditText.setText("");
                bindDataToListTitleSpinner();
            }
        });

        mListItemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListItem singleResult = mTodoDatabase.findListItem(id);
                if (singleResult != null && !mEditMode) {
                    mEditMode = true;
                    mEditId = id;
                    mAddItemButton.setVisibility(View.GONE);
                    mUpdateItemButton.setVisibility(View.VISIBLE);
                    mDeleteItemButton.setVisibility(View.VISIBLE);
                    mArchiveItemButton.setVisibility(View.VISIBLE);
                    mCompleteItemButton.setVisibility(View.VISIBLE);
                    mDateEditText.setVisibility(View.VISIBLE);

                    mListItemEditText.setText(singleResult.getListItemName());
                    mDateEditText.setText(singleResult.getDate());
                    if(singleResult.getIsComplete().equals("Completed")){
                        mCompleteItemButton.setText(R.string.not_completed_text);
                    }
                    else{
                        mCompleteItemButton.setText(R.string.complete_button_text);
                    }
                }
                else{
                    cancelEditMode();
                }

            }
        });



        bindDataToListTitleSpinner();
        rebindListItemView();
        mListTitleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<ListTitle> listTitles = mTodoDatabase.getAllListTitlePOJO();
                ListTitle selectedTitle = listTitles.get(position);
                mListTitleEditText.setText(selectedTitle.getTitleName());
                mSelectedListTitleId = selectedTitle.getId();
                rebindListItemView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void bindDataToListTitleSpinner() {
        ArrayList<ListTitle> titles = mTodoDatabase.getAllListTitlePOJO();
        ArrayList<String> titleNames = new ArrayList<>();
        for (ListTitle singleCategory : titles) {
            titleNames.add(singleCategory.getTitleName());
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                this,
                R.layout.spinner_item,
                titleNames
        );
        mListTitleSpinner.setAdapter(spinnerAdapter);
    }
    private void rebindListItemView(){
        Cursor dbCursor = mTodoDatabase.getAllListItemByTitle(mSelectedListTitleId);
        // Define an array of columns names used by the cursor
        String[] fromFields = {"listItemName", "date", "isComplete"};
        // Define an array of resource ids in the listview item layout
        int[] toViews = new int[] {
                R.id.listview_item_name,
                R.id.listview_item_date,
                R.id.listview_item_complete
        };
        // Create a SimpleCursorAdapter for the ListView
        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this,
                R.layout.listview_item,
                dbCursor,
                fromFields,
                toViews,
                0);
        mListItemListView.setAdapter(cursorAdapter);
    }
    public void onAddListItem(View view){
        String itemName = mListItemEditText.getText().toString();
        String date = mDateEditText.getText().toString();
        //add more

        StringBuilder stringBuilder = new StringBuilder();
        if(itemName.isEmpty()){
            stringBuilder.append("Item name value is required\n");
        }
        if (stringBuilder.length() > 0) {
            Toast.makeText(this, stringBuilder.toString(), Toast.LENGTH_LONG).show();
        } else {
            mTodoDatabase.createListItem(itemName, String.valueOf(mSelectedListTitleId));
            Toast.makeText(this, getResources().getString(R.string.create_new_record, mEditId), Toast.LENGTH_SHORT ).show();

            mListItemEditText.setText("");
            mDateEditText.setText("");

            rebindListItemView();
        }
    }
    public void onUpdateListItem(View view) {
        // Retrieve the value from the input views for description, amount, date
        String itemName = mListItemEditText.getText().toString();
        String date = mDateEditText.getText().toString();

        StringBuilder stringBuilder = new StringBuilder();
        if(itemName.isEmpty()){
            stringBuilder.append("Item name value is required\n");
        }
        if (date.isEmpty()) {
            stringBuilder.append("Date value is required. \n");
        }
        if (stringBuilder.length() > 0) {
            Toast.makeText(this, stringBuilder.toString(), Toast.LENGTH_LONG).show();
        } else {
            // save the record to the database
            long rowsUpdated = mTodoDatabase.updateListItem(mEditId, itemName, date);
            if (rowsUpdated == 1) {
                Toast.makeText(this, getResources().getString(R.string.update_record, mEditId), Toast.LENGTH_SHORT ).show();
            } else {
                Toast.makeText(this, "Update was not successful", Toast.LENGTH_SHORT ).show();
            }
            // clear the text in the input views
            mListItemEditText.setText("");
            mDateEditText.setText("");

            rebindListItemView();
            cancelEditMode();
        }

    }
    public void cancelEditMode(){
        mEditMode = false;
        mAddItemButton.setVisibility(View.VISIBLE);
        mUpdateItemButton.setVisibility(View.GONE);
        mDeleteItemButton.setVisibility(View.GONE);
        mArchiveItemButton.setVisibility(View.GONE);
        mCompleteItemButton.setVisibility(View.GONE);
        mDateEditText.setVisibility(View.GONE);

        mListItemEditText.setText("");
        mDateEditText.setText("");
    }
    public void onCompleteListItem(View view){
        String itemName = mListItemEditText.getText().toString();
        String date = mDateEditText.getText().toString();

        StringBuilder stringBuilder = new StringBuilder();
        if(itemName.isEmpty()){
            stringBuilder.append("Item name value is required\n");
        }
        if (date.isEmpty()) {
            stringBuilder.append("Date value is required. \n");
        }
        if (stringBuilder.length() > 0) {
            Toast.makeText(this, stringBuilder.toString(), Toast.LENGTH_LONG).show();
        } else {
            String isComplete;
            if(mCompleteItemButton.getText().toString().equals("Complete")){
                isComplete = "Completed";
            }
            else{
                isComplete = "Incomplete";
            }
            long rowsUpdated = mTodoDatabase.completeListItem(mEditId, isComplete);
            if (rowsUpdated == 1) {
                Toast.makeText(this, getResources().getString(R.string.complete_list, mEditId), Toast.LENGTH_SHORT ).show();
            } else {
                Toast.makeText(this, "Completion was not successful", Toast.LENGTH_SHORT ).show();
            }

            mListItemEditText.setText("");
            mDateEditText.setText("");

            rebindListItemView();
            cancelEditMode();
        }
    }

    public void onDeleteListItem(View view){
        deleteItem(mEditId);
    }

    public void deleteItem(long editId){
        long rowsDeleted = mTodoDatabase.deleteListItem(editId);
        if(rowsDeleted==1){
            Toast.makeText(this, getResources().getString(R.string.delete_record), Toast.LENGTH_SHORT ).show();
        }
        else{
            Toast.makeText(this, "Delete was not successful", Toast.LENGTH_SHORT ).show();
        }
        rebindListItemView();
        cancelEditMode();
    }

    public void onArchiveListItem(View view){
      ListItem singleResult = mTodoDatabase.findListItem(mEditId);
      long titleId = Long.parseLong(singleResult.getTitleId());
      ListTitle titleResult = mTodoDatabase.findListTitle(titleId);
      String listTitle = titleResult.getTitleName();
      String listItem = singleResult.getListItemName();
      String completed_flag = singleResult.getIsComplete().equals("Completed") ? "1" : "0";;
      String username = "eron";
      String password = "miranda";
      String date = singleResult.getDate();

        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
            YoucodeTodoListService service = retrofit.create(YoucodeTodoListService.class);
            Call<String> postCall = service.archiveListItem(listTitle, listItem, completed_flag, username, password,date);
            postCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Log.i(TAG, "Post was successful");

                    if (response.isSuccessful()) {
                        deleteItem(mEditId);
                        Toast.makeText(MainActivity.this, "Post was successful", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Invalid URL. Cannot access the given URL.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e(TAG, "Error calling web service");
                    Toast.makeText(MainActivity.this, "Error  calling web service", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });
        }
        catch (Exception e){
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}