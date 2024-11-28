package com.banking.databaseOperations;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;


public class YAMLReader {

	private Map<String, Map<String, Object>> mappings;

	public YAMLReader(String filePath) {
        Yaml yaml = new Yaml();
        try (InputStream in = new FileInputStream(filePath)) {
            mappings = yaml.load(in);
            System.out.println(mappings);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


	public String getTableName(String className) {
	    for (Map.Entry<String, Map<String, Object>> entry : mappings.entrySet()) {
	        String tableName = entry.getKey();
	        Map<String, Object> tableDetails = entry.getValue();

	        if (className.equals(tableDetails.get("class_name"))) {
	            return tableName;
	        }
	    }
	    return null;
	}


	public String getColumnName(String className, String fieldName) {
	    for (Map.Entry<String, Map<String, Object>> entry : mappings.entrySet()) {
	        Map<String, Object> tableDetails = entry.getValue();
	        if (className.equals(tableDetails.get("class_name"))) {
	            Map<String, String> columns = (Map<String, String>) tableDetails.get("columns");
	            return columns != null ? columns.get(fieldName) : null;
	        }
	    }
	    return null;
	}


	public Map<String, String> getColumns(String className) {
        for (Map.Entry<String, Map<String, Object>> entry : mappings.entrySet()) {
            Map<String, Object> tableDetails = entry.getValue();
            if (className.equals(tableDetails.get("class_name"))) {
                return (Map<String, String>) tableDetails.get("columns");
            }
        }
        return null;
    }


}
