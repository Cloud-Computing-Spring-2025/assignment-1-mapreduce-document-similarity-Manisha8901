package com.example;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

public class DocumentSimilarityMapper extends Mapper<Object, Text, Text, Text> {

    @Override
    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        // Convert input text to a string and remove extra spaces
        String lineContent = value.toString().trim();
        String[] segments = lineContent.split("\\s+", 2); // Split into DocumentID and its text content

        if (segments.length < 2) {
            return; // Skip if the line does not contain enough data
        }

        String docId = segments[0];
        String docText = segments[1];

        Set<String> uniqueWords = new HashSet<>();
        StringTokenizer wordTokenizer = new StringTokenizer(docText);

        while (wordTokenizer.hasMoreTokens()) {
            uniqueWords.add(wordTokenizer.nextToken().toLowerCase());
        }

        // Emit document ID along with its extracted unique words
        context.write(new Text(docId), new Text(String.join(",", uniqueWords)));
    }
}
