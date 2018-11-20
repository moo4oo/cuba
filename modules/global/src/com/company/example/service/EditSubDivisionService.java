package com.company.example.service;


import com.company.example.entity.SubDivision;

import java.util.List;

public interface EditSubDivisionService {
    String NAME = "example_EditSubDivisionService";
    public List<SubDivision> getSubDivisions();
}