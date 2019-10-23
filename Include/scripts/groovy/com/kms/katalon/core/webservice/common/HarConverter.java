package com.kms.katalon.core.webservice.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;

import com.kms.katalon.core.testobject.RequestObject;
import com.kms.katalon.core.testobject.ResponseObject;
import com.kms.katalon.core.testobject.TestObjectProperty;
import com.kms.katalon.core.webservice.constants.RequestHeaderConstants;
import com.kms.katalon.core.webservice.helper.RestRequestMethodHelper;

import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.QueryStringDecoder;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.core.har.HarLog;
import net.lightbody.bmp.core.har.HarNameValuePair;
import net.lightbody.bmp.core.har.HarPostData;
import net.lightbody.bmp.core.har.HarPostDataParam;
import net.lightbody.bmp.core.har.HarRequest;
import net.lightbody.bmp.core.har.HarResponse;
import net.lightbody.bmp.core.har.HarTimings;
import net.lightbody.bmp.exception.UnsupportedCharsetException;
import net.lightbody.bmp.util.BrowserMobHttpUtil;

public class HarConverter {
    
    private Har har;
    
    private HarEntry harEntry;
    
    private HarRequest harRequest;
    
    private HarResponse harResponse;
    
    private Date startTime;
    
    private Date endTime;

    public void initHarFile() {
        har = new Har();
        har.setLog(new HarLog());
        
        harEntry = new HarEntry();
        har.getLog().addEntry(harEntry);
        
        startTime = new Date();
    }
    
    public Har endHar(RequestObject request, ResponseObject response) {
        endTime = new Date();
        
        if (request != null) {
            convertToHarRequest(request);
        }
        
        if (response != null) {
            convertToHarResponse(response);
        }
        
        captureHarTimings(startTime, endTime);
        
        return har;        
    }
    
    private void convertToHarRequest(RequestObject request) {
        harRequest = createHarRequest(request);
        harEntry.setRequest(harRequest);
        
        captureQueryParameters(request);
        
        captureRequestHeaders(request);
        
        if (requestHasBody(request)) {
            captureRequestContent(request);
        }
    }
    
    private void convertToHarResponse(ResponseObject response) {
        harResponse = new HarResponse(response.getStatusCode(), "", "");
        harEntry.setResponse(harResponse);
        
        captureResponseMimeType(response);
        
        captureResponseHeaders(response);
        
        captureResponseHeaderSize(response);
        
        captureResponseContent(response);
        
        captureResponseBodySize(response);
    }
    
    private void captureHarTimings(Date startTime, Date endTime) {
        harEntry.setStartedDateTime(startTime);
        HarTimings timings = new HarTimings();
        timings.setConnect(endTime.getTime() - startTime.getTime(), TimeUnit.MILLISECONDS);
        harEntry.setTimings(timings);
    }
    
    private HarRequest createHarRequest(RequestObject request) {
        String url = getUrl(request);
        String requestMethod = getRequestMethod(request);
        return new HarRequest(requestMethod, url, "");
       
    }
    
    private String getUrl(RequestObject request) {
        if (RequestHeaderConstants.SOAP.equals(request.getServiceType())) {
            return request.getWsdlAddress();
        } else {
            return request.getRestUrl();
        }
    }
    
    private String getRequestMethod(RequestObject request) {
        if (RequestHeaderConstants.SOAP.equals(request.getServiceType())) {
            return request.getSoapRequestMethod();
        } else {
            return request.getRestRequestMethod();
        }
    }
    
    private void captureQueryParameters(RequestObject request) {
        String url = getUrl(request);
        QueryStringDecoder queryStringDecoder = new QueryStringDecoder(url, StandardCharsets.UTF_8);
        
        try {
            for (Map.Entry<String, List<String>> entry : queryStringDecoder.parameters().entrySet()) {
                for (String value : entry.getValue()) {
                    harEntry.getRequest().getQueryString().add(new HarNameValuePair(entry.getKey(), value));
                }
            }
        } catch (IllegalArgumentException e) {
            harEntry.setComment("Unable to decode query parameters on URI: " + url);
        }
    }
    
    private void captureRequestHeaders(RequestObject request) {
        List<TestObjectProperty> headers = request.getHttpHeaderProperties();
        
        for (TestObjectProperty header : headers) {
            harEntry.getRequest().getHeaders().add(new HarNameValuePair(header.getName(), header.getValue()));
        }
    }
    
