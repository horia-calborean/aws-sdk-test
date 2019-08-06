package ro.calborean.test.aws.ml.comprehend;


import ro.calborean.test.aws.AWSProfile;
import java.util.List;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.services.comprehend.ComprehendClient;
import software.amazon.awssdk.services.comprehend.model.DetectDominantLanguageRequest;
import software.amazon.awssdk.services.comprehend.model.DetectDominantLanguageResponse;
import software.amazon.awssdk.services.comprehend.model.DominantLanguage;

public class LanguageDetector {
    public static void main(String[] args) {
        String text = "It is raining today in Seattle";
        
        AwsCredentialsProvider cr = ProfileCredentialsProvider.create(AWSProfile.NAME); 
        ComprehendClient cl = ComprehendClient.builder().credentialsProvider(cr).build();
        
        DetectDominantLanguageRequest req = DetectDominantLanguageRequest.builder().text(text).build();
        DetectDominantLanguageResponse resp = cl.detectDominantLanguage(req);

        List<DominantLanguage> languages = resp.languages();
        languages.forEach((language) -> {
            System.out.println(language.languageCode() + " with score:" + language.score());
        });
    }
}
