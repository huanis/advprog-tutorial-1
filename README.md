# Reflection
## Exercise 1
I noticed that when a user creates a new product, the product is not assigned an ID (a unique value, different for every entity of the same model). This is not ideal as there is a possibility of two different entities to have the same values. Without IDs, there would be no method to differentiate them, leading to difficulties in updating and deleting one of them. The system would not know which one to update/delete which could lead to scenarios such as:
1. Updating/deleting the wrong entity
2. Updating/deleting both entity
3. System error

There is a java package that could be used to create an ID.
```java
import java.util.UUID;

String id = UUID.randomUUID().toString();
```
Other than that, an entity's created datetime could be used as an ID (as long as there is no concurrency). Product name could also be used as an ID if two entities are not allowed to have the same product name.

For easier implementation of CRUD, I decided to use the UUID package for exercise 1.
## Exercise 2