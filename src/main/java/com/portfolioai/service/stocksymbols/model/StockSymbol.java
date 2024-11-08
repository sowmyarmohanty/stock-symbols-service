package com.portfolioai.service.stocksymbols.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockSymbol {
    private String symbol;
    private String company_name;
    private String series;
    private String date_of_listing;
    private long paid_up_value;
    private long market_lot;
    private String isin;
    private long face_value;
}
