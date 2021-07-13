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

/**
 * S3ClientFactory
 * 
 * Creates a custom <code>S3Client</code> instance configured to work with NooBaa - the S3 component
 * of OpenShift Container Storage.
 */
public class S3ClientFactory {
    
    // S3 endpoint.
    @Inject
    @ConfigProperty(name = "aws.s3.url")
    String s3Url;

    // Create a new S3Client named noobaaClient.
    @Produces
    @ApplicationScoped
    @Named("noobaaClient")
    public S3Client newS3Client() throws URISyntaxException {
        S3Client s3Client = S3Client.builder()
            .credentialsProvider(SystemPropertyCredentialsProvider.create())
            .endpointOverride(new URI(s3Url))
            .region(Region.CA_CENTRAL_1)
            .serviceConfiguration(S3Configuration.builder()
                .pathStyleAccessEnabled(Boolean.TRUE)
                .build())
            .build();
        return s3Client;
    }

}
