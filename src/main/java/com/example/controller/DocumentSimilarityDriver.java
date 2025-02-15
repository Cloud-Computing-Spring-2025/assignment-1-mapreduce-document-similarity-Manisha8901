package com.example.controller;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import com.example.DocumentSimilarityMapper;
import com.example.DocumentSimilarityReducer;

public class DocumentSimilarityDriver {
    public static void main(String[] args) throws Exception {
        // Validate input arguments
        if (args.length != 2) {
            System.err.println("Usage: DocumentSimilarityDriver <input path> <output path>");
            System.exit(1);
        }

        // Set up Hadoop job configuration
        Configuration config = new Configuration();
        Job job = Job.getInstance(config, "Document Similarity Analysis");

        // Define job parameters
        job.setJarByClass(DocumentSimilarityDriver.class);
        job.setMapperClass(DocumentSimilarityMapper.class);
        job.setReducerClass(DocumentSimilarityReducer.class);

        // Specify output types
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        // Define input and output paths
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        // Execute the job and exit based on success or failure
        boolean success = job.waitForCompletion(true);
        System.exit(success ? 0 : 1);
    }
}
