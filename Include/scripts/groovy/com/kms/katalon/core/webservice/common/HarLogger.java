package com.kms.katalon.core.webservice.common;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import com.kms.katalon.core.logging.KeywordLogger;
import com.kms.katalon.core.testobject.RequestObject;
import com.kms.katalon.core.testobject.ResponseObject;
import com.kms.katalon.core.util.HarFileWriter;
import com.kms.katalon.core.util.KatalonHarEntry;
import com.kms.katalon.core.util.RequestInformation;

import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.core.har.HarLog;

public class HarLogger {
    
    private static final KeywordLogger logger = KeywordLogger.getInstance(HarLogger.class);

    private static final AtomicLong requestNumber = new AtomicLong(0);
    
    private HarConverter harConverter;
    
    public void initHarFile() {
        harConverter = new HarConverter();
        harConverter.initHarFile();
    }
    
    public File logHarFile(RequestObject request, ResponseObject response, String logFolder) {
        
        try {
            Har har = harConverter.endHar(request, response);
    
            RequestInformation requestInformation = new RequestInformation();
            requestInformation.setName(String.valueOf(requestNumber.getAndIncrement()));
            Map<String, String> attributes = new HashMap<>();
            String harId = UUID.randomUUID().toString();
            attributes.put("harId", harId);
            requestInformation.setHarId(harId);
            requestInformation.setTestObjectId(request.getObjectId());
            
            String threadName = Thread.currentThread().getName();
            String directoryPath = logFolder;
            File directory = new File(directoryPath, "requests" + File.separator + threadName);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            File file = new File(directory, requestInformation.getName() + ".har");
            file.createNewFile();
            
            logger.logInfo("HAR: " + file.getAbsolutePath(), attributes);
            
            HarLog harLog = har.getLog();
            List<HarEntry> originalEntries = harLog.getEntries();
            List<KatalonHarEntry> newEntries = originalEntries.stream()
                    .map(entry -> {
                        KatalonHarEntry katalonEntry = new KatalonHarEntry(entry);
                        katalonEntry.set_katalonRequestInformation(requestInformation);
                        return katalonEntry;
                    })
                    .collect(Collectors.toList());
            originalEntries.clear();
            originalEntries.addAll(newEntries);
            
            HarFileWriter.write(har, file);
            return file;
        } catch (Exception e) {
            logger.logError("Cannot create HAR file");
        }
        return null;
    }
}
