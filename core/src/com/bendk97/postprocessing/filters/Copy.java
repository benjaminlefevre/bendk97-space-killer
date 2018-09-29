/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.postprocessing.filters;
import com.bendk97.postprocessing.utils.ShaderLoader;

public class Copy extends Filter<Copy> {
	public enum Param implements Parameter {
		// @formatter:off
		Texture0("u_texture0", 0), ;
		// @formatter:on

		private final String mnemonic;
		private int elementSize;

		private Param (String m, int elementSize) {
			this.mnemonic = m;
			this.elementSize = elementSize;
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

	public Copy () {
		super(ShaderLoader.fromFile("screenspace", "copy"));
	}

	@Override
	public void rebind () {
		setParam(Param.Texture0, u_texture0);
	}

	@Override
	protected void onBeforeRender () {
		inputTexture.bind(u_texture0);
	}
}
