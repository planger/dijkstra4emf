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

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

public class ModelPathEdge {

	private int distance = -1;
	private EObject source;
	private EReference reference;
	private EObject target;
	private int index;

	public ModelPathEdge(EObject source, EReference reference, EObject target) {
		this.source = source;
		this.reference = reference;
		this.target = target;
		this.index = obtainIndex();
	}

	private int obtainIndex() {
		if (reference.isMany()) {
			return ((List<?>) source.eGet(reference)).indexOf(target);
		} else {
			return 0;
		}
	}

	public int getIndex() {
		return index;
	}

	public int getDistance() {
		if (distance == -1) {
			distance = computeDistance();
		}
		return distance;
	}

	/**
	 * This method can be overwritten by subclasses to customize the distance of
	 * the {{@link #getReference() reference}.
	 * 
	 * By default, the distance is 1.
	 * 
	 * @return the distance.
	 */
	protected int computeDistance() {
		return 1;
	}

	public EReference getReference() {
		return reference;
	}

	public EObject getSource() {
		return source;
	}

	public EObject getTarget() {
		return target;
	}

}
