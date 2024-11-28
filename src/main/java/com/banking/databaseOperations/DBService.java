package com.banking.databaseOperations;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.banking.config.ConfigReader;
import com.banking.config.Criteria;
import com.banking.config.DBConfig;
import com.banking.config.QueryBuilder;

public class DBService {

    private static final Logger LOGGER = Logger.getLogger(DBService.class.getName());
    private Connection connection;
    private ConfigReader configReader;

    public DBService(String filePath) {
        this.connection = DBConfig.getConnection();

        this.configReader = new ConfigReader(filePath);
    }

    // Properly close resources
    private void closeResources(PreparedStatement preparedStatement, ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error closing resources", e);
        }
    }

    // Close database connection
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error closing connection", e);
            }
        }
    }

    // Perform delete operation
    public int performDelete(String className, String field, String operator, String value) {
        int rowsAffected = 0;
        String query = null;
        PreparedStatement preparedStatement = null;

        try {
            query = new QueryBuilder()
                    .table(className)
                    .delete()
                    .where(field, operator, value)
                    .build();
            preparedStatement = connection.prepareStatement(query);
            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error executing delete query: " + query, e);
        } finally {
            closeResources(preparedStatement, null);
        }

        return rowsAffected;
    }

    // Insert query builder
    private <T> void insertQueryBuilder(T obj, String className) throws SQLException {
        String tableName = configReader.getTableName(className);
        Map<String, String> columns = configReader.getClassColumns(className);
        StringBuilder columnNames = new StringBuilder();
        StringBuilder values = new StringBuilder();
        List<Object> parameters = new ArrayList<>();

        for (Map.Entry<String, String> entry : columns.entrySet()) {
            String columnName = entry.getKey();
            String fieldName = entry.getValue();

            if (columnNames.length() > 0) {
                columnNames.append(", ");
                values.append(", ");
            }
            columnNames.append(columnName);

            try {
                // Access the field directly using reflection
                Field field = obj.getClass().getDeclaredField(fieldName);
                field.setAccessible(true); // Make the field accessible if it's private
                Object value = field.get(obj); // Get the value of the field

                values.append("?");
                parameters.add(value);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        String query = "INSERT INTO " + tableName + " (" + columnNames + ") VALUES (" + values + ")";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            for (int i = 0; i < parameters.size(); i++) {
                stmt.setObject(i + 1, parameters.get(i));
            }
            stmt.executeUpdate();
        }
    }


    // Insert operation
    public <T> void insert(T obj) throws SQLException {
        if (obj == null) {
            throw new SQLException("Object cannot be null.");
        }

        String superClassName = obj.getClass().getSuperclass().getSimpleName();
        if (superClassName != null && !superClassName.equals("Object")) {
            insertQueryBuilder(obj, superClassName);
        }

        String className = obj.getClass().getSimpleName();
        insertQueryBuilder(obj, className);
    }

    // Get operation with criteria
    public <T> List<T> get(Class<?> clazz, List<Criteria> criteria, List<String> logicOps) {
        String className = clazz.getSimpleName();

        QueryBuilder queryBuilder = new QueryBuilder();

        queryBuilder.select("*").table(className);

//        Map<String, String> foreignKey = configReader.getFirstForeignKeyMapping(className);
//        queryBuilder.join(configReader.getTableName(foreignKey.get("referenced_class")), foreignKey.get("field"), foreignKey.get("references"));
        if(criteria!=null) {
        	int size = criteria.size();
        	for (int i = 0; i<size; i++) {
        		Criteria c = criteria.get(i);
        		queryBuilder.where(c.getColumn(), c.getCompareOperator(), c.getValue());
        		if(logicOps != null && i<size - 1) {
        			if(logicOps.get(i).equalsIgnoreCase("and")) {
        				queryBuilder.and();       				
        			}
        			else {
        				queryBuilder.or();	
        			}
        		}
        	}
        	
        }

        String query = queryBuilder.build();
        return (List<T>) executeQuery(query, clazz);
    }

    // Perform insert or update operation
    public int performUpdate(String tableName, Map<String, String> fieldValuePairs, List<Criteria> criterias, List<String> logicalOps) {
        int rowsAffected = 0;
        String query = null;
        PreparedStatement preparedStatement = null;
        try {
            QueryBuilder queryBuilder = new QueryBuilder()
                    .table(tableName)
                    .update(fieldValuePairs);
            
            int size = criterias.size();
            for(int i = 0; i<size; i++) {
            	queryBuilder.where(criterias.get(i).getColumn(), criterias.get(i).getCompareOperator(), criterias.get(i).getValue());
            	if(i<size - 1) {
            		if(logicalOps.get(i).equalsIgnoreCase("and")) {
            			queryBuilder.and();
            		}
            		else {
            			queryBuilder.or();
            		}
            	}
            }
            

            preparedStatement = connection.prepareStatement(query);
            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error executing insert/update query: " + query, e);
        } finally {
            closeResources(preparedStatement, null);
        }

        return rowsAffected;
    }

    // Execute query and map result set to objects
    public <T> List<T> executeQuery(String query, Class<T> clazz) {
        PreparedStatement preparedStatement = null;
        List<T> list = new ArrayList<>();

        try {
            preparedStatement = connection.prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Handle cases where the target class is a built-in type (e.g., Integer, String)
            if (clazz.equals(Integer.class) || clazz.equals(String.class) || clazz.equals(Double.class) ||
                clazz.equals(Long.class) || clazz.equals(Float.class) || clazz.equals(Boolean.class)) {
                while (rs.next()) {
                    T value = (T) rs.getObject(1); // Assuming a single column result set
                    list.add(value);
                }
            } else {
                // For user-defined classes, reflectively map the result set columns to fields
                List<Field> allFields = new ArrayList<>();
                Class<?> currentClass = clazz;

                // Add fields from the class hierarchy
                while (currentClass != null) {
                    Field[] fields = currentClass.getDeclaredFields();
                    for (Field field : fields) {
                        allFields.add(field);
                    }
                    currentClass = currentClass.getSuperclass();
                }

                while (rs.next()) {
                    T obj = clazz.getDeclaredConstructor().newInstance(); // Create a new instance of T

                    // Iterate over each field in the class
                    for (Field field : allFields) {
                        field.setAccessible(true); // Make the field accessible

                        String fieldName = field.getName();
                        for (int i = 1; i <= columnCount; i++) {
                            String columnName = metaData.getColumnName(i);
                            String fieldConfigName = configReader.getFieldName(clazz.getSimpleName(), columnName);

                            if (fieldName.equalsIgnoreCase(fieldConfigName)) {
                                Object columnValue = rs.getObject(i);

                                if (columnValue != null) {
                                    // Check for compatible type assignment and convert if necessary
                                    if (field.getType().isAssignableFrom(columnValue.getClass())) {
                                        field.set(obj, columnValue);
                                    } else if (field.getType().equals(Integer.class) && columnValue instanceof Number) {
                                        field.set(obj, ((Number) columnValue).intValue());
                                    } else if (field.getType().equals(Double.class) && columnValue instanceof Number) {
                                        field.set(obj, ((Number) columnValue).doubleValue());
                                    } else if (field.getType().equals(String.class)) {
                                        field.set(obj, columnValue.toString());
                                    } else if (field.getType().equals(Boolean.class) && columnValue instanceof Boolean) {
                                        field.set(obj, columnValue);
                                    }
                                    // Handle other type conversions as needed
                                }
                                break;
                            }
                        }
                    }

                    list.add(obj);
                }
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error executing query: " + query, e);
        } finally {
            closeResources(preparedStatement, null);
        }

        return list;
    }
    
    public int executeUpdate(String query) throws SQLException {
    	int rowsAffected = 0;
        PreparedStatement preparedStatement = null;
    	preparedStatement = connection.prepareStatement(query);
        rowsAffected = preparedStatement.executeUpdate();
        return rowsAffected;
    }

}
