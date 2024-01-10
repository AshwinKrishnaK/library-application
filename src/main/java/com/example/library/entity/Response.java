package com.example.library.entity;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;

public class Response {
	
	public static ResponseEntity<Object> buildSuccessResponse(Object data) {
		return new ResponseEntity<>(data, HttpStatus.OK);
	}
	
	public static ResponseEntity<Object> buildSuccessResponse(Object data,HttpHeaders header) {
		return new ResponseEntity<Object>(data, header, HttpStatus.OK);
	}
	
	public static ResponseEntity<Object> buildFailureResponse(Object data) {
		return new ResponseEntity<>(data, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
