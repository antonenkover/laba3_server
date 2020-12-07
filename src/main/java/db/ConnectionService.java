package db;

import models.Department;
import models.Product;
import models.Warehouse;
import org.apache.log4j.Logger;

import java.sql.*;

public class ConnectionService {
    private static final Logger LOG = Logger.getLogger(ConnectionService.class);
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_CONNECTION = "jdbc:mysql://localhost:3306/storage_system?serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "nimda28280000";
    private static Statement statement;
    private static Connection connection;

    public void getConnection() throws SQLException {
        try {
            Class.forName(DB_DRIVER);
            connection = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Could not connect to database " + e);
        }
        assert connection != null;
        statement = connection.createStatement();
    }

    public void stopConnection() throws SQLException {
        connection.close();
    }

    public static Connection getConnection2() {
        Connection connection = null;
        try {
            Class.forName(DB_DRIVER);
            connection = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            LOG.error("Could not connect to database", e);
        }
        return connection;
    }

    public static PreparedStatement getPreparedStatement(String query) throws SQLException {
        return getConnection2().prepareStatement(query);
    }

    public static Warehouse createWarehouse() {
        Warehouse warehouse = new Warehouse(1, "Amazon");
        try {
            String selectTableSQL = "SELECT * FROM departments";
            PreparedStatement preparedStatement = getPreparedStatement(selectTableSQL);
            ResultSet rs = preparedStatement.executeQuery(selectTableSQL);
            while (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("d_name");
                Department department = new Department(Integer.parseInt(id), name);
                warehouse.addDepartment(department);
                String selectProducts = "SELECT * FROM products WHERE department_id = " + id;
                PreparedStatement ps = getPreparedStatement(selectProducts);
                ResultSet resultSet = ps.executeQuery(selectProducts);
                while (resultSet.next()) {
                    int barcode = resultSet.getInt("barcode");
                    String p_name = resultSet.getString("p_name");
                    int weight = resultSet.getInt("weight");
                    int price = resultSet.getInt("price");
                    int departmentId = resultSet.getInt("department_id");
                    Product product = new Product(barcode, p_name, weight, price, departmentId);
                    department.addProduct(product);
                }
            }
        } catch (SQLException e) {
            System.out.println("Could not create warehouse from database");
        }
        return warehouse;
    }

