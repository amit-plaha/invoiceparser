# Invoice Parser
This is a Java application for parsing invoices/purchase orders. Uses LLM based approach to extract, store, and display the Invoice number, seller name, date, and total amount of the invoice.  

## System Requirements
### JDK Version
Needs java jdk 21 to run. Here is the link to install the jdk: https://www.oracle.com/java/technologies/downloads/#jdk21-mac

### Gradle

The app uses Gradle version `8.5` to build the JAR.

### Dynamodb

#### Installation and Set up
The app uses dynamodb as its backend. Local dynamodb can be installed and started, instructions are provided here: https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/DynamoDBLocal.DownloadingAndRunning.html 

#### AWS CLI installation

Download and install the CLI with the following command:

````
curl "https://s3.amazonaws.com/aws-cli/awscli-bundle.zip" -o "awscli-bundle.zip"
unzip awscli-bundle.zip
sudo ./awscli-bundle/install -i /usr/local/aws -b /usr/local/bin/aws
````

#### Creating the invoice table, please use the following command on your local:

````
aws dynamodb create-table \
    --table-name Invoice \
    --attribute-definitions \
        AttributeName=InvoiceNumber,AttributeType=S \
        AttributeName=Date,AttributeType=S \
    --key-schema \
        AttributeName=InvoiceNumber,KeyType=HASH \
        AttributeName=Date,KeyType=RANGE \
    --provisioned-throughput \
        ReadCapacityUnits=5,WriteCapacityUnits=5 \
    --table-class STANDARD --endpoint-url http://localhost:8000
````

### How to run the app?

1. Download the code to your local
2. Replace <openai-api-key> in with your key in the class `OpenAPIInvoiceHelper.class`
3. Replace dynamo creds in `InvoiceDynamoDao.class`
4. Once you have the code on your local, run the following to package the JAR:

````
./gradlew jar
````
5. Run the main class from the JAR in the build folder:

````
java -jar invoiceparser-1.0-SNAPSHOT.jar "<absolute-path-to-invoice-directory-on-local>"
````

### Possible Improvements for Code

1. Leverage spring boot framework for dependency management
2. Leverage spring boot for config management: URLs, keys, directories, etc.
3. InvoiceController can be an API instead
4. Use mocking framework for unit tests 
5. Implement batching for fetching for dynamo records if the scale increases. Currently, it only returns 100 rows.

### Possible Quality Improvements

1. The attributes right now are fetched as String and stored as string in dynamo. For instance, the amount is a string and the logic doesn't handle different currencies. Need to understand more use cases around the data to define a more effective schema. 
2. Unit tests could use edges cases to try out different paths and types of invoices. 
3. There are two known issues: (1) In the example set, for one of the invoices, it extracted customer as the seller; (2) In one instance, the amount was picked up in words not numbers. I am sure with a bit more time spent on prompt engineering these issues can be easily fixed.