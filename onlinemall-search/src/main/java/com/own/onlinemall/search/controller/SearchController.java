package com.own.onlinemall.search.controller;

import com.own.onlinemall.search.service.MallSearchService;
import com.own.onlinemall.search.vo.SearchParam;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import  com.own.onlinemall.search.vo.SearchResult;

import javax.servlet.http.HttpServletRequest;


@Controller
public class SearchController {
    @Autowired
    MallSearchService mallSearchService;

    @GetMapping("/list.html")
    public String listPage(SearchParam param, Model model, HttpServletRequest request)
    {
        param.set_queryString(request.getQueryString());
        //根据传递过来的参数， 去es中检索商品
        SearchResult result=mallSearchService.search(param);
        model.addAttribute("result", result);
        return "list";
    }
}
