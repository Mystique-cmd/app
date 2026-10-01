package com.example.miamisupermarket;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int MAX_DIFFERENT_ITEMS = 5;
    private static final int MAX_QUANTITY = 100;

    private final Cart cart = new Cart();
    private final List<Product> products = ProductData.getProducts();
    private Spinner spinnerProducts;
    private EditText editQuantity;
    private TableLayout tableSummary;
    private TextView tvGrandTotal;
    private Button btnAddItem;

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

        spinnerProducts = findViewById(R.id.spinnerProducts);
        editQuantity = findViewById(R.id.editQuantity);
        tableSummary = findViewById(R.id.tableSummary);
        tvGrandTotal = findViewById(R.id.tvGrandTotal);
        btnAddItem = findViewById(R.id.btnAddItem);
        Button btnViewReceipt = findViewById(R.id.btnViewReceipt);

        ArrayAdapter<Product> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                products
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProducts.setAdapter(adapter);

        btnAddItem.setOnClickListener(v -> addSelectedProduct());
        btnViewReceipt.setOnClickListener(v -> openReceipt());
        updateSummary();
    }

    private void addSelectedProduct() {
        String quantityText = editQuantity.getText().toString().trim();
        if (quantityText.isEmpty()) {
            editQuantity.setError("Enter a quantity");
            editQuantity.requestFocus();
            return;
        }

        final int quantity;
        try {
            quantity = Integer.parseInt(quantityText);
        } catch (NumberFormatException exception) {
            editQuantity.setError("Enter a whole-number quantity");
            editQuantity.requestFocus();
            return;
        }

        if (quantity < 1 || quantity > MAX_QUANTITY) {
            editQuantity.setError("Quantity must be between 1 and " + MAX_QUANTITY);
            editQuantity.requestFocus();
            return;
        }

        Product selectedProduct = (Product) spinnerProducts.getSelectedItem();
        if (!cart.addProduct(selectedProduct, quantity)) {
            Toast.makeText(this, "Maximum of 5 different products allowed.", Toast.LENGTH_SHORT).show();
            return;
        }

        editQuantity.setText("");
        editQuantity.setError(null);
        updateSummary();
        if (cart.getNumberOfDifferentItems() == MAX_DIFFERENT_ITEMS) {
            btnAddItem.setEnabled(false);
        }
    }

    private void updateSummary() {
        tableSummary.removeViews(1, tableSummary.getChildCount() - 1);
        for (Cart.CartItem item : cart.getCartItems()) {
            TableRow row = new TableRow(this);
            row.addView(createCell(item.getProduct().getName(), Gravity.START));
            row.addView(createCell(String.valueOf(item.getQuantity()), Gravity.CENTER));
            row.addView(createCell(formatMoney(item.getProduct().getUnitPrice()), Gravity.END));
            row.addView(createCell(formatMoney(item.getTotal()), Gravity.END));
            tableSummary.addView(row);
        }
        tvGrandTotal.setText("Grand total: " + formatMoney(cart.calculateGrandTotal()));
    }

    private TextView createCell(String value, int gravity) {
        TextView cell = new TextView(this);
        cell.setText(value);
        cell.setGravity(gravity);
        cell.setPadding(8, 12, 8, 12);
        return cell;
    }

    private String formatMoney(double amount) {
        return String.format(Locale.getDefault(), "KES %.2f", amount);
    }

    private void openReceipt() {
        if (cart.isEmpty()) {
            Toast.makeText(this, "Add at least one product before checkout.", Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayList<String> names = new ArrayList<>();
        ArrayList<Integer> quantities = new ArrayList<>();
        ArrayList<Double> prices = new ArrayList<>();
        for (Cart.CartItem item : cart.getCartItems()) {
            names.add(item.getProduct().getName());
            quantities.add(item.getQuantity());
            prices.add(item.getProduct().getUnitPrice());
        }

        Intent intent = new Intent(this, ReceiptActivity.class);
        intent.putStringArrayListExtra("ITEM_NAMES", names);
        intent.putIntegerArrayListExtra("QUANTITIES", quantities);
        intent.putExtra("UNIT_PRICES", toDoubleArray(prices));
        intent.putExtra("GRAND_TOTAL", cart.calculateGrandTotal());
        startActivity(intent);
    }

    private double[] toDoubleArray(List<Double> values) {
        double[] result = new double[values.size()];
        for (int i = 0; i < values.size(); i++) {
            result[i] = values.get(i);
        }
        return result;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (cart != null && cart.isEmpty()) {
            btnAddItem.setEnabled(true);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        cart.clearCart();
        btnAddItem.setEnabled(true);
        updateSummary();
    }
}