package com.codit.talktalkcoach.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
public class S3Service {

    @Value("${aws.s3.bucket}")
    private String bucket;

    @Value("${aws.s3.region}")
    private String region;

    @Value("${aws.s3.access-key}")
    private String accessKey;

    @Value("${aws.s3.secret-key}")
    private String secretKey;

    private S3Client s3Client;

    @PostConstruct
    public void init() {
        s3Client = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)))
                .build();
        log.info("S3Client 초기화 완료 - bucket: {}, region: {}", bucket, region);
    }

    /**
     * 파일 업로드
     * @param file        업로드할 파일
     * @param folder      S3 폴더 경로 (예: "profile", "audio")
     * @return            접근 가능한 S3 URL
     */
    public String upload(MultipartFile file, String folder) {
        String originalName = file.getOriginalFilename();
        String ext = getExtension(originalName);
        String key = folder + "/" + UUID.randomUUID() + ext;

        try {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .contentType(file.getContentType())
                    .contentLength(file.getSize())
                    .build();

            s3Client.putObject(request, RequestBody.fromInputStream(
                    file.getInputStream(), file.getSize()));

            String url = buildUrl(key);
            log.info("S3 업로드 완료: {}", url);
            return url;

        } catch (IOException e) {
            log.error("S3 업로드 실패: {}", e.getMessage());
            throw new RuntimeException("파일 업로드 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * byte[] 업로드 (오디오 분석 완료 후 저장용)
     */
    public String upload(byte[] data, String folder, String filename) {
        String key = folder + "/" + UUID.randomUUID() + "_" + filename;

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType("audio/wav")
                .contentLength((long) data.length)
                .build();

        s3Client.putObject(request, RequestBody.fromBytes(data));

        String url = buildUrl(key);
        log.info("S3 업로드 완료 (bytes): {}", url);
        return url;
    }

    /**
     * 파일 삭제 (URL에서 key 추출)
     */
    public void delete(String fileUrl) {
        if (fileUrl == null || fileUrl.isBlank() || fileUrl.equals("pending")) return;
        try {
            String key = extractKey(fileUrl);
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build());
            log.info("S3 삭제 완료: {}", key);
        } catch (Exception e) {
            log.warn("S3 삭제 실패 (무시): {}", e.getMessage());
        }
    }

    // ─── 유틸 ────────────────────────────────────────────────────────────────
    private String buildUrl(String key) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucket, region, key);
    }

    private String extractKey(String url) {
        // https://버킷.s3.리전.amazonaws.com/KEY 에서 KEY 추출
        return url.substring(url.indexOf(".amazonaws.com/") + ".amazonaws.com/".length());
    }

    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) return "";
        return filename.substring(filename.lastIndexOf("."));
    }
}
