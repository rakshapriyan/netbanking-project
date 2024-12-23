package com.banking.service;

import java.util.List;

import com.banking.config.QueryBuilder;
import com.banking.databaseOperations.DBService;
import com.banking.entity.Branch;
import com.banking.util.Constant;

public class BranchService {
	
	private QueryBuilder queryBuilder;
	private DBService dbService;

	public BranchService() {
		queryBuilder = new QueryBuilder();
		dbService = new DBService(Constant.YAML_PATH);
	}
	
	public List<Branch> getAllBranches(){
		List<Branch> branchs = dbService.get(Branch.class, null, null);
		return branchs;
	}

}