    private boolean requestHasBody(RequestObject request) {
        boolean hasBody = false;
        if (RequestHeaderConstants.SOAP.equals(request.getServiceType())) {
            hasBody = !StringUtils.isBlank(request.getSoapBody());
        } else {
            hasBody = RestRequestMethodHelper.isBodySupported(request.getRestRequestMethod())
                    && request.getBodyContent() != null;
        }
        return hasBody;
    }
    
    private void captureRequestContent(RequestObject request) {
        String contentType = findContentTypeHeader(request.getHttpHeaderProperties());
        if (StringUtils.isBlank(contentType)) {
            contentType = BrowserMobHttpUtil.UNKNOWN_CONTENT_TYPE;
        }
        
        HarPostData postData = new HarPostData();
        harEntry.getRequest().setPostData(postData);
        
        postData.setMimeType(contentType);
        
        boolean urlEncoded;
        if (contentType.startsWith(HttpHeaders.Values.APPLICATION_X_WWW_FORM_URLENCODED)) {
            urlEncoded = true;
        } else {
            urlEncoded = false;
        }
        
        Charset charset;
        try {
            charset = BrowserMobHttpUtil.readCharsetInContentTypeHeader(contentType);
        } catch (UnsupportedCharsetException e) {
            return;
        }
        
        if (charset == null) {
            charset = BrowserMobHttpUtil.DEFAULT_HTTP_CHARSET;
        }
        
        byte[] requestContentByteArray = getRequestContentByteArray(request);
        
        if (urlEncoded) {     
            String textContents = BrowserMobHttpUtil.getContentAsString(requestContentByteArray, charset);
            
            QueryStringDecoder queryStringDecoder = new QueryStringDecoder(textContents, charset, false);
            
            List<HarPostDataParam> postDataParams = new ArrayList<>();
            
            for (Map.Entry<String, List<String>> entry : queryStringDecoder.parameters().entrySet()) {
                for (String value : entry.getValue()) {
                    postDataParams.add(new HarPostDataParam(entry.getKey(), value));
                }
            }
            
            harEntry.getRequest().getPostData().setParams(postDataParams);
        } else {
            String postBody = BrowserMobHttpUtil.getContentAsString(requestContentByteArray, charset);
            harEntry.getRequest().getPostData().setText(postBody);
        }
    }
    
    private byte[] getRequestContentByteArray(RequestObject request) {
        if (RequestHeaderConstants.SOAP.equals(request.getServiceType())) {
            return request.getSoapBody().getBytes(StandardCharsets.UTF_8);
        } else {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            try {
                request.getBodyContent().writeTo(os);
                return os.toByteArray();
            } catch (IOException e) {
                return new byte[0];
            }
        }
    }
    
    private String findContentTypeHeader(List<TestObjectProperty> headers) {
        return headers.stream()
            .filter(h -> h.getName().startsWith("Content-Type"))
            .findAny()
            .map(h -> h.getValue())
            .orElse(null);
    }
    
    private void captureResponseMimeType(ResponseObject response) {
        String contentType = response.getContentType();
        if (!StringUtils.isBlank(contentType)) {
            harEntry.getResponse().getContent().setMimeType(contentType);
        }
    }
    
    private void captureResponseHeaderSize(ResponseObject response) {
        harEntry.getResponse().setHeadersSize(response.getResponseHeaderSize());
    }
    
    private void captureResponseHeaders(ResponseObject response) {
        for (Map.Entry<String, List<String>> entry : response.getHeaderFields().entrySet()) {
            for (String value : entry.getValue()) {
                harEntry.getResponse().getHeaders().add(new HarNameValuePair(entry.getKey(), value));
            }
        }
    }
    
    private void captureResponseContent(ResponseObject response) {
        String contentType = response.getContentType();
        if (StringUtils.isBlank(contentType)) {
            contentType = BrowserMobHttpUtil.UNKNOWN_CONTENT_TYPE;
        }
        
        Charset charset;
        try {
            charset = BrowserMobHttpUtil.readCharsetInContentTypeHeader(contentType);
        } catch (UnsupportedCharsetException e) {
            return;
        }
        
        if (charset == null) {
            charset = BrowserMobHttpUtil.DEFAULT_HTTP_CHARSET;
        }
        
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            response.getBodyContent().writeTo(os);
        } catch (IOException e) {
            return;
        }
        
        String text = BrowserMobHttpUtil.getContentAsString(os.toByteArray(), charset);
        harEntry.getResponse().getContent().setText(text);
        
        harEntry.getResponse().getContent().setSize(os.toByteArray().length);
    }
    
    private void captureResponseBodySize(ResponseObject response) {
        harEntry.getResponse().setBodySize(response.getResponseBodySize());
    }
}
