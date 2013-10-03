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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;

public class ModelPath {

	private EObject sourceObject;
	private EObject targetObject;

	private List<ModelPathEdge> edges = new LinkedList<>();

	public ModelPath(EObject sourceObject, EObject targetObject) {
		super();
		this.sourceObject = sourceObject;
		this.targetObject = targetObject;
	}

	public EObject getSourceObject() {
		return sourceObject;
	}

	public EObject getTargetObject() {
		return targetObject;
	}

	public int getDistance() {
		int distance = 0;
		for (ModelPathEdge edge : edges)
			distance += edge.getDistance();
		return distance;
	}

	public List<ModelPathEdge> getEdges() {
		return Collections.unmodifiableList(edges);
	}

	public List<ModelPathEdge> getReversedEdges() {
		List<ModelPathEdge> reverseList = new LinkedList<>(edges);
		Collections.reverse(reverseList);
		return Collections.unmodifiableList(reverseList);
	}

	protected void addEdge(ModelPathEdge edge) {
		if (edge != null)
			edges.add(0, edge);
	}

	public boolean isReachable() {
		return !edges.isEmpty() && edges.get(0).getSource() == sourceObject;
	}

}
