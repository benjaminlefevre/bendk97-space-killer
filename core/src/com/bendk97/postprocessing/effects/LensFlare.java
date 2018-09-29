/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.postprocessing.effects;

import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.bendk97.postprocessing.PostProcessorEffect;
import com.bendk97.postprocessing.filters.Lens;

/** Lens flare effect. */
public final class LensFlare extends PostProcessorEffect {
	private Lens lens = null;

	public LensFlare (int viewportWidth, int viewportHeight) {
		setup(viewportWidth, viewportHeight);
	}

	private void setup (int viewportWidth, int viewportHeight) {
		lens = new Lens(viewportWidth, viewportHeight);
	}

	public void setIntensity (float intensity) {
		lens.setIntensity(intensity);
	}

	public float getIntensity () {
		return lens.getIntensity();
	}

	public void setColor (float r, float g, float b) {
		lens.setColor(r, g, b);
	}

	/** Sets the light position in screen coordinates [-1..1].
	 * @param x Light position x screen coordinate,
	 * @param y Light position y screen coordinate. */
	public void setLightPosition (float x, float y) {
		lens.setLightPosition(x, y);
	}

	@Override
	public void dispose () {
		if (lens != null) {
			lens.dispose();
			lens = null;
		}
	}

	@Override
	public void rebind () {
		lens.rebind();
	}

	@Override
	public void render (FrameBuffer src, FrameBuffer dest) {
		restoreViewport(dest);
		lens.setInput(src).setOutput(dest).render();
	}
}
