package com.banking.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QueryBuilder {
    private List<String> columns;
    private List<String> conditions;
    private List<String> values;
    private List<String> joinConditions;
    private String table;
    private String operation;
    private ConfigReader configReader;
    private String className;
    private List<String> logicalOperator;

    // New fields for LIMIT and ORDER BY
    private String limit;
    private String orderByColumn;
    private String orderDirection;

    public QueryBuilder() {
        // Initializing the configReader and other lists
        this.configReader = new ConfigReader("/home/raksh-pt7616/eclipse-ee-workspace/Netbanking1/src/main/java/mapping.yaml");
        this.columns = new ArrayList<>();
        this.conditions = new ArrayList<>();
        this.values = new ArrayList<>();
        this.joinConditions = new ArrayList<>();
        logicalOperator = new ArrayList<>();
        this.limit = null;
        this.orderByColumn = null;
        this.orderDirection = "ASC"; // Default to ascending order
    }

    // Method to set the className and retrieve the table name dynamically
    public QueryBuilder table(String className) {
        this.className = className;
        this.table = configReader.getTableName(this.className); // Retrieves table name from config
        return this;
    }

    // Method to specify JOIN conditions directly
    public QueryBuilder join(String joinTable, String column1, String column2) {
        String joinCondition = "JOIN " + joinTable + " ON " + column1 + " = " + column2;
        this.joinConditions.add(joinCondition);
        return this;
    }

    // Method for SELECT operation
    public QueryBuilder select(String... fields) {
        this.operation = "SELECT";
        if (fields.length == 1 && fields[0].equals("*")) {
            this.columns.add("*");
        } else {
            for (String field : fields) {
                String column = configReader.getColumnName(className, field);
                if (column != null) {
                    this.columns.add(column);
                } else {
                    throw new IllegalArgumentException("Unknown field: " + field);
                }
            }
        }
        return this;
    }


    public QueryBuilder and() {
    	logicalOperator.add("AND");
    	return this;
    }


    public QueryBuilder or() {
    	logicalOperator.add("OR");
    	return this;
    }

    // Method for UPDATE operation
    public QueryBuilder update(Map<String, String> fieldValuePairs) {
        this.operation = "UPDATE";
        for (Map.Entry<String, String> entry : fieldValuePairs.entrySet()) {
            String field = entry.getKey();
            String value = entry.getValue();

            // Get the column name from the field (consider both parent and child class)
            String column = configReader.getColumnName(className, field);
            if (column != null) {
                this.columns.add(column);
                this.values.add(value); // Add the value to the values list
            } else {
                throw new IllegalArgumentException("Unknown field: " + field);
            }
        }
        return this;
    }

    // Method for DELETE operation
    public QueryBuilder delete() {
        this.operation = "DELETE";
        return this;
    }

    // Method to add WHERE conditions
    public QueryBuilder where(String field, String operator, String value) {
        // Convert field name to column name (consider both parent and child class)
        String column = configReader.getColumnNameIncludingParent(className, field);
        if (column == null) {
            column = field;
        }
        
        // Add the condition to the list
        String condition = column + " " + operator + " '" + value + "'";
        this.conditions.add(condition);
        return this;
    }

    // Method to set LIMIT clause
    public QueryBuilder limit(int limit) {
        this.limit = String.valueOf(limit);
        return this;
    }

    // Method to set ORDER BY clause
    public QueryBuilder orderBy(String column, String direction) {
        if (!"ASC".equalsIgnoreCase(direction) && !"DESC".equalsIgnoreCase(direction)) {
            throw new IllegalArgumentException("Invalid order direction. Use 'ASC' or 'DESC'.");
        }
        this.orderByColumn = configReader.getColumnName(className, column);
        this.orderDirection = direction.toUpperCase();
        return this;
    }

    // Build the final SQL query
    public String build() {
        StringBuilder sb = new StringBuilder();
        switch (operation) {
            case "SELECT":
            	if(configReader.getParentClassName(className) != null) {
            		Map<String, String> foreignKey = configReader.getFirstForeignKeyMapping(className);
                    join(configReader.getTableName(foreignKey.get("referenced_class")), foreignKey.get("field"), foreignKey.get("references"));

            	}
            	sb.append("SELECT ").append(String.join(", ", columns))
                  .append(" FROM ").append(table);
                // Handling joins if present
                for (String join : joinConditions) {
                    sb.append(" ").append(join);
                }
                break;
            case "UPDATE":
                sb.append("UPDATE ").append(table);
                String className = configReader.getClassName(table);
                String parentClass = configReader.getParentClassName(className);
                if(parentClass != null && !parentClass.equals("Object")) {
                	sb.append(" JOIN ").append(configReader.getTableName(parentClass));
                }
                sb.append(" SET ");
                for (int i = 0; i < columns.size(); i++) {
                    sb.append(columns.get(i)).append(" = '").append(values.get(i)).append("'");
                    if (i < columns.size() - 1) {
						sb.append(", ");
					}
                }
                break;
            case "DELETE":
                sb.append("DELETE FROM ").append(table);
                break;
        }
        if (!conditions.isEmpty()) {
            sb.append(" WHERE ");
            int logicSize = logicalOperator.size();
            for(int i = 0; i<conditions.size(); i++) {
            	sb.append(conditions.get(i));
            	if(i<logicSize) {
            		sb.append(" "+logicalOperator.get(i)+" ");
            	}
            }


        }

        // Append ORDER BY if set
        if (orderByColumn != null) {
            sb.append(" ORDER BY ").append(orderByColumn).append(" ").append(orderDirection);
        }

        // Append LIMIT if set
        if (limit != null) {
            sb.append(" LIMIT ").append(limit);
        }

        sb.append(";");
        System.out.println("This is the query for operation " + operation + "     " + sb);
        
        
        this.columns = new ArrayList<>();
        this.conditions = new ArrayList<>();
        this.values = new ArrayList<>();
        this.joinConditions = new ArrayList<>();
        logicalOperator = new ArrayList<>();
        this.limit = null;
        this.orderByColumn = null;
        this.orderDirection = "ASC";
        return sb.toString();
    }
}
