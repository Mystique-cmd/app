import java.util.ArrayList;
import java.util.List;

public class Cart {

    // Assignment requirement: maximum of 5 different items
    private static final int MAX_DIFFERENT_ITEMS = 5;

    private final List<CartItem> cartItems;

    public Cart() {
        cartItems = new ArrayList<>();
    }

    /**
     * Adds a product to the cart with the specified quantity.
     */
    public boolean addProduct(Product product, int quantity) {

        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null.");
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be greater than zero."
            );
        }

        // Check whether this product is already in the cart
        for (CartItem item : cartItems) {

            if (item.getProduct().getName().equals(product.getName())) {

                // Product already exists.
                // Increase its quantity instead of adding another item.
                item.setQuantity(item.getQuantity() + quantity);

                return true;
            }
        }

        // Check maximum of 5 different products
        if (cartItems.size() >= MAX_DIFFERENT_ITEMS) {
            return false;
        }

        cartItems.add(new CartItem(product, quantity));

        return true;
    }

    /**
     * Returns all items currently in the cart.
     */
    public List<CartItem> getCartItems() {
        return cartItems;
    }

    /**
     * Calculates the total cost of one cart item.
     */
    public double calculateItemTotal(CartItem item) {

        return item.getProduct().getUnitPrice()
                * item.getQuantity();
    }

    /**
     * Calculates the grand total of everything in the cart.
     */
    public double calculateGrandTotal() {

        double grandTotal = 0.0;

        for (CartItem item : cartItems) {

            grandTotal += calculateItemTotal(item);
        }

        return grandTotal;
    }

    /**
     * Returns the number of different products in the cart.
     */
    public int getNumberOfDifferentItems() {
        return cartItems.size();
    }

    /**
     * Checks whether the cart is empty.
     */
    public boolean isEmpty() {
        return cartItems.isEmpty();
    }

    /**
     * Removes all items from the cart.
     */
    public void clearCart() {
        cartItems.clear();
    }


    /**
     * Represents a product and its quantity in the customer's cart.
     */
    public static class CartItem {

        private final Product product;
        private int quantity;

        public CartItem(Product product, int quantity) {
            this.product = product;
            this.quantity = quantity;
        }

        public Product getProduct() {
            return product;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {

            if (quantity <= 0) {
                throw new IllegalArgumentException(
                        "Quantity must be greater than zero."
                );
            }

            this.quantity = quantity;
        }

        public double getTotal() {
            return product.getUnitPrice() * quantity;
        }
    }
}