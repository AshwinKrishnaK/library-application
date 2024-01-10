package com.example.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.library.entity.Book;
import com.example.library.entity.Response;
import com.example.library.service.LibraryService;

@RestController
@RequestMapping("/api")
public class LibraryController {

	@Autowired
	private LibraryService libraryService;

	@GetMapping("/findall/books")
	public ResponseEntity<Object> findAll() {
		try {
			return Response.buildSuccessResponse(libraryService.getBooks());
		} catch (Exception e) {
			return Response.buildFailureResponse(e.getMessage());
		}
	}

	@PostMapping("/insert/newbook")
	public ResponseEntity<Object> insertProduct(@RequestBody Book book) {
		try {
			return Response.buildSuccessResponse(libraryService.insertBook(book));
		} catch (Exception e) {
			return Response.buildFailureResponse(e.getMessage());
		}
	}
	
	@GetMapping("/groupby/genre")
	public ResponseEntity<Object> groupByBook(){
		try {
			return Response.buildSuccessResponse(libraryService.groupByGenre());
		} catch (Exception e) {
			return Response.buildFailureResponse(e.getMessage());
		}
	}
	
	@PostMapping("/upload/excel")
	public ResponseEntity<Object> uploadExcel(@RequestParam("excelfile") MultipartFile multipartFile) {
		try {
			return Response.buildSuccessResponse(libraryService.uploadExcelToES(multipartFile));
		} catch (Exception e) {
			return Response.buildFailureResponse(e.getMessage());
		}
	}
	
	@GetMapping("/download/excel")
	public ResponseEntity<Object> getLibraryData(){
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Disposition", "attachment; filename=books.xlsx");
			return Response.buildSuccessResponse(libraryService.convertBooksToExcel(),headers);
		} catch (Exception e) {
			return Response.buildFailureResponse(e.getMessage());
		}
	}
}
