import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ShoppingPayment extends AppCompatActivity {

    private Spinner spinnerItems;
    private EditText editQuantity;
    private TableLayout tableSummary;
    private TextView tvGrandTotal;
    private Button btnAddItem, btnViewReceipt;

    // Inventory data
    private String[] itemNames = {
            "Bread",
            "Milk",
            "Eggs",
            "Sugar",
            "Cooking Oil",
            "Rice"
    };

    private double[] itemPrices = {
            60.0,
            70.0,
            400.0,
            150.0,
            300.0,
            200.0
    };

    private static final int MAX_ITEMS = 5;
    private static final int MAX_QUANTITY = 100;

    private int itemCount = 0;
    private double grandTotal = 0.0;

    // Names already added, used to prevent duplicates
    private ArrayList<String> addedItems = new ArrayList<>();

    // Receipt data to pass to the next activity
    private ArrayList<String> receiptDetailsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerItems = findViewById(R.id.spinnerItems);
        editQuantity = findViewById(R.id.editQuantity);
        tableSummary = findViewById(R.id.tableSummary);
        tvGrandTotal = findViewById(R.id.tvGrandTotal);
        btnAddItem = findViewById(R.id.btnAddItem);
        btnViewReceipt = findViewById(R.id.btnViewReceipt);

        // Populate drop down list
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                itemNames
        );

        spinnerItems.setAdapter(adapter);

        btnAddItem.setOnClickListener(v -> addItemToSummary());

        btnViewReceipt.setOnClickListener(v -> {

            // Validation: at least 1 item
            if (itemCount == 0) {
                Toast.makeText(
                        this,
                        "Please add at least 1 item first.",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            // Navigate to Receipt Activity
            Intent intent = new Intent(
                    ShoppingPayment.this,
                    ReceiptActivity.class
            );

            intent.putStringArrayListExtra(
                    "RECEIPT_ITEMS",
                    receiptDetailsList
            );

            intent.putExtra("GRAND_TOTAL", grandTotal);

            startActivity(intent);
        });
    }

    // Add selected item to the summary table
    private void addItemToSummary() {

        // Validation: not more than 5 items
        if (itemCount >= MAX_ITEMS) {
            Toast.makeText(
                    this,
                    "Maximum of " + MAX_ITEMS + " items allowed.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        // Validation: quantity must be entered
        String quantityStr = editQuantity
                .getText()
                .toString()
                .trim();

        if (quantityStr.isEmpty()) {
            editQuantity.setError("Enter quantity");
            editQuantity.requestFocus();
            return;
        }

        // Validation: quantity must be a valid whole number
        int quantity;

        try {
            quantity = Integer.parseInt(quantityStr);
        } catch (NumberFormatException e) {
            editQuantity.setError("Invalid quantity");
            editQuantity.requestFocus();
            return;
        }

        // Validation: quantity range
        if (quantity < 1 || quantity > MAX_QUANTITY) {
            editQuantity.setError(
                    "Quantity must be 1 to " + MAX_QUANTITY
            );

            editQuantity.requestFocus();
            return;
        }

        // Get selected item
        int selectedIndex = spinnerItems.getSelectedItemPosition();

        String itemName = itemNames[selectedIndex];

        // Prevent duplicate items
        if (addedItems.contains(itemName)) {
            Toast.makeText(
                    this,
                    itemName + " has already been added.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        // Get corresponding price
        double unitPrice = itemPrices[selectedIndex];

        double rowTotal = unitPrice * quantity;

        // Create a new row in the TableLayout
        TableRow row = new TableRow(this);

        row.setPadding(0, 8, 0, 8);

        row.addView(createCell(itemName, 1.5f));

        row.addView(
                createCell(
                        String.format("%.2f", unitPrice),
                        1f
                )
        );

        row.addView(
                createCell(
                        String.valueOf(quantity),
                        1f
                )
        );

        row.addView(
                createCell(
                        String.format("%.2f", rowTotal),
                        1f
                )
        );

        tableSummary.addView(row);

        // Record item so duplicates are blocked
        addedItems.add(itemName);

        // Update grand total
        grandTotal += rowTotal;

        tvGrandTotal.setText(
                String.format("KES %.2f", grandTotal)
        );

        // Save receipt information
        receiptDetailsList.add(
                itemName
                        + " - Qty: "
                        + quantity
                        + " - KES "
                        + String.format("%.2f", rowTotal)
        );

        itemCount++;

        // Clear quantity input for next item
        editQuantity.setText("");
        editQuantity.setError(null);
    }

    // Helper method to create table cells
    private TextView createCell(String text, float weight) {

        TextView textView = new TextView(this);

        textView.setText(text);

        textView.setTextColor(0xFF000000);

        textView.setPadding(8, 8, 8, 8);

        textView.setBackgroundColor(0xFFFFFFFF);

        TableRow.LayoutParams params =
                new TableRow.LayoutParams(
                        0,
                        TableRow.LayoutParams.WRAP_CONTENT,
                        weight
                );

        params.setMargins(0, 0, 4, 0);

        textView.setLayoutParams(params);

        return textView;
    }
}