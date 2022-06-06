package org.norma.banking.external;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Map;


@RequiredArgsConstructor
@Getter
@Setter
public class ExchangeRates {

    private String base;
    private Map<String, Double> rates;
}
