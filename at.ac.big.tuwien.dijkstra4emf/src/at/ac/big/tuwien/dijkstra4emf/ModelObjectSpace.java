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

import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

public class ModelObjectSpace {

	private Resource resource;
	private Set<EObject> initialObjectSet;

	public ModelObjectSpace(Resource modelResource) {
		this.resource = modelResource;
		initializeObjectSet();
	}

	private void initializeObjectSet() {
		initialObjectSet = new HashSet<EObject>();
		for (TreeIterator<EObject> iter = resource.getAllContents(); iter
				.hasNext();)
			initialObjectSet.add(iter.next());
	}

	public boolean isInitialObject(EObject eObject) {
		return initialObjectSet.contains(eObject);
	}

	public boolean isNewObject(EObject eObject) {
		return !isInitialObject(eObject);
	}

	public ModelPath findShortestPath(EObject source, EObject target) {
		DijkstraAlgorithm algorithm = new DijkstraAlgorithm(source);
		return algorithm.findShortestPath(target);
	}

}
