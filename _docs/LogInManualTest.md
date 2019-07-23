# Log-In Manual Tests

| Test | Expected Result | Actual Result |
| :---------------------------- | :---------------------------------- | :---------------------------------- |
| The Sign up button is pressed | The user will be able to choose a Username and password. | The user can choose a username and password |
| The user Signs up with an existing Username | A message should appear telling them that this is Username already exists | The message telling them that they can't sign in with existing username and password appears| 
| The user Signs up with a new username and password| A new account is created for them. They should now be able to log in | A new account is created for them. They scan now log in | 
| The User signs up with empty values in username. | A message should appear saying that the username field can not be empty |UNEXPECTED - The user is able to create an account (Bug). |
| The User logs in with their username and password |User should be taken back to the homepage with their name in the corner to show that they have logged in. | They are taken to the homepage with their name in the top corner meaning that they're logged in. | 
| The user logs in with an incorrect username and password | They will be not be logged in and a message should appear telling them they have an incorrect username/password | A message appears telling them that they have an incorrect usernam/password. |
| The user tries to log in without filling in their username  | A message should appear telling them to fill in the field. | A message appears telling them to fill in that field |
| The user tries to log in without filling in their password  | A message should appear telling them to fill in the field. | A message appears telling them to fill in that field |
| Once logged in, the user clicks the log-out button | The user should be logged out and should be taken to the Log-In page . | The user is logged out and taken back to the Log-in page | 
