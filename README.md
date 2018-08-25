# PaytmWebLogChallenge

## Stack and Tech Used 
   1. ELK Stack (Elastic Logstash Kibana) for Ingestion of log file, indexing and visualisation of data
   2. FileBeat to read log and watch log file

## Run project
 ### Get data from [PaytmWeblogChallenge](https://github.com/PaytmLabs/WeblogChallenge) and Place it in the data folder 

 ### Start ETL Pipeline 

```bash 
logstash -f logstash/logstash.conf
filebeat -e -c filebeat/filebeat.conf
```
