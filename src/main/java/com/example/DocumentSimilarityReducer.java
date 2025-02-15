package com.example;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.*;

public class DocumentSimilarityReducer extends Reducer<Text, Text, Text, Text> {

    private final Map<String, Set<String>> docWordMap = new HashMap<>();

    @Override
    public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        // Store the unique words for each document in a map
        for (Text value : values) {
            Set<String> uniqueWords = new HashSet<>(Arrays.asList(value.toString().split(",")));
            docWordMap.put(key.toString(), uniqueWords);
        }
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        List<String> docIds = new ArrayList<>(docWordMap.keySet());

        // Iterate over all document pairs to compute Jaccard Similarity
        for (int i = 0; i < docIds.size(); i++) {
            for (int j = i + 1; j < docIds.size(); j++) {
                String firstDoc = docIds.get(i);
                String secondDoc = docIds.get(j);

                Set<String> wordsInFirst = docWordMap.get(firstDoc);
                Set<String> wordsInSecond = docWordMap.get(secondDoc);

                // Compute intersection and union of word sets
                Set<String> commonWords = new HashSet<>(wordsInFirst);
                commonWords.retainAll(wordsInSecond);

                Set<String> allWords = new HashSet<>(wordsInFirst);
                allWords.addAll(wordsInSecond);

                if (allWords.isEmpty()) continue; // Avoid division by zero

                // Compute Jaccard similarity
                double jaccardIndex = (double) commonWords.size() / allWords.size();
                String similarityScore = String.format("Similarity: %.2f", jaccardIndex);

                // Output the document pair with their similarity score
                context.write(new Text(firstDoc + ", " + secondDoc), new Text(similarityScore));
            }
        }
    }
}
