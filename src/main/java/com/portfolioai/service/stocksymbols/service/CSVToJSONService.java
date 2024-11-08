package com.portfolioai.service.stocksymbols.service;

import com.portfolioai.service.stocksymbols.model.StockSymbol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@Service
public class CSVToJSONService {
    public static final String DELIMITER = ",";
    private static final Logger log = LoggerFactory.getLogger(CSVToJSONService.class);
    @Autowired
    private ResourceLoader resourceLoader;

    @Cacheable(cacheNames = "symbols")
    public  List<StockSymbol> convertCSVTOJSON(){
        Resource resource = resourceLoader.getResource("classpath:/data/nse-symbols.csv");
        List<StockSymbol> symbolList;
        try{
            symbolList =  new BufferedReader(new FileReader(resource.getFile()))
                    .lines()
                    .skip(1) //skip header
                    .map(lineItems -> {
                        String[] individualRecordFromCSV = lineItems.split(DELIMITER);
                        return getJsonObject(individualRecordFromCSV);
                    }).toList();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return symbolList;
    }

    private static StockSymbol getJsonObject(String[] individualRecordFromCSV) {
        return StockSymbol.builder()
                .symbol(individualRecordFromCSV[0])
                .company_name(individualRecordFromCSV[1])
                .series(individualRecordFromCSV[2])
                .date_of_listing(individualRecordFromCSV[3])
                .paid_up_value(Long.parseLong(individualRecordFromCSV[4]))
                .market_lot(Long.parseLong(individualRecordFromCSV[5]))
                .isin(individualRecordFromCSV[6])
                .face_value(Long.parseLong(individualRecordFromCSV[7]))
                .build();
    }
}
