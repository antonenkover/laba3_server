package dao;

import db.ConnectionService;
import models.Product;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDao extends AbstractDao<Product> {
    private static final Logger LOG = Logger.getLogger(ProductDao.class);
    public static final String COLUMN_BARCODE = "barcode";
    public static final String COLUMN_NAME = "p_name";
    public static final String COLUMN_WEIGHT = "weight";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_DEPARTMENT_ID = "department_id";
    public static final String INSERT_INTO_PRODUCT = "INSERT INTO `products` ("
            + COLUMN_BARCODE + ", "
            + COLUMN_NAME + ", "
            + COLUMN_WEIGHT + ", "
            + COLUMN_PRICE + ", "
            + COLUMN_DEPARTMENT_ID
            + ") VALUE (?, ?, ?, ?)";
    public static final String UPDATE_PRODUCT = "UPDATE `products` " + COLUMN_BARCODE + " = ?, " + COLUMN_NAME + " = ?, " + COLUMN_WEIGHT + " = ?, " + COLUMN_PRICE + " = ?, " + COLUMN_DEPARTMENT_ID + " = ?, " + "= ? WHERE " + COLUMN_ID + " = ? )";
    public static final String DELETE_PRODUCT = "DELETE FROM `products` " + "WHERE " + COLUMN_BARCODE + " = ?";
    public static final String SELECT_FROM_PRODUCT = "SELECT * FROM `products`";
    public static final String SELECT_PRODUCT_BY_BARCODE = "SELECT * FROM `products` WHERE " + COLUMN_BARCODE + " = ?";
    public static final String SELECT_PRODUCTS_FROM_DEPARTMENT = "SELECT * FROM `products` WHERE " + COLUMN_DEPARTMENT_ID + " = ?";

    private EntityMapper<Product> getMapper() {
        return resultSet -> new Product(resultSet.getInt(COLUMN_BARCODE),
                resultSet.getString(COLUMN_NAME), resultSet.getInt(COLUMN_WEIGHT), resultSet.getInt(COLUMN_PRICE), resultSet.getInt(COLUMN_DEPARTMENT_ID));
    }


    @Override
    public List<Product> getAll() {
        return getAll(SELECT_FROM_PRODUCT, getMapper());
    }


    @Override
    public boolean create(Product entity) {
        LOG.debug("Create department: + " + entity);
        return createUpdate(INSERT_INTO_PRODUCT, preparedStatement -> {
            preparedStatement.setInt(1, entity.getBarcode());
            preparedStatement.setString(2, entity.getName());
            preparedStatement.setInt(3, entity.getWeight());
            preparedStatement.setInt(4, entity.getPrice());
            preparedStatement.setInt(5, entity.getDepartmentId());
        });
    }

    @Override
    public boolean update(Product entity) {
        LOG.debug("Update department: " + entity);
        return createUpdate(UPDATE_PRODUCT, preparedStatement -> {
            preparedStatement.setInt(1, entity.getBarcode());
            preparedStatement.setString(2, entity.getName());
            preparedStatement.setInt(3, entity.getWeight());
            preparedStatement.setInt(4, entity.getPrice());
            preparedStatement.setInt(5, entity.getDepartmentId());
        });
    }

    @Override
    public boolean remove(Product entity) {
        LOG.debug("Delete department: " + entity);
        return createUpdate(DELETE_PRODUCT, preparedStatement -> preparedStatement
                .setInt(1, entity.getBarcode()));
    }

    @Override
    public Product getById(int id, boolean full) {
        return getById(SELECT_PRODUCT_BY_BARCODE, preparedStatement -> preparedStatement.setString(1, String.valueOf(id)),
                getMapper());
    }


    public List<Product> getProductsFromDepartmentById(int department_id) {
        List<Product> products = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = ConnectionService.getPreparedStatement(SELECT_PRODUCTS_FROM_DEPARTMENT);
            preparedStatement.setInt(1, department_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Product product = new Product();
                product.setBarcode(resultSet.getInt(COLUMN_BARCODE));
                product.setName(resultSet.getString(COLUMN_NAME));
                product.setPrice(resultSet.getInt(COLUMN_WEIGHT));
                product.setWeight(resultSet.getInt(COLUMN_PRICE));
                product.setDepartmentId(resultSet.getInt(COLUMN_DEPARTMENT_ID));
                products.add(product);
            }
        } catch (SQLException throwables) {
            LOG.error("Couldn't find product by departmentId + " + throwables.getMessage());
        }
        return products;
    }


}
