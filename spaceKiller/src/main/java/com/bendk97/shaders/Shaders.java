/*
 * Developed by Benjamin Lefèvre
 * Last modified 19/10/18 07:32
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.shaders;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import static com.bendk97.shaders.ShaderPrograms.fragmentShader_HIGHLIGHT;
import static com.bendk97.shaders.ShaderPrograms.vertexShader_HIGHLIGHT;

public class Shaders {

    public static ShaderProgram HIGHLIGHT = new ShaderProgram(vertexShader_HIGHLIGHT, fragmentShader_HIGHLIGHT);


}
