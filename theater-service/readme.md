# For Running the app on local

## Step 1: run the below command inside the project folder:

```
mvn clean install
```
It will generate a target folder with application jar in it


## Step 2: Run the below command to tag and build the docker image

```
docker build -t <your_dockerhub_id>/<app_name> .
```
e.g. 
```
docker build -t dummyuser/blogging-app .
```

Don't miss the dot in the end of above mentioned command.

## Step 3: To run the app on your local docker instance execute below command
```
docker run -p 8080:8080 <your_dockerhub_id>/blogging-app
```

## Step 4: Now you can access the app on your browser or rest client on localhost:8080. Sample endpoint is 
```
GET: localhost:8080/posts
```

# For Running the app on kubernetes After the step 1 and step 2 execute below steps

## Step 5: Push your image to dockerhub but running below command
```
docker push <your_dockerhub_id>/blogging-app
```

## Step 6: Login into GCP and create a kubernetes cluster with the name "blogging-app-cluster" or any other name of your choice

## Step 7: Open blogservice.yaml file which is included with the codebase and change the placeholder <your_dockerhub_id> at line 30 with your dockerhub id. 

## Step 8: Open gcp cloud shell and create a new file with name "blogservice.yaml" using editor. Paste the content of  blogservice.yaml from previous step

## Step 9: Run below commands on gcp cloud shell to configure project and compute zone 

```
gcloud config set project <your_project_name>
gcloud config set compute/zone <your_compute_zone>
```

## Step 10: Fetch the credentials using below command:
```
gcloud container clusters get-credentials blogging-app-cluster
```

## Step 11: run below command in gcp cloud shell to deploy the service. 

```
kubectl apply -f blogservice.yaml
```

## Step 12: Run Below command to get the external IP. (If external ip is not visible, wait for few seconds and run command again)

```
kubectl get svc
```

## PS: Make sure you stop the service when not in use to avoid unnecessary billing charges