package com.sarahehabm.carbcalculator;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sarahehabm.carbcalculator.common.Constants;
import com.sarahehabm.carbcalculator.common.OnDataRetrieveListener;
import com.sarahehabm.carbcalculator.common.database.CarbCounterInterface;
import com.sarahehabm.carbcalculator.common.model.Amount;
import com.sarahehabm.carbcalculator.common.model.Item;
import com.sarahehabm.carbcalculator.common.model.ItemAmount;
import com.sarahehabm.carbcalculator.common.model.Meal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class TestActivity extends AppCompatActivity implements OnDataRetrieveListener {
    private EditText editText;
    private TextView textView;
    private ProgressDialog progressDialog;

    private Random random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        editText = (EditText) findViewById(R.id.editText);
        textView = (TextView) findViewById(R.id.textView);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Getting data..");
        progressDialog.setIndeterminate(true);

        random = new Random();
    }

    public void onInsertItemClick(View view) {
        String itemName = editText.getText().toString();
        if (itemName == null || itemName.trim().isEmpty()) {
            itemName = "item" + random.nextInt(500);
        }

        int id = random.nextInt(5000);
        Item item = new Item(id, itemName, false);
        Uri uri = CarbCounterInterface.insertItem(this, item);
        textView.setText("Item inserted at: " + uri.getPath());
    }

    public void onGetItemClick(View view) {
        String text = editText.getText().toString();

        try {
            if (text != null && !text.trim().isEmpty()) {
                int id = Integer.parseInt(text);
                Item item = CarbCounterInterface.getItem(this, id);
                if (item == null)
                    textView.setText("");
                else
                    textView.setText(item.toJson());
            }
        } catch (NumberFormatException e) {
            textView.setText(e.getMessage());
        }
    }

    public void onGetItemsClick(View view) {
        ArrayList<Item> items = CarbCounterInterface.getAllItems(this);
        textView.setText(Item.listToJson(items));
    }

    public void onInsertMealClick(View view) {
        String mealName = editText.getText().toString();
        if (mealName == null || mealName.trim().isEmpty()) {
            mealName = "meal" + random.nextInt(500);
        }

        int id = random.nextInt(5000);
        int carbs = random.nextInt(200);
        Meal meal = new Meal(id, mealName, carbs, Calendar.getInstance().getTimeInMillis());
        Uri uri = CarbCounterInterface.insertMeal(this, meal);
        textView.setText("Meal inserted at: " + uri.getPath());
    }

    public void onGetMealClick(View view) {
        String text = editText.getText().toString();

        try {
            if (text != null && !text.trim().isEmpty()) {
                int id = Integer.parseInt(text);
                Meal meal = CarbCounterInterface.getMeal(this, id);
                if (meal == null)
                    textView.setText("");
                else
                    textView.setText(meal.toJson());
            }
        } catch (NumberFormatException e) {
            textView.setText(e.getMessage());
        }
    }

    public void onGetMealsClick(View view) {
        ArrayList<Meal> meals = CarbCounterInterface.getAllMeals(this);
        textView.setText(Meal.listToJson(meals));
    }

    public void onInsertAmountClick(View view) {
        String amountUnit = editText.getText().toString();
        if (amountUnit == null || amountUnit.trim().isEmpty()) {
            amountUnit = "unit" + random.nextInt(50);
        }

        int id = random.nextInt(5000);
        int carbGrams = random.nextInt(90);
        int quantity = random.nextInt(100);
        int itemId = random.nextInt(10);
        Amount amount = new Amount(id, carbGrams, quantity, amountUnit, itemId);
        Uri uri = CarbCounterInterface.insertAmount(this, amount);
        textView.setText("Amount inserted at: " + uri.getPath());
    }

    public void onGetAmountClick(View view) {
        String text = editText.getText().toString();

        try {
            if (text != null && !text.trim().isEmpty()) {
                int id = Integer.parseInt(text);
                Amount amount = CarbCounterInterface.getAmount(this, id);
                if (amount == null)
                    textView.setText("");
                else
                    textView.setText(amount.toJson());
            }
        } catch (NumberFormatException e) {
            textView.setText(e.getMessage());
        }
    }

    public void onGetAmountsClick(View view) {
        ArrayList<Amount> amounts = CarbCounterInterface.getAllAmounts(this);
        textView.setText(Amount.listToJson(amounts));
    }

    public void onInsertItemAmountClick(View view) {
        int id = random.nextInt(5000);
        int itemId = random.nextInt(5000);
        int amountId = random.nextInt(5000);
        int mealId = random.nextInt(5000);
        int totalWeight = random.nextInt(200);
        int totalQuantity = random.nextInt(200);
        ItemAmount itemAmount = new ItemAmount(id, itemId, amountId, totalWeight, totalQuantity, mealId);
        Uri uri = CarbCounterInterface.insertItemAmount(this, itemAmount);
        textView.setText("ItemAmount inserted at: " + uri.getPath());
    }

    public void onGetItemAmountClick(View view) {
        String text = editText.getText().toString();

        try {
            if (text != null && !text.trim().isEmpty()) {
                int id = Integer.parseInt(text);
                ItemAmount itemAmount = CarbCounterInterface.getItemAmount(this, id);
                if (itemAmount == null)
                    textView.setText("");
                else
                    textView.setText(itemAmount.toJson());
            }
        } catch (NumberFormatException e) {
            textView.setText(e.getMessage());
        }

    }

    public void onGetItemAmountsClick(View view) {
        ArrayList<ItemAmount> itemAmounts = CarbCounterInterface.getAllItemAmounts(this);
        textView.setText(ItemAmount.listToJson(itemAmounts));
    }

    public void onRetrieveDataClick(View view) {
        new TestAsyncTask(this).execute(new Pair<Context, String>(this, "SARAH"));
    }

    public void onRetrieveAmountsClick(View view) {
        new TestAsyncTaskAmounts(this).execute(new Pair<Context, String>(this, "SARAH"));
    }

    @Override
    public void onStartCall() {
        progressDialog.show();
    }

    @Override
    public void onFinishCall(String result, int serviceKey) {
        progressDialog.hide();

        switch (serviceKey) {
            case Constants.SERVICE_GET_ITEMS: {
                int insertCount = CarbCounterInterface.insertItems(this, Item.listFromJson(result));
                Toast.makeText(this, "(TestActivity); " + insertCount + " items inserted.",
                        Toast.LENGTH_LONG).show();
            }
            break;

            case Constants.SERVICE_GET_ITEM_AMOUNTS: {
                int insertCount = CarbCounterInterface.insertAmounts(this, Amount.listFromJson(result));
                Toast.makeText(this, "(TestActivity); " + insertCount + " itemAmounts inserted.",
                        Toast.LENGTH_LONG).show();
            }
            break;
        }
    }
}
