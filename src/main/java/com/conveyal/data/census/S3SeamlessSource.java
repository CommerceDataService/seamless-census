package com.conveyal.data.census;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;

import java.io.IOException;
import java.io.InputStream;

/**
 * A seamless data source based on storage in Amazon S3.
 */
public class S3SeamlessSource extends SeamlessSource {
    private static AmazonS3 s3 = new AmazonS3Client();

    public final String bucketName;

    public S3SeamlessSource(String bucketName) {
        this.bucketName = bucketName;
    }

    @Override
    protected InputStream getInputStream(int x, int y) throws IOException {
        try {
            return s3.getObject(bucketName, String.format("%d/%d.pbf.gz", x, y)).getObjectContent();
        } catch (AmazonS3Exception e) {
            // there is no data in this tile
            if ("NoSuchKey".equals(e.getErrorCode()))
                return null;
            else
                // re-throw, something else is amiss
                throw e;
        }
    }
}
