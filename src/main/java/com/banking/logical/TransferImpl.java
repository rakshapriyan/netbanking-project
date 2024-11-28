package com.banking.logical;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;

import com.banking.dto.TransferDTO;
import com.banking.service.TransactionService;
import com.banking.service.TransferService;
import com.banking.util.ConvertJson;

public class TransferImpl {
	
	public static void transfer(HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException, SQLException {
		System.out.println("In transfer Impl");
		TransferDTO dto = ConvertJson.fromJson(request, TransferDTO.class);
		TransferService service = new TransferService();
		service.transferAmount(dto);
	}

}
