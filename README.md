### Problem statement
The requirement from the pipeline is to sessionize the user events and assign session ids to each event. In addition to that the pipeline also keeps tract of the most recent non-direct channel (marketing channel) of a user.


### Overview
This repo demonstrates how to implement a simple stream processing pipeline with flink. This covers the usage of :
* Source function
* watermark assigner
* Session windows
* process function
* Allowed lateness and side outputs
* Effect of continuous event time trigger
* state management

The purpose of this repo is to gain better understanding of the features provided by flink, by experimenting on them using a small dataset.

Some of the interesting things to observe
 1. how late arriving data influences final output and window triggers
 2. when do certain events end up in side output. You may be surprised with the results :). ( Try changing timestamp of e12 to 8L)
 3. effect of continuous trigger on the number of  output events
   
### setup 

You will need sbt to compile and run this application. 

Once you install sbt, just run

` sbt run`

to run the application.


