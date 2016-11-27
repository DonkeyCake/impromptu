#version 330

out vec4 blurredMask;

uniform sampler2D u_texture;
uniform vec2 u_resolution;
uniform int u_iterations = 1;
uniform float u_strength = 1;
uniform float u_scalar = 4;

in vec2 TexCoords;

vec4 blur13(sampler2D image, vec2 uv, vec2 resolution, vec2 direction) {
  vec4 color = vec4(0.0);
  vec2 off1 = vec2(1.411764705882353) * direction;
  vec2 off2 = vec2(3.2941176470588234) * direction;
  vec2 off3 = vec2(5.176470588235294) * direction;
  color += texture2D(image, uv) * 0.1964825501511404;
  color += texture2D(image, uv + (off1 / resolution)) * 0.2969069646728344;
  color += texture2D(image, uv - (off1 / resolution)) * 0.2969069646728344;
  color += texture2D(image, uv + (off2 / resolution)) * 0.09447039785044732;
  color += texture2D(image, uv - (off2 / resolution)) * 0.09447039785044732;
  color += texture2D(image, uv + (off3 / resolution)) * 0.010381362401148057;
  color += texture2D(image, uv - (off3 / resolution)) * 0.010381362401148057;
  return color;
}

vec4 blur9(sampler2D image, vec2 uv, vec2 resolution, vec2 direction) {
  vec4 color = vec4(0.0);
  vec2 off1 = vec2(1.3846153846) * direction;
  vec2 off2 = vec2(3.2307692308) * direction;
  color += texture2D(image, uv) * 0.2270270270;
  color += texture2D(image, uv + (off1 / resolution)) * 0.3162162162;
  color += texture2D(image, uv - (off1 / resolution)) * 0.3162162162;
  color += texture2D(image, uv + (off2 / resolution)) * 0.0702702703;
  color += texture2D(image, uv - (off2 / resolution)) * 0.0702702703;
  return color;
}


vec4 blur5(sampler2D image, vec2 uv, vec2 resolution, vec2 direction) {
  vec4 color = vec4(0.0);
  vec2 off1 = vec2(1.3333333333333333) * direction;
  color += texture2D(image, uv) * 0.29411764705882354;
  color += texture2D(image, uv + (off1 / resolution)) * 0.35294117647058826;
  color += texture2D(image, uv - (off1 / resolution)) * 0.35294117647058826;
  return color;
}

bool isNearEdge(sampler2D tex, vec4 samp, vec2 pos, int thr) {
    vec2 tex_offset = 1.0 / textureSize(u_texture, 0);
    vec2 t = tex_offset * thr;

    float minX = clamp(pos.x - t.x, 0, 1);
    float maxX = clamp(pos.x + t.x, 0, 1);
    float minY = clamp(pos.y - t.y, 0, 1);
    float maxY = clamp(pos.y + t.y, 0, 1);

    for(float tmpX = minX; tmpX <= maxX; tmpX++) {
        for(float tmpY = minY; tmpY <= maxY; tmpY++) {
            vec4 s = texture(tex, vec2(tmpX, tmpY));
            if(s.rgb != samp.rgb) return true;
        }
    }
    return false;
}

void main() {
    vec4 col = texture(u_texture, TexCoords);
    for(int i = 0; i < u_iterations; i++) {
        vec4 diff = vec4(0.0);
        diff += blur13(u_texture, TexCoords, u_resolution, vec2( 0.0,  1.0) * u_strength);
        diff.rgb += blur13(u_texture, TexCoords, u_resolution, vec2( 1.0,  0.0) * u_strength).rgb;
        diff.rgb /= u_scalar;
        col.rgb += diff.rgb;
        col.a = diff.a;
    }
    blurredMask = col;
}