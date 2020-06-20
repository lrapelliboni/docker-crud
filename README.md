
# docker-crud

Crud with Docker, Docker Compose, Java, Srping Boot, MySQL

- To build docker image: 
	 ```
	$ docker-compose up --build
	```

- To only run docker image: 
	 ```
	$ docker-compose up -d
	```
> API runs on [http://localhost:80](http://localhost:80)
 
 - To access MySQL:
	 ```
	 $ docker exec -it test-mysql bash
	 $ mysql -uroot -ptest123
	 ```
- To down docker image:
	 ```
	 $ docker-compose down
	 ```

 - The [Postman](https://www.postman.com/) Collection can be see [here!](https://github.com/lrapelliboni/docker-crud/blob/master/docker-crud.postman_collection.json) 
