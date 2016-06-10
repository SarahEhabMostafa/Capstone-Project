package com.sarahehabm.carbcalculator.item.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.sarahehabm.carbcalculator.R;

public class AllItemsActivity extends AppCompatActivity {
    private final String TAG = AllItemsActivity.class.getSimpleName();

    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_items);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fragment = getSupportFragmentManager().findFragmentById(R.id.fragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_all_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            case R.id.action_done:
                Toast.makeText(this, "Should finish this activity and pass the selected items",
                        Toast.LENGTH_SHORT).show();
                //TODO finish this activity and pass the selected items
                break;

            case R.id.action_new_item:
                Toast.makeText(this, "Add new item", Toast.LENGTH_SHORT).show();
                //TODO start new item activity
                Intent intent = new Intent(this, AddNewItemActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getSelectedItems() {

    }
}
