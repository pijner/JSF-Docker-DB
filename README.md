# DockerDemo
 A sample project to demonstrate the use of Docker containers with JSF and MySQL

# Getting started with Docker for JSF

For this demo, we will be attempting to use a container running a Payara application server to host our web application and another container to host our databse server. I'm using MySQL as the database server, but this should be possible with containers for MariaDB, MongoDB, etc. See [Docker hub](https://hub.docker.com/) for container images and instructions on setting them up.


## Get docker

Docker is available via Docker Desktop for Windows and Mac operating systems. Is comes with Docker Engine, a CLI client, Docker Compose, a GUI for container management, and some additional features.
For Linux, you need to install Docker Engine and Docker Compose seperately (and sequentially).

### Installation instructions
- [Windows](https://docs.docker.com/docker-for-windows/install/) (_Note that if you're using Windows 10 Home Edition, you will need to enable WSL2_)
- [Mac](https://docs.docker.com/docker-for-mac/install/)
- Linux
  - [Docker engine](https://docs.docker.com/engine/install/#server)
  - [Docker compose](https://docs.docker.com/compose/install/#linux)


## Setting up the web application
Following the pattern of this course, we'll be setting up a web application in NetBeans with JSF and running it on a Payara server. I already have one set up in a GitHub repo (available here) if you want to follow along or try for yourself.


## Creating a network
- Containers, by default, are isolated from each other. For one container to be able to communicate with another container, they need to be in the same network. We can just create a virtual network for containers with the following command
    
        $ docker network create web-network

    This will create a network called `web-network`.

## Setting up MySQL
- Once you have docker set up, fire up a terminal and run the following command
        
        $  docker run -d --network web-network --network-alias mysqlserver  --name mysqlserver -e MYSQL_ROOT_PASSWORD=password123 -p 3305:3306 mysql:8.0
        
    The docker run command will search for an existing copy of the image specified and create a container with that. If it isn't on your machine, it will get it from Docker Hub. 

    The server will be named mysqlserver (or whatever follows the `--name` part in the command).

    The `-e` tag is used to specify environment variables for the container. In this case, we specify the password for the root account on MySQL server. The root password will be password123 (very strong password). 

    The `-p` tag is used to specify port mappings. In this case, the port number `3306` (default port used by MySQL) in the container will be mapped to `3305` in the host machine. You can map it to anything, but if you already have something using the port number specified, you will run into an error and will have to delete the container and re-run the command.


- For this demo, I'll be using a simple table called dummy with two fields: ID and name. You can find the SQL queries to create the tables and insert data in the GitHub repo.
    
    To insert data into the database, fire up a new terminal and run the following commands

        $ docker exec -it mysqlserver bash
    
    This will interactively (`-it`) execute (`exec`) the command `bash` in the `mysqlserver` container that we just created. You will notice you're in a different shell now. 
    Paste the following command into bash now.

        # mysql -u root -ppassword123
    
    I know there is not space between `-p` and the password, it's not a typo. This is how the input is expected. Alternatively, you can just enter `mysql -u root -p` and it will prompt you to enter the password.
    
    In either case, it will attempt to log into the MySQL server's root account using the password given.

    Once you're logged into MySQL, use the queries in the `queries.sql` file to create tables and add mock data.

    **Make sure you commit the changes to the database by typing `commit;`**

    You can exit out of MySQL now by typing `exit` and then again exit bash (and the container) by typing `exit`.

<br>


## Setting up the Payara container
- First, build the project.
- Before we set up the payara server, we need to build an image that contains the payara server and our web application. This is pretty simple and is going to be taken care by the DockerFile in our repo. All we need to do it build the file.
- In your terminal, navigate to the project directory and run the following command
    
        $ docker build --tag demoimg .

  This will build a new image for us that contains the payara server and our project `.war` file.

- Create a container with this image

        $ docker run --network web-network --name payaraweb -p 8080:8080 demoimg
    
    This will create a container named payaraweb with the image we just created and add it to the web-network.


## Using docker compose
Instead of using the complicated process above everytime you want to test something, it is much easier to have a file with all the set-up instructions. Docker compose makes it much easier to re-create multi-containered applications by defining the containers/services in a YAML file. In our case, we have `docker-compose.yml` in the project directory.
We define the two containers we are interested in using with out application along with the files to initialize the servers with our data.

All that needs to be done is run the following command:

    $ docker-compose up

Once the servers are up and running, open a browser tab and navigate to `http://localhost:8080/DockerDemo-1.0/`. Note that we go to port `8080` as that's where we mapped the HTTP port for payaraweb in the YAML file.

You can hit `ctrl + c` to stop the containers or run the following in the project directory

    $ docker-compose down


## Clean up
- To stop any container:

        $ docker container stop <container-name>

- To remove containers from your computer:
  
        $ docker rm <container-name>

- To remove an image from your computer:
  
        $ docker rmi <image-name>

- To remove docker network from your computer:
    
        $ docker network rm <network-name>