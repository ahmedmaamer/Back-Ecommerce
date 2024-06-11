package com.example.iotDataGenerator.dto;

import com.example.iotDataGenerator.enums.SortOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageLink {
    private  int page;
    private  int pageSize;
    private  String sortProperty;
    private SortOrder sortOrder;
    private String timeZone;
    private Map<String, List<FilterModel>> filters;
    private GlobalFilter globalFilter;
}
