# TP Cloud App Engine

## Part 1
Sujet : TPGoogleAppEngine.pdf

## Part 2
Sujet Introduction à DataStore et CloudStore.pdf

```
// Start the local datastore
gcloud beta emulators datastore start --project=gothic-concept-145217

// Set the env for the datastore
$(gcloud beta emulators datastore env-init)
// Start the server
mvn appengine:devserver
```

## Usage

```
// Start local http://localhost:8080​ & http://localhost:8080/_ah/admin
mvn appengine:devserver
// Deploy online
mvn appengine:update
```