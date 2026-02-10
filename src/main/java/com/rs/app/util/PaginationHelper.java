package com.rs.app.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class PaginationHelper {
    public Pageable buildPageable(int page, String sortBy, String direction, Set<String> allowedSort, int pageSize){
        if (!allowedSort.contains(sortBy)) {
            throw new IllegalArgumentException("Illegal sortBy argument!");
        }
        int pSize = Math.min(pageSize, 20);
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        return PageRequest.of(page, pSize, Sort.by(sortDirection, sortBy));
    }

    public Pageable buildPageable(int page, String sortBy, String direction, Set<String> allowedSort){
        int pageSize = 20;
        return buildPageable(page, sortBy, direction, allowedSort, pageSize);
    }
}
