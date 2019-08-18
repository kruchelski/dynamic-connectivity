# Activity 01 - Dynamic Connectivity  

## This activity is part of the evaluation for 'Data Structures 2' course  
---  
### The Dynamic Connectivity problem:  
**Dynamic Connectivity**: Input is a sequence of integer pairs, where each integer represents an object of some kind and we interpret the pair `p q` as "`p` is connected to `q`". We assume that "being connected to" is an equivalence relationship:  

- *Symmetrical*: if `p` is connected to `q`, then `q` is connected to `p`;  
- *Transitive*: if `p` is connected to `q` and `q` is connected to `r`, then `p` is connected to `r `;  
- *Reflective*: `p` is connected to `p`. 

An equivalence relationship partitions objects into equivalence classes or related components (or only components or groups).
In this case, two items are in the same component if and only if they are connected.  

We can identify both items and components by integers between 0 and N-1.  

Initially, there are N independent components (no connections), with each item in its own component. The identifier of a component is one of the items that compose it. Two items have the same component identifier if and only if they are part of the same component.  

The objective is to write a program to filter external pairs in a sequence:
when the program reads a pair `p q` from standard input, it must write the pair itself that was read from standard output only if the pairs read so far do not imply that `p` is connected to `q`. If the pairs read so far imply that `p` is connected to `q`, so the program should not print anything and proceed to the next pair.  

Below is the Union-Find API. It encapsulates the basic operations we need:

```java
         UF(int N)               // initializes N items with integer names (0 to N-1)
   void  union(int p, int q)     // add connection between p and q
    int  find(int p)             // identifies the component of p (0 to N-1)
boolean  connected(int p, int q) // returns true if p and q are in the same component
    int  count()                 // returns the number of components
```

The identifier of a component can change only from a call to the `union` method. This identifier **cannot** be changed by the `find`, `connected` or `count` methods.  

To test the above API, the `main()` method in the UF2.java file resolves the problem of dynamic connectivity. The challenge here is to implement the other functions (`find()`, `connected()` and `union()`) to get the best results possible (in this case best results are printing the right pairs, counting the components and finishing in the shortest time possible).
Data to test the program is also available:

The tinyUF.txt file contains 11 connections and at the end is supposed to have 2 components
The mediumUF.txt file contains 900 connections and at the end is supposed to have 3 components
The largeUF.txt file contains millions of connections and at the end is supposed to have 6 components;  

### Explanation of the implemented algorithm:
The algorithm is based on the following principles:

- Using the idea of ​​the concept of pointers in C (in this case, an element of a group will "point" to another element that will be used as a "reference" within the group).
- Identifying which group each element belongs to does not need to be up to date all the time, but needs a mechanism that enables identification based on the element that serves as the group's reference
- Within a group, the reference value that serves as the group identification is the value of the smallest element in the group and therefore the implementation of the functions is always based on the smallest value.
- The array that is created and initialized in the algorithm is the very universe of elements and groups: the array indices represent each element and the array content at each position is an integer value representing which group such element is part of.
- For two elements to be connected, they just have to be in the same group, i.e. `id[p] == id[q]` (taking `id[]` as the **int array** that is created at the beginning of the algorithm).  

**Explanation of Methods (Functions):**. 
As we assume that it is not necessary to keep updated in the array the number that identifies which group that element is part of, but to have this value updated only when necessary (i.e. when the `find(p)` function is called), this function '`find()`' implements logic to update this group information of the element being fetched. The only element that always needs to be up to date is the element being used as a reference in a group, because if it is no longer the reference (when that group is linked to a lower value element), the other elements of the group must have a way of knowing what the new group value is. Therefore, the main mechanisms in the algorithm are:

- '`union(int p, int q)`' method: checks which of the elements passed as an argument have the lowest group identifier value, so that it will be possible to change the identifying value of the right group (whether this is a unitary group or a group with multiple elements ). Before changing the group ID of `p` or `q` this method makes the same change to the element that was referencing that element to be changed. **For example**: suppose there is a group with elements `3` and `4` and `8`. In this group the group identifier is `3`, so `id[3] == 3`, `id[4] == 3` and `id[8] == 3`. Assuming that a link between `2` (which belongs to its own unitary group `2`) and `4` is made, the method will verify that `2` has the smallest stored value and will change the reference to `4` (i.e. `id[4]` will be equal to `2`). But before doing so, the function changes the value of the reference element of `4`, i.e. the `id[4]` element, which has the value of the group `id[id[4]]`, which in this case would be `id[3]`, ie `3`. Therefore, the element `3` that used to reference the group and "*pointed to itself*" will now point to `2`, i.e. `id[3] == id[2]` which is equal to `2`. Thus, element `4` had its value changed and the reference element also had its value changed. One detail is that the element `8`, which is part of the group has not yet been updated and will only be when it is requested to identify the group (in the case of this algorithm when the `find()` method is executed).

