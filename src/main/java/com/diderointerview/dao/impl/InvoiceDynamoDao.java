package com.diderointerview.dao.impl;

import com.diderointerview.dao.InvoiceDao;
import com.diderointerview.exception.PersistenceException;
import com.diderointerview.model.InvoiceData;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.net.URI;
import java.util.*;

@Slf4j
public class InvoiceDynamoDao implements InvoiceDao {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
            .endpointOverride(URI.create("http://localhost:8000"))
            // The region is meaningless for local DynamoDb but required for client builder validation
            .region(Region.US_EAST_1)
            .credentialsProvider(StaticCredentialsProvider.create(
            AwsBasicCredentials.create("fakeMyKeyId", "fakeSecretAccessKey")))
            .build();

    private static final String TABLE_NAME = "Invoice";

    @Override
    public void persistInvoices(List<InvoiceData> invoiceDataList) throws PersistenceException {
        Map<String, List<WriteRequest>> requestItemMap = new HashMap<>();
        List<WriteRequest> writeRequests = new ArrayList<>();
        try {
            for (InvoiceData invoiceData : invoiceDataList) {
                HashMap<String, AttributeValue> attributeMap = new HashMap<>();
                attributeMap.put("InvoiceNumber", AttributeValue.fromS(invoiceData.getInvoiceNumber()));
                attributeMap.put("Date", AttributeValue.fromS(invoiceData.getDate()));
                attributeMap.put("InvoiceData", AttributeValue.fromS(objectMapper.writeValueAsString(invoiceData)));

                PutRequest putRequest = PutRequest.builder()
                        .item(attributeMap)
                        .build();
                WriteRequest writeRequest = WriteRequest.builder()
                        .putRequest(putRequest)
                        .build();
                writeRequests.add(writeRequest);
            }
            requestItemMap.put(TABLE_NAME, writeRequests);
            BatchWriteItemRequest batchWriteItemRequest = BatchWriteItemRequest.builder()
                    .requestItems(requestItemMap)
                    .build();
            dynamoDbClient.batchWriteItem(batchWriteItemRequest);
        } catch (Exception exception) {
            String errorMessage = "Failed to persist invoices";
            log.error(errorMessage);
            throw new PersistenceException(errorMessage, exception);
        }
    }

    @Override
    public List<InvoiceData> fetchInvoiceData() throws PersistenceException {
        ScanRequest scanRequest = ScanRequest.builder()
                .tableName(TABLE_NAME)
                .limit(100).build(); // TODO: implement batching
        List<InvoiceData> invoiceDataList = new ArrayList<>();
        try {
            ScanResponse scanResponse = dynamoDbClient.scan(scanRequest);
            for (Map<String, AttributeValue> item : scanResponse.items()){
                String invoiceDataStr = item.get("InvoiceData").s();
                invoiceDataList.add(objectMapper.readValue(invoiceDataStr, InvoiceData.class));
            }
        } catch (Exception exception) {
            String errorMessage = "Failed to fetch invoices";
            log.error(errorMessage);
            throw new PersistenceException(errorMessage, exception);
        }

        return invoiceDataList;
    }
}
