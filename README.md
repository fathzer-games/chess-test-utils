# chess-test-utils
A test framework for chess libraries

Testing is a nightmare... The purpose of this library is to make it easier to write tests for chess libraries.

It contains a set of abstract [JUnit5](https://junit.org/junit5) test classes to test various functionalities of chess libraries ([move generator](https://www.chessprogramming.org/Move_Generation), [FEN parser](https://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation), [Chess960](https://en.wikipedia.org/wiki/Chess960) compliance, etc...).
To use it, just add the following dependency in your project:

```xml
<dependency>
    <groupId>com.fathzer</groupId>
    <artifactId>chess-test-utils</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <scope>test</scope>
</dependency>
```

 Then create subclasses of the provided abstract classes in your `src/test/java` directory, and implement the abstract methods.
 
 You're now ready to run your tests!
