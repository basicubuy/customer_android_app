package com.ubuyng.app;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ubuyng.app.ubuyapi.db.DatabaseHelper;
import com.ubuyng.app.ubuyapi.util.Tools;

public class SubmitTaskActivity extends AppCompatActivity {
    DatabaseHelper databaseHelper;
    int database_size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.terms_activity);
        initToolbar();
        databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();

    }
    private void initToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Terms of Use");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this);

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("WE_HAVE_DB", String.valueOf(database_size));
//        for(int i = 0; i < mListItem.size(); i++)
//        {
//            ItemFeed itemProperty=mListItem.get(i);
//            ItemFeed objItem = new ItemFeed();
//            objItem.setFeedId(itemProperty.getFeedId());
//            objItem.setFeedTitle(itemProperty.getFeedTitle());
//            objItem.setFeedDes(itemProperty.getFeedDes());
//            objItem.setFeedFeaturedPic(itemProperty.getFeedFeaturedPic());
//            Log.d(TAG, "offlogger: "+ itemProperty.getFeedTitle());
//            mListItem2.add(objItem);
//        }
    }


}
