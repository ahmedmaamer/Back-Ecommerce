package com.example.iotDataGenerator.controller;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.example.iotDataGenerator.entities.LogEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RestController
public class LogController {

    private static final int PAGE_SIZE = 10; // Number of log entries per page

    @GetMapping("/api/logs")
    public Page<LogEntry> getLogs(@RequestParam(defaultValue = "0") int page) {
        List<LogEntry> logEntries = new ArrayList<>();
        try {
            String logContent = Files.readString(Paths.get("logs/application.log"));
            String[] lines = logContent.split("\\r?\\n");
            for (String line : lines) {
                LogEntry logEntry = parseLogLine(line);
                if (logEntry != null) {
                    logEntries.add(logEntry);
                }
            }
        } catch (IOException e) {
            // Handle IOException
            e.printStackTrace();
        }

        // Create a pageable object
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);

        // Create a page object from the list of log entries
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), logEntries.size());
        Page<LogEntry> logPage = new org.springframework.data.domain.PageImpl<>(logEntries.subList(start, end), pageable, logEntries.size());

        return logPage;
    }

    private LogEntry parseLogLine(String line) {
        // Define your log pattern here
        String pattern = "(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3}) \\[(.*?)\\] ([A-Z]+) (.*$)";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(line);
        if (m.find()) {
            String dateTimeStr = m.group(1);
            String thread = m.group(2);
            String level = m.group(3);
            String message = m.group(4);

            // Parse date and time
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
            LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, formatter);

            return new LogEntry(dateTime, thread, level, message);
        }
        return null; // Return null if line doesn't match pattern
    }
}
