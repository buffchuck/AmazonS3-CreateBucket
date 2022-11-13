import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.GetBucketLocationRequest;

public class CreateBucket {

    public static void main(String[] args) throws AmazonServiceException {

        String clientRegion = "us-east-1";
        String bucketName = "com-rob-epps-arnold-test-bucket-3";
        // For anyone reading this I cannot stress enough how important it is
        // to make sure that your bucket name is unique. When creating my first
        // bucket I thought "there is no way anyone has this bucket name" but
        // I got so many errors that seemed to do with authorization or
        // credentials that I went down a rabbit hole of trying to figure out
        // access denied errors and malformed headers when all I had to do was
        // rename my bucket to something super unique. I ended up using my
        // domain name and all of a sudden the errors went away.

        try {
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new ProfileCredentialsProvider())
                    .withRegion(clientRegion).build();

            if (!s3Client.doesBucketExistV2(bucketName)) {
                // Because the CreateBucketRequest object doesn't specify a region, the
                // bucket is created in the region specified by the client.
                System.out.println("creating bucket....");
                s3Client.createBucket(new CreateBucketRequest(bucketName));
                System.out.println("bucket created....");
                // Verify that the bucket was created by retrieving it and checking its location

                String bucketLocation = s3Client.getBucketLocation(new GetBucketLocationRequest(bucketName));
                System.out.println("Bucket location: " + bucketLocation);
            } else {
                System.out.println("I guess the bucket exist in " + s3Client.getBucketLocation(bucketName) + " region.");
            }
        } catch(AmazonServiceException ase) {
            // The call was transmitted successfully, but Amazon S3 could not process
            // it and returned an error response.

            ase.printStackTrace();
        } catch (SdkClientException sce) {
            // Amazon S3 could not be contacted for a response, or the client could not
            // parse the response from Amazon S3.

            sce.printStackTrace();
        }
    }
}

