/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.postprocessing.filters;
import com.bendk97.postprocessing.utils.ShaderLoader;


/** Bias filter. Adapted for lensflare2 effect.
 * @see <a
 *      href="http://john-chapman-graphics.blogspot.co.uk/2013/02/pseudo-lens-flare.html">http://john-chapman-graphics.blogspot.co.uk/2013/02/pseudo-lens-flare.html</a>
 * @author Toni Sagrista */
public final class Bias extends Filter<Bias> {
	private float bias;

	public enum Param implements Parameter {
		// @formatter:off
		Texture("u_texture0", 0), Bias("u_bias", 0);
		// @formatter:on

		private String mnemonic;
		private int elementSize;

		private Param (String mnemonic, int elementSize) {
			this.mnemonic = mnemonic;
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

	public Bias () {
		super(ShaderLoader.fromFile("screenspace", "bias"));
		rebind();
	}

	public void setBias (float bias) {
		this.bias = bias;
		setParam(Param.Bias, this.bias);
	}

	public float getBias () {
		return bias;
	}

	@Override
	protected void onBeforeRender () {
		inputTexture.bind(u_texture0);
	}

	@Override
	public void rebind () {
		setParams(Param.Texture, u_texture0);
		setBias(this.bias);
	}
}
