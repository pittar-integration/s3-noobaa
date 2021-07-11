package ca.pitt.camel.s3;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.aws2.s3.AWS2S3Constants;
import org.apache.camel.model.rest.RestBindingMode;

public class S3Resource extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        restConfiguration()
            .bindingMode(RestBindingMode.json);

        rest("/")
            .post()
            .to("direct:dosomething");

        from("direct:dosomething")
            .log("Body: ${body}")
            .setHeader(AWS2S3Constants.KEY, constant("abc.txt"))
            .setBody(constant("Andrew Test create s3 resource."))
            .to("aws2-s3://{{aws.bucketName}}?amazonS3Client=#noobaaClient");

        rest("/")
            .get()
            .to("direct:getobjects");

        from("direct:getobjects")
            .to("aws2-s3://{{aws.bucketName}}?amazonS3Client=#noobaaClient&prefix=blah.txt")
            .to("file:/tmp/files");
 
    }

}
