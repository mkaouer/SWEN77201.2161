1.3	Setup and Deployment Instructions
1	Install Eclipse https://eclipse.org/downloads/
2	Download the jar file for metrics3 plugin (net.sourceforge.metrics_1.3.9.jar)
3	Place the jar file in Plugins, Dropins and Features folders within Eclipse (Dropins for Windows users, Plugins for Linux users)
4	Open Eclipse

NOTE: If there are difficulties getting the jar to work with your version of Eclipse or operating system, the following setup instructions detail how to run the source code itself as a plugin with Eclipse.
Video of setup: https://youtu.be/rxcX3OI9tQ0 
1.	Install Eclipse https://eclipse.org/downloads/
2.	Open Eclipse.
3.	Import the swen772_mmetrics3 folder using File  Import  General  File System
4.	If the project has build errors, you likely need to install the “Eclipse Plug-in Development Environment” and restart Eclipse.
5.	Now, open swen772_mmetrics3  net.sourceforge.metrics  plugin.xml.
a.	You should see a tab called “Overview.”  Select it if it isn’t already.
b.	Under the “Testing” header, select “Launch an Eclipse Application”
6.	Now, you should be in a new Eclipse window.  Import the projects you would like to analyze here.  They will remain even if you restart this instance of Eclipse.


NOW that the plug-in is running properly, we can utilize it.

Video of use: https://youtu.be/ti18OABi80c 


1.	Select the project for which Metrics have to be calculated; right-click it and choose Properties
2.	In the Properties window, select Metrics from the left pane
3.	Check the Enable Metrics checkbox and click OK
4.	From the Menu Bar, Click Window → Show View → Other → Metrics → Metrics View and click OK
5.	From the Menu Bar, Click Project → Clean
a.	Metric values will be displayed, but these may not be accurate depending if the project has been changed, another project has been cleaned in the interim, etc
6.	Clean the project a second time.
a.	Now the values are accurate
7.	The CCD table will display the “complexity rating” for a given class.  A higher complexity rating indicates a greater need for refactoring.
a.	A developer may enable a flag in CCDCalculator.java to limit this value to 3, with 1 being low, 2 being medium, and 3 being high.
8.	The CCD1 table will indicate the number of metric “rules” that have been broken by each class.  The sum of these rules’ weighted complexity values is what is displayed in the CCD table.
9.	In order to see graphic view, choose graphic view from the Show View options
