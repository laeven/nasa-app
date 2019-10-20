# Nasa Image App

## Description
This is a Spring Boot application written in Java. It encapsulates the API to only Curiosity Rover's photos (No mention
of what other rovers were needed) so it only requires the date. 

It begins be initalizing its cache for the init days passed by the dates.txt file. It then accepts request at the 
`api/v1/photos` and first checks its cache (downloading from NASA if not cached) before serving the photo to the user.

The API Key is _not_ included, this must be configured in config.json, see the provided config.json.example

## Acceptance Criteria

The exercise we’d like to see is to use the NASA API described here to build a project in GitHub that calls the Mars Rover API and selects a picture on a given day. We want your application to download and store each image locally.

- Please send a link to the GitHub repo via matt.hawkes@livingasone.com when you are complete.
- Use list of dates below to pull the images were captured on that day by reading in a text ﬁle:
  - 02/27/17
  - June 2, 2018
  - Jul-13-2016
  - April 31, 2018
- Language needs to be Java.
- We should be able to run and build (if applicable) locally after you submit it
- Include relevant documentation (.MD, etc.) in the repo

## Software Requirments
- Make
- Java
- Docker
- Maven

## How to use
- Create a configuration file as shown in config.json.example
- run `make docker`

## TODO
- Create Unit & Integration Tests