package com.example.library.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.library.entity.Book;
import com.example.library.repository.LibraryRepository;

@Service
public class LibraryService {

	@Autowired
	private LibraryRepository libraryRepo;

	public Iterable<Book> getBooks() {
		return libraryRepo.findAll();
	}

	public Book insertBook(Book book) {
		return libraryRepo.save(book);
	}

	public Book updateBook(Book book, long isbn) {
		Book book2 = libraryRepo.findById(isbn).get();
		book2.setBooksAvailable(book.getBooksAvailable());
		return book2;
	}

	public void deleteBook(long isbn) {
		libraryRepo.deleteById(isbn);
	}
	
	public Map<String, List<Book>> groupByGenre() {
		Map<String, List<Book>> groupBookByGenre = StreamSupport.stream(getBooks().spliterator(), false)
				.collect(Collectors.groupingBy(Book::getGenre));
		return groupBookByGenre;
	}

}
