package vip.appcity.hadoopclassic.elasticsearch;

import org.apache.http.HttpHost;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;

import java.io.IOException;

/**
 * @author:juforg
 */
public class EsOpeartions {
    public static void main(String[] args) {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("sj-node1", 9200, "http"),
                        new HttpHost("sj-node2", 9200, "http"),
                        new HttpHost("sj-node3", 9200, "http")));
        //创建索引
        CreateIndexRequest request = new CreateIndexRequest("sjappcity2");
        //创建的每个索引都可以有与之关联的特定设置。
        request.settings(Settings.builder()
                .put("index.number_of_shards", 3)
                .put("index.number_of_replicas", 2)
        );

        CreateIndexResponse createIndexResponse = null;
        try {
            createIndexResponse = client.indices().create(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ActionListener<CreateIndexResponse> listener = new ActionListener<CreateIndexResponse>() {
            @Override
            public void onResponse(CreateIndexResponse createIndexResponse) {
                //如果执行成功，则调用onResponse方法;
            }
            @Override
            public void onFailure(Exception e) {
                //如果失败，则调用onFailure方法。
            }
        };
        client.indices().createAsync(request, listener);
        //要执行的CreateIndexRequest和执行完成时要使用的ActionListener

        //返回的CreateIndexResponse允许检索有关执行的操作的信息，如下所示：
        boolean acknowledged = createIndexResponse.isAcknowledged();
        //指示是否所有节点都已确认请求
        boolean shardsAcknowledged = createIndexResponse.isShardsAcknowledged();
    }
}
