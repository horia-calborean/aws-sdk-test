package ro.calborean.test.aws.ml.comprehend;


import ro.calborean.test.aws.AWSProfile;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.services.comprehend.ComprehendClient;
import software.amazon.awssdk.services.comprehend.model.*;

public class SentimentAnalysis {
    public static void main(String[] args) {
        String text = "I just wanted to find some really cool new places I've never visited before but no luck here. Some of these suggestions are just terrible... I had to laugh! Most suggestions were just your typical big cities, restaurants and bars. Nothing off the beaten path here. I don't want to go these places for fun. Totally not worth getting this.";
                
        AwsCredentialsProvider cr = ProfileCredentialsProvider.create(AWSProfile.NAME); 
        ComprehendClient cl = ComprehendClient.builder().credentialsProvider(cr).build();
        
        DetectSentimentRequest req = DetectSentimentRequest.builder().text(text).languageCode(LanguageCode.EN).build();
        DetectSentimentResponse resp = cl.detectSentiment(req);



        System.out.println(resp);

    }
}
