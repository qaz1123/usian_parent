package com.usian.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("usian-search-service")
public interface SearchItemServiceFeign {

    @RequestMapping("service/search/importAll")
    Boolean importAll();
}
