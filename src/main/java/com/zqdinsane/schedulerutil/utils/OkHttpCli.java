package com.zqdinsane.schedulerutil.utils;

import com.alibaba.fastjson.JSONObject;
import okhttp3.*;

import java.io.File;
import java.util.Map;

/**
 * @author zhuangqingdian
 * @date 2021/5/14
 */
public class OkHttpCli {


    private static final MediaType JSON = MediaType
            .parse("application/json; charset=utf-8");

    private static final MediaType XML = MediaType
            .parse("application/xml; charset=utf-8");

    private OkHttpClient okHttpClient;

    public OkHttpCli(OkHttpClient okc){
        this.okHttpClient = okc;
    }
//-------------------------------GET-------------------------------------//
    /**
     * get请求 无请求头.
     * @param url 请求url地址
     * @return jsonObject
     */
    public JSONObject doGet(String url) {
        return this.executeGet(url, null, null);
    }

    /**
     * get 请求.
     * @param url 请求url地址
     * @param params 参数 params
     * @return jsonObject
     */
    public JSONObject doGetParams(String url, Map<String, Object> params) {
        return this.executeGet(url, params, null);
    }

    /**
     * get 请求.
     * @param url 请求url地址
     * @param headers 请求头 headers
     * @return jsonObject
     */
    public JSONObject doGetHeaders(String url, Map<String, String> headers) {
        return this.executeGet(url, null, headers);
    }

    /**
     * get 请求.
     * @param url 请求url地址
     * @param params 请求参数 params
     * @param headers 请求头字段 headers
     * @return jsonObject
     */
    public JSONObject doGet(String url, Map<String, Object> params,
                            Map<String, String> headers) {
        return this.executeGet(url, params, headers);
    }
    /**
     * get 请求.
     * @param url 请求url地址，已经拼接好参数
     * @param params 请求url地址，已经拼接好参数
     * @param headers 请求头字段 headers
     * @return jsonObject
     */
    private JSONObject executeGet(String url, Map<String, Object> params,
                                  Map<String, String> headers) {
        // 拼接url和prams
        StringBuilder sb = new StringBuilder(url);
        if (params != null && params.keySet().size() > 0) {
            boolean firstFlag = true;
            for (String key : params.keySet()) {
                if (firstFlag) {
                    sb.append("?").append(key).append("=").append(params.get(key));
                    firstFlag = false;
                }
                else {
                    sb.append("&").append(key).append("=").append(params.get(key));
                }
            }
        }

        Request.Builder builder = new Request.Builder();

        // 添加headers
        if (headers != null && headers.keySet().size() > 0) {
            for (String key : headers.keySet()) {
                builder.addHeader(key, headers.get(key));
            }
        }

        // 执行请求
        Request request = builder.url(sb.toString()).build();
        return this.execute(request);
    }

    //----------------DELETE----------------------------------------//
    /**
     * delete 请求.
     * @param url 请求url地址
     * @param params 请求参数 params
     * @param headers 请求头字段 headers
     * @return jsonObject
     */
    public JSONObject doDelete(String url, Map<String, Object> params,
                               Map<String, String> headers) {
        return this.executeDelete(url, null, null, headers);
    }
    /**
     * delete请求.
     * @param url 请求url地址
     * @param data 请求参数
     * @param contentType 请求参数类型
     * @param headers 请求头
     * @return jsonObject
     */
    private JSONObject executeDelete(String url, String data, MediaType contentType,
                                     Map<String, String> headers) {

        Request.Builder builder = new Request.Builder();
        // 添加headers
        if (headers != null && headers.keySet().size() > 0) {
            for (String key : headers.keySet()) {
                builder.addHeader(key, headers.get(key));
            }
        }

        // 执行请求
        Request request = builder.url(url).delete().build();
        return this.execute(request);
    }
    //---------------------------POST---------------------------------//
    /**
     * post请求 无请求头. 请求数据为 json 的字符串
     * @param url 请求url地址
     * @param json 请求数据, json 字符串
     * @return jsonObject
     */
    public JSONObject doPostJson(String url, String json) {
        return this.executePost(url, json, JSON, null);
    }

    /**
     * post请求 带请求头. 请求数据为 json 的字符串
     * @param url 请求url地址
     * @param json 请求数据, json 字符串
     * @param headers 请求头, headers
     * @return jsonObject
     */
    public JSONObject doPostJson(String url, String json, Map<String, String> headers) {
        return this.executePost(url, json, JSON, headers);
    }

    /**
     * post请求 带请求头. 请求数据为 json 的字符串
     * @param url 请求url地址
     * @param file file
     * @param headers 请求头, headers
     * @return jsonObject
     */
    public JSONObject doPostFile(String url, File file, Map<String, String> headers) {
        return this.executePostFile(url, file, headers);
    }

    /**
     * post请求 无headers. 请求数据为 xml 的字符串
     * @param url 请求url地址
     * @param xml 请求数据, xml 字符串
     * @return jsonObject
     */
    public JSONObject doPostXml(String url, String xml) {
        return this.executePost(url, xml, XML, null);
    }

    /**
     * post请求 带headers. 请求数据为 xml 的字符串
     * @param url 请求url地址
     * @param xml 请求数据, xml 字符串
     * @param headers 请求头, headers
     * @return jsonObject
     */
    public JSONObject doPostXml(String url, String xml, Map<String, String> headers) {
        return this.executePost(url, xml, XML, headers);
    }

    /**
     * post请求.
     * @param url 请求url地址
     * @param data 请求参数
     * @param contentType 请求参数类型
     * @param headers 请求头
     * @return jsonObject
     */
    private JSONObject executePost(String url, String data, MediaType contentType,
                                   Map<String, String> headers) {

        Request.Builder builder = new Request.Builder();
        RequestBody requestBody = RequestBody.create(contentType, data);
        // 添加headers
        if (headers != null && headers.keySet().size() > 0) {
            for (String key : headers.keySet()) {
                builder.addHeader(key, headers.get(key));
            }
        }

        // 执行请求
        Request request = builder.url(url).post(requestBody).build();
        return this.execute(request);
    }

    /**
     * post请求.
     * @param url 请求url地址
     * @param file 请求参数
     * @param headers 请求头
     * @return jsonObject
     */
    private JSONObject executePostFile(String url, File file,
                                       Map<String, String> headers) {
        RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"),
                file);
        Request.Builder builder = new Request.Builder();
        MultipartBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), fileBody).build();
        if (headers != null && headers.keySet().size() > 0) {
            for (String key : headers.keySet()) {
                builder.addHeader(key, headers.get(key));
            }
        }
        Request request = builder.post(body).url(url).build();
        return this.execute(request);
    }

//-------------------------------------------------------------------------
    /**
     * 真正发送请求的方法.
     * @param request request
     * @return 请求返回的内容
     */
    private JSONObject execute(Request request) {
        try (Response response = this.okHttpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {
                if (response.body() != null) {
                    String responseStr = response.body().string();
                    return JSONObject.parseObject(responseStr);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        throw new RuntimeException("系统异常");
    }
}
