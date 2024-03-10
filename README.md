# SC3020: Database System Principles - Group 1

## Project 1: B+ Tree Indexing of IMDb Movies Rating & Votes

This project focuses on implementing a relational database at a basic level. Initially, we implemented a disk storage
system to simulate how data is stored on a disk. Following that, we introduced a B+ tree for data indexing to enhance
the efficiency of data search and retrieval. You can access our comprehensive Javadocs [here](https://kristianachwan.github.io/sc3020-project1/).

#### Team members:

- Clayton Fernalo
- Nema Aarushi
- Kristian Hadinata Achwan
- Lau Yong Jie

## Installation Guides

It is recommended that IntelliJ IDEA be used to get this project up and running since the development is mainly done
there. Regardless, the project should be able to be run everywhere. In this guide, we will focus on the steps required
based on IntelliJ IDEA. We recommend you to use the community edition, which can be
found [here](https://www.jetbrains.com/idea/download/other.html):

To start, you can clone or download the repository this repository. After that, you can follow the steps to run the
project.

1. Ensure that you have installed the Java Development Kit on your computer. In case you do not have it, you can
   download it from [here](https://www.oracle.com/sg/java/technologies/downloads/). It is recommended to use the version
   that we use for development, which is JDK 21.
2. After that, open your IntelliJ IDEA and go to file > open then choose this project’s folder as the project that you
   want to open.
3. The main function that will be run is in the src > main > java > Main.java. Open that file and you can click the play
   button in the IDE
4. The project is successfully run! You can see the output of the project that reports our experiment results in the
   console.

Alternatively, you can use the pre-compiled .jar file in `jar/sc3020-project1.jar` to run this program instead from the
project root (`sc3020-project1`).

To run the .jar file (requires JDK 21), run the following command from the project root:

```bash
java -jar jar/sc3020-project1.jar
```

## Tech Stack

- JDK 20
- Gradle
