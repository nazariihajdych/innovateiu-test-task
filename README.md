# Test Task

## Requirements
- Implementation of ```save()``` method should upsert the document to your storage.
  And generate unique id if it does not exist.
- Implementation of ```search()``` method should find documents which match with request. Search request, each field could be null.
- Implementation of ```findById()``` method should find document by id.

## Overview
- ```save()``` method saves documents in storage and cover such cases as
  - if document doesn't have id; 
  - if document already has id;
  - if document duplicated;
- ```search()``` method finds documents which match with request
  - titlePrefixes; 
  - containsContents; 
  - authorIds; 
  - createdFrom; 
  - createdTo;
- ```findById()``` method finds document by id
- All methods covered with unit tests  