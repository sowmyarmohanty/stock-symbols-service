package com.portfolioai.service.stocksymbols;

import com.portfolioai.service.stocksymbols.model.StockSymbol;
import com.portfolioai.service.stocksymbols.service.CSVToJSONService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.EnableCaching;

import java.util.List;

@SpringBootApplication
@EnableCaching
public class StockSymbolsApplication {
	@Autowired
	protected CSVToJSONService csvToJSONService;
	public static void main(String[] args) {
		SpringApplication.run(StockSymbolsApplication.class, args);
	}
	@PostConstruct
	@CachePut(cacheNames = "symbols")
	private List<StockSymbol> init(){
		return csvToJSONService.convertCSVTOJSON();
	}
}
