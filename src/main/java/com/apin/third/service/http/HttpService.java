package com.apin.third.service.http;

import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/16.
 */
@Service
public class HttpService {

    private static final Logger logger= LoggerFactory.getLogger(HttpService.class);

    private static final String userAgent="Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_0) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.56 Safari/535.11";

    private static final int connectionTimeout=2000;

    private static final int socketTimeout=5000;

    private static final int maxConnectionNumber=2;

    private static final int connectionNumberPerRoute=2;

    private CloseableHttpClient httpClient;


    public void init(){

        PoolingHttpClientConnectionManager poolingHttpClientConnectionManager=new PoolingHttpClientConnectionManager();
        poolingHttpClientConnectionManager.setDefaultMaxPerRoute(connectionNumberPerRoute);
        poolingHttpClientConnectionManager.setMaxTotal(maxConnectionNumber);

        ConnectionReuseStrategy connectionReuseStrategy=new ConnectionReuseStrategy() {
            public boolean keepAlive(HttpResponse response, HttpContext context) {
                return true;
            }
        };

        ConnectionKeepAliveStrategy connectionKeepAliveStrategy=new ConnectionKeepAliveStrategy() {
            public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
                return 2*3600*1000;
            }
        };

        RequestConfig requestConfig=RequestConfig.custom()
                .setConnectTimeout(connectionTimeout)
                .setSocketTimeout(socketTimeout)
                .build();


        httpClient= HttpClientBuilder.create()
                .setUserAgent(userAgent)
                .setConnectionReuseStrategy(connectionReuseStrategy)
                .setKeepAliveStrategy(connectionKeepAliveStrategy)
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(poolingHttpClientConnectionManager)
                .build();
    }

    /**
     *
     * @param uri
     * @param params
     * @return
     * @throws Throwable
     */
    public String formUriWithQuery(String uri,Map<String,String> params) throws Throwable{
        //URI:[scheme:][//host:port][path][?query][#fragment]
        StringBuffer sb=new StringBuffer();
        int i=0;
        for(String key:params.keySet()){
            if(i!=0){
                sb.append("&");
            }
            sb.append(key).append("=").append(params.get(key));
            i++;
        }
        String uriWithQuery=uri+"?"+sb.toString();
        return uriWithQuery;
    }

    /**
     *
     * @param url
     * @param headers
     * @return
     */
    public HttpGet formHttpGet(String url,Map<String,String> headers){
        HttpGet httpGet=new HttpGet(url);
        for(String key:headers.keySet()){
            httpGet.setHeader(key,headers.get(key));
        }
        return httpGet;
    }

    /**
     *
     * @param url
     * @param headers
     * @param body
     * @param contentType
     * @return
     */
    public HttpPost formHttpPostStringEntity(String url,Map<String,String> headers,String body,ContentType contentType){
        HttpPost httpPost=new HttpPost(url);
        HttpEntity httpEntity= new StringEntity(body,contentType);
        for(String key:headers.keySet()){
            httpPost.setHeader(key,headers.get(key));
        }
        httpPost.setEntity(httpEntity);
        return httpPost;
    }

    /**
     *
     * @param url
     * @param headers
     * @param body
     * @param contentType
     * @return
     */
    public HttpPost formHttpPostByteArrayEntity(String url,Map<String,String> headers,byte [] body,ContentType contentType){
        HttpPost httpPost=new HttpPost(url);
        HttpEntity httpEntity=new ByteArrayEntity(body,contentType);
        for(String key:headers.keySet()){
            httpPost.setHeader(key,headers.get(key));
        }
        httpPost.setEntity(httpEntity);
        return httpPost;
    }


    /**
     *
     * @param url
     * @param body
     * @return
     * @throws Throwable
     */
    public HttpPost formHttpPostWithNameValuePair(String url,Map<String,String> headers,Map<String,String> body) throws Throwable{
        HttpPost httpPost =new HttpPost(url);
        for(String key:headers.keySet()){
            httpPost.setHeader(key,headers.get(key));
        }

        List<NameValuePair> nvps=new ArrayList<>();
        for(String key:body.keySet()){
            BasicNameValuePair basicNameValuePair=new BasicNameValuePair(key,body.get(key));
            nvps.add(basicNameValuePair);
        }

        UrlEncodedFormEntity urlEncodedFormEntity=new UrlEncodedFormEntity(nvps,"utf-8");
        httpPost.setEntity(urlEncodedFormEntity);
        return httpPost;
    }

    /**
     *
     * @param url
     * @param headers
     * @return
     * @throws Throwable
     */
    public HttpDelete formHttpDelete(String url,Map<String,String> headers) throws Throwable{
        HttpDelete httpDelete=new HttpDelete(url);
        for(String key:headers.keySet()){
            httpDelete.setHeader(key,headers.get(key));
        }
        return httpDelete;
    }

    /**
     *
     * @param httpRequestBase
     * @return
     * @throws Throwable
     */
    public String response(HttpRequestBase httpRequestBase) throws Throwable{
        CloseableHttpResponse closeableHttpResponse=null;
        try{
            closeableHttpResponse=httpClient.execute(httpRequestBase);
            StatusLine statusLine=closeableHttpResponse.getStatusLine();
            if(statusLine.getStatusCode()!=200){
                throw new RuntimeException("response with errorCode:"+statusLine.getStatusCode());
            }
            HttpEntity httpEntity=closeableHttpResponse.getEntity();
            String response= EntityUtils.toString(httpEntity, "utf-8");
            logger.info("response:{}",response);
            return response;
        }catch (Throwable e){
            throw e;
        }finally{
            if(null!=closeableHttpResponse){
                closeableHttpResponse.close();
            }
        }
    }

    /**
     *
     */
    @PreDestroy
    public void close(){
        if(httpClient!=null){
            try {
                httpClient.close();
            }catch(Exception e){
                logger.error(e.getMessage());
            }
        }
    }


}
