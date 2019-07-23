# Manual Testing

| Test | Expected Result | Actual Result |
| :---------------------------- | :---------------------------------- | :---------------------------------- |
| The home button is pressed. | The user will be taken to the home page. | The user is taken to the home page. |
| The library button is pressed. | The user will be taken to the library page and a list of stories are displayed. | The user is taken to the library page and a list of stories are displayed. | 
| The Storalate logo is pressed. | The user will be taken to the home page. | The user is taken to the home page. |
| The browse button is pressed. | A drop-down list will be displayed. | A drop-down list is displayed. | 
| The browse button is pressed and then 'Latest Stories' is selected. | The user will be taken to the 'latest stories' page and the stories will be arranged into chronological order, with the latest story appearing first. | UNEXPECTED - The user is taken to the 'latest stories' page but the stories are not filtered by latest. |
| The browse button is pressed and then 'Trending Stories' is selected. | The user will be taken to the 'trending stories' page and the stories will be arranged in the order of likes, where the most likes are at the front. | UNEXPECTED - The user is taken to the 'trending stories' page but the stories are not filtered by likes. |
| The browse button is pressed and then 'All Stories' is selected. | The user will be taken to the 'all stories' page. | The user is taken to the 'all stories' page. | 
| The pencil icon is pressed. | The 'create a story' page will be shown. | The 'create a story' page is shown. | 
| Create story button is pressed without entering a title. | An error will be produced, the user will be notified and the story will not be created. | The user is asked to fill in the empty field and a story is not created. |
| Create story button is pressed with a title entered but without entering the start of the story. | An error will be produced, the user will be notified and the story will not be created. | The user is asked to fill in the empty field and a story is not created. |
| Create story button is pressed with a title and the start of the story entered but without a category selected. | An error will be produced, the user will be notified and the story will not be created. | The user is asked to select a category from the list and a story is not created. |
| Create story button is pressed with a title and the start of the story entered and with a category selected. | A story will be created. | A story is created. |
| Create a story button is pressed with the same title entered of an already created story. | An error will be produced, the user will be notified and the story will not be created. | UNEXPECTED - The story is created. |
| Open the website on Mozilla Firefox web browser. | The website will run. | The website runs. |
| Open the website on Chromium web browser. | The website will run. | The website runs. |
| Entering only space characters as input for either Title or Description | Validation Error - Cannot Save empty titles | Story is not saved with empty title and/or description and error is displayed informing user to re-enter contents in correct format


