package ca.pitt.camel.s3;


import java.net.URI;
import java.net.URISyntaxException;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.auth.credentials.SystemPropertyCredentialsProvider;
import software.amazon.awssdk.services.s3.S3Configuration;

public class S3ClientFactory {
    
    @Inject
    @ConfigProperty(name = "aws.s3.url")
    String s3Url;

    @Produces
    @ApplicationScoped
    @Named("noobaaClient")
    public S3Client newS3Client() throws URISyntaxException {
        S3Client s3Client = S3Client.builder()
            .credentialsProvider(SystemPropertyCredentialsProvider.create())
            .endpointOverride(new URI(s3Url))
            .region(Region.CA_CENTRAL_1)
            .serviceConfiguration(S3Configuration.builder()
                .pathStyleAccessEnabled(Boolean.TRUE).build())
            .build();
        return s3Client;
    }

}
