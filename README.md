# apriori
Java implementation of Apriori algorithm

In data mining, Apriori is a classic algorithm for learning association rules.

It is used for mining frequent itemsets and relevant association rules. It is devised to operate on a database containing a lot of transactions, for instance, items brought by customers in a store.

### Finding Frequent Item Sets
The search for frequent item sets aims at finding items, which often occur collectively in a transaction. 

* Support metric - Support (BANANA -> EGGS) =  Number of transactions containing both / Number of all transactions
* Confidence metric - Confidence (BANANA -> EGGS) = Number of transactions containing both / Number of transactions containing BANANA




| TransactionId | Applications  |
| ------------- | ------------- |
| 123           | APPLE, CUCUMBER, DATES  |
| 456  | BANANA, CUCUMBER, EGGS  |
| 789  | APPLE, BANANA, CUCUMBER, EGGS  |
| 911  |  BANANA, EGGS  |

The most frequent items are: BANANA, CUCUMBER, EGGS

Frequency is calculated base on threshold which by default is 60% of all applications. (For above example - 2).

### What is the advantage of this library? 

Main advantage of library is that it is extremely lightweight and simple for usage.
* No need for external libraries.
* No need for deep machine learning knowedge.
* No need for complex wrappers and castings between user data and algorithm objects.


### Usage

Users have to map their custom Objects to Apriori's Iteam & Application Objects. 
Let's see the following example: 
Custom user's class:
```java 
Unit {
  String value = "value";
  ...
}
```
Where "value" field is unique identifier (it will be used for classification) for the Unit.
Let's assume that user stores in database applications (collections) of Units (where application is logical group of Units consolidated by transaction). In order to calculate Apriori needs the following mapping:
```java 
Item item = new Item(Unit.value); 
or
Item item = new Item(Unit.value, Unit); 
```
Then translate user's applications of Units to applications of Items.
```java 
Application application = new Application(new ArrayList<Item>(Arrays.asList(item));
...
// Transate every application then store them in list
List<Application> apps = new ArrayList<>();
apps.add(application);
...
```
Once we have a list of Applications Apriori is ready to work.
```java 
 Apriori apriori = new Apriori(apps);
 // Most common items
Set<Item> recomendedItems = apriori.recommend();
```
