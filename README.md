# “Phone contacts” application

## Task
Design and implement “Phone contacts” application. Phone contacts application allows adding/editing and deleting contacts data. Single contact is represented by the following data:
1. Contact name
2. Contact emails. One contact may have multiple emails
3. Contact phone number. One contact may have multiple phone numbers

**User should have a possibility to:**
1. Register in the app, login and password should be provided during registration
2. Login to the app
3. Add new contact
4. Edit existing contact
5. Delete existing contact
6. Get list of existing contacts

**The app to be implemented should:**
- Use any relational database to store contacts data. Take into account that schema should be designed in accordance with 1st and 2nd normal forms
- Use Spring Boot, Spring Web, Spring Security and Spring Data + Hibernate
- Should give access only to authorized users, so each user should have his own list of phone contacts (Use Spring Security)
- Be a RESTful webservice from a client perspective
- When contact is added or edited, emails and phone numbers should be validated so thatit is not possible to add phone number like “+38-asdas” or email like “aa@”. Also every phone number and email should be unique per contact. So it should not be possible to add already existing email, the same for phone numbers
Contact names should be unique
- Have unit and integration tests

## Some examples of server usage:

**Authorization:**

HTTP POST localhost:8080/auth

{
    "login": "xxx",
    "password": "yyy"
}

**Registration:**

HTTP POST localhost:8080/register

{
    "login": "xxx",
    "password": "yyy"
}

**User adds a new contact:**

HTTP POST localhost:8080/contacts

{
    "name": "xxx",
    "emails": ["xxx@xxx.com", "yyy@yyy.com"],
    "phones": ["+380939333333", "+380939333334", "+380939333335"]
}
