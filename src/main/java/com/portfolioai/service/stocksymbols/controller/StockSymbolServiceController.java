package com.portfolioai.service.stocksymbols.controller;

import com.portfolioai.service.stocksymbols.model.ResponseData;
import com.portfolioai.service.stocksymbols.model.StockSymbol;
import com.portfolioai.service.stocksymbols.service.CSVToJSONService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/stock-symbols")
@AllArgsConstructor
public class StockSymbolServiceController {
    private CSVToJSONService csvToJSONService;
    @GetMapping("/all")
    public ResponseEntity<ResponseData<Object>> getAll(){
        return ResponseEntity
                .ok(ResponseData.builder()
                        .data(csvToJSONService.convertCSVTOJSON())
                        .message("Success")
                        .build());
    }
    @GetMapping("search/{name}")
    public ResponseEntity<ResponseData<Object>> search(@PathVariable String name){
        List<StockSymbol> matchedRecord = csvToJSONService.convertCSVTOJSON().stream()
                .filter( symbol -> symbol.getSymbol().contains(name) || symbol.getCompany_name().contains(name))
                .toList();
        return ResponseEntity
                .ok(ResponseData.builder()
                                .data(matchedRecord)
                                .message("Success")
                                .build());
    }
}
