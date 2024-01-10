package com.example.library.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "book")
public class Book {

	@Id
	private long isbn;
	
	private String bootTitle;
	
	private Date publicationDate;
	
	private String author;
	
	private String genre;
	
	private String publisher;
	
	private int pages;
	
	private String language;
	
	private float rating;
	
	private int booksAvailable;
}
