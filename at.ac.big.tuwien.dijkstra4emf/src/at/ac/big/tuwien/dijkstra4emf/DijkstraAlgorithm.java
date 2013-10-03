/*
 * Copyright (c) 2013 Vienna University of Technology.
 * All rights reserved. This program and the accompanying materials are made 
 * available under the terms of the Eclipse Public License v1.0 which accompanies 
 * this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Langer - initial API and implementation
 */
package at.ac.big.tuwien.dijkstra4emf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EContentsEList;
import org.eclipse.emf.ecore.util.EContentsEList.FeatureIterator;

/**
 * An implementation of Dijkstra's shortest path algorithm for EMF-based models.
 * 
 * This implementation is based on the implementation by Lars Vogel available at
 * http://www.vogella.com/articles/JavaAlgorithmsDijkstra/article.html
 * 
 * @author Philip Langer (langer@big.tuwien.ac.at)
 * 
 */
public class DijkstraAlgorithm {

	private EObject source;

	private Set<EObject> visitedObjects = new HashSet<EObject>();
	private Set<EObject> unvisitedObjects = new HashSet<EObject>();
	private Map<EObject, ModelPathEdge> predecessors = new HashMap<EObject, ModelPathEdge>();
	private Map<EObject, Integer> distance = new HashMap<EObject, Integer>();

	public DijkstraAlgorithm(EObject source) {
		this.source = source;
		execute();
	}

	public ModelPath findShortestPath(EObject target) {
		ModelPath path = new ModelPath(source, target);

		// stepping backwards from target to source
		ModelPathEdge nextEdge = predecessors.get(target);
		path.addEdge(nextEdge);
		while (nextEdge != null && source != nextEdge.getSource()) {
			nextEdge = predecessors.get(nextEdge.getSource());
			path.addEdge(nextEdge);
		}

		return path;
	}

	private void execute() {
		distance.put(source, 0);
		unvisitedObjects.add(source);
		while (unvisitedObjects.size() > 0) {
			EObject node = getNearestObject(unvisitedObjects);
			setVisited(node);
			findMinimalDistances(node);
		}
	}

	private EObject getNearestObject(Set<EObject> objects) {
		EObject nearest = !objects.isEmpty() ? objects.iterator().next() : null;
		for (EObject current : objects) {
			if (getKnownShortestDistance(current) < getKnownShortestDistance(nearest)) {
				nearest = current;
			}
		}
		return nearest;
	}

	private int getKnownShortestDistance(EObject destination) {
		if (distance.containsKey(destination)) {
			return distance.get(destination);
		} else {
			return Integer.MAX_VALUE;
		}
	}

	private void setVisited(EObject node) {
		visitedObjects.add(node);
		unvisitedObjects.remove(node);
	}

	private void setUnvisited(EObject node) {
		visitedObjects.remove(node);
		unvisitedObjects.add(node);
	}

	private void findMinimalDistances(EObject source) {
		List<ModelPathEdge> edges = getAllOutgoingEdges(source);
		for (ModelPathEdge currentEdge : edges) {
			if (isShorterThanKnownDistanceToTarget(currentEdge)) {
				recordEdge(currentEdge);
				setUnvisited(currentEdge.getTarget());
			}
		}
	}

	private void recordEdge(ModelPathEdge currentEdge) {
		EObject source = currentEdge.getSource();
		EObject target = currentEdge.getTarget();
		int newDistance = getKnownShortestDistance(source)
				+ currentEdge.getDistance();
		distance.put(target, newDistance);
		predecessors.put(target, currentEdge);
	}

	private boolean isShorterThanKnownDistanceToTarget(ModelPathEdge currentEdge) {
		return getKnownShortestDistance(currentEdge.getTarget()) > getKnownShortestDistance(currentEdge
				.getSource()) + currentEdge.getDistance();
	}

	private List<ModelPathEdge> getAllOutgoingEdges(EObject node) {
		List<ModelPathEdge> edges = new ArrayList<ModelPathEdge>();
		edges.addAll(getCrossReferenceEdges(node));
		edges.addAll(getContainmentReferenceEdges(node));

		if (hasReferenceToContainer(node)) {
			edges.add(getEdgeToContainer(node));
		}

		return edges;
	}

	private boolean hasReferenceToContainer(EObject node) {
		return node.eContainer() != null
				&& getReferenceToContainer(node) != null;
	}

	private EReference getReferenceToContainer(EObject node) {
		return node.eContainmentFeature().getEOpposite();
	}

	private ModelPathEdge getEdgeToContainer(EObject node) {
		if (hasReferenceToContainer(node)) {
			return createPathEdge(node, getReferenceToContainer(node),
					node.eContainer());
		}
		return null;
	}

	private List<ModelPathEdge> getCrossReferenceEdges(EObject node) {
		List<ModelPathEdge> edges = new ArrayList<ModelPathEdge>();
		for (EContentsEList.FeatureIterator<?> referenceIterator = getCrossReferenceIterator(node); referenceIterator
				.hasNext();) {
			EObject target = (EObject) referenceIterator.next();
			EReference eReference = (EReference) referenceIterator.feature();
			edges.add(createPathEdge(node, eReference, target));
		}
		return edges;
	}

	private FeatureIterator<?> getCrossReferenceIterator(EObject node) {
		return (EContentsEList.FeatureIterator<?>) node.eCrossReferences()
				.iterator();
	}

	private Collection<? extends ModelPathEdge> getContainmentReferenceEdges(
			EObject node) {
		List<ModelPathEdge> edges = new ArrayList<ModelPathEdge>();
		for (EObject child : node.eContents()) {
			EReference eReference = child.eContainmentFeature();
			edges.add(createPathEdge(node, eReference, child));
		}
		return edges;
	}

	private ModelPathEdge createPathEdge(EObject source, EReference eReference,
			EObject target) {
		return new ModelPathEdge(source, eReference, target);
	}

}
