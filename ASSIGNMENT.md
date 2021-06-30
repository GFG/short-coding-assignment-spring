## GOALS
The Product Service is a REST API that provides endpoints to perform CRUD operations for products.
The [README.md](README.md) provides instructions to run the application in its current state.

> IMPORTANT : Don't apply any changes to this repository. Clone this repository into your own private repository on github.

> Create a new branch `product_service` from the master branch of YOUR repository.


## Assignment

The `@PatchMapping` in the [ProductController](src/main/java/com/gfgtech/product/web/ProductController.java) which is used to update product attributes has no business logic implemented, it simply saves the updates to the table.

The following business logic should be implemented:

<pre>
1. For any update send email Notification to seller on email id seller@marketplace.com. All notification emails must contain the old and new changed values.

2. Validations for price update:
    a. Price cannot be negative or zero
    b. If price is decreased, verify that it is not lower than half of the current price. If it is, send an appropriate error message to the client.

3. The service should be idempotent, for example: when an update request is made with is no change to any fields, email notification **_SHOULD NOT_** be sent.

4. If price is changed send an email also to admin@marketplace.com in addition to the seller email in step 1 (seller@marketplace.com)

5. The new business logic should be covered with Unit tests.
</pre>

> There is no need to send a REAL email. Simply print the body of the email in the logs.

This completes the assignment. 

> Don't hesitate to refactor the current code while implementing the above business logic.
## Submit YOUR code

**_IMPORTANT_** : Don't apply any changes to this repository.

Give repository access to the person/s from our organization that you should have been indicated by now. 
Ask the Hiring Manager if you did not get them yet. (_Currently these would be `@nikhilvasaikar`, `@wolframite`, `@hadimansuri` and `@fdiazarce`_)

> Please grant "Admin" access to these users since they might need to grant access to some other colleagues in turn.

Notify the **_Hiring Manager_** when you are done and provide a `link` to your repository.

**_`Happy Coding !`_**
