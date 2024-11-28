package com.banking.databaseOperations;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.banking.config.Criteria;
import com.banking.config.DBConfig;

public class DatabaseService{
	private Connection connection;
    private YAMLReader yamlReader;
    public DatabaseService(String filePath) {
    	connection = DBConfig.getConnection();
    	yamlReader = new YAMLReader(filePath);
    }

    // insert helper
    private <T> void insertQueryBuilder(T obj, String className) throws SQLException {
    	 String tableName = yamlReader.getTableName(className);
        Map<String, String> columns = yamlReader.getColumns(className);
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
                String getterMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                Method getterMethod = obj.getClass().getMethod(getterMethodName);
                Object value = getterMethod.invoke(obj);

                values.append("?");
                parameters.add(value);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
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


    // insert
    public <T> void insert(T obj) throws SQLException {
        if (obj == null) {
            throw new SQLException("Object cannot be null.");
        }


        String superClassName = obj.getClass().getSuperclass().getSimpleName();
        if(superClassName != null && !superClassName.equals("Object")) {
        	System.out.println("super class name: "+superClassName);
        	insertQueryBuilder(obj, superClassName);

        }


        String className = obj.getClass().getSimpleName();
        System.out.println("super class name: "+className);

        insertQueryBuilder(obj, className);

    }


    public <T> List<T> get(Class<?> clazz, List<Criteria> criteria, List<String> logicalOperator){
    	List<T> result = null;
    	String className = clazz.getSimpleName();
    	String tableName = yamlReader.getTableName(className);
    	StringBuilder sql = new StringBuilder("SELECT * FROM" + tableName);
    	String superClassName = clazz.getSuperclass().getSimpleName();
    	if(!superClassName.equals("Object")) {
    		sql.append(" INNER JOIN ").append(superClassName);
    	}

    	sql.append(" WHERE ");
    	int size = criteria.size();
    	for(int i = 0; i<size; i++) {
    		Criteria c = criteria.get(i);
    		String columnName = yamlReader.getColumnName(className, c.getColumn());
    		if(columnName != null && !columnName.equals("")) {
    			c.setColumn(columnName);
    		}

    		sql.append(columnName).append(" = ? , ");
    	}

    	return result;
    }


}

