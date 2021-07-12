package ca.pitt.camel.s3;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
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
            .setHeader(AWS2S3Constants.KEY, constant("test3.txt"))
            .convertBodyTo(String.class)
            //.setBody(constant("${body}"))
            .to("aws2-s3://{{aws.bucketName}}?amazonS3Client=#noobaaClient");

        rest("/")
            .get()
            .to("direct:getobjects");

        from("direct:getobjects").process(new Processor() {
                @Override
                public void process(Exchange exchange) throws Exception {
                    exchange.getIn().setHeader(AWS2S3Constants.KEY, "test3.txt");
                }
            })
            .to("aws2-s3://s3-camel?amazonS3Client=#noobaaClient&deleteAfterRead=false&operation=getObject")
            .convertBodyTo(String.class)
            .to("file:///tmp/files/?fileName=${header.CamelAwsS3Key}");

    }

}
