package com.demoapp.demoapp.service.interfaces;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.demoapp.demoapp.model.dto.FileDTO;
import com.demoapp.demoapp.model.response.S3FileDTO;

public interface AwsService {

    /**
     * Reads a file from the specified S3 bucket with the given object key.
     * 
     * @param objectKey the {@link String key} of the object to read
     * @return the {@link FileDTO file} read from S3
     * @throws IOException if an I/O error occurs during file read
     */
    FileDTO readFile(String objectKey) throws IOException;

    /**
     * Uploads a file to the specified S3 bucket with the given object key.
     * 
     * @param objectKey the {@link String key} under which to store the new object
     * @param file the {@link MultipartFile file} to upload
     * @throws IOException if an I/O error occurs during file upload
     */
    void uploadFile(String objectKey, MultipartFile file) throws IOException;

    /**
     * Deletes a file from the specified S3 bucket with the given object key.
     * 
     * @param objectKey the {@link String key} of the object to delete
     */
    void deleteFile(String objectKey);

    /**
     * Retrieves a list of file names from the S3 bucket.
     * 
     * @return a {@link List} of {@link S3FileDTO} containing file names
     */
    List<S3FileDTO> getFileList(String query);

}
