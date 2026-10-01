package com.example.miamisupermarket;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ReceiptActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        Intent intent = getIntent();
        ArrayList<String> names = intent.getStringArrayListExtra("ITEM_NAMES");
        ArrayList<Integer> quantities = intent.getIntegerArrayListExtra("QUANTITIES");
        double[] prices = intent.getDoubleArrayExtra("UNIT_PRICES");
        double grandTotal = intent.getDoubleExtra("GRAND_TOTAL", 0.0);

        if (names == null || quantities == null || prices == null
                || names.size() != quantities.size() || names.size() != prices.length) {
            Toast.makeText(this, "Receipt details are unavailable.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        TableLayout table = findViewById(R.id.receiptTable);
        for (int i = 0; i < names.size(); i++) {
            TableRow row = new TableRow(this);
            row.addView(makeCell(names.get(i)));
            row.addView(makeCell(String.valueOf(quantities.get(i))));
            row.addView(makeCell(formatMoney(prices[i])));
            row.addView(makeCell(formatMoney(quantities.get(i) * prices[i])));
            table.addView(row);
        }

        TextView tvDate = findViewById(R.id.tvReceiptDate);
        tvDate.setText(new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(new Date()));

        TextView tvGrandTotal = findViewById(R.id.tvGrandTotal);
        tvGrandTotal.setText("GRAND TOTAL: " + formatMoney(grandTotal));

        Button btnNewSale = findViewById(R.id.btnNewSale);
        btnNewSale.setOnClickListener(v -> {
            Intent homeIntent = new Intent(this, MainActivity.class);
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(homeIntent);
            finish();
        });
    }

    private TextView makeCell(String text) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setPadding(16, 8, 16, 8);
        return tv;
    }

    private String formatMoney(double amount) {
        return String.format(Locale.getDefault(), "KES %.2f", amount);
    }
}