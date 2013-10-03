Dijkstra's Algorithm For EMF Models
===================================

This is an [Eclipse](http://www.eclipse.org/) Plugin providing an API and implementation of
[Dijkstra's algorithm](http://en.wikipedia.org/wiki/Dijkstra%27s_algorithm)
for [EMF](http://www.eclipse.org/modeling/emf/ "Eclipse Modeling Framework")-based
models. Thus, it enables computing the shortest path from one EObject to another
EObject in terms of a list of links (references).

This implementation is generic in the sense that it operates on EObjects reflectively
to support *every* EMF-based model, irrespectively of the underlying metamodel. The
implementation can hence be used for Ecore models, UML models, etc.

How to use it
-------------

So let's say you have an EObject `obj1` and another EObject `obj2` and you want to
compute the shortest path going from `obj1` to `obj2` through links (either
containment-references or cross-references) among the EObjects in your model.
Therefore, you can use the following code to obtain the shortest path, which is
represented in terms of a sorted list of walked edges, whereas an edge represents
the respective source object, the reference (from the metamodel), and the target object.
```java
DijkstraAlgorithm algorithm = new DijkstraAlgorithm(obj1);
ModelPath shortestPath = algorithm.findShortestPath(obj2);

List<ModelPathEdge> edges = shortestPath.getEdges();
for (ModelPathEdge edge : edges) {
  System.out.print(edge.getIndex() + ": ");
  System.out.print(edge.getSource() + " - ");
  System.out.print(edge.Reference() + " - ");
  System.out.println(edge.getTarget());
}
```

Note that `obj1` and `obj2` do not necessarily have to be in the same model
(i.e., EMF resource). However, they have to be loaded in the same resource set.

Further Notes
-------------

This implementation has not been optimized with respect to runtime efficiency
(e.g., sorting, caching, etc.). If you need a more efficient implementation, we'd
be thrilled if you improve this implementation and let us know!

Also, we'd like to thank [Lars Vogel](http://www.vogella.com/people/larsvogel.html), who
published a
[general implementation of Dijkstra's algorithm](http://www.vogella.com/articles/JavaAlgorithmsDijkstra/article.html)
in Java, which served as an excellent basis for this implementation for EMF models.
