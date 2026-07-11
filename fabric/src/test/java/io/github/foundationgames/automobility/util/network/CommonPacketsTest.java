package io.github.foundationgames.automobility.util.network;

import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommonPacketsTest {
    @Test
    void acceptsAWellFormedAutomobileState() {
        var buffer = validStateBuffer();

        assertTrue(CommonPackets.isValidAutomobileSyncState(buffer));
        assertTrue(buffer.readerIndex() == 0, "Validation must not consume the packet");
    }

    @Test
    void rejectsNonFinitePhysicsValues() {
        var buffer = validStateBuffer();
        buffer.setFloat(Integer.BYTES, Float.NaN);

        assertFalse(CommonPackets.isValidAutomobileSyncState(buffer));
    }

    @Test
    void rejectsTruncatedStates() {
        var buffer = validStateBuffer();
        buffer.writerIndex(buffer.writerIndex() - 1);

        assertFalse(CommonPackets.isValidAutomobileSyncState(buffer));
    }

    private static FriendlyByteBuf validStateBuffer() {
        var buffer = new FriendlyByteBuf(Unpooled.buffer(CommonPackets.AUTOMOBILE_SYNC_STATE_SIZE));
        buffer.writeInt(0);
        buffer.writeFloat(0f);
        buffer.writeFloat(0f);
        buffer.writeInt(0);
        buffer.writeFloat(0f);
        buffer.writeFloat(0f);
        buffer.writeDouble(0d);
        buffer.writeDouble(0d);
        buffer.writeDouble(0d);
        buffer.writeFloat(0f);
        buffer.writeBoolean(false);
        buffer.writeBoolean(false);
        buffer.writeFloat(0f);
        buffer.writeBoolean(false);
        buffer.writeBoolean(false);
        buffer.writeBoolean(false);
        buffer.writeBoolean(false);
        buffer.writeDouble(0d);
        buffer.writeFloat(0f);
        buffer.writeFloat(0f);
        buffer.writeFloat(0f);
        buffer.writeFloat(1f);
        return buffer;
    }
}
