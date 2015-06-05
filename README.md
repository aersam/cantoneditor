This is for Java OOP 2 Course at FHNW.

## Technologies used in this project

- Google GSON
- Swing
- JSplitButton Control

## Packages

### ch.fhnw.cantoneditor.model
Contains the actual data classes and some Swing Models

### ch.fhnw.cantoneditor.controller 
Contains controllers (MVC)

### ch.fhnw.cantoneditor.views
Responsible for displaying things

### ch.fhnw.cantoneditor.datautils
- Backend for the Database (GSON) 
- Translation
- CSV Reading (only done once for initializing data)
- Searching with Levenshtein

### ch.fhnw.cantoneditor.main
The entry point for the program, not much more

### ch.fhnw.command
Responsible for Undo/Redo Functionality

### fh.fhnw.observation
- Classes for observing Values: ObservableValue, ObservableList and ObservableSet
- ComputedValue class for automatic Dependency Tracking. This is inspired by [KnockoutJs](http://knockoutjs.com/documentation/computed-dependency-tracking.html)
- Class for mapping SwingControls to Observables: SwingObservables
 
### ch.fhnw.oop.led
The LED Displayer class

### ch.fhnw.oop.nixienumber
The Number Displayer stuff

### ch.fhnw.oop.splitflap
A Splitflap Control

### org.gpl.jsplitbutton
A Splitbutton Control 