# Reflection Module 1
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
1. After writing the unit test, how do you feel? How many unit tests should be made in a class? How to make sure that our unit tests are enough to verify our program? It would be good if you learned about code coverage. Code coverage is a metric that can help you understand how much of your source is tested. If you have 100% code coverage, does that mean your code has no bugs or errors?

Answer:

How many unit tests should be made in a class depends on how many flows/cases could be run through the class. Ideally, there is a unit test for every case. Code coverage itself tells us the percentage of lines covered by our tests. However, having a 100% code coverage does not mean that there are no bugs or errors. It could be that our code doesn't handle errors properly. There is already an example in my code.

If you were to erase the unit test in ProductRepositoryTest.java that tests how the code handles the case in which we want to create a product with an ID that already exist in the product list, the code coverage stays at 100%.

2. Suppose that after writing the CreateProductFunctionalTest.java along with the corresponding test case, you were asked to create another functional test suite that verifies the number of items in the product list. You decided to create a new Java class similar to the prior functional test suites with the same setup procedures and instance variables.
   What do you think about the cleanliness of the code of the new functional test suite? Will the new code reduce the code quality? Identify the potential clean code issues, explain the reasons, and suggest possible improvements to make the code cleaner!

Answer:

The code quality *may* decrease due to duplicates on multiple files, but a lot of times, tests are excluded from quality gates. But, when we want to change the setup and each one must be the same, then it would be a repetitive work. Perhaps it would be best to create a superclass for the setup and every other functional test classes would be the subclass of it.


# Reflection Module 2
## Exercise 1
- Sonar master branch: https://sonarcloud.io/summary/new_code?id=huanis_advprog-tutorial-2-2024&branch=master
- Koyeb URL: https://advshop-huanis.koyeb.app/

1. List the code quality issue(s) that you fixed during the exercise and explain your strategy on fixing them.

**Answer**:

You can check in https://github.com/huanis/advprog-tutorial-1/pull/18
- field injection to constructor injection ProductController and ProductServiceImpl
- create a constant rather than duplicating "redirect:list"
- remove public modifier for all test classes
- surpress warning that says to put at least 1 assertion to EshopApplicationTests

There's not much of a strategy. I just check each issues, read why they are issues, look for solutions, and then implement those solutions. If it is an issue that can't be resolved (usually this is the case of overcomplexity in a single method that I can't break down), I would have to surpress the warning. After changing the code, I would run the test again to see if I broke any functionality.


2. Look at your CI/CD workflows (GitHub)/pipelines (GitLab). Do you think the current implementation has met the definition of Continuous Integration and Continuous Deployment? Explain the reasons (minimum 3 sentences)!

**Answer**:

- It does meet the definition of Continuous Integration. It automatically runs the tests in the project and automatically runs sonar everytime we push a new code into the repository. If one of these automated process failed, then it will alert the user responsible to it.
- For Continuous Deployment, I tried referencing https://github.com/koyeb/example-spring-boot/blob/main/.github/workflows/deploy.yaml , but when it tries to deploy the application, it would fail. I have given up and turned on autodeploy in Koyeb.

# Reflection Module 3
### Explain what principles you apply to your project!
Principles applied: **SRP**, **ISP**, and **DIP**

**SRP:** Single Responsibility Principle

The code in the `before-solid` branch violates SRP. This is because CarController is a subclass of ProductController. In this case, the class CarController will automatically inherit the every properties and methods ProductController has. This results in CarController having two responsibilities, which are being the controller of 'product' feature and the controller of 'car' feature. This is why I decided to erase that class and create a new CarController that is not a subclass of other controllers.

Other than that, this application implements SRP considering the fact that there is a clear separation of each MVC layers. Classes inside the Model module is responsible for the model layer. Classes inside the Service module is responsible for the business logic. Classes inside the Controller module is responsible for the routing. Classes inside the Repository module is responsible for database manipulation.

**OCP:** Open-Close Principle

There is currently no need for me to implement OCP unless Car is a subclass of Product. In that case, then we can have Car extends Product and then we wouldn't need the properties "carId", "carColor", and "carQuantity".

**LSP:** Liskov Substitution Principle

Same answer as OCP. In the case of a Car being a subclass of Product, then the car should also be listed as product in the product list. This also means that their repositories should be somewhat connected. If a car is created, then it should be persisted inside the list of cars and list of products.

**ISP:** Interface Segregation Principle

"*Rather than having one comprehensive interface, it's advocated to have multiple interfaces, each catering to distinct client needs with specific responsibilities.*" The fact that each services have their own different interface separated by distinct business responsibility is proof enough that this application implements ISP.

**DIP:** Dependency Inversion Principle

DIP presses on the concept of abstraction. Note that whenever we want to use a service, we don't call by the implementation, instead we call by their interface. This applies a degree of abstraction as we won't be exposed to methods that are in the implementation but are not defined by the interface, making it simpler. Sometimes there are multiple implementations of the same service. By calling the service's interface, we won't know which implementation is actually used. All we know from the interface is the method's name, its parameters, and the data type of its output.

## Explain the advantages of applying SOLID principles to your project with examples.

The application of SOLID principles offers maintainability, reusability, and readability.
- SRP makes sure that we have a clear distinction between application components. This way, if we find a bug for a certain part, we would have an immediate hunch on where to look. Before I create a new file for CarController, the CarController is inside the ProductController. If there were any bugs in the CarController and we're new to the repository, we probably wouldn't think of looking through the ProductController file, assuming that it is only responsible for product.
- LSP makes sure that when an object is a subtype of another object, then said object has the base behaviors of their supertypes. For example, there are flying robots, which are a subtype of robot. They could be turned on and deployed, the same as any other robots. The only difference i that they could fly.
- ISP makes sure that you only implement what you need to implement. A camera should implement the lens interface but it doesn't need to implement the engine interface. A robot might need both interfaces. A land robot doesn't have to implement the flying interface.

## Explain the disadvantages of not applying SOLID principles to your project with examples.

When used carelessly, SOLID principles could make the code more complex than necessary and unreadable. Codes that are fine the way they are being forced into implementing SOLID principle.
- For example, for the sake of implementing SRP, someone decided to breakdown the repository class into four different classes, which are CreateProductRepository, ReadProductRepository, UpdateProductRepository, DeleteProductRepository. This could lead the developer to feel fatigued as thye would have to traverse to too many classes when they could have simply been methods of the same class.
- ISP without proper categorizing may be difficult to read. When every interface is put in the same module, on the same hierarchy and there are loads of them. Looking for a specific interface may become a hassle.

