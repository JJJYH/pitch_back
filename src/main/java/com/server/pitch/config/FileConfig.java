package com.server.pitch.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Configuration
public class FileConfig implements HttpMessageConverter<byte[]>{

    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        return byte[].class.equals(clazz);
    }

    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return byte[].class.equals(clazz);
    }

    @Override
    public List<MediaType> getSupportedMediaTypes() {
        return Arrays.asList(MediaType.APPLICATION_OCTET_STREAM);
    }

    @Override
    public byte[] read(Class<? extends byte[]> clazz, HttpInputMessage inputMessage) throws IOException {
        return inputMessage.getBody().readAllBytes();
    }

    @Override
    public void write(byte[] bytes, MediaType contentType, HttpOutputMessage outputMessage) throws IOException {
        outputMessage.getBody().write(bytes);
    }

}