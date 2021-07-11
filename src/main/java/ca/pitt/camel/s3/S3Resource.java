package ca.pitt.camel.s3;

import java.net.URI;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.aws2.s3.AWS2S3Constants;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.support.DefaultRegistry;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import software.amazon.awssdk.auth.credentials.SystemPropertyCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;

public class S3Resource extends RouteBuilder {

    //@ConfigProperty(name = "aws.s3.url")
    String s3Url = "https://s3-openshift-storage.apps.cluster-35b8.35b8.sandbox1895.opentlc.com";

    @ConfigProperty(name = "aws.bucketName", defaultValue = "none")
    String bucketName;

    @Override
    public void configure() throws Exception {

        System.err.println("Bucket Name: " + bucketName);

        S3Client s3Client = S3Client.builder()
            .credentialsProvider(SystemPropertyCredentialsProvider.create())
            .endpointOverride(new URI(s3Url))
            .region(Region.CA_CENTRAL_1)
            .serviceConfiguration(S3Configuration.builder()
            .pathStyleAccessEnabled(Boolean.TRUE).build())
            .build();

        DefaultRegistry registry = (DefaultRegistry) getContext().getRegistry();
        registry.bind("noobaaClient", s3Client);

        restConfiguration()
            .bindingMode(RestBindingMode.json);

        rest("/")
            .post()
            .to("direct:dosomething");

        from("direct:dosomething")
            .log("Body: ${body}")
            .setHeader(AWS2S3Constants.KEY, constant("test.txt"))
            .setBody(constant("Test create s3 resource."))
            .to("aws2-s3://s3-camel?amazonS3Client=#noobaaClient");

        rest("/")
            .get()
            .to("direct:getobjects");

        from("direct:getobjects")
            .to("aws2-s3://s3-camel?amazonS3Client=#noobaaClient&prefix=blah.txt")
            .to("file:/tmp/files");
 
    }

}
