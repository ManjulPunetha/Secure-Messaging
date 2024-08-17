package com.example.cryptosmsfinal.key_exchange.model;

import java.math.BigInteger;

public class Diffie
{
    private String userId;
    private BigInteger generator;
    private BigInteger primeNum;

    public BigInteger getGenerator()
    {
        return generator;
    }

    public BigInteger getPrimeNum()
    {
        return primeNum;
    }
}
