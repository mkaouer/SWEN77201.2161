Setup and Deployment Instructions

There were difficulties exporting a consistently functional .jar file.  The following setup instructions detail how to run the source code itself as a plugin with Eclipse.
Video of setup: https://youtu.be/rxcX3OI9tQ0 
Video of use: https://youtu.be/ti18OABi80c 
1.	Install Eclipse https://eclipse.org/downloads/
2.	Open Eclipse.
3.	Import the swen772_mmetrics3 folder using File  Import  General  File System
4.	If the project has build errors, you likely need to install the “Eclipse Plug-in Development Environment” and restart Eclipse.
5.	Now, open swen772_mmetrics3  net.sourceforge.metrics  plugin.xml.
a.	You should see a tab called “Overview.”  Select it if it isn’t already.
b.	Under the “Testing” header, select “Launch an Eclipse Application”
6.	Now, you should be in a new Eclipse window.  Import the projects you would like to analyze here.  They will remain even if you restart this instance of Eclipse.
7.	Select the project for which Metrics have to be calculated; right-click it and choose Properties
8.	In the Properties window, select Metrics from the left pane
9.	Check the Enable Metrics checkbox and click OK
10.	From the Menu Bar, Click Window → Show View → Other → Metrics → Metrics View and click OK
11.	From the Menu Bar, Click Project → Clean
a.	Metric values will be displayed, but these may not be accurate depending if the project has been changed, another project has been cleaned in the interim, etc
12.	Clean the project a second time.
a.	Now the values are accurate
13.	The CCD table will display the “complexity rating” for a given class.  A higher complexity rating indicates a greater need for refactoring.
a.	A developer may enable a flag in CCDCalculator.java to limit this value to 3, with 1 being low, 2 being medium, and 3 being high.
14.	The CCD1 table will indicate the number of metric “rules” that have been broken by each class.  The sum of these rules’ weighted complexity values is what is displayed in the CCD table.
15.	In order to see graphic view, choose graphic view from the Show View options
