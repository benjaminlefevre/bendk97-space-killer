/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.postprocessing.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public final class ShaderLoader {
	public static String BasePath = "";
	private static final boolean Pedantic = true;

	public static ShaderProgram fromFile( String vertexFileName, String fragmentFileName ) {
		return ShaderLoader.fromFile( vertexFileName, fragmentFileName, "" );
	}

	public static ShaderProgram fromFile( String vertexFileName, String fragmentFileName, String defines ) {
		String log = "\"" + vertexFileName + "/" + fragmentFileName + "\"";
		if( defines.length() > 0 ) {
			log += " w/ (" + defines.replace( "\n", ", " ) + ")";
		}
		log += "...";
		Gdx.app.log( "ShaderLoader", "Compiling " + log );

		String vpSrc = Gdx.files.internal( BasePath + vertexFileName + ".vertex" ).readString();
		String fpSrc = Gdx.files.internal( BasePath + fragmentFileName + ".fragment" ).readString();

		return ShaderLoader.fromString( vpSrc, fpSrc, vertexFileName, fragmentFileName, defines );
	}

	public static ShaderProgram fromString( String vertex, String fragment, String vertexName, String fragmentName ) {
		return ShaderLoader.fromString( vertex, fragment, vertexName, fragmentName, "" );
	}

	private static ShaderProgram fromString(String vertex, String fragment, String vertexName, String fragmentName, String defines) {
		ShaderProgram.pedantic = ShaderLoader.Pedantic;
		ShaderProgram shader = new ShaderProgram( defines + "\n" + vertex, defines + "\n" + fragment );

		if( !shader.isCompiled() ) {
			Gdx.app.error( "ShaderLoader", shader.getLog() );
			System.exit( -1 );
		}

		return shader;
	}

	private ShaderLoader() {
	}
}
