package dao;

import db.ConnectionService;
import org.apache.log4j.Logger;
import views.View;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDao<T> implements EntityDao<T> {
    private static final Logger LOG = Logger.getLogger(AbstractDao.class);
    protected static String COLUMN_ID = "id";
    View view = new View();

    public List<T> getAll(String query, EntityMapper<T> mapper) {
        List<T> result = new ArrayList<>();
        try (PreparedStatement preparedStatement = ConnectionService.getPreparedStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                T entity = mapper.map(resultSet);
                result.add(entity);
            }

        } catch (SQLException e) {
            LOG.error("Error occured while getting all entities.");
            view.showStackTrace("Problem while getting all data ", e.getMessage());
        }

        return result;
    }


    public boolean createUpdate(String query, StatementMapper<T> statementMapper) {
        try (PreparedStatement preparedStatement = ConnectionService.getPreparedStatement(query)) {
            statementMapper.map(preparedStatement);

            int result = preparedStatement.executeUpdate();

            return result == 1;
        } catch (SQLException e) {
            LOG.error("Could not create entity!", e);
            view.showStackTrace("Problem while creating data ", e.getMessage());
        }
        return false;
    }


//    @Override
//    public T getByLogin(String login, boolean full) {
//        return null;
//    }
//
//    public T getByLogin(String query, StatementMapper<T> statementMapper, EntityMapper<T> mapper) {
//        T result = null;
//
//        try (PreparedStatement preparedStatement = DbConnectionService.getPreparedStatement(query);) {
//            statementMapper.map(preparedStatement);
//            try (ResultSet resultSet = preparedStatement.executeQuery()) {
//                while (resultSet.next()) {
//                    result = mapper.map(resultSet);
//                }
//            }
//        } catch (SQLException e) {
//            LOG.error("Exception while getting all entities", e);
//        }
//
//        return result;
//    }

    @Override
    public T getById(int id, boolean full) {
        return null;
    }

    public T getById(String query, StatementMapper<T> statementMapper, EntityMapper<T> mapper) {
        T result = null;

        try (PreparedStatement preparedStatement = ConnectionService.getPreparedStatement(query)) {
            statementMapper.map(preparedStatement);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    result = mapper.map(resultSet);
                }
            }
        } catch (SQLException e) {
            LOG.error("Exception while getting all entities", e);
            view.showStackTrace("Problem while creating data ", e.getMessage());
        }

        return result;
    }

}