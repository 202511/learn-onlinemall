package com.own.onlinemall.search.service;

import com.own.onlinemall.search.es.SkuEsModel;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public interface ProductSaveService {
    boolean productStatusUp(List<SkuEsModel> skuEsModelList) throws IOException;
}
