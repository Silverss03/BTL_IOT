package com.iot_backend_rfid.exception;

import com.iot_backend_rfid.dto.response.Response;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Arrays;

@ControllerAdvice
@RequiredArgsConstructor
public class ApiExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

	@ExceptionHandler({AppException.class})
	protected ResponseEntity<Response<Object>> handleAppException(AppException appException) {
		log.error("An unexpected error occurred: {}", appException.getMessage(), appException);
		String message=appException.getMessage();
		Response<Object> responseError=Response.builder()
				.success(false)
				.message(message)
				.build();
		return ResponseEntity.status(appException.getHttpStatus()).body(responseError);
	}


	// validate error
	@ExceptionHandler({MethodArgumentNotValidException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ResponseEntity<Response<Object>> handleValidationException(MethodArgumentNotValidException ex) {
		log.error("An unexpected runtime error occurred: {}", ex.getMessage(), ex);
		String message=ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
		Response<Object> responseError=Response.builder()
				.success(false)
				.message(message)
				.build();
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseError);
	}


	// authorization
	@ExceptionHandler({AccessDeniedException.class})
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ResponseEntity<Response<Object>> handleAccessDeniedException(Exception ex) {
		log.error("An unexpected error occurred: {}", ex.getMessage(), ex);
		Response<Object> responseError=Response.builder()
				.success(false)
				.message("Unauthorization")
				.build();
		return ResponseEntity.status(HttpStatus.CONFLICT).body(responseError);
	}

	// JSON parse error (for request body)
	@ExceptionHandler({HttpMessageNotReadableException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<Response<Object>> handleInvalidFormatException(Exception ex) {
		log.error("An unexpected error occurred: {}", ex.getMessage(), ex);
		Response<Object> responseError=Response.builder()
				.success(false)
				.message("Data format error!")
				.build();
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseError);
	}

	@ExceptionHandler({RuntimeException.class})
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<Response<Object>> handleRuntimeException(Exception ex) {
		log.error("An unexpected error occurred: {}", ex.getMessage(), ex);
		Response<Object> responseError=Response.builder()
				.success(false)
				.message("System error")
				.build();
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseError);
	}

	@ExceptionHandler({Exception.class})
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<Response<Object>> handleException(Exception ex) {
		log.error("An unexpected error occurred: {}", ex.getMessage(), ex);
		Response<Object> responseError=Response.builder()
				.success(false)
				.message("System error")
				.build();
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseError);
	}
}
