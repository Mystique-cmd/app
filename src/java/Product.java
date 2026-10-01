public class Product {

    private final String name;
    private final double unitPrice;

    public Product(String name, double unitPrice) {
        this.name = name;
        this.unitPrice = unitPrice;
    }

    public String getName() {
        return name;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    /**
     * Calculates the total cost for this product.
     */
    public double calculateTotal(int quantity) {

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }

        return unitPrice * quantity;
    }

    @Override
    public String toString() {
        return name + " - KSh " + String.format("%.2f", unitPrice);
    }
}