package cn.nukkit.network.protocol;

/**
 * @author Nukkit Project Team
 */
public class AnimatePacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.ANIMATE_PACKET;

    public long eid;
    public int action;

    @Override
    public void decode() {
        this.action = (int) this.getUnsignedVarInt();
        this.eid = getEntityId();
    }

    @Override
    public void encode() {
        this.reset();
        this.putUnsignedVarInt(action);
        this.putEntityId(eid);
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

}
