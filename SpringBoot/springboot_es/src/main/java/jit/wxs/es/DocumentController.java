package jit.wxs.es;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * @author jitwxs
 * @since 2018/10/9 14:23
 */
@RestController
public class DocumentController {
    @Resource
    private TransportClient transportClient;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 插入/更新数据（指定ID）
     * @author jitwxs
     * @since 2018/10/9 15:09
     */
    @PutMapping("/{index}/{type}/{id}")
    public ResultBean addDocument(@PathVariable String index, @PathVariable String type, @PathVariable String id
            , @RequestBody Map map) {
        logger.info("接收到数据的参数。索引名：{}，类别：{}，id：{}，参数：{}",
                index, type, id, map);

        IndexResponse indexResponse = transportClient.prepareIndex(index, type, id)
                .setSource(map)
                .get();

        int status = indexResponse.status().getStatus();

        if(status == 201) {
            return ResultBean.success("添加数据成功！");
        } else if (status == 200) {
            return ResultBean.success("更新数据成功！");
        } else {
            return ResultBean.error("插入/更新数据失败！");
        }
    }

    /**
     * 查看数据
     * @author jitwxs
     * @since 2018/10/9 15:21
     */
    @GetMapping("/{index}/{type}/{id}")
    public ResultBean getDocument(@PathVariable String index, @PathVariable String type, @PathVariable String id) {
        GetResponse response = transportClient.prepareGet(index, type, id).get();

        Map<String, Object> source = response.getSource();

        return ResultBean.success("查询成功！", source);
    }

    /**
     * 删除数据
     * @author jitwxs
     * @since 2018/10/9 15:18
     */
    @DeleteMapping("/{index}/{type}/{id}")
    public ResultBean deleteDocument(@PathVariable String index, @PathVariable String type, @PathVariable String id) {
        DeleteResponse response = transportClient.prepareDelete(index, type, id).get();

        return response.status().getStatus() == 200 ?
                ResultBean.success("删除数据成功！") : ResultBean.error("删除数据失败！");
    }

    @GetMapping("/{index}/{type}/search")
    public ResultBean search(@PathVariable String index, @PathVariable String type, String name) {
        Map<String, Object> map = new HashMap<>();
        try {
            SearchResponse response = transportClient.prepareSearch(index)
                    .setTypes(type)
                    .setQuery(QueryBuilders.boolQuery()
                            .must(QueryBuilders.matchQuery( "name", name))
                            .must(QueryBuilders.rangeQuery("price").gt(50).lte(200))
                    )
                    .highlighter(new HighlightBuilder()
                            .field("name")
                            .preTags("<span style='color:red;'>")
                            .postTags("</span>")
                    )
                    .addSort("price", SortOrder.DESC)
                    .setFrom(0)
                    .setSize(10)
                    .execute()
                    .get();

            SearchHits hits = response.getHits();
            map.put("total", hits.getTotalHits());

            List<String> result = new ArrayList<>();
            List<String> highLight = new ArrayList<>();
            for(SearchHit hit:hits){
                result.add(hit.getSourceAsString());
                highLight.add(hit.getHighlightFields().toString());
            }

            map.put("result", result);
            map.put("highLight", highLight);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return ResultBean.success("搜索完成！", map);
    }
}
