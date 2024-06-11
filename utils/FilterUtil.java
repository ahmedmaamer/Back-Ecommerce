package com.example.iotDataGenerator.utils;


import com.example.iotDataGenerator.dto.FilterModel;
import com.example.iotDataGenerator.dto.GlobalFilter;
import com.example.iotDataGenerator.enums.MatchMode;
import com.example.iotDataGenerator.enums.Operator;
import com.example.iotDataGenerator.exceptions.DataValueException;
import javax.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public class FilterUtil {
    private static final Pattern ISO_DATE_PATTERN = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");

    public static <T> Specification<T> filter(Map<String, List<FilterModel>> filters) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.and(filters.entrySet().stream().map(entry -> createPredicateForEntry(entry.getKey(), entry.getValue(), criteriaBuilder, root)).toArray(Predicate[]::new));
    }

    public static <T> Specification<T> filter(Map<String, List<FilterModel>> filters, GlobalFilter globalFilter) {
        return (root, query, criteriaBuilder) -> filter(filters).and(createGlobalFilterSpecification(globalFilter)).toPredicate((Root<Object>) root, query, criteriaBuilder);
    }

    public static <T> Specification<T> createGlobalFilterSpecification(GlobalFilter globalFilter) {
        List<FilterModel> filterModels = List.of(new FilterModel(MatchMode.CONTAINS, globalFilter.getValue(), Operator.OR));

        return (root, query, criteriaBuilder) -> criteriaBuilder.or(globalFilter.getKeys().stream().map(key -> createPredicateForEntry(key, filterModels, criteriaBuilder, root)).toArray(Predicate[]::new));
    }

    private static Predicate createPredicateForEntry(String key, List<FilterModel> filterModels, CriteriaBuilder criteriaBuilder, Root root) {
        Predicate[] predicates = filterModels.stream().map(filterModel -> createPredicateFromProperty(key, filterModel, criteriaBuilder, root)).toArray(Predicate[]::new);

        return filterModels.get(0).getOperator() == Operator.AND ? criteriaBuilder.and(predicates) : criteriaBuilder.or(predicates);
    }

    public static Predicate createPredicateFromProperty(String property, FilterModel filterModel, CriteriaBuilder criteriaBuilder, Root root) {

        Path propertyTextPath = definePropertyPath(root, property);
        return applyFilter(propertyTextPath, filterModel.getValue(), filterModel.getMatchMode(), criteriaBuilder);
    }

    private static <T> Path<Object> definePropertyPath(Root<T> root, String property) {
        String[] properties = property.split("\\.");
        Path<Object> propertyPath = null;
        propertyPath = getPropertyPath(root, properties, propertyPath);
        return propertyPath;
    }

    private static <T> Path<Object> getPropertyPath(Root<T> root, String[] properties, Path<Object> propertyPath) {
        for (String prop : properties) {
            if (Objects.isNull(propertyPath)) {
                propertyPath = root.get(prop);
            } else {
                propertyPath = propertyPath.get(prop);
            }
        }
        return propertyPath;
    }

    private static <T> Predicate applyFilter(Path<T> propertyPath, String filterValue, MatchMode textFilterMatchMode, CriteriaBuilder criteriaBuilder) {
        if (filterValue == null || filterValue.isBlank()) {
            return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
        }else if (isDate(propertyPath.getJavaType())) {

            return handleDateFilter(propertyPath, filterValue, textFilterMatchMode, criteriaBuilder);
        }else if(isNumericPath(propertyPath.getJavaType())) {
            return handleNumericFilter(propertyPath, filterValue, textFilterMatchMode, criteriaBuilder);
        }else {
            return handleTextFilter(propertyPath, filterValue, textFilterMatchMode, criteriaBuilder);
        }
    }

    private static <T> Expression<String> buildPropertyExpression(Path propertyPath, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.lower(propertyPath);
    }

    private static <T> Predicate handleNumericFilter(Path property, String filterValue, MatchMode textFilterMatchMode, CriteriaBuilder criteriaBuilder) {
        switch (textFilterMatchMode) {
            case CONTAINS:
                return criteriaBuilder.like(criteriaBuilder.toString(property), "%" + filterValue.toLowerCase() + "%");
            case EQUALS:
                return criteriaBuilder.equal(property, filterValue.toLowerCase());
            case NOT_EQUAL:
                return criteriaBuilder.notEqual(property, filterValue);
            case LESS_THAN:
                return criteriaBuilder.lessThan(property, filterValue);
            case LESS_THAN_OR_EQUAL:
                return criteriaBuilder.lessThanOrEqualTo(property, filterValue);
            case GREATER_THAN:
                return criteriaBuilder.greaterThan(property, filterValue);
            case GREATER_THAN_OR_EQUAL:
                return criteriaBuilder.greaterThanOrEqualTo(property, filterValue);
            default:
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
        }
    }


    private static <T> Predicate handleTextFilter(Path propertyPath, String filterValue, MatchMode textFilterMatchMode, CriteriaBuilder criteriaBuilder) {
        Expression<String> property = buildPropertyExpression(propertyPath, criteriaBuilder);
        switch (textFilterMatchMode) {
            case CONTAINS:
                return criteriaBuilder.like(property, "%" + filterValue.toLowerCase() + "%");
            case NOT_CONTAINS:
                return criteriaBuilder.notLike(property, "%" + filterValue.toLowerCase() + "%");
            case EQUALS:
                return criteriaBuilder.equal(property, filterValue.toLowerCase());
            case NOT_EQUAL:
                return criteriaBuilder.notEqual(property, filterValue.toLowerCase());
            case STARTS_WITH:
                return criteriaBuilder.like(property, filterValue.toLowerCase() + "%");
            case ENDS_WITH:
                return criteriaBuilder.like(property, "%" + filterValue.toLowerCase());
            default:
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
        }
    }

    private static <T> Predicate handleDateFilter(Path propertyPath, String filterValue, MatchMode textFilterMatchMode, CriteriaBuilder criteriaBuilder) {
        LocalDate filterDate = convertDateFormat(textFilterMatchMode, filterValue);
        switch (textFilterMatchMode) {
            case DATE_IS:
                return criteriaBuilder.equal(propertyPath, filterDate);
            case DATE_IS_NOT:
                return criteriaBuilder.notEqual(propertyPath, filterDate);
            case DATE_AFTER:
                return criteriaBuilder.greaterThan(propertyPath, filterDate);
            case DATE_BEFORE:
                return criteriaBuilder.lessThan(propertyPath, filterDate);

            default:
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
        }
    }


    private static boolean isDateMatchMode(MatchMode matchMode) {
        return matchMode == MatchMode.DATE_AFTER || matchMode == MatchMode.DATE_BEFORE || matchMode == MatchMode.DATE_IS || matchMode == MatchMode.DATE_IS_NOT;
    }

    private static LocalDate convertDateFormat(MatchMode textFilterMatchMode, String filterValue) {
        if (isDateMatchMode(textFilterMatchMode) && isValidIsoDateFormat(filterValue)) {
            return LocalDate.parse(filterValue, DateTimeFormatter.ISO_DATE);
        } else if (isDateMatchMode(textFilterMatchMode) && !isValidIsoDateFormat(filterValue)) {
            throw new DataValueException("INVALID DATE FORMAT");
        } else {
            return null;
        }
    }

    private static boolean isValidIsoDateFormat(String value) {
        return ISO_DATE_PATTERN.matcher(value).matches();
    }

    public static boolean isNumber(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }


    private static boolean isNumericPath(Class<?> propertyType) {
        List<Class<?>> numericTypes = Arrays.asList(int.class, Integer.class, long.class, Long.class, double.class, Double.class, float.class, Float.class);
        return numericTypes.stream().anyMatch(type -> type.isAssignableFrom(propertyType));
    }

    private static boolean isDate(Class<?> propertyType) {
        return LocalDate.class.isAssignableFrom(propertyType);
    }


}
