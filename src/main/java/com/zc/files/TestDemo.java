package com.zc.files;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sun.javafx.fxml.builder.URLBuilder;
import jdk.nashorn.internal.runtime.JSONFunctions;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.URI;
import java.util.HashMap;

public class TestDemo {

    public static void main(String[] args) throws Exception{
        //上传文件 上传的文件为E盘下test文件夹的test文件
        File file = new File("E:\\test\\test.txt");
        //创建文件操作类
        FileHttpUtils fileHttpUtils = new FileHttpUtils();
        //若上传成功则获得返回的uuid值
        String result = fileHttpUtils.upload(file);
        JSONObject jsonObject = new JSONObject();
        jsonObject = JSON.parseObject(result);
        String uuid = jsonObject.get("UUid").toString();

        //获取文件的元数据通过传入要查询的文件的uuid
        String file1 = fileHttpUtils.getFile(uuid);
        System.out.println("查询的文件的元数据为："+file1);


        //下载文件 传入要下载的文件的uuid  和要保存到的位置
        File fileNew = new File("E:\\test\\testNew.txt");
        fileHttpUtils.download(uuid,fileNew);
    }




}