- `connected(int p, int q)` method: Within the framework of this algorithm, the usage of this method will ensure that only unconnected pairs are connected. In this method there is an initial check to know which of the two numbers, `p` or `q`, has the smallest group identification. If passed to the function in reverse, i.e. `id[q] < id[p]` the method swaps, as this ensures that elements with smaller component identifiers are always updated before larger ones because components are always referenced with the lowest value.

- `find(int p)` method - What this method does is to check if the group identifier of the element passed to the method is equal to the group identifier that the element is referencing (i.e. the element that is supposed to be the smallest in the group and therefore the one that identifies the group). If not, it means that the reference has been changed (due to some connection that has occurred) and the new group reference should be searched. For this happens, it calls the method again recursively, but then passing this value that was once the reference. When the method "arrives" at the group reference, it starts the `return` by passing the number of this current reference that updates all group elements. Here's an example:

Starting from the 10-position array, just after it is started, that is, the value of the array in each index is the index value itself (each element within its own unitary group), the following connections occur:
1. `8` and `4`: `id[4] == 4` and `id[8] == 4`;
2. `3` and `4`: `id[3] == 3` and `id[4] == 3`;
3. `4` and `5`: `id[4] == 3` and `id[5] == 3`;
4. `2` and `5`: `id[2] == 2` and `id[5] == 2` and, in addition, the `union` method causes the old reference of `5` to update as well, i.e. `id[3] == 2`. It is important to notice that the other elements of the group have not been updated. So at this moment we have:
`2`, `3`, `4`, `5`, and `8` all in the same group (logically speaking), but in practice their group identifier values ​​are as follows: `id[2] == 2`, `id[3] == 2`, `id[ 4] == 3`, `id[5] == 2` and `id[8] == 4`;
5. Assuming now that it is checked if the `8` and `2` are connected. This will call the `connected(2, 8)` method which will call the `find` method for both numbers. When calling `find(2)` the function verifies that `2` points to itself, i.e. `id[2] == 2` and therefore `id[id[2]] == 2` and simply returns the value `2`. But when calling the method `find()` passing `8` as an argument, i.e. `find(8)` what happens is as follows:
1. `id[8] == 4` and then compare with `id[id[8]]`, i.e. compare with `id[4]`.
2. Like `id[4] == 3` and therefore `id[8]! = id[id[8]]` the method `find()` is called again, but this time passing `id[8]` as an argument, i.e. `4`.
3. When calling `find(4)`, it checks that `id[4]! = id[id[4]]`, since `id[4] == 3` and `id[id[4]]` (ie `id[3]`) is `2`. Therefore, it recursively calls the `find` method, but this time passing `id[4]`, i.e. `3`.
4. When calling `id[4]` (`3` in this case) the method checks that if `id[3] == id[id[3]]`, i.e. `3` is up to date with the group reference, which in this case is `2`. Thus, it starts the "cascade" `returns` by returning `2` for `id[4]` and consequently for `id[8]` and thus updating the group values ​​starting from that initial value (`8`) and checking smaller ones.
*Note*: Assuming that before all the connections listed in this example, there had been a connection between `8` and `9`, i.e. `id[9] == 8`, when `find(8)` was called, the reference of `9` would not be updated, but, if after `find(8)` were executed a `find(9)` would be updated with only one iteration.

After reading the total value of components at the beginning of the algorithm, a `count` variable counts how many groups are left. Each time a connection is made, this value decreases by 1. Therefore, the control of the total components at the end is left to the `count` attribute.

At the end of the algorithm, there will logically be the number of groups indicated by `count`, but in practice the identification of groups could point to another value, that is, assuming that there are `3 components` left (according to `count`) if the group indication was verified in each one would have more groups because group IDs only update when you need to know the group of an element. That is, if after the algorithm were asked which group each element is part of, the universe of this algorithm would have been updated as expected, i.e., in this hypothetical situation, there would be `3 components`.  

### Final considerations:
This algorithm is for analysis purposes, so it is not failsafe. This means that if it is not executed with a `.txt` file (like the ones in this repository), the standard input will be the keyboard and the program will stop working if `char` or `string` inputs differ from numbers or if a number beyond the array index is passed to make the union.

To run the code it is important to point one of the files provided as the stdin. To do this simple run in the terminal:
`java UF2 < name_of_the_file.txt`. For example: `java UF2 < largeUF.txt`



