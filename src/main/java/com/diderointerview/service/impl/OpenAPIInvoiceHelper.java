package com.diderointerview.service.impl;

import com.diderointerview.model.InvoiceData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.theokanning.openai.completion.CompletionChoice;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.CompletionResult;
import com.theokanning.openai.service.OpenAiService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Optional;

@Slf4j
public class OpenAPIInvoiceHelper {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String OPENAI_API_PROMPT = """
            You will be provided with an invoice document delimited by new lines. Your task is to answer the question using only the provided document text
            Extract fields and values for "Date", "Seller Company Name",  "Total Amount", and "Invoice Number" from the given document text.
            Return result in JSON format. The document text is as follows:
            %s
            """;

    public Optional<InvoiceData> extractInvoiceData(String documentText) throws JsonProcessingException {
        OpenAiService service = new OpenAiService("<openai-api-key>");
        String prompt = String.format(OPENAI_API_PROMPT, documentText);
        log.debug("Prompt: {}", prompt);
        CompletionRequest completionRequest = CompletionRequest.builder()
                .prompt(prompt)
                .model("gpt-3.5-turbo-instruct")
                .maxTokens(2000)
                .temperature(0.2)
                .echo(false)
                .build();
        CompletionResult completionResult = service.createCompletion(completionRequest);
        List<CompletionChoice> completionChoiceList = completionResult.getChoices();
        InvoiceData invoiceData = null;
        if (CollectionUtils.isNotEmpty(completionChoiceList)) {
            CompletionChoice firstChoice = completionChoiceList.get(0);
            String choiceText = firstChoice.getText();
            invoiceData = objectMapper.readValue(choiceText, InvoiceData.class);
        }
        return Optional.ofNullable(invoiceData);
    }
}
