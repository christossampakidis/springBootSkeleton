package com.demoapp.demoapp.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.demoapp.demoapp.model.dto.FileDTO;
import com.demoapp.demoapp.model.response.S3FileDTO;

public interface AwsService {

    /**
     * Reads a file from the specified S3 bucket with the given object key.
     * 
     * @param objectKey
     * @return
     * @throws IOException
     */
    public FileDTO readFile(String objectKey) throws IOException;

    /**
     * Uploads a file to the specified S3 bucket with the given object key.
     * 
     * @param objectKey
     * @param file
     * @throws IOException
     */
    public void uploadFile(String objectKey, MultipartFile file) throws IOException;

    /**
     * Deletes a file from the specified S3 bucket with the given object key.
     * 
     * @param objectKey
     */
    public void deleteFile(String objectKey);

    /**
     * Retrieves a list of file names from the S3 bucket.
     * 
     * @return
     */
    public List<S3FileDTO> getFileList();

}
