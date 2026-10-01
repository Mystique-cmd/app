import java.util.ArrayList;
import java.util.List;

public class ProductData {

    /**
     * Returns the list of products available in the supermarket.
     */
    public static List<Product> getProducts() {

        List<Product> products = new ArrayList<>();

        products.add(new Product("Bread", 70.00));
        products.add(new Product("Milk", 80.00));
        products.add(new Product("Sugar", 150.00));
        products.add(new Product("Rice", 200.00));
        products.add(new Product("Cooking Oil", 300.00));
        products.add(new Product("Tea Leaves", 120.00));
        products.add(new Product("Coffee", 250.00));
        products.add(new Product("Maize Flour", 75.00));
        products.add(new Product("Wheat Flour", 100.00));
        products.add(new Product("Salt", 50.00));
        products.add(new Product("Bottled Water", 60.00));
        products.add(new Product("Soda", 80.00));
        products.add(new Product("Biscuits", 100.00));
        products.add(new Product("Eggs (Tray)", 450.00));
        products.add(new Product("Margarine", 180.00));
        products.add(new Product("Laundry Detergent", 220.00));
        products.add(new Product("Toilet Paper", 300.00));
        products.add(new Product("Toothpaste", 150.00));
        products.add(new Product("Shampoo", 350.00));
        products.add(new Product("Tomato Sauce", 180.00));

        return products;
    }
}