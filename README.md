# General Description
The purpose of this application is to get the list of the San Francisco food trucks from their governments open data website. Then using that data in various ways.

Current implementation is fairly minor. See ``[To Run](#to-run)`` below for details on execution.

# Table Of Contents
1. [Assumptions](#assumptions)
2. [To Run](#to-run)
3. [Requirements](#requirements)
4. [Production Readiness](#production-readiness)
5. [General Decisions That Were Made](#general-decisions-that-were-made)
6. [User Features Yet to Implement](#user-features-yet-to-implement)
7. [Things to Fix](#things-to-fix)

# Assumptions
* Applicants are unique to businesses. So all lines with the same applicant are the same business
* A business will only have one facility type, no matter how many lines they have
* A business will only have one list of food types, no matter how many lines they have

# To Run
Ensure you have a JVM for java 17 and then from command line execute: `java -jar <jar file>`

This starts up the server on port 8081.

Right now this starts up without data. You will need to execute POST /trucks to load the data. See next lines for how to do this in a simple way.

This project incorporates the OpenAPI 3.0 spec and swagger UI. Once you run the jar, the below url will bring up the full API. From here you can see each of the available API calls and you can try them out. 

`http://localhost:8081/swagger-ui/index.`

Once the application is running you can also see the in memory database at: `http://localhost:8081/h2-console/login.do`

Just leave the password blank and click connect.

#### Running test suite
from root project directory run: `mvn test`

# Requirements
* JDK 17+
* maven

in root of project execute: `./mvn clean package`

inclusive jar file can be found: `./target/sf-food-trucks-0.0.1-SNAPSHOT.jar`
This jar contains the full application including:
* web server
* swagger ui
* h2 database

# Production Readiness
In an effort to put something together that works and at least provides a bare minimum amount of functionality, this project produces an all inclusive jar file. This jar has a built in web server along with using an in memory database. As such, the first and most important step to a production readiness is to ensure data is persisted even if the application is down. This is fairly trivial but time and "demo" constrained. 2 steps are needed:
1. Setup a database. Something like mysql in RDS maybe.
2. update src/main/resources/application.yml to change url. Sample is already there and commented out.
3. remove h2 references from application.yml

Once we have data being persisted we have a ton of options for application deployment. Of course which one to use is entirely based off of scale and personal preference.

#### Simple
Something that just you are using. Fun little app just for me.
* simply start up the jar locally when you want to use it
* if the DB is in the cloud I would probably start up the db when the app starts and shut it down when the app ends. This could be accomplished using providers SDKs 

#### Always Up - Simple
Go this route if you just want to use it for you and your team.
* See the [User Features Yet to Implement](#user-features-yet-to-implement) for some stuff that needs to be implemented before this makes sense.
I would probably just set this up in an AWS App Runner. I haven't personally used this but seems fairly trivial to point it at your github, set some configure options and bob's your uncle.

#### A More Complex AWS
This would require a lot of rework, even a different project structure likely. But if you expanded this to other areas of the country/world and were expecting large volumes of traffic, you might want to look down this path.
* turn the service functions into AWS Lambdas
	* cold starts could become an issue, there are ways to mitigate this, but would require further code break down. 
* API endpoints could be put into Amazon API Gateway and trigger the Lambdas
* The API isn't really serving static content but as the data is likely not changing constantly, as such enabling caching on at least some of the end points could make sense
* Obviously there is a ton of configuration here. Scaling and load balancing and the such. With all this configuration I would put this all into some form of IaC(terraform or something similar)

# General Decisions That Were Made
1. Java
	* my currently most practiced language
2. Spring Boot Starter Project
	* fastest way to get a self contained web service up and running in java
3. Lombok
	* great library for handling boiler plate in java.
4. HTTP API - REST like
	* fits my background. I am back-end engineer first and foremost. It is where I think the fun problems are (typically). That said I care deeply about providing a great customer experience, and I am good at putting myself in the customers shoes. That said my front-end skills are not currently honed.
5. Project Layout
	* I like to group my code into packages that deal with a specific function of the application(food trucks, users, ratings, etc.). I find when troubleshooting an issue or working on a new feature it is typically easier to get to the right place when code is grouped in this fashion. Interesting enough, this can also lend itself into breaking things out into microservices later.


# User Features Yet to Implement
* multi-tenancy - one bullet point but soooo much work.
	* user management being a lot of work. Maybe put into 3rd party tool like cognito, auth0 or the like
* with data persistence and marking things as seen, there will need to be a way to get the latest food trucks
	* get latest locations, update status of existing locations, etc.
* marking trucks/locations as having been there
	* star rating 1-5 maybe
* Being able to sort/filter by star rating
* finding trucks with no rating so you can try something new
* integration with mapping to find food trucks near me

# Things to Fix
In no particular order
* look for TODO in code comments
* unit tests - need a lot more
* food items is currently just a string. It appears to be a colon separate list, this could be separated out.
* test suite spins up spring multiple times, this can be optimized
* status is loaded but we don't currently do anything with it. Should provide filtering, default to approved only
* security - authentication and authorization
* security - TLS

