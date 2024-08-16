package com.twitter.image.config;

import com.amazonaws.Request;
import com.amazonaws.Response;
import com.amazonaws.handlers.RequestHandler2;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;

public class ETagS3Interceptor extends RequestHandler2 {
    @Override
    public void afterResponse(Request<?> request, Response<?> response) {
        if (response.getAwsResponse() instanceof ObjectMetadata objectMetadata) {
            String etag = objectMetadata.getETag();
            if (etag != null && etag.startsWith("W/\"")) {
                etag = etag.substring(3); // Modify the ETag
                // Optionally store or log the modified ETag
                System.out.println("Modified ETag: " + etag);
                ((ObjectMetadata) response.getAwsResponse()).setHeader("ETag", etag);
            }
        }
    }
}