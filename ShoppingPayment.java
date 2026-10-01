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

public class MainActivity extends AppCompatActivity {

    private Spinner spinnerItems;
    private EditText editQuantity;
    private TableLayout tableSummary;
    private TextView tvGrandTotal;
    private Button btnAddItem, btnViewReceipt;

    // Inventory data
    private String[] itemNames = {"Bread", "Milk", "Eggs", "Sugar", "Cooking Oil", "Rice"};
    private double[] itemPrices = {60.0, 70.0, 400.0, 150.0, 300.0, 200.0};

    private int itemCount = 0;
    private double grandTotal = 0.0;
    
    // To store receipt data to pass to the next activity
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

        // Populate Drop Down List
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemNames);
        spinnerItems.setAdapter(adapter);

        btnAddItem.setOnClickListener(v -> addItemToSummary());
        
        btnViewReceipt.setOnClickListener(v -> {
            if (itemCount == 0) {
                Toast.makeText(this, "Please add items first.", Toast.LENGTH_SHORT).show();
                return;
            }
            // Navigate to Receipt Activity
            Intent intent = new Intent(MainActivity.this, ReceiptActivity.class);
            intent.putStringArrayListExtra("RECEIPT_ITEMS", receiptDetailsList);
            intent.putExtra("GRAND_TOTAL", grandTotal);
            startActivity(intent);
        });
    }

    private void addItemToSummary() {
        if (itemCount >= 5) {
            Toast.makeText(this, "Maximum of 5 items allowed.", Toast.LENGTH_SHORT).show();
            return;
        }

        String quantityStr = editQuantity.getText().toString();
        if (quantityStr.isEmpty()) {
            Toast.makeText(this, "Enter quantity.", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedIndex = spinnerItems.getSelectedItemPosition();
        String itemName = itemNames[selectedIndex];
        double unitPrice = itemPrices[selectedIndex];
        int quantity = Integer.parseInt(quantityStr);
        double rowTotal = unitPrice * quantity;

        // Create a new row in the TableLayout
        TableRow row = new TableRow(this);
        row.setPadding(0, 8, 0, 8);

        row.addView(createCell(itemName, 1.5f));
        row.addView(createCell(String.valueOf(unitPrice), 1f));
        row.addView(createCell(String.valueOf(quantity), 1f));
        row.addView(createCell(String.valueOf(rowTotal), 1f));

        tableSummary.addView(row);

        // Update Grand Total
        grandTotal += rowTotal;
        tvGrandTotal.setText(String.format("KES %.2f", grandTotal));
        
        // Save string format for the next activity
        receiptDetailsList.add(itemName + " - Qty: " + quantity + " - KES " + rowTotal);

        itemCount++;
        editQuantity.setText(""); // Clear input for next item
    }

    // Helper method to format cells dynamically
    private TextView createCell(String text, float weight) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setPadding(8, 8, 8, 8);
        textView.setBackgroundColor(getResources().getColor(android.R.color.white));
        
        TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, weight);
        params.setMargins(0, 0, 4, 0); 
        textView.setLayoutParams(params);
        
        return textView;
    }
}