    public String showDepartments() {
        String res = "";
        String sql = "SELECT id, d_name FROM departments";
        try {
            ResultSet rs = statement.executeQuery(sql);
            res += "Departments in Warehouse are:\n";
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("d_name");
                res += ">>" + id + " - " + name  + "\n";
            }
            System.out.println(res);
            rs.close();
        } catch (SQLException e) {
            System.out.println(
                    "Error occurred while getting Departments list name");
            LOG.error("Couldn't get departments list "+ e.getMessage());
        }
        return res;
    }

    public String getDepartmentName(int id) {
        String sql = "SELECT d_name FROM departments WHERE id = " + id;
        try {
            ResultSet rs = statement.executeQuery(sql);
            return rs.getString("name");
        } catch (SQLException e) {
            System.out.println(
                    "Error occurred while getting Department name");
            LOG.error("Couldn't get department name with id " + id + e.getMessage());
        }
        return "error";
    }

    public boolean addDepartment(String name, int id) {
        String sql = "INSERT INTO departments (id, d_name) " +
                "VALUES ('" + id + "', '" + name + "')";
        try {
            statement.executeUpdate(sql);
            System.out.println("Department " + name + " added successfully.");
            return true;
        } catch (SQLException e) {
            System.out.println("Error occurred while adding Department with id" + id);
            LOG.error("Couldn't add department with id " + id + e.getMessage());
            return false;
        }
    }

    public boolean updateManufacturer(int id, String name) {
        String sql;
        sql = "UPDATE departments SET d_name = '" + name + "' WHERE ID = " + id;
        try {
            statement.executeUpdate(sql);
            System.out.println("Department " + name + " updated successfully.");
            return true;
        } catch (SQLException e) {
            System.out.println("Error occurred while updating Department with id" + id);
            LOG.error("Couldn't update department with id " + id + e.getMessage());
            return false;
        }
    }

    public boolean deleteDepartment(int id) {
        String deleteDepartment = "DELETE FROM departments WHERE ID =" + id;
        try {
            int c = statement.executeUpdate(deleteDepartment);
            if (c > 0) {
                System.out.println("Department with id " + id + " deleted successfully!");
                return true;
            } else {
                System.out.println("Department with id " + id + " does not exist. Try again");

                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error occurred while deleting department with id " + id);
            LOG.error("Couldn't delete department with id " + id + e.getMessage());
            return false;
        }
    }

    public void showProducts() {
        String sql = "SELECT barcode, p_name, weight, price FROM products";
        try {
            ResultSet rs = statement.executeQuery(sql);
            System.out.println("Products list:");
            System.out.println("barcode - name - weight - price");
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("p_name");
                int weight = rs.getInt("weight");
                int price = rs.getInt("price");
                System.out.println(">>" + id + " - " + name + " - " + weight + " - " + price);
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println(
                    "Error occurred while getting products list.");
            LOG.error("Error occurred while getting products list " + e.getMessage());
        }
    }

    public String showProductsByDepartment() {
        StringBuilder res = new StringBuilder(new String());
        String sql = "SELECT pr.barcode, pr.p_name FROM products AS pr INNER JOIN departments AS dp " +
                "ON pr.department_id = dp.id ORDER BY dp.d_name asc";
        try {
            ResultSet rs = statement.executeQuery(sql);
            res.append("Products by departments list:\n" + "Product - Department\n");
            while (rs.next()) {
                String product_name = rs.getString(1);
                String department_name = rs.getString(2);
                res.append(">>").append(product_name).append(" - ").append(department_name).append("\n");
            }
            System.out.println(res);
            rs.close();
        } catch (SQLException e) {
            System.out.println(
                    "Error occurred while getting products by departments list.");
            LOG.error("Error occurred while getting products by departments list" + e.getMessage());
        }
        return res.toString();
    }

    public String findProductsByDepartmentID(int dep_id) {
        StringBuilder res = new StringBuilder();
        String sql = "SELECT barcode, p_name, weight, price FROM products " +
                "WHERE department_id = " + dep_id;
        try {
            ResultSet rs = statement.executeQuery(sql);
            res.append("Products list:\n" + "barcode - name - weight - price \n");
            while (rs.next()) {
                int id = rs.getInt("barcode");
                String name = rs.getString("p_name");
                int weight = rs.getInt("weight");
                int price = rs.getInt("price");
                res.append(">>").append(id).append(" - ").append(name).append(" - ").append(weight).append(" - ").append(price).append("\n");
            }
            System.out.println(res);
            rs.close();
        } catch (SQLException e) {
            System.out.println(
                    "Error occurred while getting products by department id.");
           LOG.error("Error occurred while getting products by department id" + e.getMessage());
        }

        return res.toString();
    }

    // add model
    public boolean addProducts(String name, int barcode, int weight, int price, int department_id) {
        String sql = "INSERT INTO products (p_name, barcode, weight, price, department_id) " +
                "VALUES ('" + name + "', " + barcode + weight + ", " + price + ", "
                + department_id + ")";
        try {
            statement.executeUpdate(sql);
            System.out.println("Product " + name + "added successfully");
            return true;
        } catch (SQLException e) {
            System.out.println("Error occurred while adding the product.");
           LOG.error("Error occurred while adding the product " + barcode + e.getMessage());
            return false;
        }
    }

    public boolean updateProduct(String name, int barcode, int weight, int price) {
        String sql = "UPDATE products SET barcode = " + barcode;
        if (!name.equals("")) {
            sql += ", p_name = '" + name + "'";
        }

        if (weight != 0) {
            sql += ", weight = " + weight;
        }

        if (price != 0) {
            sql += ", price = " + price;
        }

        sql += " WHERE barcode = " + barcode;

        try {
            statement.executeUpdate(sql);
            System.out.println("Product " + name + " added successfully");
            return true;
        } catch (SQLException e) {
            System.out.println("Error occurred while adding the product.");
            LOG.error("Error occurred while adding the product " + barcode + e.getMessage());
            return false;
        }
    }

    // delete model
    public boolean deleteProduct(int id) {
        String sql = "DELETE FROM products WHERE barcode =" + id;
        try {
            int c = statement.executeUpdate(sql);
            if (c > 0) {
                System.out.println("Product with barcode " + id + " deleted successfully.");
                return true;
            } else {
                System.out.println("Couldn't find product with barcode " + id);
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error occurred while deleting the product.");
            LOG.error("Error occurred while adding the product." + e.getMessage());
            return false;
        }
    }
}
