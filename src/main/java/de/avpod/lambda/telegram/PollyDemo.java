package de.avpod.lambda.telegram;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.polly.AmazonPollyClient;
import com.amazonaws.services.polly.model.*;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by apodznoev
 * date 20.06.2019.
 */
public class PollyDemo {

    private final AmazonPollyClient polly;
    private final Voice voice;

    public PollyDemo() {
        polly = new AmazonPollyClient(new DefaultAWSCredentialsProviderChain(),
                new ClientConfiguration());
        // Create describe voices request.
        DescribeVoicesRequest describeVoicesRequest = new DescribeVoicesRequest();
        describeVoicesRequest.setLanguageCode(LanguageCode.DeDE);

        // Synchronously ask Amazon Polly to describe available TTS voices.
        DescribeVoicesResult describeVoicesResult = polly.describeVoices(describeVoicesRequest);
        voice = describeVoicesResult.getVoices().get(0);
    }

    public InputStream synthesize(String text) throws IOException {
        SynthesizeSpeechRequest synthReq =
                new SynthesizeSpeechRequest()
                        .withText(text)
                        .withVoiceId(voice.getId())
                        .withOutputFormat(OutputFormat.Mp3);
        SynthesizeSpeechResult synthRes = polly.synthesizeSpeech(synthReq);

        return synthRes.getAudioStream();
    }

}
