package com.example.iotDataGenerator.utils;

import com.example.iotDataGenerator.dto.PageData;
import com.example.iotDataGenerator.dto.PageLink;
import com.example.iotDataGenerator.enums.SortOrder;
import com.example.iotDataGenerator.mapper.BaseMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Objects;

public class PaginationUtil {


  public static <T> PageData<T> paginate(Page page, BaseMapper mapper) {
    List<T> data = mapper.convertEntitiesToDtos(page.getContent());
    return createPageData(page, data);
  }

  public static <T> PageData<T> paginate(Page page) {
    return createPageData(page, page.getContent());
  }

  private static <T> PageData<T> createPageData(Page<T> page, List<T> data) {
    return new PageData<>(
        data, page.getTotalPages(), Math.toIntExact(page.getTotalElements()), page.hasNext());
  }

    public static Pageable toPageable(PageLink pageLink) {
        Sort sort = Sort.by(Sort.Order.desc("createdAt"));
        if (Objects.nonNull(pageLink.getSortProperty())) {
            sort = Sort.by(pageLink.getSortProperty());
            sort = pageLink.getSortOrder() == SortOrder.DESC ? sort.descending() : sort.ascending();
        }
        return PageRequest.of(pageLink.getPage(), pageLink.getPageSize(), sort);
    }
}
