package dev.be.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;
import org.springframework.data.web.config.SortHandlerMethodArgumentResolverCustomizer;

@Configuration(proxyBeanMethods = false)
@EnableJpaAuditing
public class JpaConfig {

	@Bean
	public SortHandlerMethodArgumentResolverCustomizer sortHandlerMethodArgumentResolverCustomize() {
		return s -> {
			s.setFallbackSort(Sort.by(Direction.DESC, "createdDate"));
		};
	}
	
	@Bean
	public PageableHandlerMethodArgumentResolverCustomizer pageableHandlerMethodArgumentResolverCustomize() {
		return p -> {
			p.setOneIndexedParameters(true);
			p.setPageParameterName("page");
			p.setMaxPageSize(8);
			p.setFallbackPageable(PageRequest.of(0, 8));
		};
	}
}
