package edu.epam.izhevsk.junit;


import java.math.BigDecimal;

public interface DepositService {
    String deposit(Long amount, Long userId) throws InsufficientFundsException;
}
