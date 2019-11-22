# Spell Checker [OUI - 2019]

[![made-with-java](https://img.shields.io/badge/Made%20with-Java-1f425f.svg)](https://www.python.org/) [![Ask Me Anything !](https://img.shields.io/badge/Ask%20me-anything-1abc9c.svg)](https://GitHub.com/Naereen/ama) [![Build Status](https://img.shields.io/badge/license-Coffeware-yellowgreen.svg)](https://img.shields.io/badge/license-Coffeware-yellowgreen.svg)

## Spell Checker implementation for DSA couse. 



- :japan: Custom HashMap implementation 
    
- :palm_tree: Okasaki RedBlack Trees
    
- :grey_question: Basically hashes one file as dictionary and checks other file with it
    
 

## What is it you say?

**This project tries to accomplish:**

- :flushed: Adhering to OOP programming style [*~40%*]
- :relieved: Implementing custom HashMap [*~90%*]
- :anguished: Implementing custom RedBlack Tree [*~60%*]
- :smiley: Basic file I/O usability [*~80%*]

**Implementation details:**

- **MajellaMap** - Open adressed HashMap
	- Uses Round Robin linear probing with log2n limit
	- Uses MurMurHash3 to hash *(currently can hash only strings)*
	- Uses Fibbonachi Hashing to map the values to current size
	- Operates with immutable map cells
	- *Doesn't know how to decrease its size after expanding*

- **OkasakiRBTree** - Okasaki funcitonal RedBlack Tree
	- Follows Chris's original algorithms for addition
	- Sadly is NOT, fully functional, uses highly mutable objects as nodes

**Here is my excuse for design desicions:**

![Deadline](https://melmeric.files.wordpress.com/2011/02/codequality.png) 



## Resourses 

Here are the most crucial acrticles, that I've used to understand the topics:

* [I Wrote The Fastest Hashtable] - Insiration on HashMap desing, a lot of cool info. besides that. 
* [Backward Shift Deletion] - Helped visualize the swaps in Robin Hood hashning mechanism
* [The Missing Method] - Good explanation for deletion method from Okasaki RB Trees
* [Algorithms Visualisation] - Red Black Tree Visualisation
* [Gianmarco De Francisci Morales] -  Author of hard hitting pic. in description : )

## How to run it

Use command line to show the program location of dictionary and text file

```sh
$ java -jar spell-checker-1.0.jar text_file dictionary_file
```
It will then generate the output file

   [I Wrote The Fastest Hashtable]: <https://probablydance.com/2017/02/26/i-wrote-the-fastest-hashtable>
   [Algorithms Visualisation]: <https://www.cs.usfca.edu/~galles/visualization/RedBlack.html>
   [The Missing Method]:  <http://matt.might.net/articles/red-black-delete/>
   [Backward Shift Deletion]: <http://codecapsule.com/2013/11/17/robin-hood-hashing-backward-shift-deletion/>
   
   [Gianmarco De Francisci Morales]: <https://gdfm.me/author/melmeric/>

