private void addItemToSummary() {

    // 4. Maximum of 5 items
    if (itemCount >= 5) {
        Toast.makeText(this, "Maximum of 5 items allowed.", Toast.LENGTH_SHORT).show();
        return;
    }

    // Validate quantity
    String quantityStr = editQuantity.getText().toString().trim();
    if (quantityStr.isEmpty()) {
        Toast.makeText(this, "Enter quantity.", Toast.LENGTH_SHORT).show();
        return;
    }

    int quantity;
    try {
        quantity = Integer.parseInt(quantityStr);
    } catch (NumberFormatException e) {
        Toast.makeText(this, "Invalid quantity.", Toast.LENGTH_SHORT).show();
        return;
    }
    if (quantity <= 0) {
        Toast.makeText(this, "Quantity must be at least 1.", Toast.LENGTH_SHORT).show();
        return;
    }

    // 1. Read the selected item from the dropdown
    int selectedIndex = spinnerItems.getSelectedItemPosition();
    String itemName = itemNames[selectedIndex];

    // 5. Handle duplicate selection
    if (addedItems.contains(itemName)) {
        Toast.makeText(this, itemName + " has already been added.", Toast.LENGTH_SHORT).show();
        return;
    }

    // 2. Determine the corresponding price
    double unitPrice = itemPrices[selectedIndex];
    double rowTotal = unitPrice * quantity;

    // 3. Display in the summary table
    TableRow row = new TableRow(this);
    row.setPadding(0, 8, 0, 8);
    row.addView(createCell(itemName, 1.5f));
    row.addView(createCell(String.format("%.2f", unitPrice), 1f));
    row.addView(createCell(String.valueOf(quantity), 1f));
    row.addView(createCell(String.format("%.2f", rowTotal), 1f));
    tableSummary.addView(row);

    // Update totals and records
    addedItems.add(itemName);
    grandTotal += rowTotal;
    tvGrandTotal.setText(String.format("KES %.2f", grandTotal));
    receiptDetailsList.add(itemName + " - Qty: " + quantity + " - KES " + String.format("%.2f", rowTotal));

    itemCount++;
    editQuantity.setText("");
}