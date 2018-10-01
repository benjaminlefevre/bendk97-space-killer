
/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.postprocessing.effects;

import com.bendk97.postprocessing.PostProcessorEffect;

abstract class Antialiasing extends PostProcessorEffect {

	public abstract void setViewportSize (int width, int height);
}
