# General Description
The purpose of this application is to get the list of the San Francisco food trucks from their government's open data website to then use in various ways. Their website can be found at:  [https://data.sfgov.org/Economy-and-Community/Mobile-Food-Facility-Permit/rqzj-sfat/data](https://data.sfgov.org/Economy-and-Community/Mobile-Food-Facility-Permit/rqzj-sfat/data)

Due to time constraints this is currently a simplistic implementation. There are thoughts in sections below on ways to further enhance this application.

For execution details see [To Run](#to-run) below.

# Table Of Contents
1. [Assumptions](#assumptions)
2. [To Run](#to-run)
3. [Requirements and Building](#requirements-and-building)
4. [Production Readiness](#production-readiness)
5. [General Decisions That Were Made](#general-decisions-that-were-made)
6. [User Features Yet to Implement](#user-features-yet-to-implement)
7. [Things to Fix](#things-to-fix)

# Assumptions
This section outlines some of the assumptions that were made based off of looking at the data. For a production release these items would need to be investigated to determine if the assumptions are correct.

Although the original file was CSV, I noticed that it is 3 years old. When I looked at the SF website, I saw it supported JSON files. With JSON being simpler to parse, I went that way. Normally I would expect this to be a quick discussion with the stakeholders to determine if this was okay. As there was a forum for that discussion in this instance, I made a decision and went with it. 

* Applicants are unique to businesses. So all lines with the same applicant are the same business
* A business will only have one facility type, no matter how many lines they have
* A business will only have one list of food types, no matter how many lines they have

# To Run
Ensure you have a JVM for java 17 installed. Then change to the directory containing the jar file and execute: `java -jar <jar file>`. This will start up the server on port 8081. If you need to change the port you will neeed to recompile after updating `src/main/resources/application.yml`

NOTE: for the purposes of making it simple to demo, a jar file can be found in the root project directory with the name: `sf-food-trucks.jar`

This starts without any data. Load the data by executing a `POST /trucks`.

This project incorporates the OpenAPI 3.0 spec and swagger UI. Once you run the jar, the below url will bring up the full API, where you can see each of the available API calls and you can try them out.

`http://localhost:8081/swagger-ui/index.html`

After the application is running, you can also see the in-memory database at: `http://localhost:8081/h2-console/login.do`

Leave the password blank and click connect to login to the DB.

# Requirements and Building
* JDK 17+
* maven

In the root directory of the project, execute: `./mvn clean package`

The inclusive jar file can then be found at: `./target/sf-food-trucks-0.0.1-SNAPSHOT.jar`
This jar contains the full application including:
* web server
* swagger ui
* h2 database

You can also run the test suite from the root of the project by executing: `mvn test`.

# Production Readiness
In an effort to put something together that works and at least provides a bare minimum amount of functionality, this project produces an all-inclusive jar file. This jar has a built in web server, along with using an in-memory database. As such, the first and most important step to production readiness is to ensure data is persisted, even if the application is shutdown. This is fairly trivial but due to time and "demo" constraints has not been done yet. 3 steps are needed to do it:
1. Setup a database. Consider something like mysql in RDS.
2. Update src/main/resources/application.yml to change url. A sample is already in the file and commented out.
3. Remove h2 references from application.yml

Once we have data being persisted, we have a ton of options for application deployment. Of course, which one to use is entirely based off of scale and personal preference.

#### Simple
Something for personal user only, a fun little app just for me.
* Simply start up the jar locally when you want to use it.
* If the DB is in the cloud, I would probably start up the DB when the app starts and shut it down when the app ends. This could be accomplished using provider's SDKs and either in code or to keep things separate a simple script in bash or python. 

#### Always Up - Simple
This option would be optimal for a small group of people to use, like you and your team.
* See the [User Features Yet to Implement](#user-features-yet-to-implement) for some stuff that needs to be implemented before this makes sense.
* I would probably set this up in an AWS App Runner. I haven't personally used this, but seems fairly trivial to point it at your github, set some configure options and you're good to go.

#### A More Complex AWS
This would require a lot of rework, likely even a different project. If you expanded this to other areas of the country/world and were expecting large volumes of traffic, you might want to consider this.
* Turn the service functions into AWS Lambdas
	* Cold starts could become an issue. There are ways to mitigate this, but would it require further code break down. 
* API endpoints could be put into Amazon API Gateway and trigger the Lambdas
* The API isn't really serving static content as the data is changing. That said it is changing infrequently and enabling caching on at least some of the end points could make sense.
* Obviously, there are a ton of configurations here: auto-scaling, load balancing, routing, etc. With all this configuration, I would put this all into some form of IaC (terraform or something similar).

# General Decisions That Were Made
1. Java
	* My currently most practiced language
2. Spring Boot Starter Project
	* Fastest way to get a self-contained web service up and running in java
3. Lombok
	* Great library for handling boiler plate in java.
4. HTTP API - REST like
	* Fits my background. I am a back-end engineer first and foremost. It is where I think the fun problems are (typically). That said, I care deeply about providing a great customer experience, and I am good at putting myself in the customer's shoes, but my front-end skills are not currently honed.
5. Project Layout
	* I like to group my code into packages that deal with a specific function of the application (food trucks, users, ratings, etc.). I find when troubleshooting an issue or working on a new feature, it is typically easier to locate the right place in the code when grouped in this fashion. Interesting enough, this can also lend itself into breaking things out into microservices later.


# User Features Yet to Implement
* Multi-tenancy - one bullet point but so much work.
	* User management being a lot of work. Consider a 3rd party tool like cognito, auth0 or other similar options.
* With data persistence and the next bullet, a way is needed to update the DB with the latest SF data.
	* Get latest locations, update status of existing locations, etc. Don't lose data. Maybe want to audit changes.
* Marking trucks/locations as having been to them.
	* Consider the marking being a 1-5 star rating.
* Being able to sort/filter by star rating
* Finding trucks with no rating so you can try something new
* Integration with mapping to find food trucks near me
* User's could update some new datapoint like (open hours, price point, wait time)
	* Wait time could be an interesting algorithm based off of the individual wait times. Rolling average over time.

# Things to Fix
In no particular order
* look for TODO in code comments
* unit tests - need a lot more
* food items is currently just a string. It appears to be a colon separate list, this could be separated out.
* test suite spins up spring multiple times, this can be optimized
* status is loaded but we don't currently do anything with it. Should provide filtering, default to approved only
* security - authentication and authorization
* security - TLS

