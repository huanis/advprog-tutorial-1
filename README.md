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

I also noticed how in the template, the product's quantity field is set to text. I decided to change it to number and set the minimum as 0. The name field is also set to be mandatory so that there is no product without a name.

Some other things I want to reflect on:
- If the user tries to go to a URL that does not exist, it should show a page that says "404 error"
- Input validation should be in backend rather than frontend. There should be a minimal amount of logic in frontend. It could become repetitive.

One of the things learned in modul 1 is for error handling:
"Don't return Null. Never pass Null."

I broke that rule for the product repository. I do have my reasons. When trying to find an entity using JPA Repository and it turns out the entity does not exist, the repository would return null, which makes sense because the entity does not exist. Same logic goes to this.

And what if you want to update a product that does not exist? Do you end up creating a new one based on the values you want to put, or to ignore the request? I chose the second option for the sake of the Single Responsibility Principle (SRP). An update function should only be used to update an item. Not create nor delete.

One thing I didn't do is handle a case in which we try to create a new product with the same productId as another product. I created the test for that and as expected, the test failed.
## Exercise 2