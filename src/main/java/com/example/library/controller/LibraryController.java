package com.example.library.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.library.entity.Book;
import com.example.library.service.LibraryService;

@RestController
@RequestMapping("/api")
public class LibraryController {

	@Autowired
	private LibraryService libraryService;

	@GetMapping("/test")
	public String testController() {
		return "test worked";
	}

	@GetMapping("/findAll")
	public Iterable<Book> findAll() {
		return libraryService.getBooks();
	}

	@PostMapping("/insert")
	public Book insertProduct(@RequestBody Book book) {
		return libraryService.insertBook(book);
	}
	
	@GetMapping("/groupbybook")
	public Map<String, List<Book>> groupByBook(){
		return libraryService.groupByGenre();
	}
}
