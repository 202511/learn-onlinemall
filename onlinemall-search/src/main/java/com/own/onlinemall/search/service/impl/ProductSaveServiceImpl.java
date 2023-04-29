package com.own.onlinemall.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.own.onlinemall.search.config.ElasticSearchConfig;
import com.own.onlinemall.search.constant.EsConstant;
import com.own.onlinemall.search.es.SkuEsModel;
import com.own.onlinemall.search.service.ProductSaveService;
import lombok.AllArgsConstructor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
@Service
public class ProductSaveServiceImpl implements ProductSaveService {
    @Autowired
    RestHighLevelClient restHighLevelClient;
    @Override
    public boolean productStatusUp(List<SkuEsModel> skuEsModelList) throws IOException {
        //保存到es
        //给es中建立索引, 建立好映射关系
        //给es中保存数据
        BulkRequest bulkRequest = new BulkRequest();
        for (SkuEsModel skuEsModel : skuEsModelList) {
            IndexRequest indexRequest = new IndexRequest(EsConstant.PRODUCT_INDEX);
            indexRequest.id(skuEsModel.getSkuId().toString());
            String s = JSON.toJSONString(skuEsModel);
            //指定内容类型
            indexRequest.source(s, XContentType.JSON);

            //加入群体
            bulkRequest.add(indexRequest);
        }

        BulkResponse bulk=restHighLevelClient.bulk(bulkRequest, ElasticSearchConfig.COMMON_OPTIONS);
        //处理上架失败的商品
        boolean b = bulk.hasFailures();
        System.out.println(b==false ? "上架成功": "上架失败");
        return b;
    }
}
