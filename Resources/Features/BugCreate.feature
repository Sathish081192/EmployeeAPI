Feature: Bug Creation

#@GenerateJESSIONID
#Scenario: Generate JESSIONID with Cookie Authentication API
#Given User creates the JSON for Cookie Authentication
#When User hits Post Request for the Cookie Authentication with created JSON
#Then JSESSIONID should be generated Successfully as Output

@GenerateJESSIONID_POJO
Scenario: Generate JESSIONID with Cookie Authentication API
Given User creates the JSON for Cookie Authentication
When User hits Post Request for the Cookie Authentication with created JSON
Then Cookie Response needs to be validated and JSESSION to be Retrieved