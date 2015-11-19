
package cn.nukkit.level.particle;

import cn.nukkit.block.Block;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.LevelEventPacket;

public class DestroyBlockParticle extends Particle{
	
	protected int data;

	public DestroyBlockParticle(Vector3 pos, Block b){
		super.setComponents(pos.x, pos.y, pos.z);
		this.data = b.getId() + (b.getDamage() << 12);
	}
	
	public DataPacket[] encode(){
		LevelEventPacket pk = new LevelEventPacket();
		pk.evid = LevelEventPacket.EVENT_PARTICLE_DESTROY;
		pk.x = (float) this.x;
		pk.y = (float) this.y;
		pk.z = (float) this.z;
		pk.data = this.data;
		
		return new DataPacket[]{pk};
	}
}
