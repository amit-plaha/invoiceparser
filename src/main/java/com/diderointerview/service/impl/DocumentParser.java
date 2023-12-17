package com.diderointerview.service.impl;

import java.util.List;

public interface DocumentParser {

    List<String> extractTextForDocuments(String documentFolderPath);
}
