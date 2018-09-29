/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.postprocessing.filters;

import com.bendk97.postprocessing.utils.PingPongBuffer;

/** The base class for any multi-pass filter. Usually a multi-pass filter will make use of one or more single-pass filters,
 * promoting composition over inheritance. */
public abstract class MultipassFilter {
	public abstract void rebind ();

	public abstract void render (PingPongBuffer srcdest);
}
