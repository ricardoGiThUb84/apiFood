//package com.algaworks.algafood.jpa;
//
//import java.math.BigDecimal;
//import java.util.List;
//
//import org.hibernate.stat.CacheableDataStatistics;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.WebApplicationType;
//import org.springframework.boot.builder.SpringApplicationBuilder;
//import org.springframework.context.ApplicationContext;
//
//import com.algaworks.algafood.AlgafoodApplication;
//import com.algaworks.algafood.domain.model.Cozinha;
//import com.algaworks.algafood.domain.model.Restaurante;
//import com.algaworks.algafood.domain.repository.CozinhaRepository;
//import com.algaworks.algafood.domain.repository.RestauranteRepository;
//
//public class ConsultarCozinhaMain {
//	
//	
//	public static void main(String[] args) {
//		
//		ApplicationContext apliApplicationContext = 
//				new SpringApplicationBuilder(AlgafoodApplication.class)
//				.web(WebApplicationType.NONE)
//				.run(args);
//		
//		
//		Cozinha c1 = new Cozinha();
//		
//		c1.setNome("Brasileira");
//		
//		
//		apliApplicationContext.getBean(CozinhaRepository.class).salvar(c1);
//		
//		
//		List<Cozinha> listaCozinhas = 
//		        apliApplicationContext.getBean(CozinhaRepository.class).listar();
//		
//		RestauranteRepository beanRestaurante = apliApplicationContext.getBean(RestauranteRepository.class);
//		
//		System.out.println(listaCozinhas);
//		
//		System.out.println(beanRestaurante.listar());
//	}
//
//}
