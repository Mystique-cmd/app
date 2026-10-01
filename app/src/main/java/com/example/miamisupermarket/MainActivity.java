package com.example.miamisupermarket;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (v, insets) -> {
                    Insets systemBars = insets.getInsets(
                            WindowInsetsCompat.Type.systemBars()
                    );
                    v.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom
                    );
                    return insets;
                }
        );

        // Temporary sample data for testing Intent data transfer
        String[] itemNames = {"Milk", "Bread", "Sugar"};
        int[] quantities = {2, 1, 3};
        double[] unitPrices = {60.0, 55.0, 150.0};

        double grandTotal = 0.0;

        for (int i = 0; i < itemNames.length; i++) {
            grandTotal += quantities[i] * unitPrices[i];
        }

        // Send receipt data to ReceiptActivity
        Intent intent = new Intent(MainActivity.this, ReceiptActivity.class);

        intent.putExtra("ITEM_NAMES", itemNames);
        intent.putExtra("QUANTITIES", quantities);
        intent.putExtra("UNIT_PRICES", unitPrices);
        intent.putExtra("GRAND_TOTAL", grandTotal);

        // Temporary: open receipt screen for testing
        // Later, move this to the Checkout/Generate Receipt button.
        startActivity(intent);
    }
}