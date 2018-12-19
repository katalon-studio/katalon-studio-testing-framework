package com.kms.katalon.core.testobject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import com.kms.katalon.core.testobject.impl.HttpTextBodyContent;

public class ResponseObject implements PerformanceResourceTiming, HttpMessage {

    private static final String DF_CHARSET = "UTF-8";

    private String contentType = "text/plain";

    @Deprecated
    private String responseText;

    private int statusCode;

    private Map<String, List<String>> headerFields;
    
    private long responseHeaderSize;
    
    private long responseBodySize;
    
    private long waitingTime;
    
    private long contentDownloadTime;
    
    private String contentCharset;

    private HttpBodyContent responseBodyContent;

    public ResponseObject() {
    }

    public ResponseObject(String responseText) {
        this.responseText = responseText;
    }

    /**
     * Get the response body content as a String
     * 
     * @return the response body content as a String
     * @throws Exception if errors happened
     */
    // TODO: Detect the source to see if it is JSON, XML, HTML or plain text
    public String getResponseBodyContent() throws Exception {
        if (responseText != null) {
            if (contentType != null && contentType.startsWith("application/xml")) {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                Document doc = dbf.newDocumentBuilder().parse(new InputSource(new StringReader(responseText)));
                XPath xPath = XPathFactory.newInstance().newXPath();
                Node node = (Node) xPath.evaluate("//*//*//*", doc, XPathConstants.NODE);
                return nodeToString(node);
            } else if (contentType != null && contentType.startsWith("application/json")) {
                return responseText;
            }
            // plain text/html
            else {
                return responseText;
            }
        }
        return "";
    }

    /**
     * Get the raw response text
     * 
     * @return the raw response text
     * @throws IOException if content could not be parsed to String.
     */
    public String getResponseText() throws IOException {
        ByteArrayOutputStream outstream = new ByteArrayOutputStream();
        responseBodyContent.writeTo(outstream);
        return outstream.toString(getContentCharset());
    }

    /**
     * Set the raw response text
     * 
     * @deprecated from 5.4
     * @param responseText the new raw response text
     */
    public void setResponseText(String responseText) {
        responseBodyContent = new HttpTextBodyContent(responseText);
        this.responseText = responseText;
    }

    /**
     * Get the content type
     * 
     * @return the content type
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * Set the content type
     * 
     * @param contentType the new content type
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    private String nodeToString(Node node) throws TransformerException {
        StringWriter writer = new StringWriter();
        Transformer xform = TransformerFactory.newInstance().newTransformer();
        xform.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        xform.transform(new DOMSource(node), new StreamResult(writer));
        return writer.toString();
    }

    /**
     * Check if the content type of this response is json
     * 
     * @return true if the content type of this response is json, otherwise false
     */
    public boolean isJsonContentType() {
        return contentType != null && contentType.toLowerCase().startsWith("application/json");
    }

    /**
     * Check if the content type of this response is xml
     * 
     * @return true if the content type of this response is xml, otherwise false
     */
    public boolean isXmlContentType() {
        String contentTypeString = contentType.toLowerCase();
        return contentType != null && (contentTypeString.startsWith("application/xml")
                || contentTypeString.equals("application/soap+xml") || contentTypeString.equals("text/xml"));
    }

    /**
     * Check if the content type of this response is raw text
     * 
     * @return true if the content type of this response is raw text, otherwise false
     */
    public boolean isTextContentType() {
        return !isJsonContentType() && !isXmlContentType();
    }

    /**
     * Get the header fields as a {@link Map}
     * 
     * @return the header fields as a {@link Map}
     */
    public Map<String, List<String>> getHeaderFields() {
        if (headerFields == null) {
            headerFields = Collections.emptyMap();
        }
        return headerFields;
    }

    /**
     * Set the header fields
     * 
     * @param headerFields the new header fields as a {@link Map}
     */
    public void setHeaderFields(Map<String, List<String>> headerFields) {
        this.headerFields = headerFields;
    }

    /**
     * Get the status code
     * 
     * @return the status code
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Set the status code
     * 
     * @param statusCode the status code
     */
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * Returns size (byte) as long value of the response.</br></br>
     * Response size = Header size + Body size
     * 
     * @see #getResponseHeaderSize()
     * @see #getResponseBodySize()
     * 
     */
    public long getResponseSize() {
        return getResponseHeaderSize() + getResponseBodySize();
    }

    /**
     * Returns headers size (byte) as long value of the response.
     * 
     * @see #getResponseBodySize()
     * @see #getResponseSize()
     */
    public long getResponseHeaderSize() {
        return responseHeaderSize;
    }

    public void setResponseHeaderSize(long reponseHeaderSize) {
        this.responseHeaderSize = reponseHeaderSize;
    }

    public long getResponseBodySize() {
        return responseBodySize;
    }

    /**
     * Returns body size (byte) as long value of the response.
     * 
     * @see #getResponseHeaderSize()
     * @see #getResponseSize()
     */
    public void setResponseBodySize(long reponseBodySize) {
        this.responseBodySize = reponseBodySize;
    }

    @Override
    public long getElapsedTime() {
        return getWaitingTime() + getContentDownloadTime();
    }

    @Override
    public long getWaitingTime() {
        return waitingTime;
    }
    
    public void setWaitingTime(long waitingTime) {
        this.waitingTime = waitingTime;
    }

    @Override
    public long getContentDownloadTime() {
        return contentDownloadTime;
    }
    
    public void setContentDownloadTime(long contentDownloadTime) {
        this.contentDownloadTime = contentDownloadTime;
    }

    @Override
    public HttpBodyContent getBodyContent() {
        return responseBodyContent;
    }
    
    public void setBodyContent(HttpBodyContent bodyContent) {
        this.responseBodyContent = bodyContent;
    }

    /**
     * @return Returns the Charset specified in the Content-Type of this response or the "UTF-8" charset as a default.
     * 
     * @since 5.4
     */
    public String getContentCharset() {
        if (StringUtils.isEmpty(contentCharset)) {
            return DF_CHARSET;
        }
        return contentCharset;
    }

    public void setContentCharset(String contentCharset) {
        this.contentCharset = contentCharset;
    }
    
    @Override
    public String toString() {
        return getStatusCode() + " " + FileUtils.byteCountToDisplaySize(getResponseSize());
    }
}
