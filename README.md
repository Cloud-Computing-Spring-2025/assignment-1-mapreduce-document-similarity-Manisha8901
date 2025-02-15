[![Open in Visual Studio Code](https://classroom.github.com/assets/open-in-vscode-2e0aaae1b6195c2367325f4f02e2d04e9abb55f0b24a779b69b11b9e10269abc.svg)](https://classroom.github.com/online_ide?assignment_repo_id=18032704&assignment_repo_type=AssignmentRepo)
### **📌 Document Similarity Using Hadoop MapReduce**  

#### **Objective**  
The goal of this assignment is to compute the **Jaccard Similarity** between pairs of documents using **MapReduce in Hadoop**. You will implement a MapReduce job that:  
1. Extracts words from multiple text documents.  
2. Identifies which words appear in multiple documents.  
3. Computes the **Jaccard Similarity** between document pairs.  
4. Outputs document pairs with similarity **above 50%**.  

---

### **📥 Example Input**  

You will be given multiple text documents. Each document will contain several words. Your task is to compute the **Jaccard Similarity** between all pairs of documents based on the set of words they contain.  

#### **Example Documents**  

##### **doc1.txt**  
```
hadoop is a distributed system
```

##### **doc2.txt**  
```
hadoop is used for big data processing
```

##### **doc3.txt**  
```
big data is important for analysis
```

---

# 📏 Jaccard Similarity Calculator

## Overview

The Jaccard Similarity is a statistic used to gauge the similarity and diversity of sample sets. It is defined as the size of the intersection divided by the size of the union of two sets.

## Formula

The Jaccard Similarity between two sets A and B is calculated as:

```
Jaccard Similarity = |A ∩ B| / |A ∪ B|
```

Where:
- `|A ∩ B|` is the number of words common to both documents
- `|A ∪ B|` is the total number of unique words in both documents

## Example Calculation

Consider two documents:
 
**doc1.txt words**: `{hadoop, is, a, distributed, system}`
**doc2.txt words**: `{hadoop, is, used, for, big, data, processing}`

- Common words: `{hadoop, is}`
- Total unique words: `{hadoop, is, a, distributed, system, used, for, big, data, processing}`

Jaccard Similarity calculation:
```
|A ∩ B| = 2 (common words)
|A ∪ B| = 10 (total unique words)

Jaccard Similarity = 2/10 = 0.2 or 20%
```

## Use Cases

Jaccard Similarity is commonly used in:
- Document similarity detection
- Plagiarism checking
- Recommendation systems
- Clustering algorithms

## Implementation Notes

When computing similarity for multiple documents:
- Compare each document pair
- Output pairs with similarity > 50%

### **📤 Expected Output**  

The output should show the Jaccard Similarity between document pairs in the following format:  
```
(doc1, doc2) -> 60%  
(doc2, doc3) -> 50%  
```

---

### **🛠 Environment Setup: Running Hadoop in Docker**  

Since we are using **Docker Compose** to run a Hadoop cluster, follow these steps to set up your environment.  

#### **Step 1: Install Docker & Docker Compose**  
- **Windows**: Install **Docker Desktop** and enable WSL 2 backend.  
- **macOS/Linux**: Install Docker using the official guide: [Docker Installation](https://docs.docker.com/get-docker/)  

#### **Step 2: Start the Hadoop Cluster**  
Navigate to the project directory where `docker-compose.yml` is located and run:  
```sh
docker-compose up -d
```  
This will start the Hadoop NameNode, DataNode, and ResourceManager services.  

#### **Step 3: Access the Hadoop Container**  
Once the cluster is running, enter the **Hadoop master node** container:  
```sh
docker exec -it hadoop-master /bin/bash
```

---

### **📦 Building and Running the MapReduce Job with Maven**  

#### **Step 1: Build the JAR File**  
Ensure Maven is installed, then navigate to your project folder and run:  
```sh
mvn clean package
```  
This will generate a JAR file inside the `target` directory.  

#### **Step 2: Copy the JAR File to the Hadoop Container**  
Move the compiled JAR into the running Hadoop container:  
```sh
docker cp target/similarity.jar hadoop-master:/opt/hadoop-3.2.1/share/hadoop/mapreduce/similarity.jar
```

---

### **📂 Uploading Data to HDFS**  

#### **Step 1: Create an Input Directory in HDFS**  
Inside the Hadoop container, create the directory where input files will be stored:  
```sh
hdfs dfs -mkdir -p /input
```

#### **Step 2: Upload Dataset to HDFS**  
Copy your local dataset into the Hadoop cluster’s HDFS:  
```sh
hdfs dfs -put /path/to/local/input/* /input/
```

---

### **🚀 Running the MapReduce Job**  

Run the Hadoop job using the JAR file inside the container:  
```sh
hadoop jar similarity.jar DocumentSimilarityDriver /input /output_similarity /output_final
```

---

### **📊 Retrieving the Output**  

To view the results stored in HDFS:  
```sh
hdfs dfs -cat /output_final/part-r-00000
```

If you want to download the output to your local machine:  
```sh
hdfs dfs -get /output_final /path/to/local/output
```
---

### **Approach & Implementation**  
This project is designed to measure how similar different documents are using Hadoop MapReduce. The process follows these steps:

1. The Mapper (DocumentSimilarityMapper)
Reads each document, extracts its unique words, and outputs them along with the document ID.
Think of it as sorting through each document and making a list of all the words it contains—just without duplicates.
2. The Reducer (DocumentSimilarityReducer)
Gathers the words from each document and compares them with other documents.
It calculates Jaccard similarity, which is a fancy way of saying:
How many words do the two documents share? (Intersection)
How many words are in either document? (Union)
Similarity Score = Intersection / Union
Finally, it outputs document pairs along with their similarity scores.
3. The Driver (DocumentSimilarityDriver)
This is like the project manager—it sets everything up and makes sure the Mapper and Reducer run smoothly.
It tells Hadoop where to find the input files, which Java classes to use, and where to store the results.


### **How to Run the Project (Step-by-Step Guide)**  
1. Get the Code
git clone https://github.com/Cloud-Computing-Spring-2025/assignment-1-mapreduce-document-similarity-Manisha8901.git
cd assignment-1-mapreduce-document-similarity-Manisha8901
2. Build the Project
mvn clean package
This compiles the Java code and creates a .jar file, which is needed to run the job.

3. Start Hadoop Inside Docker
docker-compose up -d
This starts all the necessary Hadoop services in the background.

4. Move the Files into Hadoop
docker cp target/DocumentSimilarity-0.0.1-SNAPSHOT.jar resourcemanager:/opt/hadoop-3.2.1/share/hadoop/mapreduce/
docker cp Input/input resourcemanager:/opt/hadoop-3.2.1/share/hadoop/mapreduce/
This ensures the program and input data are available inside the Hadoop container.

5. Run the Hadoop Job
docker exec -it resourcemanager /bin/bash
hadoop fs -mkdir -p /input/dataset
hadoop fs -put /opt/hadoop-3.2.1/share/hadoop/mapreduce/input /input/dataset/
hadoop jar /opt/hadoop-3.2.1/share/hadoop/mapreduce/DocumentSimilarity-0.0.1-SNAPSHOT.jar \
com.example.controller.DocumentSimilarityDriver /input/dataset /output
This tells Hadoop to start processing the files using our DocumentSimilarityDriver.

6. See the Results
hadoop fs -cat /output/part-r-00000
hdfs dfs -get /output /opt/hadoop-3.2.1/share/hadoop/mapreduce/
docker cp resourcemanager:/opt/hadoop-3.2.1/share/hadoop/mapreduce/output/ output/
cat output/part-r-00000
These commands help fetch and display the final output, showing the similarity scores for each document pair.


### **Challenges We Faced & How We Solved Them**  
1. File Format Confusion
At first, the program couldn’t properly separate document IDs from their content.
Solution: Used a proper string split method to ensure the document ID and content were extracted correctly.
2. Files Not Found Errors
Hadoop kept complaining that the files didn’t exist.
Solution: Double-checked file paths and made sure the hadoop fs -put commands actually copied them correctly.
3. Incorrect Similarity Scores
The initial calculation didn’t correctly count words that were in both documents.
Solution: Used Java’s built-in set operations (retainAll() for intersection and addAll() for union) to ensure the similarity was computed correctly.






