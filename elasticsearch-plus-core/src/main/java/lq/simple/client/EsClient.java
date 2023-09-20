package lq.simple.client;

import lq.simple.util.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;


public class EsClient{

    private String ip;
    private Integer port;
    private String root;
    private String password;

    public EsClient(String ip, Integer port, String root, String password) {
        this.ip = ip;
        this.port = port;
        this.root = root;
        this.password = password;
    }

    public RestHighLevelClient getClient() {
        RestClientBuilder builder = getRestClientBuilder(ip, port);
        if (StringUtils.isNotEmpty(root) && StringUtils.isNotEmpty(password)) {
            final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(root, password));
            builder.setHttpClientConfigCallback(httpClientBuilder -> {
                httpClientBuilder.disableAuthCaching();
                return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            });
        }
        return client(builder);
    }

    private RestClientBuilder getRestClientBuilder(String ip, Integer port) {
        return RestClient.builder(new HttpHost(ip, port));
    }

    private RestHighLevelClient client(RestClientBuilder builder) {
        return new RestHighLevelClient(builder);
    }

}
