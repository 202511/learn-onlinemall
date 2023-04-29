package com.own.onlinemall.search.service;

import com.own.onlinemall.search.vo.SearchParam;

import  com.own.onlinemall.search.vo.SearchResult;
import org.springframework.stereotype.Service;

@Service
public interface MallSearchService {
    //根据检索参数 得到最终检索结果
    SearchResult search(SearchParam param);
}
