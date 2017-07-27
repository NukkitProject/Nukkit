package cn.nukkit.entity.item;

import cn.nukkit.entity.EntityInteractable;
import cn.nukkit.entity.EntityRideable;
import cn.nukkit.entity.data.IntEntityData;
import cn.nukkit.event.vehicle.VehicleCreateEvent;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public abstract class EntityVehicle extends EntityInteractable implements EntityRideable {

    public EntityVehicle(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    public int getRollingAmplitude() {
        return this.getDataPropertyInt(DATA_HURT_TIME);
    }

    public void setRollingAmplitude(int time) {
        this.setDataProperty(new IntEntityData(DATA_HURT_TIME, time));
    }

    public int getRollingDirection() {
        return this.getDataPropertyInt(DATA_HURT_DIRECTION);
    }

    public void setRollingDirection(int direction) {
        this.setDataProperty(new IntEntityData(DATA_HURT_DIRECTION, direction));
    }

    public int getDamage() {
        return this.getDataPropertyInt(DATA_HEALTH); // false data name (should be DATA_DAMAGE_TAKEN)
    }

    public void setDamage(int damage) {
        this.setDataProperty(new IntEntityData(DATA_HEALTH, damage));
    }

    @Override
    public String getInteractButton() {
        return "Mount";
    }

    @Override
    public boolean canDoInteraction() {
        return linkedEntity != null;
    }

    @Override
    public boolean onUpdate(int currentTick) {
        if (justCreated) {
            VehicleCreateEvent event = new VehicleCreateEvent(this);
            getServer().getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                kill();
                return false;
            }
            justCreated = false;
        }

        // The rolling amplitude
        if (getRollingAmplitude() > 0) {
            setRollingAmplitude(getRollingAmplitude() - 1);
        }
        // The damage token
        if (getDamage() > 0) {
            setDamage(getDamage() - 1);
        }
        // A killer task
        if (y < -16) {
            kill();
        }
        // Movement code
        updateMovement();
        return true;
    }

    public boolean performHurtAnimation(int damage) {
        // Vehicle does not respond hurt animation on packets
        // It only respond on vehicle data flags. Such as these
        setRollingAmplitude(10);
        setRollingDirection(-getRollingAmplitude());
        setDamage(getDamage() + damage);
        return true;
    }
}
