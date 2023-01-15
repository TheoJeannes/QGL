package fr.unice.polytech.si3.qgl.kihm.serializers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import fr.unice.polytech.si3.qgl.kihm.equipments.Equipment;
import fr.unice.polytech.si3.qgl.kihm.equipments.Rudder;
import fr.unice.polytech.si3.qgl.kihm.equipments.Sail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EquipmentDeserializerTest {
    ObjectMapper om;

    @BeforeEach
    void setUp() {
        om = new ObjectMapper();
        SimpleModule equipmentModule = new SimpleModule();
        equipmentModule.addDeserializer(Equipment.class, new EquipmentDeserializer());
        om.registerModule(equipmentModule);
    }

    @Test
    void wrongType() {
        Equipment eq = null;
        try {
            eq = om.readValue("{\"Type\": \"rudder\",\"x\": 1,\"y\": 1}", Equipment.class);
        } catch (Exception e) {
            fail();
        }
        assertNull(eq);
    }

    @Test
    void noX() {
        Equipment eq = null;
        try {
            eq = om.readValue("{\"type\": \"rudder\",\"y\": 1}", Equipment.class);
        } catch (Exception e) {
            fail();
        }
        assertEquals(new Rudder(0, 1), eq);
    }

    @Test
    void noY() {
        Equipment eq = null;
        try {
            eq = om.readValue("{\"type\": \"rudder\",\"x\": 1}", Equipment.class);
        } catch (Exception e) {
            fail();
        }
        assertEquals(new Rudder(1, 0), eq);
    }

    void opened(String s, boolean res) {
        Equipment eq = null;
        try {
            eq = om.readValue(s, Equipment.class);
        } catch (Exception e) {
            fail();
        }
        assertEquals(new Sail(1, 1, res), eq);
    }

    @Test
    void noOpenedSail() {
        opened("{\"type\": \"sail\",\"x\": 1,\"y\": 1,\"opnned\":true}", false);
    }

    @Test
    void openedSail() {
        opened("{\"type\": \"sail\",\"x\": 1,\"y\": 1,\"opened\":true}", true);
    }

    @Test
    void opennedSail() {
        opened("{\"type\": \"sail\",\"x\": 1,\"y\": 1,\"openned\":false}", false);
    }

    @Test
    void noOpen() {
        Equipment eq = null;
        try {
            eq = om.readValue("{\"type\": \"rudder\",\"x\": 1,\"y\": 1,\"openned\":true}", Equipment.class);
        } catch (Exception e) {
            fail();
        }
        assertEquals(new Rudder(1, 1), eq);
    }
}