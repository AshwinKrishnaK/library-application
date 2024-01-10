package com.example.library.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
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
public class LibraryService {

	@Autowired
	private LibraryRepository libraryRepo;

	public List<Book> getBooks() {
		List<Book> books = new ArrayList<>();
		libraryRepo.findAll().iterator().forEachRemaining(books::add);
		return books;
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
		return getBooks().stream().collect(Collectors.groupingBy(Book::getGenre));
	}

	public Object uploadExcelToES(MultipartFile excelFile) {
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
			libraryRepo.saveAll(books);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (errorList.size()>0) {
			Map<String, Object> errorMessage = new HashMap<>();
			errorMessage.put("Message", "Partial Data Uploaded to Elastic search , there is some problem with excel sheet values. ISBN number and error is given, Please check and reupload");
			errorMessage.put("Error data", errorList);
			return errorMessage;
		}
		return "Data is successfully uploaded";
	}

	public ByteArrayInputStream convertBooksToExcel() {
		SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
		SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
		List<Book> books = getBooks();
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Book Data");
		Row headerRow = sheet.createRow(0);
		Field[] fields = Book.class.getDeclaredFields();
		int cellNum = 0;
		for (Field field : fields) {
			Cell cell = headerRow.createCell(cellNum++);
			cell.setCellValue(field.getName());
		}
		int rowNum = 1;
		for (Book book : books) {
			Row row = sheet.createRow(rowNum++);
			cellNum = 0;
			for (Field field : fields) {
				field.setAccessible(true);
				Cell cell = row.createCell(cellNum++);
				try {
					Object value = field.get(book);
					if (value instanceof Date) {
						// Format Date objects as desired
						String formattedDate = outputFormat.format(inputFormat.parse(value.toString()));
						cell.setCellValue(formattedDate);
					} else {
						cell.setCellValue(value == null ? "" : value.toString());
					}
				} catch (IllegalAccessException | ParseException e) {
					// Handle exception
				}
			}
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			workbook.write(out);
			workbook.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ByteArrayInputStream(out.toByteArray());
	}
}
