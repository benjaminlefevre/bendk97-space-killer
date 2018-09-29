/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.postprocessing.effects;

import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.bendk97.postprocessing.filters.NfaaFilter;

/** Implements the normal filter anti-aliasing. Very fast and useful for combining with other post-processing effects.
 * @author Toni Sagrista */
public final class Nfaa extends Antialiasing {
	private NfaaFilter nfaaFilter = null;

	/** Create a NFAA with the viewport size */
	public Nfaa (int viewportWidth, int viewportHeight) {
		setup(viewportWidth, viewportHeight);
	}

	private void setup (int viewportWidth, int viewportHeight) {
		nfaaFilter = new NfaaFilter(viewportWidth, viewportHeight);
	}

	public void setViewportSize (int width, int height) {
		nfaaFilter.setViewportSize(width, height);
	}

	@Override
	public void dispose () {
		if (nfaaFilter != null) {
			nfaaFilter.dispose();
			nfaaFilter = null;
		}
	}

	@Override
	public void rebind () {
		nfaaFilter.rebind();
	}

	@Override
	public void render (FrameBuffer src, FrameBuffer dest) {
		restoreViewport(dest);
		nfaaFilter.setInput(src).setOutput(dest).render();
	}
}
