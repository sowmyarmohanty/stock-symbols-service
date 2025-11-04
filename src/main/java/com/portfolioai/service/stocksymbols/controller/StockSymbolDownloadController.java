package com.portfolioai.service.stocksymbols.controller;

import com.portfolioai.service.stocksymbols.model.ResponseData;
import com.portfolioai.service.stocksymbols.model.StockSymbol;
import com.portfolioai.service.stocksymbols.repository.StockSymbolRepository;
import com.portfolioai.service.stocksymbols.service.StockSymbolDownloadService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/stock-symbols/download")
@RequiredArgsConstructor
public class StockSymbolDownloadController {
    
    private static final Logger log = LoggerFactory.getLogger(StockSymbolDownloadController.class);
    
    private final StockSymbolDownloadService stockSymbolDownloadService;
    private final StockSymbolRepository stockSymbolRepository;
    
    @PostMapping("/refresh")
    public ResponseEntity<ResponseData<Object>> downloadAndRefreshStockSymbols() {
        try {
            log.info("Received request to download and refresh stock symbols");
            
            int savedCount = stockSymbolDownloadService.downloadAndSaveStockSymbols();
            
            Map<String, Object> result = Map.of(
                "message", "Stock symbols downloaded and saved successfully",
                "savedCount", savedCount,
                "totalCount", stockSymbolDownloadService.getStockSymbolCount()
            );
            
            return ResponseEntity.ok(
                ResponseData.builder()
                    .data(result)
                    .message("Success")
                    .build()
            );
            
        } catch (Exception e) {
            log.error("Error refreshing stock symbols", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseData.builder()
                    .data(null)
                    .message("Failed to refresh stock symbols: " + e.getMessage())
                    .build());
        }
    }
    
    @GetMapping("/status")
    public ResponseEntity<ResponseData<Object>> getDownloadStatus() {
        try {
            long totalCount = stockSymbolDownloadService.getStockSymbolCount();
            
            Map<String, Object> status = Map.of(
                "totalStockSymbols", totalCount,
                "databaseStatus", totalCount > 0 ? "Populated" : "Empty",
                "h2ConsoleUrl", "/h2-console"
            );
            
            return ResponseEntity.ok(
                ResponseData.builder()
                    .data(status)
                    .message("Success")
                    .build()
            );
            
        } catch (Exception e) {
            log.error("Error getting download status", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseData.builder()
                    .data(null)
                    .message("Failed to get status: " + e.getMessage())
                    .build());
        }
    }
    
    @GetMapping("/database/all")
    public ResponseEntity<ResponseData<Object>> getAllStockSymbolsFromDatabase() {
        try {
            List<StockSymbol> stockSymbols = (List<StockSymbol>) stockSymbolRepository.findAll();
            
            return ResponseEntity.ok(
                ResponseData.builder()
                    .data(stockSymbols)
                    .message("Success")
                    .build()
            );
            
        } catch (Exception e) {
            log.error("Error retrieving stock symbols from database", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseData.builder()
                    .data(null)
                    .message("Failed to retrieve stock symbols: " + e.getMessage())
                    .build());
        }
    }
    
    @GetMapping("/database/search/{searchTerm}")
    public ResponseEntity<ResponseData<Object>> searchStockSymbolsInDatabase(@PathVariable String searchTerm) {
        try {
            List<StockSymbol> stockSymbols = stockSymbolRepository
                .findBySymbolOrCompanyNameContainingIgnoreCase(searchTerm);
            
            return ResponseEntity.ok(
                ResponseData.builder()
                    .data(stockSymbols)
                    .message("Success")
                    .build()
            );
            
        } catch (Exception e) {
            log.error("Error searching stock symbols in database", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseData.builder()
                    .data(null)
                    .message("Failed to search stock symbols: " + e.getMessage())
                    .build());
        }
    }
    
    @GetMapping("/database/series/{series}")
    public ResponseEntity<ResponseData<Object>> getStockSymbolsBySeries(@PathVariable String series) {
        try {
            List<StockSymbol> stockSymbols = stockSymbolRepository.findBySeries(series);
            
            return ResponseEntity.ok(
                ResponseData.builder()
                    .data(stockSymbols)
                    .message("Success")
                    .build()
            );
            
        } catch (Exception e) {
            log.error("Error retrieving stock symbols by series", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseData.builder()
                    .data(null)
                    .message("Failed to retrieve stock symbols by series: " + e.getMessage())
                    .build());
        }
    }
    
    @DeleteMapping("/database/clear")
    public ResponseEntity<ResponseData<Object>> clearDatabase() {
        try {
            stockSymbolRepository.deleteAllStockSymbols();
            
            Map<String, Object> result = Map.of(
                "message", "Database cleared successfully",
                "remainingCount", stockSymbolDownloadService.getStockSymbolCount()
            );
            
            return ResponseEntity.ok(
                ResponseData.builder()
                    .data(result)
                    .message("Success")
                    .build()
            );
            
        } catch (Exception e) {
            log.error("Error clearing database", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseData.builder()
                    .data(null)
                    .message("Failed to clear database: " + e.getMessage())
                    .build());
        }
    }
}