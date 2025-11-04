package com.portfolioai.service.stocksymbols.service;

import com.portfolioai.service.stocksymbols.model.StockSymbol;
import com.portfolioai.service.stocksymbols.repository.StockSymbolRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StockSymbolDownloadService {
    
    private static final Logger log = LoggerFactory.getLogger(StockSymbolDownloadService.class);
    private static final String DELIMITER = ",";
    
    @Value("${nse.stock-symbols.url}")
    private String nseStockSymbolsUrl;
    
    private final StockSymbolRepository stockSymbolRepository;
    private final RestTemplate restTemplate;
    
    public String downloadCsvFile() {
        try {
            log.info("Downloading CSV file from URL: {}", nseStockSymbolsUrl);
            String csvContent = restTemplate.getForObject(nseStockSymbolsUrl, String.class);
            log.info("Successfully downloaded CSV file with {} characters", csvContent != null ? csvContent.length() : 0);
            return csvContent;
        } catch (Exception e) {
            log.error("Error downloading CSV file from URL: {}", nseStockSymbolsUrl, e);
            throw new RuntimeException("Failed to download CSV file: " + e.getMessage(), e);
        }
    }
    
    public List<StockSymbol> parseCsvContent(String csvContent) {
        List<StockSymbol> stockSymbols = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new StringReader(csvContent))) {
            String line;
            boolean isFirstLine = true;
            
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip header
                }
                
                try {
                    StockSymbol stockSymbol = parseStockSymbolFromCsvLine(line);
                    if (stockSymbol != null) {
                        stockSymbols.add(stockSymbol);
                    }
                } catch (Exception e) {
                    log.warn("Error parsing line: {}, Error: {}", line, e.getMessage());
                }
            }
        } catch (IOException e) {
            log.error("Error reading CSV content", e);
            throw new RuntimeException("Failed to parse CSV content", e);
        }
        
        log.info("Parsed {} stock symbols from CSV", stockSymbols.size());
        return stockSymbols;
    }
    
    private StockSymbol parseStockSymbolFromCsvLine(String line) {
        try {
            String[] fields = line.split(DELIMITER);
            
            if (fields.length < 8) {
                log.warn("Insufficient fields in line: {}", line);
                return null;
            }
            
            return StockSymbol.builder()
                    .symbol(fields[0].trim())
                    .company_name(fields[1].trim())
                    .series(fields[2].trim())
                    .date_of_listing(fields[3].trim())
                    .paid_up_value(parseLongSafely(fields[4]))
                    .market_lot(parseLongSafely(fields[5]))
                    .isin(fields[6].trim())
                    .face_value(parseLongSafely(fields[7]))
                    .created_at(LocalDateTime.now())
                    .updated_at(LocalDateTime.now())
                    .build();
        } catch (Exception e) {
            log.error("Error parsing line: {}", line, e);
            return null;
        }
    }
    
    private long parseLongSafely(String value) {
        try {
            return Long.parseLong(value.trim());
        } catch (NumberFormatException e) {
            log.warn("Invalid number format: {}, returning 0", value);
            return 0L;
        }
    }
    
    public int saveStockSymbolsToDatabase(List<StockSymbol> stockSymbols) {
        try {
            log.info("Saving {} stock symbols to database", stockSymbols.size());
            
            // Clear existing data
            stockSymbolRepository.deleteAllStockSymbols();
            log.info("Cleared existing stock symbols from database");
            
            // Save new data
            Iterable<StockSymbol> savedSymbols = stockSymbolRepository.saveAll(stockSymbols);
            int savedCount = 0;
            for (StockSymbol ignored : savedSymbols) {
                savedCount++;
            }
            
            log.info("Successfully saved {} stock symbols to database", savedCount);
            return savedCount;
        } catch (Exception e) {
            log.error("Error saving stock symbols to database", e);
            throw new RuntimeException("Failed to save stock symbols to database", e);
        }
    }
    
    public long getStockSymbolCount() {
        return stockSymbolRepository.countStockSymbols();
    }
    
    public int downloadAndSaveStockSymbols() {
        try {
            log.info("Starting stock symbols download and save process");
            
            // Download CSV content
            String csvContent = downloadCsvFile();
            
            // Parse CSV content
            List<StockSymbol> stockSymbols = parseCsvContent(csvContent);
            
            // Save to database
            int savedCount = saveStockSymbolsToDatabase(stockSymbols);
            
            log.info("Completed stock symbols download and save process. Saved {} symbols", savedCount);
            return savedCount;
            
        } catch (Exception e) {
            log.error("Error in download and save process", e);
            throw new RuntimeException("Failed to download and save stock symbols", e);
        }
    }
}