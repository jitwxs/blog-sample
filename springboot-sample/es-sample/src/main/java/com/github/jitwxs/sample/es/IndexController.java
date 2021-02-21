package com.github.jitwxs.sample.es;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author jitwxs
 * @since 2018/10/9 10:41
 */
@RestController
public class IndexController {
    private final String INDEX = "index";
    private final String TYPE = "type";
    private final String PROPERTIES = "properties";
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private TransportClient transportClient;

    /**
     * 创建索引
     * map包含：index、type、properties。其中properties为map，key：属性名；value：类型
     * @author jitwxs
     * @since 2018/10/9 15:07
     */
    @PostMapping("/index/create")
    public ResultBean createIndex(@RequestBody Map param) {
        logger.info("接收的创建索引的参数：" + param);

        // 1. 校验参数
        String index = (String)param.get(INDEX);
        String type = (String)param.get(TYPE);
        Map<String, String> properties = (Map<String, String>) param.get(PROPERTIES);

        if(StringUtils.isBlank(index) || StringUtils.isBlank(type) || properties == null || properties.size() == 0){
            return ResultBean.error("参数错误！");
        }

        try {
            /* 2. 设置索引setting部分
             "settings":{
                "number_of_shards": "6",
                "number_of_replicas": "1",
                "analysis":{
                  "analyzer":{
                    "ik":{
                      "tokenizer":"ik_max_word"
                    }
                  }
                }
              }*/
            XContentBuilder settings = XContentFactory.jsonBuilder()
                    .startObject()
                    .field("number_of_shards",6)
                    .field("number_of_replicas",1)
                    .startObject("analysis").startObject("analyzer").startObject("ik")
                    .field("tokenizer","ik_max_word")
                    .endObject().endObject().endObject()
                    .endObject();

            /* 3. 设置索引mapping部分
            "mappings":{
                "dynamic":"strict",
                "properties":{
                    "name":{
                      "type":"text"
                    },
                    "author":{
                      "type":"text"
                    },
                    "price":{
                      "type":"integer"
                    },
                    "publishDate":{
                      "type":"date",
                      "format":"yyyy-MM-dd HH:mm:ss || yyyy-MM-dd"
                    }
                }
            }*/
            XContentBuilder mapping = XContentFactory.jsonBuilder();
            mapping.startObject().field("dynamic","strict").startObject("properties");

            for(Map.Entry<String, String> entry : properties.entrySet()) {
                String field = entry.getKey();
                String fieldType = entry.getValue();
                mapping.startObject(field).field("type",fieldType);
                // 对于日期属性增加format
                if("date".equals(fieldType.trim())){
                    mapping.field("format","yyyy-MM-dd HH:mm:ss || yyyy-MM-dd ");
                }
                mapping.endObject();
            }
            mapping.endObject().endObject();

            // 4. 创建索引
            CreateIndexRequest createIndexRequest = Requests.createIndexRequest(index).settings(settings).mapping(type,mapping);
            CreateIndexResponse response = transportClient.admin().indices().create(createIndexRequest).get();

            logger.info("建立索引映射成功：" + response.isAcknowledged());
            return ResultBean.success("创建索引成功！");
        } catch (Exception e) {
            logger.error("创建索引失败！要创建的索引为{}，文档类型为{}，异常为：",index,type,e.getMessage(),e);
            return ResultBean.error("创建索引失败！");
        }
    }

    /**
     * 删除索引
     * @param index 索引名
     * @author jitwxs
     * @since 2018/10/9 15:08
     */
    @DeleteMapping("/index/delete/{index}")
    public ResultBean deleteIndex(@PathVariable String index) {
        if (StringUtils.isBlank(index)) {
            return ResultBean.error("参数错误，索引为空！");
        }
        try {
            // 删除索引
            DeleteIndexRequest deleteIndexRequest = Requests.deleteIndexRequest(index);
            DeleteIndexResponse response = transportClient.admin().indices().delete(deleteIndexRequest).get();

            logger.info("删除索引结果:{}",response.isAcknowledged());
            return ResultBean.success("删除索引成功！");
        } catch (Exception e) {
            logger.error("删除索引失败！要删除的索引为{}，异常为：",index,e.getMessage(),e);
            return ResultBean.error("删除索引失败！");
        }
    }

    /**
     * 判断索引是否存在
     * @param index 索引名
     * @author jitwxs
     * @since 2018/10/9 15:08
     */
    @PostMapping("/index/hasExist")
    public ResultBean hasExist(String index) {
        IndicesExistsRequest inExistsRequest = new IndicesExistsRequest(index);
        IndicesExistsResponse inExistsResponse = transportClient.admin().indices().exists(inExistsRequest).actionGet();

        return inExistsResponse.isExists() ? ResultBean.success("索引存在！") : ResultBean.error("索引不存在！");
    }
}
