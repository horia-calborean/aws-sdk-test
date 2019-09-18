package ro.calborean.test.aws.ml.comprehend;

import ro.calborean.test.aws.AWSProfile;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.services.comprehend.ComprehendClient;
import software.amazon.awssdk.services.comprehend.model.DetectEntitiesRequest;
import software.amazon.awssdk.services.comprehend.model.DetectEntitiesResponse;

public class DetectEntities {
    public static void main(String[] args) {
        String text = "It is raining today in Cluj, but at least I met Barack Obama. He was at Burning Man festival. He had two bottles of Stella Artois!";

        AwsCredentialsProvider cr = ProfileCredentialsProvider.create(AWSProfile.NAME);
        ComprehendClient cl = ComprehendClient.builder().credentialsProvider(cr).build();

        DetectEntitiesRequest detectEntitiesRequest = DetectEntitiesRequest.builder().text(text).languageCode("en").build();
        DetectEntitiesResponse detectEntitiesResult = cl.detectEntities(detectEntitiesRequest);
        detectEntitiesResult.entities().forEach(System.out::println);
    }
}
