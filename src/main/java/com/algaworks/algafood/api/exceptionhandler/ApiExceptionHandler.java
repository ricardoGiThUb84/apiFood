package com.algaworks.algafood.api.exceptionhandler;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;


@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler{


	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

		Throwable rootCause = extraiCausa(ex);

		if(rootCause instanceof InvalidFormatException){

			return handleInvalidFormatException((InvalidFormatException) rootCause,
					 headers, status, request);
		}

		HttpStatus statusApp = status;
		ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
		String detail = "Verifique erro de sintaxe no corpo da requisição.";
		Problem problem = createProblemBuilder(status,problemType, detail).build();

		return handleExceptionInternal(ex,
				problem,
				new HttpHeaders(),
				statusApp,
				request);
	}

	private ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException ex,
														   HttpHeaders headers,

														   HttpStatus status, WebRequest request) {

		String propriedade = ex.getPath().stream()
				.map(c -> c.getFieldName())
				.collect(Collectors.joining("."));

		HttpStatus statusApp = status;
		ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
		String detail = String.format("A propriedade '%s' com valor '%s'," +
				" com tipo incompatível. Campo de ter o tipo: '%s'.",
				propriedade,ex.getValue(), ex.getTargetType().getSimpleName());
		Problem problem = createProblemBuilder(status,problemType, detail).build();

		return handleExceptionInternal(ex,
				problem,
				headers,
				statusApp,
				request);
	}

	private Throwable extraiCausa(Throwable rootCause) {
		Throwable causa = rootCause.getCause();
		if(causa == null || causa == rootCause){

			return rootCause;
		}

		return extraiCausa(causa);
	}

	@ExceptionHandler({EntidadeNaoEncontradaException.class})
	public ResponseEntity<?> handleEntidadeNaoEncontradaException(EntidadeNaoEncontradaException ex,
			WebRequest request){

		HttpStatus status = HttpStatus.NOT_FOUND;
		ProblemType problemType = ProblemType.ENTIDADE_NAO_ENCONTRADA;
		String detail = ex.getMessage();
		Problem problem = createProblemBuilder(status,problemType, detail).build();

		return handleExceptionInternal(ex,
				problem,
				new HttpHeaders(),
				status,
				request);
	}
	
	@ExceptionHandler({NegocioException.class})
	public ResponseEntity<?> handleEntidadeNegocioException(NegocioException ex, WebRequest request){

		HttpStatus status = HttpStatus.BAD_REQUEST;
		ProblemType problemType = ProblemType.ERRO_NEGOCIO;
		String detail = ex.getMessage();

		Problem problem = createProblemBuilder(status, problemType, detail).build();

		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}
	
	@ExceptionHandler({EntidadeEmUsoException.class})
	public ResponseEntity<?> handleEntidadeEntidadeEmUsoException(EntidadeEmUsoException ex, WebRequest request){
		
		return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(),HttpStatus.CONFLICT,
				request);

		
//		return ResponseEntity.status(HttpStatus.CONFLICT).body(problema);
	}
	
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		if(Objects.isNull(body)){
			body = Problem.builder()
					.title(status.getReasonPhrase())
					.status(status.value()).build();
		}else if(body instanceof String){

			body = Problem.builder()
					.title((String) body)
					.status(status.value()).build();

		}
	

		
		return super.handleExceptionInternal(ex, body, headers, status, request);
	}


	private Problem.ProblemBuilder createProblemBuilder(HttpStatus status, ProblemType problemType, String details){

		return Problem.builder()
				.status(status.value())
				.type(problemType.getUri())
				.title(problemType.getTitle())
				.detail(details);
	}

}
