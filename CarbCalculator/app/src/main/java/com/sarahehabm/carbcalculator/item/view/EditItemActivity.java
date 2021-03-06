package com.sarahehabm.carbcalculator.item.view;

import android.content.Intent;
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
import com.sarahehabm.carbcalculator.common.Constants;
import com.sarahehabm.carbcalculator.common.database.CarbCounterInterface;
import com.sarahehabm.carbcalculator.common.model.Amount;
import com.sarahehabm.carbcalculator.common.model.Item;

import java.util.ArrayList;

public class EditItemActivity extends AppCompatActivity implements OnItemPropertyChangeListener{
    private static final String TAG = EditItemActivity.class.getSimpleName();
    private EditText editText_name;
    private RecyclerView recyclerView_amounts;

    private AddNewItemAmountsAdapter adapter;
    private int itemId;
    private String itemName;
    private Item item;
    private ArrayList<Amount> newAmounts;
    private boolean validItemName, validQuantities, nameChanged, amountAdded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if(intent!=null && intent.hasExtra(Constants.KEY_ITEM_ID)) {
            int itemId = intent.getIntExtra(Constants.KEY_ITEM_ID, -1);
            item = CarbCounterInterface.getItem(this, itemId);
        }

        validItemName = true;
        validQuantities = false;
        nameChanged = false;
        amountAdded = false;

        editText_name = (EditText) findViewById(R.id.editText_name);
        if(item!=null)
            editText_name.setText(item.getName());
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
                nameChanged = true;
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
        if(item!=null) {
            ArrayList<Amount> amounts = CarbCounterInterface.getAmountsByItemId(this,
                    item.getId());
            adapter = new AddNewItemAmountsAdapter(amounts, this);
        }

        validQuantities = adapter.isValidAmounts();
        supportInvalidateOptionsMenu();
        invalidateOptionsMenu();

        recyclerView_amounts.setAdapter(adapter);
    }

    public void onAddNewAmountClick(View view) {
        amountAdded = true;
        if (adapter == null)
            adapter = new AddNewItemAmountsAdapter(this);

        adapter.addItem();

        validQuantities = adapter.isValidAmounts();
        supportInvalidateOptionsMenu();
        invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_item, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (validItemName && validQuantities) {
            menu.getItem(0).setEnabled(true);
        } else {
            menu.getItem(0).setEnabled(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            case R.id.action_save_edit:
                boolean result = save();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean save() {
        if(itemName!=null && !itemName.trim().isEmpty())
            item.setName(itemName);

        int updateCount = CarbCounterInterface.updateItem(this, item);

        if(updateCount>0) {
            ArrayList<Amount> amounts = adapter.getAmounts(item.getId());
            if(amounts==null || amounts.isEmpty()) {
                Toast.makeText(this, getString(R.string.amount_required), Toast.LENGTH_SHORT).show();
                return false;
            }

            int insertCount = CarbCounterInterface.insertOrUpdateAmounts(this, amounts);
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

    private void refreshMenuButton() {
        validQuantities = adapter.isValidAmounts();
        supportInvalidateOptionsMenu();
        invalidateOptionsMenu();
    }


}
