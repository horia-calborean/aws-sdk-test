package ro.calborean.test.aws.ml.textract;

import ro.calborean.test.aws.AWSProfile;
import ro.calborean.test.aws.s3.FileUploader;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.services.textract.TextractClient;
import software.amazon.awssdk.services.textract.model.DetectDocumentTextRequest;
import software.amazon.awssdk.services.textract.model.DetectDocumentTextResponse;
import software.amazon.awssdk.services.textract.model.Document;
import software.amazon.awssdk.services.textract.model.S3Object;

import java.io.IOException;

public class Textract {

    private static final String S3_IMAGE_NAME = "image.jpg";
    private static final String IMAGE_FILE_PATH = "D:/image.jpg";
    private static final String BUCKET_NAME = "com.visma.odp.test.aws.ml.textract";

    public static void main(String[] args) throws IOException {
        AwsCredentialsProvider cr = ProfileCredentialsProvider.create(AWSProfile.NAME);
        FileUploader.upload(IMAGE_FILE_PATH, BUCKET_NAME,S3_IMAGE_NAME);

        //Detect text - build the client
        TextractClient cl = TextractClient.builder().credentialsProvider(cr).build();
        //Detect text - get the s3 object
        final S3Object s3Image = S3Object.builder().bucket(BUCKET_NAME).name(S3_IMAGE_NAME).build();
        Document dcmnt = Document.builder().s3Object(s3Image).build();

        //Detect text - call the textract
        DetectDocumentTextRequest req = DetectDocumentTextRequest.builder().document(dcmnt).build();
        DetectDocumentTextResponse resp = cl.detectDocumentText(req);

        System.out.println(resp.toString());
    }
}
