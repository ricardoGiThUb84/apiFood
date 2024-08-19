package com.algaworks.algafood.api.exceptionhandler;



import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;
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
		}else if(rootCause instanceof PropertyBindingException){

			return handlePropertyBindingException((PropertyBindingException) rootCause,
					headers, status, request);

		}

		ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
		String detail = "O corpo da requisição está inválido. Verifique erro de sintaxe.";

		Problem problem = createProblemBuilder(status, problemType, detail).build();

		return handleExceptionInternal(ex, problem, headers, status, request);
	}

	private ResponseEntity<Object> handlePropertyBindingException(PropertyBindingException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

		String path = joinPath(ex.getPath());

		ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
		String detail = String.format("A propriedade '%s' não existe. "
				+ "Corrija ou remova essa propriedade e tente novamente.", path);

		Problem problem = createProblemBuilder(status, problemType, detail).build();

		return handleExceptionInternal(ex, problem, headers, status, request);


	}

	private ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException ex,
														   HttpHeaders headers, HttpStatus status,
																WebRequest request) {

		String path = joinPath(ex.getPath());

		ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
		String detail = String.format("A propriedade '%s' recebeu o valor '%s', "
						+ "que é de um tipo inválido. Corrija e informe um valor compatível com o tipo %s.",
				path, ex.getValue(), ex.getTargetType().getSimpleName());

		Problem problem = createProblemBuilder(status, problemType, detail).build();

		return handleExceptionInternal(ex, problem, headers, status, request);
	}

	private Throwable extraiCausa(Throwable rootCause) {
		Throwable causa = rootCause.getCause();
		if(causa == null || causa == rootCause){

			return rootCause;
		}

		return extraiCausa(causa);
	}

	private String joinPath(List<JsonMappingException.Reference> references) {

		return references.stream()
				.map(ref -> ref.getFieldName())
				.collect(Collectors.joining("."));
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
