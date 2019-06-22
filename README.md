# continuum

Access and structure for events passing through time.
Think of a time-factored business concept (I originally designed this with a meeting in mind).  An event consistes of phases (or stages, statuses, if you like).
These can be defined in advance, like a meeting that has an open ended pre-meeting and post-meeting time phase, and a distinct closed meeting time.

Between phases are Boundaries, and at those Boundaries there are Events.  Events are processes that are launched at a given time.  
For example, the end of a meeting might trigger the posting of meeting notes, of even the generation of a sumamry of the meeting from notes.

Project Status:

Event Types are defined, the basic mechanisms appear to be in place.  Their use as an ongoing utility is not implemented (yet).
The object model is complete,as are the triggering activities.  

Needs: 

A REST interface is very partial and poorly structured at present
Serialization, Deserialization of object, shutdown and restart needed.
Charting, analysis search

