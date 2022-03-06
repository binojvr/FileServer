## Simple File Storage client  


* Tested only in windows 10

**Prerequisites**

1. Java - 1.8.x

2. Maven - 3.x.x


**Run the app using maven**


Ppackage the application in the form of a jar and then run the jar file as

```
mvn clean package

```

```
run

```

Now run the client command as 

```
fs-store -h

```

1. fs-store upload-file <file>

2. fs-store delete-file <file>

3. fs-store list-files

**3. Future Enhancements**

1. Not written Test cases because of time constraints. Unit test and integration test will be implemented

2. Linux support (ubuntu or redhat)

**4. Special Note**


Unfortunately due to new year and arrival of my family I could not spend much on weekends this assignment. I have spent 6-7 hrs for client project  and 5 hours on server project. I have spent more time on client project because this is first time I workng client command line tool.

Following items I am still working and will be published by end of next week (I am working on weekends for thsi assignment) 

- Proving correctness of the code (and ensuring future code changes don’t break CLI
/ Server interaction)
- CLI design, and ease of use
- Project tooling and reproducible builds (what tooling or checks would you need in
place in an OSS project, for example)
- Installation (how would you distribute the software to a user)


