package io.github.foundationgames.automobility.util.network;

import io.github.foundationgames.automobility.Automobility;
import io.github.foundationgames.automobility.automobile.attachment.rear.BannerPostRearAttachment;
import io.github.foundationgames.automobility.automobile.attachment.rear.ExtendableRearAttachment;
import io.github.foundationgames.automobility.entity.AutomobileEntity;
import io.github.foundationgames.automobility.platform.Platform;
import io.github.foundationgames.automobility.util.TriCons;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BannerPatternLayers;

import java.util.HashMap;
import java.util.Map;

public enum CommonPackets {;
    static final int AUTOMOBILE_SYNC_STATE_SIZE = 86;
    public static final Map<Identifier, TriCons<MinecraftServer, ServerPlayer, FriendlyByteBuf>> SERVERBOUND_HANDLERS = new HashMap<>();

    public static void registerReceiver(Identifier rl, TriCons<MinecraftServer, ServerPlayer, FriendlyByteBuf> run) {
        SERVERBOUND_HANDLERS.put(rl, run);
    }

    public static void sendClientboundAutomobileSyncPacket(AutomobileEntity entity, ServerPlayer player) {
        var buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeInt(entity.getId());
        entity.writeSyncStateData(buf);
        Platform.get().serverSendPacket(player, Automobility.rl("sync_automobile_data"), buf);
    }

    public static void sendSyncAutomobileAttachmentsPacket(AutomobileEntity entity, ServerPlayer player) {
        var buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeInt(entity.getId());
        buf.writeUtf(entity.getRearAttachmentType().id().toString());
        buf.writeUtf(entity.getFrontAttachmentType().id().toString());
        Platform.get().serverSendPacket(player, Automobility.rl("sync_automobile_attachments"), buf);
    }

    public static void sendBannerPostAttachmentUpdatePacket(AutomobileEntity entity, DyeColor baseColor, BannerPatternLayers layers, ServerPlayer player) {
        var buf = new RegistryFriendlyByteBuf(Unpooled.buffer(), entity.level().registryAccess());

        if (entity.getRearAttachment() instanceof BannerPostRearAttachment) {
            buf.writeInt(entity.getId());
            if (baseColor != null && layers != null) {
                buf.writeBoolean(true);
                DyeColor.STREAM_CODEC.encode(buf, baseColor);
                BannerPatternLayers.STREAM_CODEC.encode(buf, layers);
            } else {
                buf.writeBoolean(false);
            }
            Platform.get().serverSendPacket(player, Automobility.rl("update_banner_post"), buf);
        }
    }

    public static void sendExtendableAttachmentUpdatePacket(AutomobileEntity entity, boolean extended, ServerPlayer player) {
        var buf = new FriendlyByteBuf(Unpooled.buffer());

        if (entity.getRearAttachment() instanceof ExtendableRearAttachment) {
            buf.writeInt(entity.getId());
            buf.writeBoolean(extended);
            Platform.get().serverSendPacket(player, Automobility.rl("update_extendable_attachment"), buf);
        }
    }

    public static void init() {
        CommonPackets.registerReceiver(Automobility.rl("sync_automobile_data"), (server, player, buf) -> {
            if (buf.readableBytes() != Integer.BYTES + AUTOMOBILE_SYNC_STATE_SIZE) {
                return;
            }

            int entityId = buf.readInt();
            if (!isValidAutomobileSyncState(buf)) {
                return;
            }

            byte[] stateData = new byte[AUTOMOBILE_SYNC_STATE_SIZE];
            buf.readBytes(stateData);
            server.execute(() -> {
                if (player.level().getEntity(entityId) instanceof AutomobileEntity automobile
                        && automobile.isDriving(player)
                        && automobile.tryAcquireClientSyncSlot()) {
                    automobile.readSyncStateData(new FriendlyByteBuf(Unpooled.wrappedBuffer(stateData)));
                    automobile.markDirty();
                }
            });
        });
        CommonPackets.registerReceiver(Automobility.rl("request_sync_automobile_components"), (server, player, buf) -> {
            int entityId = buf.readInt();
            server.execute(() -> {
                if (player.level().getEntity(entityId) instanceof AutomobileEntity automobile) {
                    sendSyncAutomobileAttachmentsPacket(automobile, player);

                    var fAtt = automobile.getFrontAttachment();
                    if (fAtt != null) fAtt.updatePacketRequested(player);

                    var rAtt = automobile.getRearAttachment();
                    if (rAtt != null) rAtt.updatePacketRequested(player);
                }
            });
        });
    }

    static boolean isValidAutomobileSyncState(FriendlyByteBuf buf) {
        if (buf.readableBytes() != AUTOMOBILE_SYNC_STATE_SIZE) {
            return false;
        }

        int readerIndex = buf.readerIndex();
        try {
            int boostTimer = buf.readInt();
            float steering = buf.readFloat();
            float wheelAngle = buf.readFloat();
            int turboCharge = buf.readInt();
            float engineSpeed = buf.readFloat();
            float boostSpeed = buf.readFloat();
            double x = buf.readDouble();
            double y = buf.readDouble();
            double z = buf.readDouble();
            float yaw = buf.readFloat();
            buf.readBoolean();
            buf.readBoolean();
            float inputSteering = buf.readFloat();
            buf.readBoolean();
            buf.readBoolean();
            buf.readBoolean();
            buf.readBoolean();
            double vertical = buf.readDouble();
            float qx = buf.readFloat();
            float qy = buf.readFloat();
            float qz = buf.readFloat();
            float qw = buf.readFloat();

            double quaternionLength = qx * qx + qy * qy + qz * qz + qw * qw;
            return boostTimer >= 0 && boostTimer <= 1_000_000
                    && turboCharge >= 0 && turboCharge <= 1_000_000
                    && finiteInRange(steering, -1.01, 1.01)
                    && Float.isFinite(wheelAngle)
                    && finiteInRange(engineSpeed, -1000, 1000)
                    && finiteInRange(boostSpeed, -1000, 1000)
                    && finiteInRange(x, -32_000_000, 32_000_000)
                    && finiteInRange(y, -32_000_000, 32_000_000)
                    && finiteInRange(z, -32_000_000, 32_000_000)
                    && Float.isFinite(yaw)
                    && finiteInRange(inputSteering, -1.01, 1.01)
                    && finiteInRange(vertical, -32_000_000, 32_000_000)
                    && Float.isFinite(qx) && Float.isFinite(qy) && Float.isFinite(qz) && Float.isFinite(qw)
                    && quaternionLength >= 0.5 && quaternionLength <= 1.5
                    && !buf.isReadable();
        } catch (IndexOutOfBoundsException ignored) {
            return false;
        } finally {
            buf.readerIndex(readerIndex);
        }
    }

    private static boolean finiteInRange(double value, double min, double max) {
        return Double.isFinite(value) && value >= min && value <= max;
    }
}
