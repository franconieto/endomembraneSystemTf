The simulations were performed in RepastSimphony-2.8 (2020-06)

I) To import the project into Repast:

1- Select an empty workspace
2- Import projects...
3- Git
4- Projects from Git (with smart import)

A- If the repository is already cloned on the PC -> Existing local repository
5- Add... -> Browse... -> Select the directory where it was cloned -> Check the "endomembraneSystemTf" repository box -> Finish -> Next

B- If it has not been cloned -> Clone URI
5- Copy and paste into URI: https://github.com/franconieto/endomembraneSystemTf.git -> Next -> Next -> Select a directory where the repository will be cloned (it can be the same directory as the workspace or a different one)

6- Select the checkboxes "Search for nested projects" and "Detect and configure project natures"
In the "Folder" box, only check the "endomembraneSystemTf\immunity" box (in "Import as" it should appear as Eclipse project) -> Finish

II) To run the model for the first time:

1- Go to "Run Configurations" (green Play button in the toolbar)
2- Java Application -> immunity Model -> Run
3- In the new window, start the simulation with the blue Play button

III) If there is an error in the .java classes related to library imports:

1- Go to Project -> Properties -> Java Build Path -> Libraries
2- Double-click on the missing libraries
3- In the explorer, locate the libraries (they can be found in: dir-where-the-repo-was-cloned...\immunity\lib)