package com.apin.third.service.instant.messenger;

import com.apin.third.service.http.HttpService;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/24.
 */
public class ChatRoomService {

    private final static Logger logger = LoggerFactory.getLogger(ChatRoomService.class);

    private HttpService httpService;

    private static String token="";

    private static long expiredTime;

    private static final String clientId="YXA6IGcH0D9DEeWZlmERtoJT_Q";

    private static final String clientSecret="YXA6OqI2r_90NZWyP-7ZOn-Ne9cNUjM";

    private final static String baseUrl="https://a1.easemob.com/apinclub/apin";

    private final static String tokenGetUrl=baseUrl+"/token";

    private final static String createUrl=baseUrl+"/chatrooms";

    private void getToken() throws Throwable{
        JSONObject body=new JSONObject();
        body.put("grant_type","client_credentials");
        body.put("client_id",clientId);
        body.put("client_secret",clientSecret);
        String httpBody=body.toString();

        Map<String,String> headers=new HashMap<>();
        headers.put("Content-Type","application/json");

        HttpPost httpPost=httpService.formHttpPostStringEntity(tokenGetUrl, headers, httpBody, ContentType.APPLICATION_JSON);
        String response=httpService.response(httpPost);
        JSONObject responseJson=new JSONObject(response);
        token=responseJson.getString("access_token");
        expiredTime=System.currentTimeMillis()+responseJson.getLong("expires_in")*1000;
    }

    private synchronized Map<String,String> getAuthenticationHeaders() throws Throwable{
        if(token==""||expiredTime<System.currentTimeMillis()){
            getToken();
        }
        String authorization="Bearer "+token;
        Map<String,String> headers=new HashMap<>();
        headers.put("Authorization",authorization);
        return headers;
    }


    /**
     * 创建房间
     * @param name
     * @param description
     * @param maxusers
     * @param owner
     * @param members
     * @return
     * @throws Throwable
     */
    public String create(String name,String description,Integer maxusers,String owner,String... members) throws Throwable{
        JSONObject body=new JSONObject();
        body.put("name",name);
        body.put("description",description);
        body.put("maxusers",maxusers);
        body.put("owner",owner);
        JSONArray memberArray=new JSONArray();
        for(String member:members){
            memberArray.put(member);
        }
        body.put("members",memberArray);
        String httpBody=body.toString();

        Map<String,String> headers=getAuthenticationHeaders();
        headers.put("Accept","application/json");
        headers.put("Content-Type","application/json;charset=UTF-8");

        HttpPost httpPost=httpService.formHttpPostStringEntity(createUrl,headers,httpBody, ContentType.APPLICATION_JSON);
        String response=httpService.response(httpPost);
        JSONObject responseJson=new JSONObject(response);
        JSONObject data=responseJson.getJSONObject("data");
        return data.getString("id");
    }

    /**
     * 删除房间
     * @param roomId
     * @return
     * @throws Throwable
     */
    public boolean delete(String roomId) throws Throwable{
        Map<String,String> headers=getAuthenticationHeaders();
        headers.put("Accept","application/json");
        String deleteUrl=createUrl+"/"+roomId;
        HttpDelete httpDelete=httpService.formHttpDelete(deleteUrl,headers);
        String response=httpService.response(httpDelete);
        JSONObject responseJson=new JSONObject(response);
        JSONObject data=responseJson.getJSONObject("data");
        return data.getBoolean("success");
    }

    public void setHttpService(HttpService httpService) {
        this.httpService = httpService;
    }
}
