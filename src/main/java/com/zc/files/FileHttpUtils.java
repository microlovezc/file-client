package com.zc.files;

import org.apache.http.HttpEntity;
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

public class FileHttpUtils {
    /**
     * 上传文件
     * @param file 传入要上传的文件
     * @return  返回该文件的uuid
     * @throws Exception 抛出异常 可try catch处理
     */
    public String upload(File file) throws Exception{
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost("http://localhost:8080/file/upload");
        RequestConfig config = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(20000).build();
        post.setConfig(config);
        //模拟表单提交file
        FileBody fileBody = new FileBody(file, ContentType.DEFAULT_BINARY);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addPart("file", fileBody);

        HttpEntity entity = builder.build();
        post.setEntity(entity);
        String result = "";

        try {
            CloseableHttpResponse e = client.execute(post);
            HttpEntity resEntity = e.getEntity();
            if(entity != null) {
                result = EntityUtils.toString(resEntity);
                System.out.println("response content:" + result);
            }
        } catch (IOException var10) {
            var10.printStackTrace();
        }

        return result;
    }

    /**
     * 下载文件
     * @param uuid 传入要下载的文件uuid
     * @param downloadPath  要下载到文件的目录
     * @throws Exception   抛出异常 可try catch处理
     */
    public void download(String uuid,File downloadPath) throws Exception {
        CloseableHttpClient client = HttpClients.createDefault();
        URI url = new URIBuilder("http://localhost:8080/file/download").setParameter("uid",uuid).build();
        HttpGet get = new HttpGet(url);
        CloseableHttpResponse execute = client.execute(get);


        InputStream content = execute.getEntity().getContent();
        OutputStream fileOutputStream = new FileOutputStream(downloadPath);
        byte[] bytes = new byte[1024];
        int len = 0;
        while (( len = content.read(bytes)) != -1){
            fileOutputStream.write(bytes,0,len);
        }
        content.close();
        fileOutputStream.close();

    }

    /**
     * 获取文件元数据
     * @param uuid 传入要获取的文件的uuid
     * @return  返回的是该文件的json格式的元数据
     * @throws Exception 抛出异常 可try catch处理
     */
    public String getFile(String uuid) throws Exception {
        CloseableHttpClient client = HttpClients.createDefault();
        URI url = new URIBuilder("http://localhost:8080/file/getFileInfo").setParameter("uid",uuid).build();
        HttpGet get = new HttpGet(url);
        CloseableHttpResponse execute = client.execute(get);
        String result = "";
        try {
            if( execute.getStatusLine().getStatusCode() == 200 ){
                result = EntityUtils.toString(execute.getEntity(), "UTF-8");
            }else {
                throw new Exception("获取元数据信息失败！");
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            execute.close();
            client.close();
        }
        return result;
    }
}
