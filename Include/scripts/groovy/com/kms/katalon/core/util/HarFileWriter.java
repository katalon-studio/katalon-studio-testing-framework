package com.kms.katalon.core.util;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import net.lightbody.bmp.core.har.Har;

public class HarFileWriter {

    private static ObjectMapper mapper;
    
    private static ObjectMapper mapper() {
        if (mapper == null) {
            mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
        }
        return mapper;
    }
    
    public static void  write(Har har, File file) throws IOException {
        mapper().writeValue(file, har);
    }
}
