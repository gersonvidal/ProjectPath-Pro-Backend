package com.gerson.projectpath_pro.mappers.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.gerson.projectpath_pro.calculation.repository.Calculation;
import com.gerson.projectpath_pro.calculation.repository.dto.CalculationDto;
import com.gerson.projectpath_pro.mappers.Mapper;

@Component
public class CalculationMapperImpl implements Mapper<Calculation, CalculationDto> {

    private ModelMapper modelMapper;

    public CalculationMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public CalculationDto mapTo(Calculation calculation) {
        return modelMapper.map(calculation, CalculationDto.class);
    }

    @Override
    public Calculation mapFrom(CalculationDto calculationDto) {
        return modelMapper.map(calculationDto, Calculation.class);
    }

}
