package net.boardrank.boardgame.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
@NoArgsConstructor
public class S3Service {

    @Autowired
    AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.cloudfront.url}")
    String cloudFrontDomain;


    public String upload(String fileName, InputStream inputStream) throws Exception {
        amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, null)
                .withCannedAcl(CannedAccessControlList.PublicRead));

        return fileName;
    }

    public String getURLAsCloundFront(String filename) {
        return "https://" + this.cloudFrontDomain + "/" + filename;
    }
}