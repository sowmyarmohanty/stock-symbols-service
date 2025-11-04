package com.portfolioai.service.stocksymbols.repository;

import com.portfolioai.service.stocksymbols.model.StockSymbol;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockSymbolRepository extends CrudRepository<StockSymbol, Long> {
    
    @Query("SELECT * FROM stock_symbol WHERE symbol = :symbol")
    Optional<StockSymbol> findBySymbol(@Param("symbol") String symbol);
    
    @Query("SELECT * FROM stock_symbol WHERE UPPER(symbol) LIKE UPPER(CONCAT('%', :searchTerm, '%')) OR UPPER(company_name) LIKE UPPER(CONCAT('%', :searchTerm, '%'))")
    List<StockSymbol> findBySymbolOrCompanyNameContainingIgnoreCase(@Param("searchTerm") String searchTerm);
    
    @Query("SELECT * FROM stock_symbol WHERE series = :series")
    List<StockSymbol> findBySeries(@Param("series") String series);
    
    @Query("SELECT COUNT(*) FROM stock_symbol")
    long countStockSymbols();
    
    @Query("DELETE FROM stock_symbol")
    void deleteAllStockSymbols();
}