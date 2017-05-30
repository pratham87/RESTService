
* ## Unity RestService

* ### **Techologies used:** 
	* Rest Service:
		* Eclipse IDE
		* Jersey framework
		* JAX-RS API
		* Maven build tool
		* Tomcat 8 web server
		* Log4j
	*  Testing:
		* Postman: Web service tests 
		* TestNG: Unit tests  	

* ### **How to Run the service:**
	* After git clone, please run ```mvn clean install``` and then ```mvn package``` to generate the war.
	* You can use any web server to deploy the war and hit the endpoints.
	* I used [Tomcat 8](https://tomcat.apache.org/download-80.cgi) to deploy and run the service.
	* In Eclipse IDE if you have server set up, then add the war to the web container and just start up the server. 
	* Note: Please make sure your context path is set to / in server.xml
	* For example in Tomcat 8, conf/server.xml file:
``` <Context docBase="Unity" path="/" reloadable="true" source="org.eclipse.jst.jee.server:Unity"/></Host> ``` 

* ### **Testing:**
	* Postman:
		* You can get Unity.postman_collection.json from the project to run all the tests in Postman.
		* You can import the Unity.postman_collection.json in Postman and run all the tests together. 
		* CreateProjects tests will test createProject API as well as set the test data for requestProject API tests.
		* Once tests are executed it will create projects.txt and projects.log at ${user.home} directory.  
	* TestNG: 
		* Unit tests should execute when you run ```mvn clean install``` command.  

* ### **Service Requirement: (All covered)**
* #### API Calls:
	* CreateProject API: POST
		* To save the project, make a POST call to below URL:
		```http:\\${service-url}:${port-number}\createproject``` 
		
	* RequestProject API: GET
		* Service should accept get request with below mentioned parameters
		```http:\\${service-url}:${port-number}\requestproject?projectid=1&country=usa&number=29&keyword=sports```
        * Service should return projects from file (projects.txt) created in part A  

* #### Filter:
	* Service should never return a project which is expired (if today’s date is above expiry date then project should not be selected).
	* Service should always return projects which are enabled, which means the project where “enabled”:true (default will be false).
	* Service should never return a project if projectUrl is null.

* #### Rules:
	1. Service should always return the matching project id if it is sent in request. For ex. If request is http:\\localhost:5000\requestProject?projectid=1&country=usa&number=29 then it should return a project with matching id regardless of any other rule.

	2. Service should return a project with highest price if no url parameter is sent in request. Example url - http:\\localhost:5000\requestProject

	3. If project id is not sent in request url parameters then it should select all the matching projects based on url parameters and return the one with highest cost.

	4. Url parameters should be considered as AND operator and not OR. Ex. if country=usa&number=30 then it should select all project which has USA in their targetCountries and number is 30 or above in their targetedKeys number.

	5. Service should return {“message”:”no project found”} if any of the parameter is not matched.
