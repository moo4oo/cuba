package com.company.example.service;


public interface UniqueNumbersHelperService {
    String NAME = "example_UniqueNumbersHelperService";
    public long getNextUniqueNumber(String domain);
    public void setNextUniqueNumber(String domain, long number);
}