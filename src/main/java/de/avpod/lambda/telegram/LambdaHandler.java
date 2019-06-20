package de.avpod.lambda.telegram;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.event.S3EventNotification;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * Created by apodznoev
 * date 20.06.2019.
 */
public class LambdaHandler implements RequestHandler<S3EventNotification, Void> {

    private static final Logger logger = LogManager.getLogger(LambdaHandler.class);

    @Override
    public Void handleRequest(S3EventNotification event, Context context) {
        logger.info("Got invocation {}", event);
        AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();

        for (S3EventNotification.S3EventNotificationRecord record : event.getRecords()) {
            String s3Key = record.getS3().getObject().getKey();
            String s3Bucket = record.getS3().getBucket().getName();
            logger.info("found id: " + s3Bucket + " " + s3Key);
            // retrieve s3 object
            S3Object object = s3.getObject(new GetObjectRequest(s3Bucket, s3Key));
            InputStream objectData = object.getObjectContent();
            try {
                String s3Content = IOUtils.toString(objectData, Charset.forName("UTF-8"));
                logger.info("Got s3 file content {}", s3Content);

                PollyDemo helloWorld = new PollyDemo();
                InputStream speechStream = helloWorld.synthesize(s3Content);
                MyAmazingBot amazingBot = new MyAmazingBot();
                amazingBot.execute(new SendAudio(
                ).setChatId(System.getenv("TELEGRAM_CHAT_ID"))
                        .setAudio(s3Key, speechStream));
            } catch (IOException | TelegramApiException e) {
                logger.error(e);
            }
        }

        return null;
    }

}
