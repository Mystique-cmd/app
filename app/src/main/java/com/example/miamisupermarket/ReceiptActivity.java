package com.example.miamisupermarket;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ReceiptActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        Intent intent = getIntent();
        String[] names = intent.getStringArrayExtra("ITEM_NAMES");
        int[] qty = intent.getIntArrayExtra("QUANTITIES");
        double[] prices = intent.getDoubleArrayExtra("UNIT_PRICES");
        double grandTotal = intent.getDoubleExtra("GRAND_TOTAL", 0.0);

        // Sample data so this screen can be tested on its own.
        // Only used when nothing was sent from the main screen.
        if (names == null || qty == null || prices == null) {
            names = new String[]{"Milk", "Bread", "Sugar"};
            qty = new int[]{2, 1, 3};
            prices = new double[]{60.0, 55.0, 150.0};
            grandTotal = 2 * 60.0 + 1 * 55.0 + 3 * 150.0;
        }

        TableLayout table = findViewById(R.id.receiptTable);
        for (int i = 0; i < names.length; i++) {
            TableRow row = new TableRow(this);
            row.addView(makeCell(names[i]));
            row.addView(makeCell(String.valueOf(qty[i])));
            row.addView(makeCell(String.format("%.2f", prices[i])));
            row.addView(makeCell(String.format("%.2f", qty[i] * prices[i])));
            table.addView(row);
        }

        TextView tvGrandTotal = findViewById(R.id.tvGrandTotal);
        tvGrandTotal.setText("GRAND TOTAL: " + String.format("%.2f", grandTotal));

        Button btnNewSale = findViewById(R.id.btnNewSale);
        btnNewSale.setOnClickListener(v -> finish());
    }

    private TextView makeCell(String text) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setPadding(16, 8, 16, 8);
        return tv;
    }
}