package com.li.blog.controller;

import com.alibaba.druid.stat.DruidStatManagerFacade;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/** druid监控
 * @author li
 * @version 1.0
 * @date 18-8-10 下午5:13
 **/
@RestController
public class DruidStatController {
    @GetMapping("/druid/stat")
    public Object druidStat() {
        return DruidStatManagerFacade.getInstance().getDataSourceStatDataList();
    }
}
