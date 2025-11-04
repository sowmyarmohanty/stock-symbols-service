package com.portfolioai.service.stocksymbols.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table("stock_symbol")
public class StockSymbol {
    @Id
    private Long id;
    
    @Column("symbol")
    private String symbol;
    
    @Column("company_name")
    private String company_name;
    
    @Column("series")
    private String series;
    
    @Column("date_of_listing")
    private String date_of_listing;
    
    @Column("paid_up_value")
    private long paid_up_value;
    
    @Column("market_lot")
    private long market_lot;
    
    @Column("isin")
    private String isin;
    
    @Column("face_value")
    private long face_value;
    
    @Column("created_at")
    private LocalDateTime created_at;
    
    @Column("updated_at")
    private LocalDateTime updated_at;
}
