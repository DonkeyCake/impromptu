#version 330

out vec4 finalCol;

in vec4 Color;
in vec4 Position;
in vec2 TexCoords;

uniform sampler2D u_mask;
uniform sampler2D u_blur;
uniform float u_strength = 10;

void main() {
    vec4 mask = texture(u_mask, TexCoords);
    vec4 blur = texture(u_blur, TexCoords);
    if(mask.r > 0.95 && mask.g > 0.95 && mask.b > 0.95)
        discard;
    if(blur.r < 0.01 && blur.g < 0.01 && blur.b < 0.01 && blur.a < 0.01)
        discard;
    blur.a = (blur.r + blur.g + blur.b) / 3;
    blur.rgb = vec3(0.0);
    blur.a *= u_strength;
	finalCol = blur;
}
