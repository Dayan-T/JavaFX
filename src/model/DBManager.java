package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBManager {

    public Connection connector() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Vérifie bien ton user/password ici
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/womenshop", "root", "");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Product> loadProducts() {
        List<Product> list = new ArrayList<>();
        Connection conn = connector();
        Statement stmt = null;
        ResultSet rs = null;

        try {
            if (conn == null) return list;
            stmt = conn.createStatement();
            String sql = "SELECT p.*, c.size, s.shoe_size " +
                    "FROM products p " +
                    "LEFT JOIN clothes c ON p.id = c.product_id " +
                    "LEFT JOIN shoes s ON p.id = s.product_id";

            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String typeBDD = rs.getString("product_type");
                Product p = null;

                if ("Clothing".equals(typeBDD)) {
                    p = new Clothes(rs.getString("name"), rs.getDouble("purchase_price"), rs.getDouble("sell_price"), rs.getInt("size"));
                } else if ("Shoes".equals(typeBDD)) {
                    p = new Shoes(rs.getString("name"), rs.getDouble("purchase_price"), rs.getDouble("sell_price"), rs.getInt("shoe_size"));
                } else {
                    p = new Accessories(rs.getString("name"), rs.getDouble("purchase_price"), rs.getDouble("sell_price"));
                }

                p.setId(rs.getInt("id"));
                p.setNbitems(rs.getInt("stock"));
                list.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn, stmt, rs);
        }
        return list;
    }

    public void updateStock(Product p) {
        Connection conn = connector();
        PreparedStatement ps = null;
        try {
            if (conn == null) return;
            String sql = "UPDATE products SET stock = ? WHERE id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, p.getNbitems());
            ps.setInt(2, p.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn, ps, null);
        }
    }

    public double[] loadFinancials() {
        double[] stats = {30000, 0, 0};
        Connection conn = connector();
        try {
            if (conn == null) return stats;
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM store_stats WHERE id = 1");
            if (rs.next()) {
                stats[0] = rs.getDouble("capital");
                stats[1] = rs.getDouble("total_income");
                stats[2] = rs.getDouble("total_cost");
            }
            conn.close();
        } catch (Exception e) { e.printStackTrace(); }
        return stats;
    }

    public void saveFinancials(double cap, double inc, double cost) {
        Connection conn = connector();
        try {
            if (conn == null) return;
            PreparedStatement ps = conn.prepareStatement("UPDATE store_stats SET capital=?, total_income=?, total_cost=? WHERE id=1");
            ps.setDouble(1, cap);
            ps.setDouble(2, inc);
            ps.setDouble(3, cost);
            ps.executeUpdate();
            conn.close();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void createProduct(Product p) {
        Connection conn = connector();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "INSERT INTO products (name, purchase_price, sell_price, stock, product_type) VALUES (?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, p.getName());
            ps.setDouble(2, p.getPurprice());
            ps.setDouble(3, p.getSellprice());
            ps.setInt(4, p.getNbitems());

            // Gestion du Type SQL ici
            if (p instanceof Clothes) ps.setString(5, "Clothing");
            else if (p instanceof Shoes) ps.setString(5, "Shoes");
            else ps.setString(5, "Accessory");

            ps.executeUpdate();
            rs = ps.getGeneratedKeys();

            if (rs.next()) {
                int id = rs.getInt(1);
                p.setId(id);

                if (p instanceof Clothes) {
                    PreparedStatement ps2 = conn.prepareStatement("INSERT INTO clothes (product_id, size) VALUES (?, ?)");
                    ps2.setInt(1, id);
                    ps2.setInt(2, ((Clothes)p).getSize());
                    ps2.executeUpdate();
                    ps2.close();
                } else if (p instanceof Shoes) {
                    PreparedStatement ps2 = conn.prepareStatement("INSERT INTO shoes (product_id, shoe_size) VALUES (?, ?)");
                    ps2.setInt(1, id);
                    ps2.setInt(2, ((Shoes)p).getShoeSize());
                    ps2.executeUpdate();
                    ps2.close();
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        finally { close(conn, ps, rs); }
    }

    public void resetDemoData() {
        Connection conn = connector();
        try {
            if (conn == null) return;
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("DELETE FROM products");
            stmt.executeUpdate("ALTER TABLE products AUTO_INCREMENT = 1");
            stmt.executeUpdate("UPDATE store_stats SET capital=30000, total_income=0, total_cost=0 WHERE id=1");

            createProduct(new Clothes("Dress 1", 70, 100, 38));
            createProduct(new Clothes("Dress 2", 90, 120, 40));
            createProduct(new Shoes("Shoe 1", 30, 50, 38));
            createProduct(new Shoes("Shoe 2", 50, 70, 40));
            createProduct(new Accessories("Accessory 1", 20, 30));
            createProduct(new Accessories("Accessory 2", 30, 40));

            stmt.close();
            conn.close();
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void close(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (Exception e) { System.out.println(e.getMessage()); }
    }
}
