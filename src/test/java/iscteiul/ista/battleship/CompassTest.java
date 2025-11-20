package iscteiul.ista.battleship;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CompassTest {

    @Test
    @DisplayName("getDirection devolve o char correto para cada direção")
    void getDirectionWorks() {
        assertEquals('n', Compass.NORTH.getDirection());
        assertEquals('s', Compass.SOUTH.getDirection());
        assertEquals('e', Compass.EAST.getDirection());
        assertEquals('o', Compass.WEST.getDirection());
        assertEquals('u', Compass.UNKNOWN.getDirection());
    }

    @Test
    @DisplayName("toString devolve exatamente o mesmo char da direção")
    void toStringWorks() {
        assertEquals("n", Compass.NORTH.toString());
        assertEquals("s", Compass.SOUTH.toString());
        assertEquals("e", Compass.EAST.toString());
        assertEquals("o", Compass.WEST.toString());
        assertEquals("u", Compass.UNKNOWN.toString());
    }

    @Test
    @DisplayName("charToCompass converte caracteres válidos nas direções corretas")
    void charToCompassValid() {
        assertEquals(Compass.NORTH, Compass.charToCompass('n'));
        assertEquals(Compass.SOUTH, Compass.charToCompass('s'));
        assertEquals(Compass.EAST, Compass.charToCompass('e'));
        assertEquals(Compass.WEST, Compass.charToCompass('o'));
    }

    @Test
    @DisplayName("charToCompass devolve UNKNOWN para caracteres inválidos")
    void charToCompassInvalid() {
        assertEquals(Compass.UNKNOWN, Compass.charToCompass('x'));
        assertEquals(Compass.UNKNOWN, Compass.charToCompass('N')); // case sensitive
        assertEquals(Compass.UNKNOWN, Compass.charToCompass(' '));
        assertEquals(Compass.UNKNOWN, Compass.charToCompass('\0'));
    }
}
