package com.example.library.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.library.entity.Book;
import com.example.library.repository.LibraryRepository;


@Service
public class ExcelService {

	@Autowired
	private LibraryRepository libraryRepository;
	
	public String bulkUploadToES(MultipartFile excelFile) {
		List<Map<String, String>> errorList = new ArrayList<>();
		try {
			Workbook workbook = new XSSFWorkbook(excelFile.getInputStream());
			Sheet sheet = workbook.getSheetAt(0);
			Iterator<Row> iterator = sheet.iterator();
			List<Book> books = new ArrayList<>();
			iterator.next();
			while (iterator.hasNext()) {
				Row currentRow = iterator.next();
				try {
					String title = currentRow.getCell(0).getStringCellValue();
					String author = currentRow.getCell(1).getStringCellValue();
					Date publicationDate = currentRow.getCell(2).getDateCellValue();
					Long isbn = (long) currentRow.getCell(10).getNumericCellValue();
					String genre = currentRow.getCell(4).getStringCellValue().toUpperCase();
					String publisher = currentRow.getCell(5).getStringCellValue();
					Integer pages = (int) currentRow.getCell(6).getNumericCellValue();
					String language = currentRow.getCell(7).getStringCellValue();
					Float rating = Double.valueOf(currentRow.getCell(8).getNumericCellValue()).floatValue();
					Integer availablity = (int) currentRow.getCell(9).getNumericCellValue();
					Book book = new Book(isbn, title, publicationDate, author, genre, publisher, pages, language,
							rating, availablity);
					books.add(book);
				} catch (Exception e) {
					Map<String, String> error = new HashMap<>();
					error.put(currentRow.getCell(10).getStringCellValue(), e.getMessage());
					errorList.add(error);
				}
			}
			workbook.close();
			libraryRepository.saveAll(books);
			System.out.println(books);
			System.out.println(books.size());
			System.out.println(errorList);
			System.out.println(errorList.size());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
