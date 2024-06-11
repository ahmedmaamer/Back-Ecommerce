package com.example.iotDataGenerator.dto;



import com.example.iotDataGenerator.enums.MatchMode;
import com.example.iotDataGenerator.enums.Operator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterModel {

    private MatchMode matchMode;
    private String value;
    private Operator operator;
}
