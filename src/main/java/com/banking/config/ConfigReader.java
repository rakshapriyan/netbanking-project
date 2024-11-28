package com.banking.config;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public class ConfigReader {
    private Map<String, Map<String, Object>> mappings;

    public ConfigReader(String filePath) {
        Yaml yaml = new Yaml();
        try (InputStream in = new FileInputStream(filePath)) {
            mappings = yaml.load(in);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load mapping configuration", e);
        }
    }

    // Get table name by class name
    public String getTableName(String className) {
        for (String tableName : mappings.keySet()) {
            Map<String, Object> tableData = mappings.get(tableName);
            if (className.equals(tableData.get("class_name"))) {
                return tableName;
            }
        }
        return null;
    }

    // Get column name by class name and field name
    public String getColumnName(String className, String fieldName) {
        // First check in the current class
        String columnName = getColumnFromClass(className, fieldName);
        if (columnName != null) {
			return columnName;
		}

        // Check in parent classes if field is not found
        String parentClass = getParentClassName(className);
        while (parentClass != null) {
            columnName = getColumnFromClass(parentClass, fieldName);
            if (columnName != null) {
				return columnName;
			}
            // Move to the parent class of the current parent class
            parentClass = getParentClassName(parentClass);
        }

        // Return null if no match found
        return null;
    }
//    where("emailId", " = ", email).or().where("username", " = ", email)
    // Retrieve the column name from the class
    private String getColumnFromClass(String className, String fieldName) {

        for (String tableName : mappings.keySet()) {
            Map<String, Object> tableData = mappings.get(tableName);
            if (className.equals(tableData.get("class_name"))) {
                Map<String, String> columns = (Map<String, String>) tableData.get("columns");
                for (Map.Entry<String, String> entry : columns.entrySet()) {
                    if (entry.getValue().equals(fieldName)) {
                        return entry.getKey();
                    }
                }
            }
        }
        return null;
    }

    // Get parent class name if any
    public String getParentClassName(String className) {
        for (String tableName : mappings.keySet()) {
            Map<String, Object> tableData = mappings.get(tableName);
            if (className.equals(tableData.get("class_name")) && tableData.containsKey("extends")) {
                return (String) tableData.get("extends");
            }
        }
        return null;
    }

    // Get columns for a class
    public Map<String, String> getClassColumns(String className) {
        for (String tableName : mappings.keySet()) {
            Map<String, Object> tableData = mappings.get(tableName);
            if (className.equals(tableData.get("class_name"))) {
                return (Map<String, String>) tableData.get("columns");
            }
        }
        return new HashMap<>();
    }


    public Map<String, String> getFirstForeignKeyMapping(String className) {
    	String tableName = getTableName(className);
        Map<String, Object> classConfig = mappings.get(tableName);

        // Check if the classConfig exists and contains the foreign_keys property
        if (classConfig != null && classConfig.containsKey("foreign_keys")) {
            List<Map<String, String>> foreignKeys = (List<Map<String, String>>) classConfig.get("foreign_keys");

            // Return the first foreign key mapping if available
            if (foreignKeys != null && !foreignKeys.isEmpty()) {
                return foreignKeys.get(0); // Return the first foreign key mapping
            }
        }

        // Return null if no foreign keys are found
        return null;
    }


    public String getColumnNameIncludingParent(String className, String field) {
        String column = getColumnName(className, field); // Get column from child class
        if (column != null) {
			return column;
		}

        // If no column found in child class, check in parent class
        String parentClassName = getParentClassName(className);
        if (parentClassName != null) {
            return getColumnName(parentClassName, field); // Get column from parent class
        }
        return null;
    }



 // Get field name by class name and column name
    public String getFieldName(String className, String columnName) {
        // Check in the current class
        String fieldName = getFieldFromClass(className, columnName);
        if (fieldName != null) {
			return fieldName;
		}

        // Check in parent classes if column is not found
        String parentClass = getParentClassName(className);
        while (parentClass != null) {
            fieldName = getFieldFromClass(parentClass, columnName);
            if (fieldName != null) {
				return fieldName;
			}

            // Move to the parent class of the current parent class
            parentClass = getParentClassName(parentClass);
        }

        // Return null if no match found
        return null;
    }

    // Helper method to retrieve the field name from a class's column mapping
    private String getFieldFromClass(String className, String columnName) {
        for (String tableName : mappings.keySet()) {
            Map<String, Object> tableData = mappings.get(tableName);
            if (className.equals(tableData.get("class_name"))) {
                Map<String, String> columns = (Map<String, String>) tableData.get("columns");
                for (Map.Entry<String, String> entry : columns.entrySet()) {
                    if (entry.getKey().equals(columnName)) {
                        return entry.getValue(); // Return the field name
                    }
                }
            }
        }
        return null;
    }


    public String getClassName(String tableName) {
        // Loop through all the tables in the mappings
        for (String table : mappings.keySet()) {
            // Get the data for the current table
            Map<String, Object> tableData = mappings.get(table);

            // If the table name matches the input tableName, return the associated class_name
            if (table.equals(tableName)) {
                return (String) tableData.get("class_name");
            }
        }

        // Return null if no matching table is found
        return null;
    }



    public static void main(String[] args) {
    	ConfigReader configReader = new ConfigReader("/home/raksh-pt7616/Downloads/Zoho/Zoho-Training-main/Project/src/mapping.yaml");
		System.out.println(configReader.getColumnName("Customer", "userId"));

	}
}
