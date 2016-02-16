---------------- Lodbrok Endpoint Project ---------------------

Overview

This server login a user, insert a new scre for a particular high score list and retrieve the high score lists (the sessions expire in 10 minutes)

Start application:
	
	- Jave 8

	- C:>java -jar LodbrockEndpoint.jar

Kill application

	Press <Ctrl>+c
	
Typical Requests

	- http://localhost:8081/4711/login (GET)

	- http://localhost:8081/2/score?sessionkey=UICSNDK (POST)

	- http://localhost:8081/2/highscorelist (GET)


