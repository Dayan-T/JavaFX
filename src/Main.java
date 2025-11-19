import model.Clothes;
import model.Product;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
            public static void main(String[] args) {

                // 1. Create products
                Clothes c1 = new Clothes("Dress", 20, 50, 40);
                Clothes c2 = new Clothes("T-Shirt", 10, 25, 38);
                //Shoes s1 = new Shoes("Nike Air", 40, 90, 42);
                //Accessories a1 = new Accessories("Handbag", 15, 35);

                // 2. Create array of products
                Product[] products = {c1, c2};

                // 3. Display products
                System.out.println("\n=== PRODUCTS ===");
                for (Product p : products) {
                    p.display();
                    System.out.println("------------------------");
                }

                // Purchase some items
                System.out.println("\n=== PURCHASE OPERATIONS ===");
                c1.buy(10);
                c2.buy(5);
                //s1.buy(3);
                //a1.buy(7);

                // Sell some items
                System.out.println("\n=== SELL OPERATIONS ===");
                c1.sell(2);
                //a1.sell(1);
                //s1.sell(2);

                // 5. Display stock
                System.out.println("\n=== STOCK LEVELS ===");
                for (Product p : products) {
                    System.out.println(p.getName() + " stock: " + p.getNbitems());
                }

                // 6. Display incomes, costs, capital
                System.out.println("\n=== FINANCIAL STATS ===");
                Product.displayStats();
            }
        }