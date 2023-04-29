package com.own.onlinemall.search.controller;


import com.own.onlinemall.common.utils.Result;
import com.own.onlinemall.search.es.SkuEsModel;
import com.own.onlinemall.search.service.ProductSaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;


@RequestMapping("/search/save")
@RestController
public class ElasticSaveController {
       @Autowired
       ProductSaveService productSaveService;
       @PostMapping("/product")
      public Result productStatusUp(@RequestBody List<SkuEsModel> skuEsModelList) throws IOException {
          boolean b=false;
          Result<Object> objectResult = new Result<>();
          try {
             b = productSaveService.productStatusUp(skuEsModelList);
         }
         catch (Exception e )
         {
             System.out.println("出现错误");
         }
         return b==true  ? objectResult.error("失败") : objectResult.ok("成功");
      }

}
