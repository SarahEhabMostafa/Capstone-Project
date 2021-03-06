package com.sarahehabm.carbcalculator.item.view;

import android.content.ContentUris;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.sarahehabm.carbcalculator.R;
import com.sarahehabm.carbcalculator.common.database.CarbCounterInterface;
import com.sarahehabm.carbcalculator.common.model.Amount;
import com.sarahehabm.carbcalculator.common.model.Item;

import java.util.ArrayList;

public class AddNewItemActivity extends AppCompatActivity implements OnItemPropertyChangeListener {
    private static final String TAG = AddNewItemActivity.class.getSimpleName();
    private EditText editText_name;
    private RecyclerView recyclerView_amounts;

    private AddNewItemAmountsAdapter adapter;
    private String itemName;
    private boolean validItemName, validQuantities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        validItemName = false;
        validQuantities = false;

        editText_name = (EditText) findViewById(R.id.editText_name);
        editText_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                itemName = editText_name.getText().toString().trim();
                if (itemName != null && !itemName.isEmpty())
                    validItemName = true;
                else
                    validItemName = false;

                supportInvalidateOptionsMenu();
                invalidateOptionsMenu();
            }
        });
        recyclerView_amounts = (RecyclerView) findViewById(R.id.recyclerView_amounts);
        recyclerView_amounts.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AddNewItemAmountsAdapter(this);

        validQuantities = adapter.isValidAmounts();
        supportInvalidateOptionsMenu();
        invalidateOptionsMenu();

        recyclerView_amounts.setAdapter(adapter);
    }

    public void onAddNewAmountClick(View view) {
        if (adapter == null)
            adapter = new AddNewItemAmountsAdapter(this);

        adapter.addItem();

        validQuantities = adapter.isValidAmounts();
        supportInvalidateOptionsMenu();
        invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_new_item, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (validItemName && validQuantities) {
            menu.getItem(0).setEnabled(true);
        } else {
            menu.getItem(0).setEnabled(false);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    private void refreshMenuButton() {
        validQuantities = adapter.isValidAmounts();
        supportInvalidateOptionsMenu();
        invalidateOptionsMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;


            case R.id.action_add:
                boolean result = save();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean save() {
        String itemName = editText_name.getText().toString();
        if(itemName == null || itemName.trim().isEmpty()) {
            Toast.makeText(this, getString(R.string.name_required), Toast.LENGTH_SHORT).show();
            return false;
        }

        Uri itemUri = CarbCounterInterface.insertItem(this, new Item(itemName, false));
        int itemId = (int) ContentUris.parseId(itemUri);

        ArrayList<Amount> amounts = adapter.getAmounts(itemId);
        if(amounts==null || amounts.isEmpty()) {
            Toast.makeText(this, getString(R.string.amount_required), Toast.LENGTH_SHORT).show();
            return false;
        }
        int insertCount = CarbCounterInterface.insertAmounts(this, amounts);

        if(insertCount == amounts.size()) {
            finish();
            return true;
        }

        return false;
    }

    @Override
    public void onAmountChanged(String amount) {
        refreshMenuButton();
    }

    @Override
    public void onUnitChanged(String unit) {
        refreshMenuButton();
    }

    @Override
    public void onCarbsChanged(String carbs) {
        refreshMenuButton();
    }
}
