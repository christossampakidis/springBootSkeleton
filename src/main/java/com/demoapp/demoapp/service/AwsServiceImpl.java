package com.demoapp.demoapp.service;

import java.io.IOException;
import java.net.URLConnection;
import java.util.List;

import com.demoapp.demoapp.service.interfaces.AwsService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.demoapp.demoapp.model.dto.FileDTO;
import com.demoapp.demoapp.model.dto.S3FileDTO;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

@Service
@RequiredArgsConstructor
public class AwsServiceImpl implements AwsService {

    private final S3Client s3Client;

    @Value("${spring.cloud.aws.s3.bucket-name}")
    private String bucketName;

    /**
     * {@inheritDoc}
     */
    @Override
    public FileDTO readFile(String objectKey) throws IOException {
        try (ResponseInputStream<GetObjectResponse> response = s3Client
                .getObject(req -> req.bucket(bucketName).key(objectKey))) {

            byte[] bytes = response.readAllBytes();

            String contentType = response.response().contentType();
            if (contentType == null || contentType.isEmpty()) {
                contentType = URLConnection.guessContentTypeFromName(objectKey);
            }
            if (contentType == null) {
                contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            }

            return new FileDTO(bytes, contentType, objectKey);

        } catch (NoSuchKeyException e) {
            throw new RuntimeException("Object '" + objectKey
                    + "' does not exist in bucket '" + bucketName + "'!", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void uploadFile(String objectKey, MultipartFile file)
            throws IOException {
        String contentType = file.getContentType();
        if (contentType == null || contentType.isBlank()) {
            contentType = URLConnection
                    .guessContentTypeFromName(file.getOriginalFilename());
        }
        if (contentType == null) {
            contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }

        PutObjectRequest request =
                PutObjectRequest.builder().bucket(bucketName).key(objectKey)
                        .acl(ObjectCannedACL.PUBLIC_READ)
                        .contentType(contentType).build();

        s3Client.putObject(request, RequestBody.fromBytes(file.getBytes()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<S3FileDTO> getFileList(String query) {
        ListObjectsV2Request request = ListObjectsV2Request.builder()
                .bucket(bucketName).prefix(query).build();

        ListObjectsV2Response response = s3Client.listObjectsV2(request);

        return response.contents().stream().map(
                obj -> new S3FileDTO(obj.key(), obj.size(), obj.lastModified()))
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteFile(String objectKey) {
        s3Client.deleteObject(req -> req.bucket(bucketName).key(objectKey));
    }

}
