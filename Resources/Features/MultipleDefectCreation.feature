Feature: Multiple Defect Creation

Background:
Given User creates the JSON for Cookie Authentication
When User hits Post Request for the Cookie Authentication with created JSON
Then JSESSIONID should be generated Successfully as Output

@MultipleDefectCreation
Scenario Outline: Multiple Defect Creation in JIRA
Given User Create Input JSON for Defect Creation <summary> and <issuetype> and <description> and <project> and <assignee> and <priority>
When User hits Post Request for Defect Creation with created JSON
#Then New DefectID should be generated Successfully
Then Defect Response should be validated and New DefectID should be generated Successfully
Examples:
|summary|issuetype|description|project|assignee|priority|
|Test Summary1|Bug|Test Description1|RES|sathish081192G|High|
|Test Summary2|Bug|Test Description2|RES|sathish081192G|Medium|
|Test Summary3|Bug|Test Description3|RES|sathish081192G|Low|
|Test Summary4|Bug|Test Description4|RES|sathish081192G|Medium|
|Test Summary5|Bug|Test Description5|RES|sathish081192G|High|

#@MultipleDefectCreation_POJO
#Scenario Outline: Multiple Defect Creation in JIRA
#Given User Create Input JSON for Defect Creation <summary> and <issuetype> and <description> and <project> and <assignee> and <priority>
#When User hits Post Request for Defect Creation with created JSON
#Then Defect Response should be validated and New DefectID should be generated Successfully
#Examples:
#|summary|issuetype|description|project|assignee|priority|
#|Test Summary1|Bug|Test Description1|RES|sathish081192G|High|
#|Test Summary2|Bug|Test Description2|RES|sathish081192G|Medium|
#|Test Summary3|Bug|Test Description3|RES|sathish081192G|Low|
#|Test Summary4|Bug|Test Description4|RES|sathish081192G|Medium|
#|Test Summary5|Bug|Test Description5|RES|sathish081192G|High|