# currency-conversion
A command line tool that converts values from an input file into a different currency using Java and fixer.io

## Prerequisites
- JDK 1.8.0 and JRE
- Maven

## Execution  Steps
First you need to compile the project by executing
```shell script
mvn install
```
This will create a target folder containing a .jar file called `currency-conversion-1.0-SNAPSHOT-jar-with-dependencies.jar`.

In order to run this file you now have to create a file called ``apikey.properties`` in which you have to specify your Fixer API key just as below (without the brackets.)
````text
FIXER_KEY = <YOUR_KEY>
````

Now you need to place the ``apikey.properties`` together with the previously created JAR and your input file, which contains one amount in EUR per row, in the same folder.

You now should be able to execute the JAR file with following command:
````shell script
java -jar currency-conversion-1.0-SNAPSHOT-jar-with-dependencies.jar input.txt
````

## Design Decisions
### Assumptions
The requirement asks to always use the latest conversion rate. Fixer.io has a limiated to API calls, which is why i decided to add a boolean flag ``fixedExchangeRate`` to the ``convertValues`` method. If this flag is set to ``true``, the exchange rate stays the same for the whole chunk. If this flag is set to ``false``, the exchange rate updates for every value. For future upgrades it would be possible to add another command line argument, stating wether or not to update the exchange rate for each value by controlling the previousonly mentioned boolean flag.
### Security
The usage of Fixer.io requires a private key. In order to keep this key outside my VCS i decided to store it in a seperate file ``apikey.properties`` and exclude this file from git by adding it to the ``.gitignore``. A private method would then read this key when the FixerClient is being intantiated. However, this appraoch still requires the key to be stored as readable plaintext, even though it is only stored in the local repository. A future upgrade might be to encrypt the key, ie. buy using BCrypt. 
### Maintainability
In order to keep the code maintainable i tried to focus on satisfying the single-responsibility and open-closed principles as good as possible. Therefor classes and methods should have excatly one purpose, the code itself should be easy to extend without the need of modyfying the already existing code. 
In the current implementation following possible extensions were considered:
- The usage of a different base and target currency:
The free Fixer.io API key has a restriction to EUR as base currency. However, the ``FixerClient`` was implemented in a way, which would make it possible to request other base and target currencys (given that the KEY is not restricted withing the free plan) by using a custom constructor `` FixerClient(String baseCurrency, String targetCurrency)`` instead of the default constructor. However, a minor modification within the main method of the ``CurrencyConverter`` class is still possible, in order to allow passing the new base and target currencies as command line arguments.
- Compatibility with different file formats
currency-converter currently supports text files with and without a .txt extension and .csv files containing only lines with the values and no delimiters. In order to allow future extensions to other file formats, the ``FileManager`` class contains a method ``getFileExtension`` which returns the extension from any given file. The FileManager could simply be extended by other methods which handle reading and writing. The usage of the ``getFileExtension`` method would then allow us to figure out the input file type in order to chose which method should be used for reading and writing.
- Compatibility with a different API
The ``FixerClient`` class does not depend on any other class that is limited to the project but is invoked 3 times from within the ``CurrencyConverter`` class. If one would want to replace this API by another, it would be sufficient to change 3 lines of code (excluding the lines needed to create the client for the new API). 
- Possible performance improvements by splitting the input list into smaller chunks
After reading the input values from the input file and storing them in an ArrayList, the whole list is passed to a function that requests the exchange rate and iterates through each value in this list and passes it to another function that then calculated the new value. The idea behind having a function that takes an List of values is, that we could split the whole input list into smaller chunks, and invoke the conversion function in parallel. 
### Scalability and Performance
As indirectly mentioned above, the current implication has a linear runtime. One performance improvement was already mentioned above, however i don't consider local parallelization as scaling improvement. Also, the memory usage is currently limited to the size of the heap available to the JVM. In order to avoid running out of available memory, instead of reading the whole file at once, one could read the file chunk by chunk. However, this performance upgrade was not considered within the maintainability of the code, because the current implementation can handle more than 10 million input lines. As opposite to the memory, a scaleable runtime upgrade would definitely be more interesting. My approach on doing so would be to implement a serverless function (ie. AWS Lambda) that takes a list of input values and converts them with a certain exchange rate. One could then invoke this function in parallel. However, it is disputable whether or not this would lead to a real performance upgrade since the time needed to push and pull the in- and output data might take longer than doing the calculation on a local machine up to a certain amount of values.
### External Libraries
The project uses gson because there is no elegant way to parse JSON data, which we get from the API request, in Java without the use of external libraries.
````XML
<dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
    <version>2.8.6</version>
</dependency>
````
