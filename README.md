# kinesis-ner

[![CircleCI](https://circleci.com/gh/chyikwei/kinesis-ner.svg?style=svg)](https://circleci.com/gh/chyikwei/kinesis-ner)

This is a exmaple to build a Named Entity Recognizer(NER) pipeline that:

1. fetch data from Amazon Kinesis stream
2. process text fields by Stanford CoreNLP and extract entities
3. store result to dynamoDB table

Getting started:
----------------

1. To build, run:
```
mvn package
```

2. Maven will generate a zip file `kinesis-ner-{version}-SNAPSHOT-package.zip` in the `target/` folder. The zip file contains all dependencies except Stanford NLP model jar since it is too large.

(Note: To include the NLP models in package, you can remove the `provided` scope for coreNLP models in [pom.xml](https://github.com/chyikwei/kinesis-ner/blob/master/pom.xml#L49) before you run `mvn package`. Then you can skip step 3.)

3. Downlaod [CoreNLP model](https://stanfordnlp.github.io/CoreNLP/download.html).

4. Unzip the package in step 2.

4. Make sure your machine have permission to create/read/write Kinesis streams and DynanoDB tables.

5. Make sure all jars are in your `classpath` and run:
```
java -Xmx1536m -cp {class_path} com.chyikwei.app.KinesisNerApplication
```
(Note: the process will use ~1GB ram)

6. Put some data into the stream. the sample format is json with `uuid`, `title`, `text` fields. Example:
```json
{
  "uuid": "04947df8-0e9e-4471-a2f9-9af509fb5801",
  "title": "news title",
  "text": "news text"
}
```

7. Check entities extracted from coreNLP. they will be stored in DynamoDB's `ddb-news-entities` table.

8. clean up AWS resources (kinesis stream, dynamoDB tables) after test. (The settings for stream & table names are [here](https://github.com/chyikwei/kinesis-ner/blob/master/src/main/java/com/chyikwei/app/KinesisNerApplication.java#L31-L36)) 
