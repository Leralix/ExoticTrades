package org.leralix.exotictrades.storage;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.leralix.exotictrades.factory.AbstractionFactory;
import org.mockito.Mockito;

import org.bukkit.Location;

import java.util.UUID;

import static org.mockito.Mockito.when;

class TraderStorageTest {

    @BeforeEach
    void setUp() {
        AbstractionFactory.initializeConfigs();
    }

    @Test
    void testRegister() {


//        Location location = Mockito.mock(Location.class);
//        World world = AbstractionFactory.createWorld("world", UUID.randomUUID());
//        when(world.getUID()).thenReturn(UUID.randomUUID());
//        when(location.getWorld()).thenReturn(world);
//        when(location.getBlockX()).thenReturn(1);
//        when(location.getBlockY()).thenReturn(2);
//        when(location.getBlockZ()).thenReturn(3);
//
//        TraderStorage.register(location);
//
//        TraderStorage.save();
    }

}