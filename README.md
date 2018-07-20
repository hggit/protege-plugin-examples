This Protege plugin helps in managing Aristotelian ontologies.

File Structure:
The "AO-core" folder has the ACE API code.
The "tab" folder has the "AristotleTab.java" file which implements the Aristotle tab.
The "view" folder has the file "AttributesView.java" which generates the details of a class using "DisplayAttributes.java".
The "resources" folder has "plugin.xml" and "viewconfig-aristotletab.xml" which set the layout of the tab and views.

Below are the guidelines to run the plugin.




# protege-plugin-examples

This repository contains example code for developing a tab, view, or menu plug-in for the Protege Desktop ontology editor (*versions 5.0.0 and higher*).  The Maven POM file in the top-level directory demonstrates one possible method for packaging plug-in code into the required OSGi bundle format using the [Maven Bundle Plugin](http://felix.apache.org/site/apache-felix-maven-bundle-plugin-bnd.html).

#### Prerequisites

To build and run the examples, you must have the following items installed:

+ Apache's [Maven](http://maven.apache.org/index.html).
+ A tool for checking out a [Git](http://git-scm.com/) repository.
+ A Protege distribution (5.0.0 or higher).  The Protege 5.2.0 release is [available](http://protege.stanford.edu/products.php#desktop-protege) from the main Protege website. 

#### Build and install example plug-ins

1. Get a copy of the example code:

        git clone https://github.com/protegeproject/protege-plugin-examples.git protege-plugin-examples
    
2. Change into the protege-plugin-examples directory.

3. Type mvn clean package.  On build completion, the "target" directory will contain a protege.plugin.examples-${version}.jar file.

4. Copy the JAR file from the target directory to the "plugins" subdirectory of your Protege distribution.
 
#### View example plug-ins in Protege

Launch your Protege distribution.  Select About from the Help menu to verify successful installation:

![Protege Desktop About box](http://jvendetti.github.io/img/protege/protege%20about%20box.png)

The examples bundle contains:

+ Two custom tabs - "Example Tab" and "Example Tab (2)".  Enable either tab via the Window | Tabs menu.
+ One custom view - "Example view component".  If you enabled the Example Tab in the previous step, the Example view component will be visible on the right-hand side.  Alternatively, you can enable the view via Window | Views | Ontology views.
+ Several custom menu items.  Expand the Tools menu to see the custom menu items.
+ A custom top-level menu - "Example Menu".  The custom top-level menu appears in the main menu bar between the Server and Tools menus.  Select Example Menu to see several submenu items.
 
#### Example plug-in screenshots

Example Tab and Example view component:

![](http://jvendetti.github.io/img/protege/example-view-component.png)

Example Tab (2):

![](http://jvendetti.github.io/img/protege/example-tab.png)

Example menu items:

![](http://jvendetti.github.io/img/protege/example-menu-items.png)

Example menu:

![](http://jvendetti.github.io/img/protege/example-menu.png)

#### Questions

If you have questions about developing Protege plug-ins, please navigate to the main Protege website and subscribe to the [Protege Developer Support mailing list](http://protege.stanford.edu/support.php#mailingListSupport).  After subscribing, send messages to protege-dev at lists.stanford.edu.

