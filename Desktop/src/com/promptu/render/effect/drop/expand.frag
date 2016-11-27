#version 330

out vec4 finalCol;

in vec4 Color;
in vec4 Position;
in vec2 TexCoords;

uniform sampler2D u_texture;

// This approximates the error function, needed for the gaussian integral
vec4 erf(vec4 x) {
  vec4 s = sign(x), a = abs(x);
  x = 1.0 + (0.278393 + (0.230389 + 0.078108 * (a * a)) * a) * a;
  x *= x;
  return s - s / (x * x);
}

// Return the mask for the shadow of a box from lower to upper
float boxShadow(vec2 lower, vec2 upper, vec2 point, float sigma) {
  vec4 query = vec4(point - lower, upper - point);
  vec4 integral = 0.5 + 0.5 * erf(query * (sqrt(0.5) / sigma));
  return (integral.z - integral.x) * (integral.w - integral.y);
}

bool isNearEdge(sampler2D img, vec3 c, float x, float y, vec2 threshold) {
    float minX = clamp(x - threshold.x, 0, 1);
    float maxX = clamp(x + threshold.x, 0, 1);
    float minY = clamp(y - threshold.y, 0, 1);
    float maxY = clamp(y + threshold.y, 0, 1);
    for(float tmpX = minX; tmpX <= maxX; tmpX++) {
        for(float tmpY = minY; tmpY <= maxY; tmpY++) {
            vec4 s = texture(img, vec2(tmpX, tmpY));
            if(s.rgb != c) return true;
        }
    }
    return false;
}

void main() {
    vec4 selfCol = texture(u_texture, TexCoords);
    vec4 col = selfCol;
    vec2 tex_offset = 1.0 / textureSize(u_texture, 0);

    if(isNearEdge(u_texture, selfCol.rgb, TexCoords.x, TexCoords.y, tex_offset*4))
        col = vec4(1.0, 0.0, 0.0, 1.0);


    finalCol = col;
}
