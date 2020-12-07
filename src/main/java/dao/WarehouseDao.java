package dao;

import db.ConnectionService;
import models.Department;
import models.Product;
import models.Warehouse;
import org.apache.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static dao.DepartmentDao.SELECT_FROM_DEPARTMENT;
import static db.ConnectionService.getPreparedStatement;

public class WarehouseDao {
    private static final Logger LOG = Logger.getLogger(ConnectionService.class);
    public static DepartmentDao departmentDao = new DepartmentDao();
    private static final int id = 1;
    private static final String title = "Amazon";

    
    public static String printAll() {
        return "Warehouse{\n" +
                "id=" + id +
                ",\n title='" + title + '\'' +
                ",\n departments=" + departmentDao.getAll() +
                '}';
    }

}
