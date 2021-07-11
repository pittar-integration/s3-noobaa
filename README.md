# Camel Quarkus - S3/NooBaa Integration

## Setup

1. Create a new `BucketClaim`.
2. Find the credentials and bucket name from the `Secret` and `ConfigMap` created when the `BucketClaim` is created.
3. Find your S3 URL: `oc get route -n openshift-storage s3`

## Run

```
mvn compile quarkus:dev \
    -Daws.accessKeyId="lok64Ks4G6sDOFQRe5ly" \
    -Daws.secretAccessKey="YqOtzZcWlfFJhbg8qvwqCD3y9UXBPw9RkR2faqy3"
```
