package ro.calborean.test.aws.ml.textract;

import ro.calborean.test.aws.AWSProfile;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.services.textract.TextractClient;
import software.amazon.awssdk.services.textract.model.DetectDocumentTextRequest;
import software.amazon.awssdk.services.textract.model.Document;
import software.amazon.awssdk.services.textract.model.S3Object;
import software.amazon.awssdk.services.s3.model.CreateBucketResponse;
import software.amazon.awssdk.services.s3.model.GetBucketLocationRequest;
import software.amazon.awssdk.services.s3.model.GetBucketLocationResponse;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;
import java.io.IOException;
import java.nio.ByteBuffer;
import javax.imageio.ImageIO;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.textract.model.DetectDocumentTextResponse;

public class Textract {

    private static final String S3_IMAGE_NAME = "image.jpg";
    private static final String IMAGE_FILE_PATH = "D:/image.jpg";

    public static void main(String[] args) throws IOException {
        AwsCredentialsProvider cr = ProfileCredentialsProvider.create(AWSProfile.NAME);

        //Creating the bucket or finding the existing one
        String bucketName = "com.visma.odp.test.aws.ml.textract";
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
        s3.putObject(PutObjectRequest.builder().bucket(bucketName).key(S3_IMAGE_NAME)
                .build(), RequestBody.fromByteBuffer(getImageAsByteBuffer(IMAGE_FILE_PATH)));

        //Detect text - build the client
        TextractClient cl = TextractClient.builder().credentialsProvider(cr).build();
        //Detect text - get the s3 object
        final S3Object s3Image = S3Object.builder().bucket(bucketName).name(S3_IMAGE_NAME).build();
        Document dcmnt = Document.builder().s3Object(s3Image).build();

        //Detect text - call the textract
        DetectDocumentTextRequest req = DetectDocumentTextRequest.builder().document(dcmnt).build();
        DetectDocumentTextResponse resp = cl.detectDocumentText(req);

        System.out.println(resp.toString());
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
