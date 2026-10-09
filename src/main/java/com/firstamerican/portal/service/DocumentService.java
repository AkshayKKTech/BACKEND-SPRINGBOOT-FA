package com.firstamerican.portal.service;

import com.firstamerican.portal.model.Order;
import com.firstamerican.portal.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
public class DocumentService {

    @Autowired
    private S3Client s3Client;

    @Autowired
    private OrderRepository orderRepository;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    public String uploadDocumentToS3(String orderId, MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String s3Key = "orders/" + orderId + "/" + UUID.randomUUID() + "-" + originalFilename;

        // 1. Upload File directly to AWS S3
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Key)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        // 2. Build S3 File URL
        String s3Url = String.format("https://%://amazonaws.com", bucketName, s3Key);

        // 3. Save S3 URL metadata into AWS RDS Database
        Order order = orderRepository.findByOrderId(orderId)
                .orElseGet(() -> {
                    Order newOrder = new Order();
                    newOrder.setOrderId(orderId);
                    newOrder.setStatus("UNDER_REVIEW");
                    newOrder.setAssignedTeam("Bangalore Ops");
                    newOrder.setPropertyAddress("");
                    return newOrder;
                });
        
        order.setDocumentS3Url(s3Url);
        orderRepository.save(order);

        // 🚀 THE FIXED LINE: Ensure the calculated string is returned
        return s3Url; 
    }
}
