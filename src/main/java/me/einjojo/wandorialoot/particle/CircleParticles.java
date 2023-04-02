package me.einjojo.wandorialoot.particle;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import me.einjojo.wandorialoot.WandoriaLoot;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;


public class CircleParticles {




    public void spawnCircle(Player target, Location location, double radius, Particle particle, int particleCount) {

        double increment = 2 * Math.PI / 20; // Angle between points
        double angle = 0;
        for (int i = 0; i < 20; i++) {
            double x = location.getX() + radius * Math.cos(angle);
            double y = location.getY();
            double z = location.getZ() + radius * Math.sin(angle);
            PacketContainer packet = particlePacket(particle, (float) x, (float) y, (float) z, particleCount);

            WandoriaLoot.getInstance().getProtocolManager().sendServerPacket(target, packet);


            angle += increment;
        }
    }

    private PacketContainer particlePacket(Particle particle, double x, double y, double z, int particleCount) {

        PacketContainer packet = new PacketContainer(PacketType.Play.Server.WORLD_PARTICLES);
        packet.getParticles().write(0, EnumWrappers.Particle.valueOf(particle.toString()));
        packet.getBooleans().write(0, false);
        packet.getDoubles().write(0,  x);
        packet.getDoubles().write(1,  y);
        packet.getDoubles().write(2,  z);
        packet.getFloat().write(0, 0f); // offsetX
        packet.getFloat().write(1, 0f); // offsetY
        packet.getFloat().write(2, 0f); // offsetZ
        packet.getFloat().write(3, 0f); // particleData
        packet.getIntegers().write(0, particleCount);

        return packet;
    }

}
