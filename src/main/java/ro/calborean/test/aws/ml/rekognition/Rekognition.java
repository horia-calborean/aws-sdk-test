package ro.calborean.test.aws.ml.rekognition;

import ro.calborean.test.aws.AWSProfile;
import ro.calborean.test.aws.s3.FileUploader;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.*;

import java.io.IOException;

public class Rekognition {

    private static final String S3_IMAGE_NAME = "image.jpg";
    private static final String IMAGE_FILE_PATH = "D:/"+S3_IMAGE_NAME;
    private static final String BUCKET_NAME = "com.visma.odp.test.aws.ml.rekognition";

    public static void main(String[] args) throws IOException {
        AwsCredentialsProvider cr = ProfileCredentialsProvider.create(AWSProfile.NAME);
        FileUploader.upload(IMAGE_FILE_PATH, BUCKET_NAME,S3_IMAGE_NAME);
        //Detect text - build the client
        RekognitionClient cl = RekognitionClient.builder().credentialsProvider(cr).build();

        //Detect text - get the s3 object
        final S3Object s3Image = S3Object.builder().bucket(BUCKET_NAME).name(S3_IMAGE_NAME).build();
        Image image = Image.builder().s3Object(s3Image).build();

        //Detect labels - call the rekognition
        DetectLabelsRequest req = DetectLabelsRequest.builder().image(image).build();
        final DetectLabelsResponse resp = cl.detectLabels(req);
        System.out.println(resp.toString());

        //Detect Faces
        DetectFacesRequest request = DetectFacesRequest.builder().image(image).build();
        final DetectFacesResponse resp2 = cl.detectFaces(request);
        System.out.println(resp2.toString());

        //Detect celebrity
        RecognizeCelebritiesRequest r = RecognizeCelebritiesRequest.builder().image(image).build();
        final RecognizeCelebritiesResponse resp3 = cl.recognizeCelebrities(r);
        System.out.println(resp3.toString());

    }
}
