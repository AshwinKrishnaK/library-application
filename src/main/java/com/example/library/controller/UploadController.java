package com.example.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.library.service.ExcelService;

@RestController
public class UploadController {

	@Autowired
	private ExcelService excelService;
	
	@PostMapping("/upload")
	public String uploadExcel(@RequestParam("excelfile") MultipartFile multipartFile) {
		excelService.bulkUploadToES(multipartFile);
		return "Done";
	}
}
