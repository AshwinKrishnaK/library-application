package com.example.library.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.example.library.entity.Book;

@Repository
public interface LibraryRepository extends ElasticsearchRepository<Book, Long>{

}
