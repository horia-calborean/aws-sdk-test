package ro.calborean.test.aws.s3;

import ro.calborean.test.aws.AWSProfile;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class FileUploader {
    public static void upload(String filePath,String bucketName, String s3FileName) throws IOException {
        AwsCredentialsProvider cr = ProfileCredentialsProvider.create(AWSProfile.NAME);

        //Creating the bucket or finding the existing one

        S3Client s3 = S3Client.builder().credentialsProvider(cr).build();
        try {
            GetBucketLocationResponse bucketLocation = s3.getBucketLocation(GetBucketLocationRequest.builder().bucket(bucketName).build());
            System.out.println("Bucket found");
        } catch (NoSuchBucketException ex) {
            System.out.println("Bucket not found. Creating ...");
            CreateBucketRequest createBucketRequest = CreateBucketRequest
                    .builder()
                    .bucket(bucketName)
                    .build();
            CreateBucketResponse createBucket = s3.createBucket(createBucketRequest);
        }

        //Upload image
        s3.putObject(PutObjectRequest.builder().bucket(bucketName).key(s3FileName)
                .build(), RequestBody.fromByteBuffer(getImageAsByteBuffer(filePath)));
    }

    private static ByteBuffer getImageAsByteBuffer(String imagejpg) throws IOException {
        BufferedImage originalImage = ImageIO.read(new File(imagejpg));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(originalImage, "jpg", baos);
        baos.flush();
        byte[] imageInByte = baos.toByteArray();
        baos.close();
        return ByteBuffer.wrap(imageInByte);
    }
}
