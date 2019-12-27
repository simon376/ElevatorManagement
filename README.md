# ElevatorManagement
Final Assignment for Object-Oriented Concurrent Programming @ Universidade de Aveiro

 The application creates a simulation of people entering a building
 and using different elevators called via Messsage Passing Communication to move to their desired floors.
 The Elevator works through its queue of pending routines to bring the person to its destination.
 

-- Currently has a Programbreaking bug!
  the elevator removes a Routine from its queue without properly finishing it, 
  which leads to one person left waiting for the future to finish forever and everybody starves
