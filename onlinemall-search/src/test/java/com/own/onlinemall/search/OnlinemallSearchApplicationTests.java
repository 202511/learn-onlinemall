package com.own.onlinemall.search;

import com.alibaba.fastjson.JSON;
import com.own.onlinemall.search.config.ElasticSearchConfig;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.AvgAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
class OnlinemallSearchApplicationTests {
    @Autowired
    private RestHighLevelClient client;

    // 创建检索请求
    @Test
    public void searchData() throws IOException {
         SearchRequest searchRequest=new SearchRequest();
         //指定索引 -- 数据库
        searchRequest.indices("bank");
        //指定DSL 检索条件  封装条件
        SearchSourceBuilder sourceBuilder=new SearchSourceBuilder();

        sourceBuilder.query(QueryBuilders.matchQuery("address", "mill"));

         //按照年龄的分布进行聚合
        TermsAggregationBuilder aggregationBuilder= AggregationBuilders.terms("ageAgg").field("age").size(10);
        sourceBuilder.aggregation(aggregationBuilder);
         //计算平均薪资
        AvgAggregationBuilder balanceAvg=AggregationBuilders.avg("balanceAvg").field("balance");
        sourceBuilder.aggregation(balanceAvg);

        searchRequest.source(sourceBuilder);
//        sourceBuilder.query();
//        sourceBuilder.from();
//        sourceBuilder.size();

        SearchResponse searchResponse=client.search(searchRequest, ElasticSearchConfig.COMMON_OPTIONS);
          // 分析返回结果
//        System.out.println(searchResponse);
        //获取所有查到的记录
        SearchHits searchHits=searchResponse.getHits();
        SearchHit[] hits= searchHits.getHits();

        for (SearchHit hit : hits) {
            //拿到具体对象数据
            String t= hit.getSourceAsString();
            System.out.println(t);
            // 然后string转对象
            //Account account= JSON.parseObject(string,Account.class);
        }
    }






    @Test
    public void contextLoads() throws IOException {
        IndexRequest indexRequest=new IndexRequest("users");
        indexRequest.id("1");
//        indexRequest.source("username", "zhangsan");
        User user= new User();
        user.setAge(11);
        user.setGender("男");
        user.setName("真会");
        String json= JSON.toJSONString(user);
        indexRequest.source(json, XContentType.JSON);
        //执行操作
        IndexResponse index= client.index(indexRequest, ElasticSearchConfig.COMMON_OPTIONS);
        //提取有用的响应数据
        System.out.println( index );
    }
    class  User{
        private  String name;
        private  int age;
        private  String gender;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }
    }

}
