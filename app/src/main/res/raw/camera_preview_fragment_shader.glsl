#extension GL_OES_EGL_image_external : require

precision mediump float;

uniform samplerExternalOES u_TextureUnit;

varying vec2 v_TextureCoordinate;

void main() {
	gl_FragColor = texture2D(u_TextureUnit, v_TextureCoordinate);
	//gl_FragColor = vec4(1, 1, 1, 1);
}
