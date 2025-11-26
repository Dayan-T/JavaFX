package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBManager {

    private static final String URL = "jdbc:mysql://localhost:3306/store";
    private static final String USER = "root";
    private static final String PASS = "Dayan";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    /* LOAD ALL */
    public static List<Product> loadProducts() {
        List<Product> list = new ArrayList<>();

        String sql = "SELECT * FROM products";

        try (Connection conn = getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                double pur = rs.getDouble("purprice");
                double sell = rs.getDouble("sellprice");
                double disc = rs.getDouble("discprice");
                int qty = rs.getInt("nbitems");
                String type = rs.getString("type");
                String spec = rs.getString("size");

                try {
                    Product p;

                    if (type.equals("Shoes")) {
                        p = new Shoes(name, pur, sell, Integer.parseInt(spec));
                    }
                    else if (type.equals("Clothes")) {
                        p = new Clothes(name, pur, sell, Integer.parseInt(spec));
                    }
                    else if (type.equals("Accessories")) {
                        p = new Accessories(name, pur, sell);
                    }
                    else {
                        throw new IllegalArgumentException("Unknown product type: " + type);
                    }

                    // Apply DB values
                    p.setId(id);
                    p.setNbitems(qty);
                    p.setDiscountPer(disc);

                    list.add(p);
                }
                catch (Exception e) {
                    System.out.println("Invalid product detected and removed (ID = " + id + "): " + e.getMessage());
                    deleteProduct(id);   // ⬅ SUPPRESSION AUTOMATIQUE
                }
            }

        } catch (Exception e) {
            System.out.println("DB Load Error: " + e.getMessage());
        }

        return list;
    }

    /* UPDATE SINGLE PRODUCT */
    public static void updateProduct(Product p) {

        String sql = "UPDATE products SET purprice=?, sellprice=?, discprice=?, nbitems=? WHERE id=?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, p.getPurprice());
            ps.setDouble(2, p.getSellprice());
            ps.setDouble(3, p.getDiscprice());
            ps.setInt(4, p.getNbitems());
            ps.setInt(5, p.getId());

            ps.executeUpdate();

        } catch (Exception e) {
            System.out.println("DB Update Error: " + e.getMessage());
        }
    }

    /* UPDATE ALL */
    public static void updateAllProducts(List<Product> list) {
        for (Product p : list)
            updateProduct(p);
    }
    public static void deleteProduct(int id) {
        String sql = "DELETE FROM products WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            System.out.println("DB Delete Error: " + e.getMessage());
        }
    }
}
