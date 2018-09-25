# PaytmWebLogChallenge

## Stack and Tech Used 
   1. ELK Stack (Elastic Logstash Kibana) for Ingestion of log file, indexing and visualisation of data
   2. FileBeat to read log and watch log file

## Run project
 ### Get data from [PaytmWeblogChallenge](https://github.com/PaytmLabs/WeblogChallenge) and Place it in the data folder 

 ### Start ETL Pipeline 

```bash 
elasticsearch
sudo service mongod start
logstash -f logstash/logstash.conf
filebeat -e -c filebeat/filebeat.conf
```


## Fields Extracted from a single log line

*Input* :   **2015-07-22T12:00:27.539654Z marketpalce-shop 1.39.97.17:42956 10.0.6.99:80 0.000047 0.009735 0.000029 200 200 0 211 "GET https://paytm.com:443/shop/cart HTTP/1.1" "Mozilla/5.0 (Linux; U; Android 4.2.1; en-US; Q700 Build/XOLOQ700) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 UCBrowser/10.5.2.582 U3/0.8.0 Mobile Safari/534.30" ECDHE-RSA-AES128-SHA TLSv1**

```json
{  
   "ssl_cipher":"ECDHE-RSA-AES128-SHA",
   "domain":"paytm.com",
   "geoip":{  
      "ip":"1.39.97.17",
      "longitude":72.8258,
      "city_name":"Mumbai",
      "timezone":"Asia/Kolkata",
      "latitude":18.975,
      "location":{  
         "lat":18.975,
         "lon":72.8258
      },
      "continent_code":"AS",
      "country_code3":"IN",
      "country_code2":"IN",
      "country_name":"India",
      "region_code":"MH",
      "region_name":"Maharashtra"
   },
   "alb-name":"marketpalce-shop",
   "request_uri":"/shop/cart",
   "user_agent_exists":true,
   "target":"10.0.6.99",
   "target_processing_time":0.009735,
   "user_agent":"Mozilla/5.0 (Linux; U; Android 4.2.1; en-US; Q700 Build/XOLOQ700) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 UCBrowser/10.5.2.582 U3/0.8.0 Mobile Safari/534.30",
   "received_bytes":0,
   "http_version":"HTTP/1.1",
   "target_port":80,
   "http_method":"GET",
   "protocol":"https",
   "target_status_code":200,
   "source":"/home/hasali/personal/PaytmWebLogChallenge/data/test.log",
   "http_uri":"https://paytm.com:443/shop/cart",
   "@timestamp":"2015-07-22T12:00:27.539Z",
   "http_port":443,
   "client_port":42956,
   "response_processing_time":2.9e-05,
   "request_processing_time":4.7e-05,
   "agent_details":{  
      "device":"Xolo Q700",
      "patch":"2",
      "os_major":"4",
      "os":"Android",
      "os_name":"Android",
      "build":"",
      "os_minor":"2",
      "name":"UC Browser",
      "major":"10",
      "minor":"5"
   },
   "client":"1.39.97.17",
   "log_timestamp" : "2015-07-22T09:00:28.019143Z",
   "ssl_protocol":"TLSv1",
   "sent_bytes":211,
   "elb_status_code":200
}
```