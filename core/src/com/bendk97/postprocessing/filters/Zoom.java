/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.postprocessing.filters;

import com.bendk97.postprocessing.utils.ShaderLoader;

public final class Zoom extends Filter<Zoom> {
	private float x, y, zoom;

	public enum Param implements Parameter {
		// @formatter:off
		Texture("u_texture0", 0), OffsetX("offset_x", 0), OffsetY("offset_y", 0), Zoom("zoom", 0), ;
		// @formatter:on

		private final String mnemonic;
		private final int elementSize;

		Param(String mnemonic, int arrayElementSize) {
			this.mnemonic = mnemonic;
			this.elementSize = arrayElementSize;
		}

		@Override
		public String mnemonic () {
			return this.mnemonic;
		}

		@Override
		public int arrayElementSize () {
			return this.elementSize;
		}
	}

	public Zoom () {
		super(ShaderLoader.fromFile("zoom", "zoom"));
		rebind();
		setOrigin(0.5f, 0.5f);
		setZoom(1f);
	}

	/** Specify the zoom origin, in normalized screen coordinates. */
	public void setOrigin (float x, float y) {
		this.x = x;
		this.y = y;
		setParams(Param.OffsetX, this.x);
		setParams(Param.OffsetY, this.y);
		endParams();
	}

	public void setZoom (float zoom) {
		this.zoom = zoom;
		setParam(Param.Zoom, this.zoom);
	}

	public float getZoom () {
		return zoom;
	}

	public float getOriginX () {
		return x;
	}

	public float getOriginY () {
		return y;
	}

	@Override
	public void rebind () {
		// reimplement super to batch every parameter
		setParams(Param.Texture, u_texture0);
		setParams(Param.OffsetX, x);
		setParams(Param.OffsetY, y);
		setParams(Param.Zoom, zoom);
		endParams();
	}

	@Override
	protected void onBeforeRender () {
		inputTexture.bind(u_texture0);
	}
}
