
package cn.nukkit.level.particle;

import cn.nukkit.math.Vector3;

public class SmokeParticle extends GenericParticle{
	public SmokeParticle(Vector3 pos){
		this(pos, 0);
	}

	public SmokeParticle(Vector3 pos, int scale){
		super(pos, Particle.TYPE_SMOKE, scale);
	}
}
