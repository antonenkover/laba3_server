package dao;

import models.Department;
import org.apache.log4j.Logger;
import java.util.List;

public class DepartmentDao extends AbstractDao<Department> {
    private static final Logger LOG = Logger.getLogger(DepartmentDao.class);
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "d_name";
    public static final String INSERT_INTO_DEPARTMENT = "INSERT INTO departments ("
            + COLUMN_ID + ", "
            + COLUMN_NAME
            + ") VALUE (?, ?)";
    public static final String UPDATE_DEPARTMENT = "UPDATE `departments` " + COLUMN_ID + " = ?, " + COLUMN_NAME + " = ?, "
            + "= ? WHERE " + COLUMN_ID + " = ? )";
    public static final String DELETE_DEPARTMENT = "DELETE FROM `departments` " + "WHERE " + COLUMN_ID + " = ?";
    public static final String SELECT_FROM_DEPARTMENT = "SELECT * FROM `departments`";
    private static final String SELECT_DEPARTMENT_BY_ID = "SELECT * FROM `departments` WHERE " + COLUMN_ID + " = ?";
    private static final String SELECT_DEPARTMENT_AND_PRODUCTS = "SELECT * FROM `departments` INNER JOIN `products` on departments.id == products.department_id";

    private EntityMapper<Department> getMapper() {
        return resultSet -> new Department(resultSet.getInt(COLUMN_ID),
                resultSet.getString(COLUMN_NAME));
    }


    @Override
    public List<Department> getAll() {
        return getAll(SELECT_FROM_DEPARTMENT, getMapper());
    }


    @Override
    public boolean create(Department entity) {
        LOG.debug("Create department: + " + entity);
        return createUpdate(INSERT_INTO_DEPARTMENT, preparedStatement -> {
            preparedStatement.setInt(1, entity.getId());
            preparedStatement.setString(2, entity.getName());
        });
    }

    @Override
    public boolean update(Department entity) {
        LOG.debug("Update department: " + entity);
        return createUpdate(UPDATE_DEPARTMENT, preparedStatement -> {
            preparedStatement.setInt(1, entity.getId());
            preparedStatement.setString(2, entity.getName());
        });
    }

    @Override
    public boolean remove(Department entity) {
        LOG.debug("Delete department: " + entity);
        return createUpdate(DELETE_DEPARTMENT, preparedStatement -> preparedStatement
                .setInt(1, entity.getId()));
    }

    @Override
    public Department getById(int id, boolean full) {
        return getById(SELECT_DEPARTMENT_BY_ID, preparedStatement -> preparedStatement.setInt(1, id),
                getMapper());
    }

    public String printDepartmentAndProductData(Department entity) {
        ProductDao productDao = new ProductDao();
        return "\nDepartment{\n" +
                "id=" + entity.getId() +
                ",\n title='" + entity.getName() + '\'' +
                ",\n products=" + productDao.getProductsFromDepartmentById(entity.getId()) +
                '}';
    }
}